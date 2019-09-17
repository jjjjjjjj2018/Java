package ui;

import actions.AppActions;
import classification.RandomClassifier;
import cluster.KMeansClusterer;
import cluster.RandomClusterer;
import data.DataSet;
import dataprocessors.*;
import static java.io.File.separator;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import static settings.AppPropertyTypes.*;
import vilij.components.Dialog;
import vilij.components.ErrorDialog;
import vilij.propertymanager.PropertyManager;
import static vilij.settings.PropertyTypes.GUI_RESOURCE_PATH;
import static vilij.settings.PropertyTypes.ICONS_RESOURCE_PATH;
import vilij.templates.ApplicationTemplate;
import vilij.templates.UITemplate;

/**
 * This is the application's user interface implementation.
 *
 * @author Ritwik Banerjee
 */
public final class AppUI extends UITemplate {

    /**
     * The application to which this class of actions belongs.
     */
    private ApplicationTemplate applicationTemplate;
    @SuppressWarnings("FieldCanBeLocal")
    private Button scrnshotButton; // toolbar button to take a screenshot of the data
    private XYChart<Number, Number> chart; // the chart where data will be displayed
    private Button displayButton;  // workspace button to display data on the chart
    private Button runButton;
    private TextArea textArea = new TextArea();       // text area for new data input
    private boolean hasNewText;     // whether or not the text area has any new data since last display
    private VBox dataBox = new VBox();
    private CheckBox countRun = new CheckBox("Countinous Run?");
    private Button done = new Button("DONE");
    private int maxIteration;
    private int updateInterval;
    private int numClust;
    private DataSet dataset;

    public XYChart<Number, Number> getChart() {
        return chart;
    }

    public boolean hasText() {
        this.hasNewText = textArea.getText().isEmpty();
        return hasNewText;
    }

    public TextArea getTextArea() {
        return this.textArea;
    }

    public void setTextArea(String text) {
        textArea.setText(text);
    }

    public String getText() {
        String text = textArea.getText();
        return text;
    }

    public void disableSaveButton() {
        saveButton.setDisable(true);
    }

    public void enableTextArea() {
        textArea.setDisable(false);
        textArea.setVisible(true);
    }

    public void disableTextArea() {
        textArea.setDisable(true);
    }

    public AppUI(Stage primaryStage, ApplicationTemplate applicationTemplate) {
        super(primaryStage, applicationTemplate);
        this.applicationTemplate = applicationTemplate;
    }

    @Override
    protected void setResourcePaths(ApplicationTemplate applicationTemplate) {
        super.setResourcePaths(applicationTemplate);
    }

    @Override
    protected void setToolBar(ApplicationTemplate applicationTemplate) {
        super.setToolBar(applicationTemplate);
        PropertyManager manager = applicationTemplate.manager;
        String iconsPath = "/" + String.join(separator,
                manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                manager.getPropertyValue(ICONS_RESOURCE_PATH.name()));
        String scrnshoticonPath = String.join(separator, iconsPath, manager.getPropertyValue(SCREENSHOT_ICON.name()));
        scrnshotButton = new Button(null, new ImageView(new Image(getClass().getResourceAsStream(scrnshoticonPath))));
        toolBar.getItems().add(scrnshotButton);
        scrnshotButton.setDisable(true);

    }

    @Override
    protected void setToolbarHandlers(ApplicationTemplate applicationTemplate) {
        applicationTemplate.setActionComponent(new AppActions(applicationTemplate));
        newButton.setOnAction(e -> {
            displayButton.setVisible(false);
            runButton.setVisible(false);
            countRun.setSelected(false);
            maxIteration = 0;
            updateInterval = 0;
            numClust = 0;
            dataset = null;
            if (algo1 != null) {
                algo1.setSelected(false);
            }
            if (algo2 != null) {
                algo2.setSelected(false);
            }
            if (algo4 != null) {
                algo4.setSelected(false);
            }
            hideAlgo();
            metaData.setText("");
            applicationTemplate.getActionComponent().handleNewRequest();
        });
        saveButton.setOnAction(e -> applicationTemplate.getActionComponent().handleSaveRequest());
        loadButton.setOnAction(e -> {
            textArea.clear();
            runButton.setVisible(false);
            displayButton.setVisible(false);
            countRun.setSelected(false);
            maxIteration = 0;
            updateInterval = 0;
            numClust = 0;
            dataset = null;
            if (algo1 != null) {
                algo1.setSelected(false);
            }
            if (algo2 != null) {
                algo2.setSelected(false);
            }
            if (algo4 != null) {
                algo4.setSelected(false);
            }
            hideAlgo();
            clear();
            s.clear();
            ((AppData) applicationTemplate.getDataComponent()).cleanData();
            applicationTemplate.getActionComponent().handleLoadRequest();
            try {
                setDataSet();
            } catch (IOException ex) {
                Logger.getLogger(AppUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DataSet.InvalidDataNameException ex) {
                Logger.getLogger(AppUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            done_edit.setVisible(false);
        });
        exitButton.setOnAction(e -> {
            if (textArea.getText().isEmpty()) {
                applicationTemplate.getActionComponent().handleExitRequest();
            }
            if (!textArea.getText().isEmpty()) {
                if (!((AppActions) applicationTemplate.getActionComponent()).isSaved()) {
                    try {
                        if (!((AppActions) applicationTemplate.getActionComponent()).promptToSave()) {
                            applicationTemplate.getActionComponent().handleExitRequest();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(AppUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (randomClassifier != null) {
                if (randomClassifier.isRunning()) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setContentText("Program is running. Do you want to exit anyway?");
                    ButtonType buttonTypeOne = new ButtonType("Yes");
                    ButtonType buttonTypeTwo = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne) {
                        applicationTemplate.getActionComponent().handleExitRequest();
                    } else if (result.get() == buttonTypeTwo) {
                        alert.close();
                    }
                }
                if (!randomClassifier.isRunning()) {
                    applicationTemplate.getActionComponent().handleExitRequest();
                }
            }
            if (randomClusterer != null) {
                if (randomClusterer.isRunning()) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setContentText("Program is running. Do you want to exit anyway?");
                    ButtonType buttonTypeOne = new ButtonType("Yes");
                    ButtonType buttonTypeTwo = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne) {
                        applicationTemplate.getActionComponent().handleExitRequest();
                    } else if (result.get() == buttonTypeTwo) {
                        alert.close();
                    }
                }
                if (!randomClusterer.isRunning()) {
                    applicationTemplate.getActionComponent().handleExitRequest();
                }
            }
            if (kmeansClusterer != null) {
                if (kmeansClusterer.isRunning()) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setContentText("Program is running. Do you want to exit anyway?");
                    ButtonType buttonTypeOne = new ButtonType("Yes");
                    ButtonType buttonTypeTwo = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne) {
                        applicationTemplate.getActionComponent().handleExitRequest();
                    } else if (result.get() == buttonTypeTwo) {
                        alert.close();
                    }
                }
                if (!kmeansClusterer.isRunning()) {
                    applicationTemplate.getActionComponent().handleExitRequest();
                }
            }

//            if (((AppActions) applicationTemplate.getActionComponent()).isSaved()) {
//                applicationTemplate.getActionComponent().handleExitRequest();
//            }
        });
        printButton.setOnAction(e -> applicationTemplate.getActionComponent().handlePrintRequest());
        scrnshotButton.setOnAction(e -> ((AppActions) applicationTemplate.getActionComponent()).handleScreenshotRequest());
    }

    public void setDataSet() throws IOException, DataSet.InvalidDataNameException {
        dataset = null;
        dataset = dataset.fromTSDFile(((AppActions) applicationTemplate.getActionComponent()).getFilePath());
        if (((AppActions) applicationTemplate.getActionComponent()).getFilePath() == null) {
            String[] stringTextArea = textArea.getText().split("\n");
            for (int i = 0; i < stringTextArea.length; i++) {
                dataset.addInstance(stringTextArea[i]);
            }

        }

    }
    ToggleButton done_edit = new ToggleButton("Done");

    public void addDoneButton() {
        done_edit.setVisible(true);
        done_edit.setOnAction(e -> {
            try {
                setDataSet();
            } catch (IOException ex) {
                Logger.getLogger(AppUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DataSet.InvalidDataNameException ex) {
                Logger.getLogger(AppUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (done_edit.isSelected()) {

                    ((AppData) (applicationTemplate.getDataComponent())).processor.clearMeta();
                    ((AppData) (applicationTemplate.getDataComponent())).processor.processString(textArea.getText());
                    ((AppData) (applicationTemplate.getDataComponent())).processor.clear();

                    disableTextArea();

                    String numAndPath = "";
                    int numOfIns = 0;
                    String[] dataString = textArea.getText().split("\n");
                    numOfIns = dataString.length;
                    int numOfLab = (((AppData) applicationTemplate.getDataComponent()).getNumLab());
                    ArrayList<String> nameLabels = (((AppData) applicationTemplate.getDataComponent()).getAllLabels());
                    Path path = ((AppActions) applicationTemplate.getActionComponent()).getFilePath();
                    numAndPath = numOfIns + " instances with " + numOfLab + " labels loaded. The label(s) are:" + "\n" + nameLabels;
                    Label label = new Label(numAndPath);
                    setMetaDataLabel(numAndPath);
                    enableAlgoType();

                }
            } catch (Exception ex) {
                ErrorDialog.getDialog().show("error", ex.getMessage());
            }
            if (!done_edit.isSelected()) {
                enableTextArea();
                hideAlgoType();
                cleanMetaData();
                displayButton.setDisable(true);
            }

        });

    }
    ComboBox<String> algoType = new ComboBox<>();

    public void enableAlgoType() {
        if ((((AppData) applicationTemplate.getDataComponent()).getNumLab()) == 2) {
            algoType.getItems().addAll("Classification", "Clustering");
        } else {
            algoType.getItems().add("Clustering");
        }
        algoType.setVisible(true);
    }

    public void hideAlgoType() {
        algoType.setVisible(false);
        algoType.getItems().clear();
    }

    @Override
    public void initialize() {
        layout();
        newButton.setDisable(false);
        setWorkspaceActions();

        PropertyManager manager = applicationTemplate.manager;
        String iconsPath = "/" + String.join(separator,
                manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                manager.getPropertyValue(ICONS_RESOURCE_PATH.name()));
        String configiconPath = String.join(separator, iconsPath, manager.getPropertyValue(CONFIG_ICON.name()));
        configButton = new Button(null, new ImageView(new Image(getClass().getResourceAsStream(configiconPath))));
        configButton2 = new Button(null, new ImageView(new Image(getClass().getResourceAsStream(configiconPath))));
    }

    @Override
    public void clear() {
        chart.getData().clear();
        textArea.clear();
        scrnshotButton.setDisable(true);
    }
    Label metaData = new Label();

    public void setMetaDataLabel(String s) {
        metaData.setText(s);
    }

    public void cleanMetaData() {
        metaData.setText("");
    }
    Button configButton;
    Button configButton2;

    private void layout() {
        configButton = new Button();
        configButton2 = new Button();
        textArea.setVisible(false);
        displayButton = new Button("display");
        runButton = new Button("Run");
        displayButton.setVisible(false);
        runButton.setVisible(false);
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setMinorTickLength(5);
        xAxis.setMinorTickLength(5);
        chart = new LineChart(xAxis, yAxis);
        algoType.setPromptText("Algorithm Type");
        hideAlgoType();

        primaryScene.getStylesheets().add(getClass().getResource("/ChartStyle.css").toExternalForm());
        done_edit.setVisible(false);
        dataBox.getChildren().addAll(textArea, metaData, done_edit, algoType, displayButton, runButton);
        workspace = new HBox();
        workspace.getChildren().addAll(dataBox, chart);

        appPane.getChildren().add(workspace);

    }

    ArrayList<String> s = new ArrayList<String>();

    public ArrayList getRest() {
        return s;
    }
    VBox clust = new VBox();
    RadioButton algo1;
    RadioButton algo2;

    public void clustAlgo() {
        if (!dataBox.getChildren().contains(clust)) {
            clust.setVisible(true);
            HBox algo1Box = new HBox();
            HBox algo2Box = new HBox();
            algo1 = new RadioButton("K Mean Clusterer");
            algo2 = new RadioButton("Random Clusterer");
            algo1.setToggleGroup(algoGroup);
            algo2.setToggleGroup(algoGroup);
            Label clu = new Label("Clustering");
            algo1.setOnAction(e -> {
                if (algo1.isSelected() || algo2.isSelected()) {
                    displayButton.setVisible(true);
                }
                if (!algo1.isSelected() && algo2.isSelected() || (maxIteration == 0 && updateInterval == 0 && numClust == 0)) {
                    displayButton.setVisible(false);
                    runButton.setVisible(false);
                }
            });
            setClusterAction();
            algo1Box.getChildren().addAll(algo1, configButton);
            algo2Box.getChildren().addAll(algo2, configButton2);
            clust.getChildren().addAll(clu, algo1Box, algo2Box);
            try {
                dataBox.getChildren().add(clust);
            } catch (IllegalArgumentException e) {
            }
        }
    }
    ToggleGroup algoGroup = new ToggleGroup();
    VBox classifi = new VBox();
    RadioButton algo4;
    RandomClassifier randomClassifier;
    KMeansClusterer kmeansClusterer;
    RandomClusterer randomClusterer;

    public void classAlgo() {
        if (!dataBox.getChildren().contains(classifi)) {
            classifi.setVisible(true);
            HBox algo4Box = new HBox();
            Label cla = new Label("Classification");
            algo4 = new RadioButton("Algorithm 4");
            algo4.setToggleGroup(algoGroup);
            algo4.setOnAction(e -> {
                if (algo4.isSelected()) {
                    displayButton.setVisible(true);
                }
                if (!algo4.isSelected() || (maxIteration == 0 && updateInterval == 0)) {
                    displayButton.setVisible(false);
                    runButton.setVisible(false);
                }
            });
            setClassiAction();
            algo4Box.getChildren().addAll(algo4, configButton);
            classifi.getChildren().addAll(cla, algo4Box);
            dataBox.getChildren().add(classifi);
        }
    }

    public void hideAlgo() {
        classifi.setVisible(false);
        clust.setVisible(false);
    }

    public void setClassiAction() {
        Stage configStage = new Stage();
        TextField maxInte = new TextField();
        TextField upInter = new TextField();
        configButton.setOnAction(e -> {
            if (!countRun.isSelected()) {
                maxIteration = 0;
                updateInterval = 0;
            }
            configStage.setTitle("Algorithm Run Configuration");
            GridPane pane = new GridPane();
            pane.setAlignment(Pos.CENTER);

            HBox iterations = new HBox();
            Label inte = new Label("Max. Iterations:");
            maxInte.setText(maxIteration + "");
            iterations.getChildren().addAll(inte, maxInte);
            HBox update = new HBox();
            Label upda = new Label("Updata Interval:");
            upInter.setText(updateInterval + "");
            update.getChildren().addAll(upda, upInter);
            VBox configBox = new VBox();
            configBox.getChildren().addAll(iterations, update, countRun, done);

            pane.getChildren().addAll(configBox);
            Scene configScene = new Scene(pane);
            configStage.setScene(configScene);
            configStage.show();
        });

        done.setOnAction(e -> {
            try {
                maxIteration = Integer.parseInt(maxInte.getText());
                updateInterval = Integer.parseInt(upInter.getText());
                if (maxIteration > 0 || updateInterval > 0) {
                    configStage.close();
                }
                if (maxIteration <= 0 || updateInterval <= 0) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("All configuration should be positive.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("All configurations must be interger");
                alert.showAndWait();
            }
            if (algo4.isSelected() && maxIteration != 0 && updateInterval != 0) {
                displayButton.setVisible(true);
                runButton.setVisible(false);

            }
        });
    }

    public void setClusterAction() {
        Stage configStage = new Stage();
        TextField maxInte = new TextField();
        TextField upInter = new TextField();
        TextField numClus = new TextField();

        configButton.setOnAction(e -> {
            if (!countRun.isSelected()) {
                maxIteration = 0;
                updateInterval = 0;
                numClust = 0;
            }
            configStage.setTitle("Algorithm Run Configuration");
            GridPane pane = new GridPane();

            HBox iterations = new HBox();
            Label inte = new Label("Max. Iterations:");
            maxInte.setText(maxIteration + "");
            iterations.getChildren().addAll(inte, maxInte);

            HBox update = new HBox();
            Label upda = new Label("Updata Interval:");
            upInter.setText(updateInterval + "");
            update.getChildren().addAll(upda, upInter);

            HBox numCluster = new HBox();
            Label numClu = new Label("Number of cluster:");
            numClus.setText(numClust + "");
            numCluster.getChildren().addAll(numClu, numClus);

            VBox configBox = new VBox();
            configBox.getChildren().addAll(iterations, update, numCluster, countRun, done);

            pane.getChildren().addAll(configBox);
            Scene configScene = new Scene(pane);
            configStage.setScene(configScene);
            configStage.show();
        });
        configButton2.setOnAction(e -> {
            if (!countRun.isSelected()) {
                maxIteration = 0;
                updateInterval = 0;
                numClust = 0;
            }
            configStage.setTitle("Algorithm Run Configuration");
            GridPane pane = new GridPane();

            HBox iterations = new HBox();
            Label inte = new Label("Max. Iterations:");
            maxInte.setText(maxIteration + "");
            iterations.getChildren().addAll(inte, maxInte);

            HBox update = new HBox();
            Label upda = new Label("Updata Interval:");
            upInter.setText(updateInterval + "");
            update.getChildren().addAll(upda, upInter);

            HBox numCluster = new HBox();
            Label numClu = new Label("Number of cluster:");
            numClus.setText(numClust + "");
            numCluster.getChildren().addAll(numClu, numClus);

            VBox configBox = new VBox();
            configBox.getChildren().addAll(iterations, update, numCluster, countRun, done);

            pane.getChildren().addAll(configBox);
            Scene configScene = new Scene(pane);
            configStage.setScene(configScene);
            configStage.show();
        });
        done.setOnAction(e -> {
            try {
                maxIteration = Integer.parseInt(maxInte.getText());
                updateInterval = Integer.parseInt(upInter.getText());
                numClust = Integer.parseInt(numClus.getText());
                if (maxIteration > 0 || updateInterval > 0 || numClust > 0) {
                    configStage.close();
                }
                if (maxIteration <= 0 || updateInterval <= 0 || numClust <= 0) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("All configuration should be positive.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("All configuration should be interger");
                alert.showAndWait();
            }
            if ((algo1.isSelected() || algo2.isSelected()) && (maxIteration != 0 && updateInterval != 0 && updateInterval != 0)) {
                displayButton.setVisible(false);
                runButton.setVisible(true);
                if (countRun.isSelected() && algo1.isSelected()) {
                    kmeansClusterer = new KMeansClusterer(applicationTemplate, dataset, maxIteration, updateInterval, numClust, true);
                }
                if (!countRun.isSelected() && algo1.isSelected()) {
                    kmeansClusterer = new KMeansClusterer(applicationTemplate, dataset, maxIteration, updateInterval, numClust, false);
                }
                if (countRun.isSelected() && algo2.isSelected()) {
                    randomClusterer = new RandomClusterer(applicationTemplate, dataset, maxIteration, updateInterval, numClust, true);
                }
                if (!countRun.isSelected() && algo2.isSelected()) {
                    randomClusterer = new RandomClusterer(applicationTemplate, dataset, maxIteration, updateInterval, numClust, false);
                }
            }
        });
    }

    public void enableRun() {
        runButton.setDisable(false);
    }

    public void enableScrnshot() {
        scrnshotButton.setDisable(false);
    }

    private void setWorkspaceActions() {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (textArea.getText().isEmpty() == true) {
                    saveButton.setDisable(true);
                } else {
                    saveButton.setDisable(false);
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println(newValue);
            }
            s = (((AppData) applicationTemplate.getDataComponent()).getLines());
            String[] existed = textArea.getText().split("\n");
        });
        algoType.setOnAction(e -> {
            if (algoType.getValue() != null) {
                if (algoType.getValue().equals("Clustering")) {
                    algoType.setVisible(false);
                    done_edit.setVisible(false);
                    clustAlgo();
                    clust.setVisible(true);
                }
                if (algoType.getValue().equals("Classification")) {
                    algoType.setVisible(false);
                    done_edit.setVisible(false);
                    classAlgo();
                    classifi.setVisible(true);
                }
            }
        });

        runButton.setOnAction(e -> {
            if (algo1 != null && algo1.isSelected()) {
                runButton.setDisable(true);
                scrnshotButton.setDisable(true);
                Thread thread = new Thread(kmeansClusterer);
                thread.start();
            }
            if (algo2 != null && algo2.isSelected()) {
                runButton.setDisable(true);
                scrnshotButton.setDisable(true);
                Thread thread = new Thread(randomClusterer);
                thread.start();
            }
            if (algo4 != null && algo4.isSelected()) {
                runButton.setDisable(true);
                scrnshotButton.setDisable(true);
                Thread thread = new Thread(randomClassifier);
                thread.start();
            }
        });

        displayButton.setOnAction(e -> {
            try {
                chart.getData().clear();
                String data = textArea.getText();
                AppData appData = (AppData) applicationTemplate.getDataComponent();
                ArrayList<String> r = new ArrayList<String>();
                r = appData.getLines();
                if (r.size() == 0) {
                    appData.loadData(data);
                    appData.displayData();
                } else {
                    String rest = "";
                    for (int i = 0; i < r.size(); i++) {
                        rest += r.get(i);
                    }
                    appData.loadData(data + rest);
                    appData.displayData();
                }
                scrnshotButton.setDisable(false);
                appData.clear();
            } catch (Exception ex) {
                PropertyManager manager = applicationTemplate.manager;
                applicationTemplate.getDialog(Dialog.DialogType.ERROR).show(manager.getPropertyValue(LOAD_ERROR.name()), manager.getPropertyValue(FILE_NOT_VALID.name()));
            }
            displayButton.setVisible(false);
            runButton.setVisible(true);
            if (algo4.isSelected() && countRun.isSelected()) {
                randomClassifier = new RandomClassifier(applicationTemplate, dataset, maxIteration, updateInterval, true);
            }
            if (algo4.isSelected() && !countRun.isSelected()) {
                randomClassifier = new RandomClassifier(applicationTemplate, dataset, maxIteration, updateInterval, false);
            }

        }
        );

    }

}
