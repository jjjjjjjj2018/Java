package actions;

import dataprocessors.AppData;
import java.io.File;
import java.io.FileWriter;
import vilij.components.ActionComponent;
import vilij.templates.ApplicationTemplate;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.*;
import javafx.scene.chart.Chart;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import static settings.AppPropertyTypes.DATA_FILE_EXT;
import static settings.AppPropertyTypes.DATA_FILE_EXT_DESC;
import ui.AppUI;
import vilij.components.ConfirmationDialog;
import vilij.components.ConfirmationDialog.Option;
import vilij.components.Dialog;
import vilij.components.ErrorDialog;
import vilij.propertymanager.PropertyManager;

/**
 * This is the concrete implementation of the action handlers required by the
 * application.
 *
 * @author Ritwik Banerjee
 */
public final class AppActions implements ActionComponent {

    /**
     * The application to which this class of actions belongs.
     */
    private ApplicationTemplate applicationTemplate;

    /**
     * Path to the data file currently active.
     */
    Path dataFilePath;
    Boolean isSaved;

    public AppActions(ApplicationTemplate applicationTemplate) {
        this.applicationTemplate = applicationTemplate;
        isSaved = false;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public Path getFilePath() {
        return dataFilePath;
    }

    @Override
    public void handleNewRequest() {
        if (((AppUI) applicationTemplate.getUIComponent()).hasText()) {
            ((AppUI) applicationTemplate.getUIComponent()).clear();
            dataFilePath = null;
            isSaved = false;
        } else {
            try {
                promptToSave();
                dataFilePath = null;
            } catch (IOException ex) {
                Logger.getLogger(AppActions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ((AppUI) applicationTemplate.getUIComponent()).hideAlgoType();
        ((AppUI) applicationTemplate.getUIComponent()).clear();
        ((AppUI) applicationTemplate.getUIComponent()).enableTextArea();
        ((AppUI) applicationTemplate.getUIComponent()).addDoneButton();
    }

    @Override
    public void handleSaveRequest() {
        if (dataFilePath == null) {
            try {
                String toSave = ((AppUI) applicationTemplate.getUIComponent()).getText();
                ((AppData) (applicationTemplate.getDataComponent())).processor.processString(toSave);
                ((AppData) (applicationTemplate.getDataComponent())).processor.clear();
                PropertyManager manager = applicationTemplate.manager;
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(manager.getPropertyValue(DATA_FILE_EXT_DESC.name()), manager.getPropertyValue(DATA_FILE_EXT.name()));
                fileChooser.getExtensionFilters().add(extFilter);
                Stage saveStage = new Stage();
                File file = fileChooser.showSaveDialog(saveStage);
                if (file != null) {
                    dataFilePath = file.toPath();
                    AppData appData = (AppData) applicationTemplate.getDataComponent();
                    appData.saveData(dataFilePath);
                }
            } catch (Exception ex) {
                ErrorDialog.getDialog().show("error", ex.getMessage());
            }
        } else {
            try {
                String toSave = ((AppUI) applicationTemplate.getUIComponent()).getText();
                ((AppData) (applicationTemplate.getDataComponent())).processor.processString(toSave);
                ((AppData) (applicationTemplate.getDataComponent())).processor.clear();
                AppData appData = (AppData) applicationTemplate.getDataComponent();
                appData.saveData(dataFilePath);
            } catch (Exception ex) {
                ErrorDialog.getDialog().show("error", ex.getMessage());
            }
        }
        ((AppUI) applicationTemplate.getUIComponent()).disableSaveButton();
    }

    @Override
    public void handleLoadRequest() {
        ((AppUI) applicationTemplate.getUIComponent()).cleanMetaData();
        ((AppUI) applicationTemplate.getUIComponent()).hideAlgoType();
        ((AppData) (applicationTemplate.getDataComponent())).processor.clear();
        PropertyManager manager = applicationTemplate.manager;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("tsdfiles", "*.tsd"));
        Stage loadStage = new Stage();
        File file = fileChooser.showOpenDialog(loadStage);
        try {
            dataFilePath = file.toPath();
            AppData appData = (AppData) applicationTemplate.getDataComponent();
            appData.loadData(dataFilePath);
        } catch (NullPointerException ne) {
        }
        isSaved = true;
        ((AppUI) applicationTemplate.getUIComponent()).disableTextArea();
    }

    @Override
    public void handleExitRequest() {
        applicationTemplate.getUIComponent().getPrimaryWindow().close();
    }

    @Override
    public void handlePrintRequest() {
        // TODO: NOT A PART OF HW 1
    }

    /**
     *
     * @throws IOException
     */
    public void handleScreenshotRequest() {
        Chart chart = ((AppUI) applicationTemplate.getUIComponent()).getChart();
        try {
            WritableImage image = chart.snapshot(new SnapshotParameters(), null);
            File file = new File("screenshot.png");
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (Exception ex) {
        }
    }

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. The user will be presented
     * with three options:
     * <ol>
     * <li><code>yes</code>, indicating that the user wants to save the work and
     * continue with the action,</li>
     * <li><code>no</code>, indicating that the user wants to continue with the
     * action without saving the work, and</li>
     * <li><code>cancel</code>, to indicate that the user does not want to
     * continue with the action, but also does not want to save the work at this
     * point.</li>
     * </ol>
     *
     * @return <code>false</code> if the user presses the <i>cancel</i>, and
     * <code>true</code> otherwise.
     */
    public boolean promptToSave() throws IOException {
        PropertyManager manager = applicationTemplate.manager;
        Dialog cd = applicationTemplate.getDialog(Dialog.DialogType.CONFIRMATION);
        cd.show("Save Current Work", "Would you like to save current work?");
        Option option = ConfirmationDialog.getDialog().getSelectedOption();
        if (option == Option.YES) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(manager.getPropertyValue(DATA_FILE_EXT_DESC.name()), manager.getPropertyValue(DATA_FILE_EXT.name()));
            fileChooser.getExtensionFilters().add(extFilter);
            Stage saveStage = new Stage();
            File file = fileChooser.showSaveDialog(saveStage);
            try {
                FileWriter fileWriter = null;
                fileWriter = new FileWriter(file);
                fileWriter.write(((AppUI) applicationTemplate.getUIComponent()).getText());
                dataFilePath = file.toPath();
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(AppActions.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException exception) {
            }

            ((AppUI) applicationTemplate.getUIComponent()).clear();
            ((AppUI) applicationTemplate.getUIComponent()).getTextArea().clear();
            isSaved = true;
            return true;
        }
        if (option == Option.NO) {
            ((AppUI) applicationTemplate.getUIComponent()).clear();
            ((AppUI) applicationTemplate.getUIComponent()).getTextArea().clear();
            return false;
        }
        if (option == Option.CANCEL) {
            return true;
        }
        return true;
    }
}
