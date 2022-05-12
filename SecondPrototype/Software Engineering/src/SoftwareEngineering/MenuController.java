package SoftwareEngineering;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public Button createGameButton;

    public void createGame() throws IOException {
        Parent boardView = FXMLLoader.load(getClass().getResource("WatsonGames.fxml"));
        Scene boardViewScene = new Scene(boardView);
        Stage window = (Stage) createGameButton.getScene().getWindow();

        window.setScene(boardViewScene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
