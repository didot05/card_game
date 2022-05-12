package SoftwareEngineering;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for "BuySell.fxml"
 */
public class BuySellController implements Initializable {
    public HBox root;
    public Label playerLabel, actionLabel, selectedProperty, numOfHouses, numOfHotels;
    public Button buyButton, sellButton;
    public ListView<String> listProperties, listImprovements, listTransactions;
    private Controller controller;
    private Player currentPlayer;
    private Player owedPlayer;
    private Square selectedSquare;
    private int property = 0;
    private int house = 1;
    private int hotel = 2;
    private int rentToPay;
    private boolean payingRent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actionLabel.setText("");
        selectedProperty.setText("");
        numOfHouses.setText("");
        numOfHotels.setText("");
        payingRent = false;
        owedPlayer = null;
        rentToPay = 0;
    }

    /**
     * Initializes the GUI view with all
     * of the properties the player owns.
     * Adds listeners to listProperties
     * and listImprovements.
     *
     * @param board The board object
     */
    public void initialize(Board board) {
        currentPlayer = board.getCurrentPLayer();
        Square currentSquare = board.getAllSquares().get(currentPlayer.getPosition());
        if (payingRent) {
            if (currentSquare.getOwnedBy() != null) {
                owedPlayer = currentSquare.getOwnedBy();
            }
            playerLabel.setText("Player " + currentPlayer.getId() + " currently has £" + currentPlayer.getBalance() + ", and must sell assets to pay £" + rentToPay);
        } else { //not paying rent/tax
            playerLabel.setText("List of properties for Player " + currentPlayer.getId() + ":");
        }

        for (Square square : currentPlayer.getOwnedSquares()) {
            listProperties.getItems().add(square.getName());
        }
        //listener for listProperties
        listProperties.getSelectionModel().selectedItemProperty().addListener((observableValue, s, name) -> {
            listImprovements.getItems().clear();
            for (Square square : currentPlayer.getOwnedSquares()) {
                if (square.getName().equals(name)) {
                    selectedSquare = square;
                }
            }
            if (selectedSquare.getProperty().getImprovementCosts().size() == 2) { //its a property
                listImprovements.getItems().add("Sell " + selectedSquare.getName() + " for £" + selectedSquare.getSellingPrice());
                listImprovements.getItems().add("Cost of buying/selling a house: £" + selectedSquare.getProperty().getHouseCost());
                listImprovements.getItems().add("Cost of buying/selling a hotel: £" + selectedSquare.getProperty().getHotelCost());
                buyButton.setDisable(false);
            } else if (selectedSquare.getProperty().getRentPrices().length == 4) {//its a station
                listImprovements.getItems().add("Sell " + selectedSquare.getName() + " for £" + selectedSquare.getSellingPrice());
                listImprovements.getItems().add("The rent on Stations will increase the more stations you own!");
                buyButton.setDisable(true);
                for (int i = 0; i < selectedSquare.getProperty().getRentPrices().length; i++) {
                    listImprovements.getItems().add("Owning " + (i + 1) + " stations, the rent will be £" + selectedSquare.getProperty().getRentPrices()[i]);
                }
            } else if (selectedSquare.getProperty().getRentPrices().length == 2) {//its a utility
                listImprovements.getItems().add("Sell " + selectedSquare.getName() + " for £" + selectedSquare.getSellingPrice());
                listImprovements.getItems().add("The rent on Utilities will increase the more utilities you own!");
                buyButton.setDisable(true);
                for (int i = 0; i < selectedSquare.getProperty().getRentPrices().length; i++) {
                    listImprovements.getItems().add("Owning " + (i + 1) + " utilities, the rent will be £" + selectedSquare.getProperty().getRentPrices()[i]
                            + " times the total rolled by the player.");
                }
            }
            actionLabel.setText("");
            listTransactions.getItems().clear();
            updateLabels();
        });
        //listener for listImprovements
        listImprovements.getSelectionModel().selectedItemProperty().addListener((observableValue, s, square) -> {
            if (listImprovements.getItems().indexOf(square) == property) {
                actionLabel.setText("Sell " + selectedSquare.getName() + " for £" + selectedSquare.getSellingPrice());
            }
            if (selectedSquare.getProperty().getImprovementCosts().size() == 2) {//its a property
                if (listImprovements.getItems().indexOf(square) == house) {
                    actionLabel.setText("Buy/Sell house for: £" + selectedSquare.getProperty().getHouseCost());
                } else if (listImprovements.getItems().indexOf(square) == hotel) {
                    actionLabel.setText("Buy/Sell hotel for: £" + selectedSquare.getProperty().getHotelCost());
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
                bought = currentPlayer.buyHouse(selectedSquare);
            }
            if (bought) {
                listTransactions.getItems().add("Transaction complete. Bought house on " + selectedSquare.getName());
                controller.addToGameLog("Player " + currentPlayer.getId() + " bought a house on " + selectedSquare.getName()
                        + " for £" + selectedSquare.getProperty().getHouseCost());
            } else {
                listTransactions.getItems().addAll("Transaction declined for one or more of the following reasons: ", "(1) Not enough funds.",
                        "(2) Maximum 4 houses on a property.", "(3) Must own all properties of the colour group.",
                        "(4) There may never be a difference of more than 1 house between the properties in that set");
            }
        } else if (listImprovements.getSelectionModel().getSelectedItem().equals(listImprovements.getItems().get(hotel))) {//buying a hotel
            if (ownsAllGroup()) {
                bought = currentPlayer.buyHotel(selectedSquare);
            }
            if (bought) {
                listTransactions.getItems().add("Transaction complete. Bought Hotel on " + selectedSquare.getName());
                controller.addToGameLog("Player " + currentPlayer.getId() + " bought a hotel on " + selectedSquare.getName()
                        + " for £" + selectedSquare.getProperty().getHouseCost());
            } else {
                listTransactions.getItems().addAll("Transaction declined for one or more of the following reasons: ", "(1) Not enough funds.",
                        "(2) Maximum 1 hotel on a property", "(3) There may never be a difference of more than 1 house between the properties in that set");
            }
        } else {
            listTransactions.getItems().add("Please click \"Sell\", not \"Buy\", to sell " + selectedSquare.getName());
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
            sold = currentPlayer.sellHouse(selectedSquare);
            if (sold) {
                listTransactions.getItems().add("Transaction complete. Sold house on " + selectedSquare.getName());
                controller.addToGameLog("Player " + currentPlayer.getId() + " sold a house on " + selectedSquare.getName()
                        + " for £" + selectedSquare.getProperty().getHouseCost());
            } else {
                listTransactions.getItems().addAll("Transaction declined for one or more of the following reasons: ", "(1) No houses to sell",
                        "(2) You must sell the hotel before selling houses.", "(3) There may never be a difference of more than 1 house between the properties in that set");
            }
        } else if (listImprovements.getSelectionModel().getSelectedItem().equals(listImprovements.getItems().get(hotel))) {//selling a hotel
            sold = currentPlayer.sellHotel(selectedSquare);
            if (sold) {
                listTransactions.getItems().add("Transaction complete. Sold hotel on " + selectedSquare.getName());
                controller.addToGameLog("Player " + currentPlayer.getId() + " sold a hotel on " + selectedSquare.getName()
                        + " for £" + selectedSquare.getProperty().getHotelCost());
            } else {
                listTransactions.getItems().addAll("Transaction declined for the following reasons: ", "(1) No hotel to sell",
                        "(3) There may never be a difference of more than 1 house between the properties in that set");
            }
        } else if (listImprovements.getSelectionModel().getSelectedItem().equals(listImprovements.getItems().get(property))) {//selling the property
            int sellingPrice = selectedSquare.getSellingPrice();
            sold = currentPlayer.sellProperty(selectedSquare);
            if (sold) {
                controller.addToGameLog("Player " + currentPlayer.getId() + " sold " + selectedSquare.getName() + " for £" + sellingPrice);
                listProperties.getItems().remove(selectedSquare.getName());
            } else {
                listTransactions.getItems().addAll("Transaction declined.", "(1) You must sell all houses and hotels before selling the hotel.",
                        "(2) You must sell all houses/hotels on all properties belonging to " + selectedSquare.getGroup());
            }
        } else {
            listTransactions.getItems().addAll("Not a valid action.", "Please make sure you have chosen an appropriate transaction.");
        }
        if (payingRent) {
            if (currentPlayer.getBalance() >= rentToPay) {
                if (owedPlayer != null) {
                    currentPlayer.payPlayer(owedPlayer, rentToPay);
                    controller.addToGameLog(currentPlayer.getName() + " has paid a rent of £" + rentToPay + " to Player " + owedPlayer.getId());
                } else {
                    currentPlayer.payBank(rentToPay);
                    controller.addToGameLog(currentPlayer.getName() + " has paid £" + rentToPay + " to the bank.");
                }
                payingRent = false;
                owedPlayer = null;
                Stage stage = (Stage) sellButton.getScene().getWindow();
                stage.close();
            }
        }
        updateLabels();
        controller.updateBalanceLabels();
    }

    /**
     * Updates all GUI components of the window with respect to
     * the current square selected.
     */
    public void updateLabels() {
        selectedProperty.setText("Selected Property: " + selectedSquare.getName());
        if (selectedSquare.getGroup().equals("Station") || selectedSquare.getGroup().equals("Utilities")) {
            numOfHouses.setText("");
            numOfHotels.setText("");
        } else {
            numOfHouses.setText("Number of Houses: " + selectedSquare.getProperty().numOfHouses());
            numOfHotels.setText("Number of Hotel: " + selectedSquare.getProperty().numOfHotels());
        }
        if (payingRent) {
            playerLabel.setText("Player " + currentPlayer.getId() + " currently has £" + currentPlayer.getBalance() + ", and must sell assets to pay £" + rentToPay);
        }
    }

    /**
     * Returns true if the player owns all properties of the
     * colour group, false otherwise.
     *
     * @return boolean
     */
    public boolean ownsAllGroup() {
        return controller.getBoard().getNumOfGroup(selectedSquare.getGroup().toLowerCase()) == currentPlayer.numOfOwnedGroup(selectedSquare.getGroup().toLowerCase());
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

    /**
     * States if this window is for paying a rent/tax,
     * or just buying/selling assets.
     *
     * @param payingRent Whether this window is for paying rent/taxes
     */
    public void setPayingRent(boolean payingRent) {
        this.payingRent = payingRent;
    }

    /**
     * Sets the rent to pay of the window
     *
     * @param rentToPay The rent to pay
     */
    public void setRentToPay(int rentToPay) {
        this.rentToPay = rentToPay;
    }
}
