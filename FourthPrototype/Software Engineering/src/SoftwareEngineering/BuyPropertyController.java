package SoftwareEngineering;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BuyPropertyController {
    public Button yesButton, noButton, mortgageButton, confirmButton;
    public Label positionOfSquare, nameOfSquare, groupOfSquare, costOfSquare, rentOfSquare, improvementOfSquare;
    private Controller controller;
    private Player currentPlayer;
    private Square currentSquare;
    private boolean buy, mortgage;

    /**
     * Populates the GUI with information
     * about the square up for sale.
     *
     * @param board The board
     */
    public void initialize(Board board) {
        this.buy = false;
        noButton.setDisable(true);
        currentSquare = board.getAllSquares().get(board.getCurrentPLayer().getPosition());
        currentPlayer = board.getCurrentPLayer();
        positionOfSquare.setText("Position: " + currentSquare.getPosition());
        nameOfSquare.setText("Name: " + currentSquare.getName());
        groupOfSquare.setText("Group: " + currentSquare.getGroup());
        costOfSquare.setText("Cost: £" + currentSquare.getCost() + ", Mortgage: £" + currentSquare.getCost() / 2);
        rentOfSquare.setText("Rental Prices: " + Arrays.toString(currentSquare.getProperty().getRentPrices()));

        if (currentSquare.getProperty().getImprovementCosts().size() != 0) {
            improvementOfSquare.setText("Improvement Costs for a House: £" + currentSquare.getProperty().getImprovementCosts().get(0) +
                    ", Hotel: £" + currentSquare.getProperty().getImprovementCosts().get(1));
        } else {
            improvementOfSquare.setText("No improvement costs");
        }
    }

    /**
     * Handles the event when the user
     * clicks "Yes". Disables "Yes"
     * button and enables "No" and
     * "Mortgage" button.
     */
    public void wantToBuy() {
        this.buy = true;
        yesButton.setDisable(true);
        noButton.setDisable(false);
        mortgageButton.setDisable(false);
    }

    /**
     * Handles the event when the user
     * click "No". Disables the "No"
     * button and enables "Yes"
     * and "Mortgage" button.
     */
    public void dontWantToBuy() {
        this.buy = false;
        this.mortgage = false;
        yesButton.setDisable(false);
        noButton.setDisable(true);
        mortgageButton.setDisable(false);
    }

    /**
     * Handles the event when the user
     * clicks "Mortgage". Disables the
     * "Mortgage" button and enables
     * the "Yes" and "No" button.
     */
    public void mortgage() {
        this.buy = false;
        this.mortgage = true;
        yesButton.setDisable(false);
        noButton.setDisable(false);
        mortgageButton.setDisable(true);
    }

    /**
     * Handles the event when the user
     * clicks "Confirm Choice" button.
     * If the player clicks "Yes" and
     * has enough money in their balance
     * to buy the square, then they buy the
     * square. If the player does not have
     * enough money, or does not want to
     * buy the property, then the auction
     * is started.
     */
    public void confirmChoice() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        boolean bought = false;
        if (this.buy) {
            bought = currentPlayer.buyProperty(currentSquare);
        } else if (this.mortgage) {
            bought = currentPlayer.mortgageProperty(currentSquare);
        }
        if (bought) {
            if (currentSquare.getMortgaged()) {
                controller.addToGameLog("Player " + currentPlayer.getId() + " mortgaged " + currentSquare.getName() + " for £" + (currentSquare.getCost() / 2));
            } else {
                controller.addToGameLog("Player " + currentPlayer.getId() + " bought " + currentSquare.getName() + " for £" + currentSquare.getCost());
            }
        } else {
            controller.addToGameLog("Player " + currentPlayer.getId() + " has insufficient funds or has rejected the offer to purchase " + currentSquare.getName());
            controller.addToGameLog("Starting an auction");
            createAuctionWindow();
        }
        controller.updateBalanceLabels();
        stage.close();
    }

    /**
     * Handles the event the when a player
     * rejects the offer to buy a property.
     * Creates and loads the auction window.
     */
    public void createAuctionWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Auction.fxml"));
        Parent auctionView = null;
        try {
            auctionView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AuctionController ac = loader.getController();
        ac.setController(this.controller);
        ac.initialize(this.controller.getBoard());

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Auction");
        stage.setScene(new Scene(auctionView));
        stage.showAndWait();
    }

    /**
     * Sets the Controller field to the
     * controller given in the parameter.
     *
     * @param controller The controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
