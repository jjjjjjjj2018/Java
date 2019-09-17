package redboxman_javafx;

import java.io.InputStream;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 *
 * @author McKillaGorilla
 */
public class RedBoxManRenderer extends Application {

    Canvas canvas;
    GraphicsContext gc;
    ArrayList<Point2D> imagesRedBoxManLocations;
    ArrayList<Point2D> shapesRedBoxManLocations;
    Image redBoxManImage;

    @Override
    public void start(Stage primaryStage) {
        // INIT THE DATA MANAGERS
        imagesRedBoxManLocations = new ArrayList<>();
        shapesRedBoxManLocations = new ArrayList<>();

        // LOAD THE RED BOX MAN IMAGE
        InputStream str = getClass().getResourceAsStream("/RedBoxMan.png");
        redBoxManImage = new Image(str);

        // MAKE THE CANVAS
        canvas = new Canvas();
        canvas.setStyle("-fx-background-color: cyan");
        gc = canvas.getGraphicsContext2D();

        // PUT THE CANVAS IN A CONTAINER
        Group root = new Group();
        root.getChildren().add(canvas);

        canvas.setOnMouseClicked(e -> {
            if (e.isShiftDown()) {
                shapesRedBoxManLocations.add(new Point2D(e.getX(), e.getY()));
                render();
            } else if (e.isControlDown()) {
                imagesRedBoxManLocations.add(new Point2D(e.getX(), e.getY()));
                render();
            } else {
                clear();
            }
        });

        // PUT THE CONTAINER IN A SCENE
        Scene scene = new Scene(root, 800, 600);
        canvas.setWidth(scene.getWidth());
        canvas.setHeight(scene.getHeight());

        // AND START UP THE WINDOW
        primaryStage.setTitle("Red Box Man Renderer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void clear() {
        shapesRedBoxManLocations.clear();
        imagesRedBoxManLocations.clear();
        render();
    }

    public void render() {
        clearCanvas();
        for (int i = 0; i < shapesRedBoxManLocations.size(); i++) {
            renderShapeRedBoxMan(shapesRedBoxManLocations.get(i));
        }
        for (int j = 0; j < imagesRedBoxManLocations.size(); j++) {
            renderImageRedBoxMan(imagesRedBoxManLocations.get(j));
        }
    }

    public void renderShapeRedBoxMan(Point2D location) {
        String headColor = "#DD0000";
        String outlineColor = "#000000";
        int headW = 115;
        int headH = 88;

        // DRAW HIS RED HEAD
        gc.setFill(Paint.valueOf(headColor));
        gc.fillRect(location.getX(), location.getY(), headW, headH);
        gc.beginPath();
        gc.setStroke(Paint.valueOf(outlineColor));
        gc.setLineWidth(1);
        gc.rect(location.getX(), location.getY(), headW, headH);
        gc.stroke();

        // AND THEN DRAW THE REST OF HIM
        String eyeColor = "#FFFF00";
        String pupilColor = "#000000";
        String mouthColor = "#000000";
        String bodyColor = "#000000";

        int eyeW = 30;
        int eyeH = 25;
        int pupilS = 5;
        int mouthW = 70;
        int mouthH = 10;
        int bodyW = 51;
        int bodyH = 30;
        int handW = 10;
        int handH = 25;
        int footS = 10;

        //left eye
        gc.setFill(Paint.valueOf(eyeColor));
        gc.fillRect(location.getX() + 15, location.getY() + 13, eyeW, eyeH);
        gc.beginPath();

        gc.setStroke(Paint.valueOf(outlineColor));
        gc.setLineWidth(1);
        gc.rect(location.getX() + 15, location.getY() + 13, eyeW, eyeH);
        gc.stroke();

        gc.setFill(Paint.valueOf(pupilColor));
        gc.fillRect(location.getX() + 15 + 12.5, location.getY() + 13 + 10, pupilS, pupilS);
        gc.beginPath();

        //right eye
        gc.setFill(Paint.valueOf(eyeColor));
        gc.fillRect(location.getX() + 15 + 30 + 25, location.getY() + 13, eyeW, eyeH);
        gc.beginPath();

        gc.setStroke(Paint.valueOf(outlineColor));
        gc.setLineWidth(1);
        gc.rect(location.getX() + 15 + 30 + 25, location.getY() + 13, eyeW, eyeH);
        gc.stroke();

        gc.setFill(Paint.valueOf(pupilColor));
        gc.fillRect(location.getX() + 15 + 12.5 + 30 + 25, location.getY() + 13 + 10, pupilS, pupilS);
        gc.beginPath();

        //mouth
        gc.setFill(Paint.valueOf(mouthColor));
        gc.fillRect(location.getX() + 20, location.getY() + 66, mouthW, mouthH);
        gc.beginPath();

        //body
        gc.setFill(Paint.valueOf(bodyColor));
        gc.fillRect(location.getX() + 32, location.getY() + 88, bodyW, bodyH);
        gc.beginPath();

        //hands
        gc.setFill(Paint.valueOf(mouthColor));
        gc.fillRect(location.getX() + 23, location.getY() + 88, handW, handH);
        gc.beginPath();

        gc.setFill(Paint.valueOf(mouthColor));
        gc.fillRect(location.getX() + 82, location.getY() + 88, handW, handH);
        gc.beginPath();

        //feet
        gc.setFill(Paint.valueOf(mouthColor));
        gc.fillRect(location.getX() + 25.5, location.getY() + 88 + 30, footS, footS);
        gc.beginPath();

        gc.setFill(Paint.valueOf(mouthColor));
        gc.fillRect(location.getX() + 78, location.getY() + 88 + 30, footS, footS);
        gc.beginPath();

    }

    public void renderImageRedBoxMan(Point2D location) {
        gc.drawImage(redBoxManImage, location.getX(), location.getY());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
