package SoftwareEngineering;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for "BuySell.fxml"
 */
public class BuySellController implements Initializable {
    public HBox root;
    public Label playerLabel, actionLabel, selectedProperty, numOfHouses, numOfHotels;
    public Button improveButton, sellButton;
    public ListView<String> listProperties, listImprovements, listTransactions;
    private Controller controller;
    private Player currentPlayer;
    private Square currentSquare;
    private int property = 0;
    private int house = 1;
    private int hotel = 2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actionLabel.setText("");
        selectedProperty.setText("");
        numOfHouses.setText("");
        numOfHotels.setText("");
    }

    /**
     * Initializes the GUI view. Adds listeners to
     * listProperties and listImprovements.
     *
     * @param board The board object
     */
    public void initialize(Board board) {
        currentPlayer = board.getCurrentPLayer();
        playerLabel.setText("List of properties for Player " + currentPlayer.getId() + ":");

        for (Square square : currentPlayer.getOwnedSquares()) {
            listProperties.getItems().add(square.getName());
        }
        //listener for listProperties
        listProperties.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            listImprovements.getItems().clear();
            for (Square square : currentPlayer.getOwnedSquares()) {
                if (square.getName().equals(t1)) {
                    currentSquare = square;
                }
            }
            if (currentSquare.getProperty().getImprovementCosts().size() == 2) { //its a property
                listImprovements.getItems().add("Sell " + currentSquare.getName() + " for £" + currentSquare.getCost());
                listImprovements.getItems().add("Cost of buying/selling a house: £" + currentSquare.getProperty().getHouseCost());
                listImprovements.getItems().add("Cost of buying/selling a hotel: £" + currentSquare.getProperty().getHotelCost());
                improveButton.setDisable(false);
            } else if (currentSquare.getProperty().getRentPrices().length == 4) {//its a station
                listImprovements.getItems().add("Sell " + currentSquare.getName() + " for £" + currentSquare.getCost());
                listImprovements.getItems().add("The rent on Stations will increase the more stations you own!");
                improveButton.setDisable(true);
                for (int i = 0; i < currentSquare.getProperty().getRentPrices().length; i++) {
                    listImprovements.getItems().add("Owning " + (i + 1) + " stations, the rent will be £" + currentSquare.getProperty().getRentPrices()[i]);
                }
            } else if (currentSquare.getProperty().getRentPrices().length == 2) {//its a utility
                listImprovements.getItems().add("Sell " + currentSquare.getName() + " for £" + currentSquare.getCost());
                listImprovements.getItems().add("The rent on Utilities will increase the more utilities you own!");
                improveButton.setDisable(true);
                for (int i = 0; i < currentSquare.getProperty().getRentPrices().length; i++) {
                    listImprovements.getItems().add("Owning " + (i + 1) + " utilities, the rent will be £" + currentSquare.getProperty().getRentPrices()[i]
                            + " times the total rolled by the player.");
                }
            }
            actionLabel.setText("");
            listTransactions.getItems().clear();
            updateLabels();
        });
        //listener for listImprovements
        listImprovements.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (listImprovements.getItems().indexOf(t1) == property) {
                actionLabel.setText("Sell " + currentSquare.getName() + " for £" + currentSquare.getCost());
            }
            if (currentSquare.getProperty().getImprovementCosts().size() == 2) {//its a property
                if (listImprovements.getItems().indexOf(t1) == house) {
                    actionLabel.setText("Buy/Sell house for: £" + currentSquare.getProperty().getHouseCost());
                } else if (listImprovements.getItems().indexOf(t1) == hotel) {
                    actionLabel.setText("Buy/Sell hotel for: £" + currentSquare.getProperty().getHotelCost());
                }
            }
        });
    }

    /**
     * Handles the event when the user clicks the "Buy" button.
     * The GUI is updated depending on whether the transaction
     * was successful or not. It will also update the player
     * balance labels in the Board window.
     */
    public void improve() {
        boolean bought = false;
        if (listImprovements.getSelectionModel().getSelectedItem().equals(listImprovements.getItems().get(house))) {//buying a house
            if (ownsAllGroup()) {
                bought = currentPlayer.buyHouse(currentSquare);
            }
            if (bought) {
                listTransactions.getItems().add("Transaction complete. Bought house on " + currentSquare.getName());
                controller.addToGameLog("Player " + currentPlayer.getId() + " bought a house on " + currentSquare.getName()
                        + " for £" + currentSquare.getProperty().getHouseCost());
            } else {
                listTransactions.getItems().addAll("Transaction declined for one or more of the following reasons: ", "(1) Not enough funds.",
                        "(2) Maximum 4 houses on a property.", "(3) Must own all properties of the colour group.",
                        "(4) There may never be a difference of more than 1 house between the properties in that set");
            }
        } else if (listImprovements.getSelectionModel().getSelectedItem().equals(listImprovements.getItems().get(hotel))) {//buying a hotel
            if (ownsAllGroup()) {
                bought = currentPlayer.buyHotel(currentSquare);
            }
            if (bought) {
                listTransactions.getItems().add("Transaction complete. Bought Hotel on " + currentSquare.getName());
                controller.addToGameLog("Player " + currentPlayer.getId() + " bought a hotel on " + currentSquare.getName()
                        + " for £" + currentSquare.getProperty().getHouseCost());
            } else {
                listTransactions.getItems().addAll("Transaction declined for one or more of the following reasons: ", "(1) Not enough funds.",
                        "(2) Maximum 1 hotel on a property", "(3) There may never be a difference of more than 1 house between the properties in that set");
            }
        } else {
            listTransactions.getItems().add("Please click \"Sell\", not \"Buy\", to sell " + currentSquare.getName());
        }
        updateLabels();
        controller.updateBalanceLabels();
    }

    /**
     * Handles the event when the user clicks the "Sell" button.
     * The GUI is updated depending on whether the transaction
     * was successful or not. It will also update the player
     * balance labels in the Board window.
     */
    public void sell() {
        boolean sold;
        if (listImprovements.getSelectionModel().getSelectedItem().equals(listImprovements.getItems().get(house))) {//selling a house
            sold = currentPlayer.sellHouse(currentSquare);
            if (sold) {
                listTransactions.getItems().add("Transaction complete. Sold house on " + currentSquare.getName());
                controller.addToGameLog("Player " + currentPlayer.getId() + " sold a house on " + currentSquare.getName()
                        + " for £" + currentSquare.getProperty().getHouseCost());
            } else {
                listTransactions.getItems().addAll("Transaction declined for one or more of the following reasons: ", "(1) No houses to sell",
                        "(2) You must sell the hotel before selling houses.", "(3) There may never be a difference of more than 1 house between the properties in that set");
            }
        } else if (listImprovements.getSelectionModel().getSelectedItem().equals(listImprovements.getItems().get(hotel))) {//selling a hotel
            sold = currentPlayer.sellHotel(currentSquare);
            if (sold) {
                listTransactions.getItems().add("Transaction complete. Sold hotel on " + currentSquare.getName());
                controller.addToGameLog("Player " + currentPlayer.getId() + " sold a hotel on " + currentSquare.getName()
                        + " for £" + currentSquare.getProperty().getHotelCost());
            } else {
                listTransactions.getItems().addAll("Transaction declined for the following reasons: ", "(1) No hotel to sell",
                        "(3) There may never be a difference of more than 1 house between the properties in that set");
            }
        } else if (listImprovements.getSelectionModel().getSelectedItem().equals(listImprovements.getItems().get(property))) {//selling the property
            sold = currentPlayer.sellProperty(currentSquare);
            if (sold) {
                controller.addToGameLog("Player " + currentPlayer.getId() + " sold " + currentSquare.getName() + " for £" + currentSquare.getCost());
                listProperties.getItems().remove(currentSquare.getName());
            } else {
                listTransactions.getItems().addAll("Transaction declined.", "(1) You must sell all houses and hotels before selling the hotel.",
                        "(2) You must sell all houses/hotels on all properties belonging to " + currentSquare.getGroup());
            }
        } else {
            listTransactions.getItems().addAll("Not a valid action.", "Please make sure you have chosen an appropriate transaction.");
        }
        updateLabels();
        controller.updateBalanceLabels();
    }

    /**
     * Updates all GUI components of the window with respect to
     * the current square selected.
     */
    public void updateLabels() {
        selectedProperty.setText("Selected Property: " + currentSquare.getName());
        if (currentSquare.getGroup().equals("Station") || currentSquare.getGroup().equals("Utilities")) {
            numOfHouses.setText("");
            numOfHotels.setText("");
        } else {
            numOfHouses.setText("Number of Houses: " + currentSquare.getProperty().numOfHouses());
            numOfHotels.setText("Number of Hotel: " + currentSquare.getProperty().numOfHotels());
        }
    }

    /**
     * Returns true if the player owns all properties of the
     * colour group, false otherwise.
     *
     * @return boolean
     */
    public boolean ownsAllGroup() {
        return controller.getBoard().getNumOfGroup(currentSquare.getGroup().toLowerCase()) == currentPlayer.numOfOwnedGroup(currentSquare.getGroup().toLowerCase());
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
