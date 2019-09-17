
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class WorkerDemo extends Application implements Runnable {
    
    private LinkedBlockingQueue<String> workQueue;
    
    @Override
    public void start(Stage primaryStage) {
        workQueue = new LinkedBlockingQueue<>();
        Button btn = new Button();
        btn.setText("Post Work!");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                postWork("Work posted at: " + new Date());
            }
        });
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Worker Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
        for(int i = 1; i <= 5; i++) {
            new Thread(this, "Thread " + i).start();
        }
    }

    /**
     * Entry point for a "worker" thread.
     */
    public void run() {
        String myName = Thread.currentThread().getName();
        System.out.println(myName + " starting");
        try {
            while (!Thread.interrupted()) {
                String work = waitForWork();
                System.out.println(myName + " got work: " + work);
                Thread.sleep((long)(Math.random() * 20000));  //Hardly working!
                System.out.println(myName + " ready at " + new Date());
            }
        } catch (InterruptedException x) {
            // Terminate thread.
        }
    }

    /**
     * Method used by the event dispatch thread to post work.
     */
    private void postWork(String work) {
        workQueue.add(work);
    }
    /**
     * Method used to wait for work notification.
     */
    private String waitForWork() throws InterruptedException {
        return workQueue.take();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
