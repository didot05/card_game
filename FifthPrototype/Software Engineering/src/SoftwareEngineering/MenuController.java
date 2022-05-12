package SoftwareEngineering;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for "Menu.fxml"
 */
public class MenuController implements Initializable {
    public Button createGameButton;
    public ChoiceBox<Integer> numOfPlayers, numOfAI;
    public TextField boardDataPath, cardDataPath, minutesField;
    public ChoiceBox<String> gameMode;
    public Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameMode.getItems().addAll("Full Game", "Abridged Game");
        gameMode.getSelectionModel().select(0);
        numOfPlayers.getItems().addAll(0, 1, 2, 3, 4, 5, 6);
        numOfPlayers.getSelectionModel().select(2);
        numOfAI.getItems().addAll(0, 1, 2, 3, 4, 5, 6);
        numOfAI.getSelectionModel().select(0);
        minutesField.setDisable(true);

        //responsive text field for board and card data
        boardDataPath.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                boardDataPath.setPrefWidth(boardDataPath.getText().length() * 7);
            }
        });
        cardDataPath.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                cardDataPath.setPrefWidth(cardDataPath.getText().length() * 7);
            }
        });

        gameMode.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> disableMinutes(newValue));
        minutesField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                minutesField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        boardDataPath.setText("src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv");
        cardDataPath.setText("src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv");
    }

    /**
     * Loads the board view of the game with the given number of players and
     * path to both the board and card data files entered by the user.
     */
    public void createGame() throws IOException {
        errorLabel.setText("");
        if (getNumOfPlayers() + getNumOfGameAgent() > 6) {
            errorLabel.setText(errorLabel.getText() + "\nThe maximum number of players allowed to play the game is 6.");
        } else if (getNumOfPlayers() + getNumOfGameAgent() < 2) {
            errorLabel.setText(errorLabel.getText() + "\nThe minimum number of players required to play the game is 2.");
        }
        if (getGameMode().equals("Abridged Game") && minutesField.getText().matches("0+")) {
            errorLabel.setText(errorLabel.getText() + "\n0 minutes is not a valid time configuration.");
        } else if (getGameMode().equals("Abridged Game") && minutesField.getText().equals("")) {
            errorLabel.setText(errorLabel.getText() + "\nPlease enter the length of time the game should last for.");
        }
        if (errorLabel.getText().equals("")) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("WatsonGames.fxml"));
            Parent boardView = loader.load();
            Scene boardViewScene = new Scene(boardView);

            Controller controller = loader.getController();
            controller.setBoard(getNumOfPlayers(), getBoardDataPath(), getCardDataPath(), getGameMode(), getMinutes(), getNumOfGameAgent());
            controller.initializeSquares();

            Stage window = (Stage) createGameButton.getScene().getWindow();
            window.setTitle("Board View");
            window.setScene(boardViewScene);
            window.show();
        }
    }

    /**
     * If the game mode selected is "full game",
     * then disable the TextField for minutes.
     * Otherwise, enable the TextField.
     *
     * @param gameMode The game mode
     */
    public void disableMinutes(String gameMode) {
        if (gameMode.toLowerCase().equals("full game")) {
            minutesField.setText("");
            minutesField.setDisable(true);
        } else {
            minutesField.setDisable(false);
        }
    }

    /**
     * Returns the number of players selected in the "Number of players"
     * choice box.
     *
     * @return int
     */
    public int getNumOfPlayers() {
        return numOfPlayers.getValue();
    }

    /**
     * Returns the text entered in the "Path of Board Data" text field.
     *
     * @return String
     */
    public String getBoardDataPath() {
        return boardDataPath.getText();
    }

    /**
     * Returns the text entered in the "Path of Card Data" text field.
     *
     * @return String
     */
    public String getCardDataPath() {
        return cardDataPath.getText();
    }

    /**
     * Returns the game mode selected by the user in the
     * "Game Mode" ChoiceBox
     *
     * @return String
     */
    public String getGameMode() {
        return gameMode.getValue();
    }

    /**
     * Returns the number of minutes the game
     * should last for. If the game mode is
     * "Full Game", then return 0. If the
     * "Game Mode" is abridged then return
     * the value entered by the user.
     *
     * @return int
     */
    public int getMinutes() {
        if (gameMode.getValue().toLowerCase().equals("full game")) {
            return 0;
        } else {
            return Integer.parseInt(minutesField.getText());
        }
    }

    /**
     * Returns the number of AI selected in the "Number of AI"
     * choice box.
     *
     * @return int
     */
    public int getNumOfGameAgent() {
        return numOfAI.getValue();
    }
}
