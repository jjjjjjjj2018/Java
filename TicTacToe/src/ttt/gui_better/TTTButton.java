package ttt.gui_better;

import javafx.scene.control.Alert;
import ttt.engine.TTTEngine;
import javafx.scene.control.Button;

/**
 * Button for graphical Tic-Tac-Toe board.
 *
 * @author Eugene Stark
 * @version 20180211
 */
public class TTTButton extends Button {

    private static final int PREF_SIZE = 50;

    private final int row, col;
    private final TTTAppGUI gui;
    private final TTTEngine engine;

    public TTTButton(TTTAppGUI app, TTTEngine engine, int row, int col) {
        this.gui = app;
        this.engine = engine;
        this.row = row;
        this.col = col;
        this.setListener();

    }

    private void setListener() {
         this.setOnAction(e -> {
                    try {
                        this.setText(engine.getPlayerToMove()
                                == TTTEngine.X_PLAYER ? "X" : "O");
                        engine.makeMove(row, col);
                        if (engine.gameOver()) {
                            int winner = engine.getWinner();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Game Over");
                            alert.setHeaderText("Game is over");
                            alert.setContentText("The game is over: "
                                    + (winner == TTTEngine.NO_PLAYER ? "Tie game"
                                            : engine.playerName(winner) + " has won."));
                            alert.showAndWait();
                        }
                    } catch (TTTEngine.IllegalMoveException x) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Illegal Move");
                        alert.setHeaderText("Move is illegal");
                        alert.setContentText(x.getMessage());
                        alert.showAndWait();
                    }
                });

    }

}
