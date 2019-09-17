
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TimerDemo extends Application {
    
    private Label label;
    
    @Override
    public void start(Stage primaryStage) {
        label = new Label(new Date().toString());
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Timer Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(() -> label.setText(new Date().toString()));
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
