package dataprocessors;

import javafx.geometry.Point2D;
import javafx.scene.chart.XYChart;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;

/**
 * The data files used by this data visualization applications follow a
 * tab-separated format, where each data point is named, labeled, and has a
 * specific location in the 2-dimensional X-Y plane. This class handles the
 * parsing and processing of such data. It also handles exporting the data to a
 * 2-D plot.
 * <p>
 * A sample file in this format has been provided in the application's
 * <code>resources/data</code> folder.
 *
 * @author Ritwik Banerjee
 * @see XYChart
 */
public final class TSDProcessor {

    int x = 0;
    double sumY = 0;
    double avgY = 0;
    double minX = 0;
    double maxX = 0;

    public static class InvalidDataNameException extends Exception {

        private static final String NAME_ERROR_MSG = "All data instance names must start with the @ character.";

        public InvalidDataNameException(String name) {
            super(String.format("Invalid name '%s'." + NAME_ERROR_MSG, name));
        }
    }

    public static class DuplicateNameException extends Exception {

        private static final String NAME_ERROR_MSG = "Duplicate name ";

        public DuplicateNameException(String name) {
            super(String.format("Duplicate name '%s'." + NAME_ERROR_MSG, name));
        }
    }

    private Map<String, String> dataLabels;
    private Map<String, Point2D> dataPoints;
    private Map<Point2D, String> dataNames;

    public TSDProcessor() {
        dataLabels = new HashMap<>();
        dataPoints = new HashMap<>();
        dataNames = new HashMap<>();
    }
    ArrayList<String> allLabels = new ArrayList<String>();
    int labelCount = 0;

    /**
     * Processes the data and populated two {@link Map} objects with the data.
     *
     * @param tsdString the input data provided as a single {@link String}
     * @throws Exception if the input string does not follow the
     * <code>.tsd</code> data format
     */
    public void processString(String tsdString) throws Exception {
        AtomicBoolean hadAnError = new AtomicBoolean(false);
        AtomicInteger lineNumber = new AtomicInteger();
        StringBuilder errorMessage = new StringBuilder();
        Stream.of(tsdString.split("\n"))
                .map(line -> Arrays.asList(line.split("\t")))
                .forEach(list -> {
                    try {
                        lineNumber.set(lineNumber.get() + 1);
                        String name = checkedname(list.get(0));
                        String label = list.get(1);
                        String[] pair = list.get(2).split(",");
                        Point2D point = new Point2D(Double.parseDouble(pair[0]), Double.parseDouble(pair[1]));
                        dataLabels.put(name, label);
                        dataPoints.put(name, point);
                        dataNames.put(point, name);
                        if (!allLabels.contains(label)) {
                            allLabels.add(label);
                            labelCount++;
                        }

                    } catch (Exception e) {
                        errorMessage.setLength(0);
                        errorMessage.append(e.getClass().getSimpleName()).append(": ").append("Error at line: " + lineNumber);
                        hadAnError.set(true);
                    }
                });
        if (errorMessage.length() > 0) {
            throw new Exception(errorMessage.toString());
        }
    }

    public int getNumLabels() {
        return labelCount;
    }

    public ArrayList getLabels() {
        return allLabels;
    }

    public ArrayList<Double> getMinMax() {
        ArrayList<Double> minmax = new ArrayList<Double>();
        minmax.add(minX);
        minmax.add(maxX);
        return minmax;
    }

    /**
     * Exports the data to the specified 2-D chart.
     *
     * @param chart the specified chart
     */
    void toChartData(XYChart<Number, Number> chart) {
        Set<String> labels = new HashSet<>(dataLabels.values());
        for (String label : labels) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(label);
            dataLabels.entrySet().stream().filter(entry -> entry.getValue().equals(label)).forEach(entry -> {
                Point2D point = dataPoints.get(entry.getKey());
                series.getData().add(new XYChart.Data<>(point.getX(), point.getY()));
                x += 1;
                sumY += point.getY();
                if (minX == 0 || minX > point.getX()) {
                    minX = point.getX();
                }
                if (maxX < point.getX()) {
                    maxX = point.getX();
                }
                

            });
            chart.getData().add(series);
            for (XYChart.Series<Number, Number> s : chart.getData()) {
                for (XYChart.Data<Number, Number> d : s.getData()) {
                    Point2D val = new Point2D((double) d.getXValue(), (double) d.getYValue());
                    String name = dataNames.get(val);
                    Tooltip.install(d.getNode(), new Tooltip(name));

                    //Adding class on hover
                    d.getNode().setOnMouseEntered(e -> d.getNode().getStyleClass().add("onHover"));

                    //Removing class on exit
                    d.getNode().setOnMouseExited(e -> d.getNode().getStyleClass().remove("onHover"));
                }
            }

            series.getNode().setStyle("-fx-stroke:transparent");

        }
        //create average y value line         
        XYChart.Series<Number, Number> seriesAvg = new XYChart.Series();
        avgY = sumY / x;
        seriesAvg.setName("Average Y value");
        seriesAvg.getData().add(new XYChart.Data<Number, Number>(minX, avgY));
        seriesAvg.getData().add(new XYChart.Data<Number, Number>(maxX, avgY));

        Rectangle rec = new Rectangle(0, 0);
        Rectangle rec1 = new Rectangle(0, 0);
        seriesAvg.getData().get(0).setNode(rec);
        seriesAvg.getData().get(1).setNode(rec1);
        chart.getData().addAll(seriesAvg);
    }
    XYChart.Series<Number, Number> seriesAvg = new XYChart.Series();

    public void clear() {
        dataPoints.clear();
        dataLabels.clear();
        dataNames.clear();
        x = 0;
        sumY = 0;
        avgY = 0;
    }

    public void clearMeta() {
        allLabels.clear();
        labelCount = 0;
    }

    private String checkedname(String name) throws InvalidDataNameException, DuplicateNameException {
        if (dataLabels.containsKey(name)) {
            throw new DuplicateNameException(name);
        }
        if (!name.startsWith("@")) {
            throw new InvalidDataNameException(name);
        }

        return name;
    }
}
