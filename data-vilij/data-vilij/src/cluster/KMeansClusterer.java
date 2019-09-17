package cluster;

import algorithms.Clusterer;
import classification.RandomClassifier;
import data.DataSet;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import ui.AppUI;
import vilij.templates.ApplicationTemplate;

/**
 * @author Ritwik Banerjee
 */
public class KMeansClusterer extends Clusterer {

    private DataSet dataset;
    private List<Point2D> centroids;
    private ApplicationTemplate applicationTemplate;
    private XYChart<Number, Number> chart;
    private final int maxIterations;
    private final int updateInterval;
    private final AtomicBoolean tocontinue;
    private final AtomicBoolean countRun;
    private int iteration;
    private boolean isRunning;
    private XYChart.Series<Number, Number> series = new XYChart.Series<>();
    private Point2D dataPoint;

    public KMeansClusterer(ApplicationTemplate applicationTemplate, DataSet dataset, int maxIterations, int updateInterval, int numberOfClusters, boolean countRun) {
        super(numberOfClusters);
        this.applicationTemplate = applicationTemplate;
        this.chart = ((AppUI) applicationTemplate.getUIComponent()).getChart();
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.tocontinue = new AtomicBoolean(false);
        this.countRun = new AtomicBoolean(countRun);
        iteration = 0;
        isRunning = false;
        initializeCentroids();
    }

    @Override
    public int getMaxIterations() {
        return maxIterations;
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public boolean tocontinue() {
        return tocontinue.get();
    }

    public boolean countRun() {
        return countRun.get();
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        isRunning = true;
        if (countRun()) {
            for (int i = 1; i <= maxIterations; i += updateInterval) {
                assignLabels();
                recomputeCentroids();
                Platform.runLater(() -> {
                    chart.getData().clear();
                    toChartData(chart);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ((AppUI) applicationTemplate.getUIComponent()).enableRun();
            ((AppUI) applicationTemplate.getUIComponent()).enableScrnshot();
        }
        if (!countRun()) {
            if (iteration < maxIterations) {
                assignLabels();
                recomputeCentroids();
                Platform.runLater(() -> {
                    chart.getData().clear();
                    toChartData(chart);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            iteration += updateInterval;
            ((AppUI) applicationTemplate.getUIComponent()).enableRun();
            ((AppUI) applicationTemplate.getUIComponent()).enableScrnshot();
        }
        if (iteration > maxIterations) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Max iterations reached");
                alert.showAndWait();
            });
        }
        isRunning = false;
    }

    private void initializeCentroids() {
        Set<String> chosen = new HashSet<>();
        List<String> instanceNames = new ArrayList<>(dataset.getLabels().keySet());
        Random r = new Random();
        while (chosen.size() < numberOfClusters) {
            int i = r.nextInt(instanceNames.size());
            while (chosen.contains(instanceNames.get(i))) {
                ++i;
            }
            chosen.add(instanceNames.get(i));
        }
        centroids = chosen.stream().map(name -> dataset.getLocations().get(name)).collect(Collectors.toList());
        tocontinue.set(true);
    }

    private void assignLabels() {
        dataset.getLocations().forEach((instanceName, location) -> {
            double minDistance = Double.MAX_VALUE;
            int minDistanceIndex = -1;
            for (int i = 0; i < centroids.size(); i++) {
                double distance = computeDistance(centroids.get(i), location);
                if (distance < minDistance) {
                    minDistance = distance;
                    minDistanceIndex = i;
                }
            }
            dataset.getLabels().put(instanceName, Integer.toString(minDistanceIndex));
        });
    }

    private void recomputeCentroids() {
        tocontinue.set(false);
        IntStream.range(0, numberOfClusters).forEach(i -> {
            AtomicInteger clusterSize = new AtomicInteger();
            Point2D sum = dataset.getLabels()
                    .entrySet()
                    .stream()
                    .filter(entry -> i == Integer.parseInt(entry.getValue()))
                    .map(entry -> dataset.getLocations().get(entry.getKey()))
                    .reduce(new Point2D(0, 0), (p, q) -> {
                        clusterSize.incrementAndGet();
                        return new Point2D(p.getX() + q.getX(), p.getY() + q.getY());
                    });
            Point2D newCentroid = new Point2D(sum.getX() / clusterSize.get(), sum.getY() / clusterSize.get());
            if (!newCentroid.equals(centroids.get(i))) {
                centroids.set(i, newCentroid);
                tocontinue.set(true);
            }
        });
    }

    private static double computeDistance(Point2D p, Point2D q) {
        return Math.sqrt(Math.pow(p.getX() - q.getX(), 2) + Math.pow(p.getY() - q.getY(), 2));
    }

    private void toChartData(XYChart<Number, Number> chart) {
        Set<String> labels = new HashSet<>(dataset.getLabels().values());
        for (String label : labels) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(label);
            dataset.getLabels().entrySet().stream().filter(entry -> entry.getValue().equals(label)).forEach(entry -> {
                Point2D point = dataset.getLocations().get(entry.getKey());
                series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
            });
            chart.getData().add(series);
            series.getNode().setStyle("-fx-stroke:transparent");
        }
    }
}
