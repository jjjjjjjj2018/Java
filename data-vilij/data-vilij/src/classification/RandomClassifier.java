package classification;

import algorithms.Classifier;
import data.DataSet;
import dataprocessors.AppData;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.shape.Rectangle;
import ui.AppUI;
import vilij.components.ActionComponent;
import vilij.templates.ApplicationTemplate;

/**
 * @author Ritwik Banerjee
 */
public class RandomClassifier extends Classifier {

    private static final Random RAND = new Random();

    @SuppressWarnings("FieldCanBeLocal")
    // this mock classifier doesn't actually use the data, but a real classifier will
    private DataSet dataset;

    private final int maxIterations;
    private final int updateInterval;

    // currently, this value does not change after instantiation
    private final AtomicBoolean tocontinue;

    private ApplicationTemplate applicationTemplate;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private XYChart<Number, Number> chart;
    private XYChart.Series<Number, Number> classifySeries = new XYChart.Series();
    private int iterations;
    private boolean isRunning;

    @Override
    public int getMaxIterations() {
        return maxIterations;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public boolean tocontinue() {
        return tocontinue.get();
    }

    public RandomClassifier(ApplicationTemplate applicationTemplate, DataSet dataset, int maxIterations, int updateInterval, boolean tocontinue) {
        this.applicationTemplate = applicationTemplate;
        this.chart = ((AppUI) applicationTemplate.getUIComponent()).getChart();
        this.dataset = dataset;
        this.maxIterations = maxIterations;
        this.updateInterval = updateInterval;
        this.tocontinue = new AtomicBoolean(tocontinue);
        iterations = 0;
        classifySeries.setName("Classification Series");
        chart.getData().add(classifySeries);
        minX = ((AppData) (applicationTemplate.getDataComponent())).processor.getMinMax().get(0);
        maxX = ((AppData) (applicationTemplate.getDataComponent())).processor.getMinMax().get(1);
        classifySeries.getData().add(new XYChart.Data<Number, Number>(0, 0));
        classifySeries.getData().add(new XYChart.Data<Number, Number>(0, 0));
        isRunning = false;
    }

    @Override
    public void run() {
        if (tocontinue()) {
            isRunning = true;
            for (int i = 1; i <= maxIterations; i += updateInterval) {
                int xCoefficient = new Long(-1 * Math.round((2 * RAND.nextDouble() - 1) * 10)).intValue();
                int yCoefficient = 10;
                int constant = RAND.nextInt(11);

                // this is the real output of the classifier
                output = Arrays.asList(xCoefficient, yCoefficient, constant);

                minY = (-xCoefficient * minX - constant) / yCoefficient;
                maxY = (-xCoefficient * maxX - constant) / yCoefficient;

                // everything below is just for internal viewing of how the output is changing
                // in the final project, such changes will be dynamically visible in the UI
//                if (i % updateInterval == 0) {
//                    System.out.printf("Iteration number %d: ", i); //
//                    flush();
//                }
//                if (i > maxIterations * .6 && RAND.nextDouble() < 0.05) {
//                    System.out.printf("Iteration number %d: ", i);
//                    flush();
//                    break;
//                }
                Platform.runLater(() -> {
                    classifySeries.getData().set(0, new XYChart.Data<Number, Number>(minX, minY));
                    classifySeries.getData().set(1, new XYChart.Data<Number, Number>(maxX, maxY));
                    Rectangle rec = new Rectangle(0, 0);
                    Rectangle rec1 = new Rectangle(0, 0);
                    classifySeries.getData().get(0).setNode(rec);
                    classifySeries.getData().get(1).setNode(rec1);
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
        if (!tocontinue()) {
            isRunning = true;
            if (iterations < maxIterations) {
                int xCoefficient = new Long(-1 * Math.round((2 * RAND.nextDouble() - 1) * 10)).intValue();
                int yCoefficient = 10;
                int constant = RAND.nextInt(11);

                minY = (-xCoefficient * minX - constant) / yCoefficient;
                maxY = (-xCoefficient * maxX - constant) / yCoefficient;

                Platform.runLater(() -> {
                    classifySeries.getData().set(0, new XYChart.Data<Number, Number>(minX, minY));
                    classifySeries.getData().set(1, new XYChart.Data<Number, Number>(maxX, maxY));
                    Rectangle rec = new Rectangle(0, 0);
                    Rectangle rec1 = new Rectangle(0, 0);
                    classifySeries.getData().get(0).setNode(rec);
                    classifySeries.getData().get(1).setNode(rec1);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RandomClassifier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            iterations += updateInterval;
            ((AppUI) applicationTemplate.getUIComponent()).enableRun();
            ((AppUI) applicationTemplate.getUIComponent()).enableScrnshot();
        }
        if (iterations > maxIterations) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Max iterations reached");
                alert.showAndWait();
            });
        }
        isRunning = false;
    }

    // for internal viewing only
    protected void flush() {
        System.out.printf("%d\t%d\t%d%n", output.get(0), output.get(1), output.get(2));
    }

}
