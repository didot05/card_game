package SoftwareEngineering;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for "Status.fxml".
 */
public class StatusController implements Initializable {
    public VBox root;
    private Controller controller;
    private ArrayList<Label> playerLabels;
    private ArrayList<ListView<String>> playerProperties;
    public ListView<String> buyableProperties;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerLabels = new ArrayList<Label>();
        playerProperties = new ArrayList<>();

        for (Node hbox : root.getChildren()) {
            if (hbox instanceof HBox) {
                for (Node vbox : ((HBox) hbox).getChildren()) {
                    if (vbox instanceof VBox) {
                        for (Node n : ((VBox) vbox).getChildren()) {
                            if (n instanceof Label) {
                                playerLabels.add((Label) n);
                            } else if (n instanceof ListView) {
                                playerProperties.add((ListView<String>) n);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Populates the GUI, displaying the worth, net worth and list
     * of all properties every player owns.
     *
     * @param board The board
     */
    public void initialize(Board board) {
        int maxPlayers = 6;
        int numOfPlayers = board.getAllPlayers().size();
        for (int i = 0; i < numOfPlayers; i++) {
            Player currentPlayer = board.getAllPlayers().get(i);
            playerLabels.get(i).setText("Player " + currentPlayer.getId() + "; Balance: £" + currentPlayer.getBalance() +
                    ", Net Worth: £" + currentPlayer.getNetWorth());
            for (Square square : board.getAllPlayers().get(i).getOwnedSquares()) { // for every property this player owns
                playerProperties.get(i).getItems().add("Name: " + square.getName() + ", Group: " + square.getGroup() + ", Total Selling price: £" + square.calculateSquareWorth() +
                        ", Current rent: £" + square.getProperty().getCurrentRent());
            }
        }
        for (int i = 0; i < maxPlayers - numOfPlayers; i++) {
            ((VBox) playerLabels.get(maxPlayers - 1 - i).getParent()).getChildren().remove(playerLabels.get(maxPlayers - 1 - i));
            ((VBox) playerProperties.get(maxPlayers - 1 - i).getParent()).getChildren().remove(playerProperties.get(maxPlayers - 1 - i));
        }
        for (Square s : board.getAllSquares()) {
            if (s.getCanBeBought()) {
                buyableProperties.getItems().add(s.getName());
            }
        }
    }

    /**
     * Sets the controller field of the BuyAndSellController object
     * to the controller given in the parameter.
     *
     * @param controller The controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
