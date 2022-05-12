package SoftwareEngineering;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for "Menu.fxml"
 */
public class MenuController implements Initializable {
    public Button createGameButton;
    public ChoiceBox<Integer> numOfPlayers;
    public TextField boardDataPath;
    public TextField cardDataPath;

    /**
     * Loads the board view of the game with the given number of players and
     * path to both the board and card data files entered by the user.
     */
    public void createGame() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("WatsonGames.fxml"));
        Parent boardView = loader.load();

        Scene boardViewScene = new Scene(boardView);

        Controller controller = loader.getController();
        controller.setBoard(getNumOfPlayers(), getBoardDataPath(), getCardDataPath());
        controller.initializeSquares();

        Stage window = (Stage) createGameButton.getScene().getWindow();
        window.setScene(boardViewScene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numOfPlayers.getItems().addAll(2, 3, 4, 5, 6);
        numOfPlayers.getSelectionModel().select(0);

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

        boardDataPath.setText("src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv");
        cardDataPath.setText("src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv");
    }

    /**
     * Returns the number of players entered in the "Number of players"
     * text field.
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
}
