package SoftwareEngineering;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("WatsonGames.fxml")); // BoardView: "WatsonGames.fxml"
        primaryStage.setTitle("Board View");
        primaryStage.setScene(new Scene(root, 1300, 750));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
