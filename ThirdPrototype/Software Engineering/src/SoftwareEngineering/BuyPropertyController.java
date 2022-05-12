package SoftwareEngineering;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BuyPropertyController {
    public Button yesButton, noButton, confirmButton;
    public Label positionOfSquare, nameOfSquare, groupOfSquare, costOfSquare, rentOfSquare, improvementOfSquare;
    private Controller controller;
    private Player currentPlayer;
    private Square currentSquare;
    private boolean buy;

    public void initialize(Board board) {
        this.buy = false;
        currentSquare = board.getAllSquares().get(board.getCurrentPLayer().getPosition());
        currentPlayer = board.getCurrentPLayer();
        positionOfSquare.setText("Position: " + currentSquare.getPosition());
        nameOfSquare.setText("Name: " + currentSquare.getName());
        groupOfSquare.setText("Group: " + currentSquare.getGroup());
        costOfSquare.setText("Cost: £" + currentSquare.getCost());
        rentOfSquare.setText("Rental Prices: " + Arrays.toString(currentSquare.getProperty().getRentPrices()));

        if (currentSquare.getProperty().getImprovementCosts().size() != 0) {
            improvementOfSquare.setText("Improvement Costs for a House: £" + currentSquare.getProperty().getImprovementCosts().get(0) +
                    ", Hotel: £" + currentSquare.getProperty().getImprovementCosts().get(1));
        } else {
            improvementOfSquare.setText("No improvement costs");
        }
    }

    public void wantToBuy() {
        this.buy = true;
        yesButton.setDisable(true);
        noButton.setDisable(false);
    }

    public void dontWantToBuy() {
        this.buy = false;
        yesButton.setDisable(false);
        noButton.setDisable(true);
    }

    public void confirmChoice() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        boolean bought = false;
        if (this.buy) {
            bought = currentPlayer.buyProperty(currentSquare);
        }
        if (bought) {
            controller.addToGameLog("Player " + currentPlayer.getId() + " bought " + currentSquare.getName() + " for £" + currentSquare.getCost());
        } else {
            controller.addToGameLog("Player " + currentPlayer.getId() + " has insufficient funds or has rejected the offer to purchase " + currentSquare.getName());
            controller.addToGameLog("Starting an auction");
        }
        controller.updateBalanceLabels();
        stage.close();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
