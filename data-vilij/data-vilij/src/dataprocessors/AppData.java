package dataprocessors;

import actions.AppActions;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import ui.AppUI;
import vilij.components.DataComponent;
import vilij.templates.ApplicationTemplate;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import vilij.components.ErrorDialog;

/**
 * This is the concrete application-specific implementation of the data
 * component defined by the Vilij framework.
 *
 * @author Ritwik Banerjee
 * @see DataComponent
 */
public class AppData implements DataComponent {

    public TSDProcessor processor;
    private ApplicationTemplate applicationTemplate;
    private ArrayList<String> rest = new ArrayList<String>();
    String s = "";

    public AppData(ApplicationTemplate applicationTemplate) {
        this.processor = new TSDProcessor();
        this.applicationTemplate = applicationTemplate;
    }

    public ArrayList getLines() {
        return rest;
    }

    public void cleanData() {
        rest.clear();
    }

    public int getNumOfInstaces() {
        int numOfIns = 0;
        String[] dataString = s.split("\n");
        numOfIns = dataString.length;
        return numOfIns;
    }

    public String getData() {
        return s;
    }

    @Override
    public void loadData(Path dataFilePath) {
        String first = "";
        try {
            s = new String(Files.readAllBytes((dataFilePath)));
            processor.processString(s);
            processor.clear();
            String[] lines = s.split("\n");
            for (int i = 0; i < lines.length; i++) {
                if (i < 10) {
                    first += lines[i];
                    first += "\n";
                } else {
                    rest.add(lines[i] + "\n");
                }
            }
            String numAndPath = "";
            int numOfIns = getNumOfInstaces();
            int numOfLab = processor.getNumLabels();
            ArrayList<String> nameLabels = processor.getLabels();
            Path path = ((AppActions) applicationTemplate.getActionComponent()).getFilePath();
            numAndPath = numOfIns + " instances with " + numOfLab + " labels loaded from " + path + ". The label(s) are:" + "\n" + nameLabels;
            Label label = new Label(numAndPath);
            ((AppUI) applicationTemplate.getUIComponent()).setMetaDataLabel(numAndPath);
            
            ((AppUI) applicationTemplate.getUIComponent()).enableAlgoType();
            processor.clearMeta();
            ((AppUI) applicationTemplate.getUIComponent()).getTextArea().setVisible(true);
            ((AppUI) applicationTemplate.getUIComponent()).setTextArea(first);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppActions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ErrorDialog.getDialog().show("error", ex.getMessage());
        }
    }

    public void loadData(String dataString) throws Exception {
        processor.processString(dataString);
    }

    @Override
    public void saveData(Path dataFilePath) {
        try {
            File file = new File(dataFilePath.toString());
            FileWriter fileWriter = new FileWriter(file);
            String toSave = ((AppUI) applicationTemplate.getUIComponent()).getText();
            ArrayList save = ((AppUI) applicationTemplate.getUIComponent()).getRest();
            if (save.size() > 0) {
                for (int i = 0; i < save.size(); i++) {
                    toSave += save.get(i);
                }
            }
            fileWriter.write(toSave);
            fileWriter.close();

        } catch (IOException ex) {
            Logger.getLogger(AppData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void clear() {
        processor.clear();
    }

    public void displayData() {
        processor.toChartData(((AppUI) applicationTemplate.getUIComponent()).getChart());
    }

    public int getNumLab() {
        return processor.getNumLabels();
    }

    public ArrayList getAllLabels() {
        return processor.getLabels();

    }
}
