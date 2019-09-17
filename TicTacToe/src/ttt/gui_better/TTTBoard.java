package ttt.gui_better;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import ttt.engine.TTTEngine;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Graphical Tic-Tac-Toe board.
 *
 * @author Eugene Stark
 * @version 20180211
 */
public class TTTBoard extends GridPane {

    private final TTTAppGUI gui;
    private final TTTEngine engine;
    private final int dim;

    public TTTBoard(TTTAppGUI app, TTTEngine engine) {
        this.gui = app;
        this.engine = engine;
        this.dim = engine.getDim();
        setConstraints();
        addButtons();
    }

    private void setConstraints() {
        for (int i = 0; i < dim; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            rc.setFillHeight(true);
            this.getRowConstraints().add(rc);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            this.getColumnConstraints().add(cc);
        }
    }

    private void addButtons() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                TTTButton btn = new TTTButton(gui,engine,dim,dim);
                btn.setPrefWidth(50);
                btn.setPrefHeight(50);
                btn.setMaxWidth(Double.MAX_VALUE);
                btn.setMaxHeight(Double.MAX_VALUE);
                final int ii = i;
                final int jj = j;
                this.add(btn, j, i);
            }
        }
       
    }
}
