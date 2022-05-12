package SoftwareEngineering;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for "EndGame.fxml"
 */
public class EndGameController {

    public Label winnerLabel;
    public Button mainMenuButton;

    /**
     * Initializes the GUI view by listing
     * the winners of the game
     *
     * @param board The board
     */
    public void initialize(Board board) {
        winnerLabel.setText("");
        if (board.getWinners().size() == 1) {
            winnerLabel.setText(board.getWinners().get(0).getName() + " has won the game with a net worth of £" + board.getWinners().get(0).getNetWorth());
        } else {
            for (Player player : board.getWinners()) {
                winnerLabel.setText(winnerLabel.getText() + player.getName() + "\n has tied for the game with a net worth of £" + player.getNetWorth());
            }
        }
    }

    /**
     * Handles the event when "Go to Main Menu"
     * is clicked. Closes the "Congratulations"
     * window and opens the "Main Menu" window.
     */
    public void mainMenu() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Menu.fxml"));
        Parent mainView = null;
        try {
            mainView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(mainView));
        stage.show();

        Stage end = (Stage) mainMenuButton.getScene().getWindow();
        end.close();
    }
}
