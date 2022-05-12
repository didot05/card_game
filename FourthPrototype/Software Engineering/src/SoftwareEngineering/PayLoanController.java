package SoftwareEngineering;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for "PayLoan.fxml".
 */
public class PayLoanController {
    public Button buyButton;
    public Label playerLabel, actionLabel, transactionLabel;
    public ListView<String> listProperties;
    private Controller controller;
    private Player currentPlayer;
    private Square selectedSquare;

    /**
     * Populates the GUI with information
     * about the current player and the list
     * of all mortgaged properties that player
     * owns. Also adds a listener to listProperties.
     *
     * @param board The board
     */
    public void initialize(Board board) {
        actionLabel.setText("");
        transactionLabel.setText("");
        this.currentPlayer = board.getCurrentPLayer();
        playerLabel.setText("List of mortgaged properties for Player " + currentPlayer.getId());
        for (Square square : currentPlayer.getOwnedSquares()) {
            if (square.getMortgaged()) {
                listProperties.getItems().add(square.getName());
            }
        }
        // listener for listProperties
        listProperties.getSelectionModel().selectedItemProperty().addListener((observableValue, s, name) -> {
            for (Square square : currentPlayer.getOwnedSquares()) {
                if (square.getName().equals(name)) {
                    selectedSquare = square;
                }
            }
            if (selectedSquare.getMortgaged()) {
                actionLabel.setText("Pay off the mortgage of " + selectedSquare.getName() + " for £" + selectedSquare.getSellingPrice());
            } else {
                actionLabel.setText("");
            }
        });
    }

    /**
     * Handles the event when the player
     * clicks "Buy". The player pays off
     * the mortgage of the selected property
     * if they have enough money in their
     * balance. Transaction is declined if
     * there is not enough money.
     */
    public void buy() {
        if (selectedSquare == null) {
            actionLabel.setText("Please select a property to buy first");
        } else {
            if (currentPlayer.payMortgage(selectedSquare)) {
                transactionLabel.setText("Transaction successful. You have paid off the mortgage for " + selectedSquare.getName());
                controller.addToGameLog("Player " + currentPlayer.getId() + " has paid off the mortgage of " + selectedSquare.getName());
                listProperties.getItems().remove(selectedSquare.getName());
                listProperties.getSelectionModel().clearSelection();
                selectedSquare = null;
            } else {
                transactionLabel.setText("Transaction declined. You do not have £" + selectedSquare.getSellingPrice() + " in your balance");
            }
        }
        controller.updateBalanceLabels();
    }

    /**
     * Sets the controller field of the ImproveAndSellController object
     * to the controller given in the parameter.
     *
     * @param controller The controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
