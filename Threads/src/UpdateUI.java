
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateUI extends Application implements Runnable {

    private ListView threadView;
    private int threadID;

    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Launch thread");
        btn.setOnAction(e -> new Thread(this, "Thread "
                + (++threadID)).start());
        threadView = new ListView();
        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(threadView);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Update UI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void run() {
        String myName = Thread.currentThread().getName();
        Label myLabel = new Label(myName + " active");
        Platform.runLater(() -> { threadView.getItems().add(myLabel); });
        try {
            Thread.sleep((long)(Math.random() * 20000));
        } catch(InterruptedException x) { /* Do nothing */ }
        Platform.runLater(() -> { threadView.getItems().remove(myLabel); });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
