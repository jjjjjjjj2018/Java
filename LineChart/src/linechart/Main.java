
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Line Chart Sample");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        yAxis.setLabel("Stock Price");

        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        lineChart.setCreateSymbols(false);

        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");

        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("My portfolio 2");

        series2.getData().add(new XYChart.Data(1, 14));
        series2.getData().add(new XYChart.Data(2, 20));
        series2.getData().add(new XYChart.Data(3, 18));
        series2.getData().add(new XYChart.Data(4, 30));
        series2.getData().add(new XYChart.Data(5, 29));
        series2.getData().add(new XYChart.Data(6, 31));
        series2.getData().add(new XYChart.Data(7, 28));
        series2.getData().add(new XYChart.Data(8, 28));
        series2.getData().add(new XYChart.Data(9, 17));
        series2.getData().add(new XYChart.Data(10, 27));
        series2.getData().add(new XYChart.Data(11, 45));
        series2.getData().add(new XYChart.Data(12, 18));

        lineChart.getData().add(series);
        lineChart.getData().add(series2);

        
        XYChart.Series<Number,Number> series3 = new XYChart.Series();
        series3.setName("My portfolio sum");

        int[] sumY = new int[12];

        for (XYChart.Series<Number, Number> allSeries : lineChart.getData()) {
            for (int i = 0; i < 12; i++) {
                sumY[i] += (int) allSeries.getData().get(i).getYValue();
                System.out.println(sumY[i]);

            }
        }
        for (int j = 1; j <= 12; j++) {
            series3.getData().add(new XYChart.Data<>(j, sumY[j-1]));

        }
        lineChart.getData().add(series3);
         
        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
