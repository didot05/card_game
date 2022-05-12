package SoftwareEngineering;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller for "WatsonGames.fxml".
 */
public class Controller implements Initializable {
    public GridPane theGrid;
    public VBox playerStatus;
    public ImageView diceImage1, diceImage2;
    public Button rollDiceButton, endTurnButton, statusButton, improveSellButton, mortgageButton, leaveGameButton;
    public Label showCurrentPlayer, freeParkingLabel, timeLeftLabel;
    public ListView<String> gameLog;
    private Board board;
    private ArrayList<StackPane> squareCoordinates;
    private ArrayList<Label> allNames;
    private ArrayList<ImageView> allGroups;
    private ArrayList<Label> playerBalance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        squareCoordinates = new ArrayList<>();
        allNames = new ArrayList<>();
        allGroups = new ArrayList<>();
        playerBalance = new ArrayList<>();

        for (Node n : theGrid.getChildren()) {
            if (n instanceof StackPane) {
                squareCoordinates.add((StackPane) n);
            }
        }

        for (StackPane s : squareCoordinates) {
            for (Node n : s.getChildren()) {
                if (n instanceof Label) {
                    allNames.add((Label) n);
                }
                if (n instanceof ImageView) {
                    allGroups.add((ImageView) n);
                }
            }
        }

        for (Node n : playerStatus.getChildren()) {
            if (n instanceof HBox) {
                for (Node label : ((HBox) n).getChildren()) {
                    if (label instanceof Label) {
                        playerBalance.add((Label) label);
                    }
                }
            }
        }
        endTurnButton.setDisable(true);
    }

    /**
     * Handles the event when a player clicks "Roll
     * Dice". Updates the board view by moving the
     * player token to their new position based on
     * the numbers rolled on the dice. Each die
     * image updates to the value it rolled on.
     */
    public void rolledTheDice() {
        Player currentPlayer = board.getCurrentPLayer();
        int numOfDice = 2;
        int total = board.getDice().roll();
        int face = board.getDice().getFirstRoll();
        ImageView currentDice = diceImage1;

        if (face * 2 == total) { // check for a "double" roll
            if (currentPlayer.getRolledDoubleDouble()) {//if the player has rolled three doubles
                currentPlayer.setRolledDouble(false);
                currentPlayer.setRolledDoubleDouble(false);
                addToGameLog(currentPlayer.getName() + " has rolled a double thrice and has been sent to jail.");
                goToJail();
            }
            if (currentPlayer.getRolledDouble()) {//if the player has rolled two doubles
                currentPlayer.setRolledDoubleDouble(true);
                addToGameLog(currentPlayer.getName() + " has rolled two consecutive doubles!");
            } else {//if the player has rolled a double
                rollDiceButton.setDisable(false);
                endTurnButton.setDisable(true);
                currentPlayer.setRolledDouble(true);
                currentPlayer.setRolledDoubleDouble(false);
                addToGameLog(currentPlayer.getName() + " rolled a double!");
            }
        } else {
            rollDiceButton.setDisable(true);
            endTurnButton.setDisable(false);
            currentPlayer.setRolledDouble(false);
            currentPlayer.setRolledDoubleDouble(false);
        }

        for (int i = 0; i < numOfDice; i++) {
            if (i == 1) {
                face = board.getDice().getSecondRoll();
                currentDice = diceImage2;
            }
            switch (face) {
                case 1:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-1.png"));
                    break;
                case 2:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-2.png"));
                    break;
                case 3:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-3.png"));
                    break;
                case 4:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-4.png"));
                    break;
                case 5:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-5.png"));
                    break;
                case 6:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-6.png"));
                    break;
            }
        }
        //remove player token from old square
        squareCoordinates.get(currentPlayer.getPosition()).getChildren().remove(currentPlayer.getCharacter());
        //set new position of player
        currentPlayer.newPosition(total);
        //move player token to new position
        squareCoordinates.get(currentPlayer.getPosition()).getChildren().add(currentPlayer.getCharacter());
        addToGameLog(currentPlayer.getName() + " rolled a " + total + " and landed on " + board.getAllSquares().get(board.getCurrentPLayer().getPosition()).getName());
        turnAction();
    }

    /**
     * Called after the player has rolled the dice to determine
     * the action of the turn based on the square the player landed
     * on.
     */
    public void turnAction() {
        Player currentPlayer = board.getCurrentPLayer();
        Square currentSquare = board.getAllSquares().get(currentPlayer.getPosition());

        if (currentSquare.getCanBeBought() && currentPlayer.getPassedGo()) {
            if (currentPlayer instanceof GameAgent) {
                buyPropertyGameAgent();
            } else {
                createBuyPropertyWindow();
            }
        } else if (!Objects.isNull(currentSquare.getOwnedBy()) && !currentSquare.getOwnedBy().equals(currentPlayer) && !currentSquare.getOwnedBy().isJailed() && !currentSquare.getMortgaged()) {//pay rent
            payRent();
        } else if (currentSquare.getGroup().toLowerCase().equals("income tax") || currentSquare.getGroup().toLowerCase().equals("super tax")) {
            payTax();
        } else if (currentSquare.getGroup().toLowerCase().equals("opportunity knocks") || currentSquare.getGroup().toLowerCase().equals("pot luck")) {
            cardAction();
        } else if (currentSquare.getGroup().toLowerCase().equals("go to jail")) {
            goToJail();
        } else if (currentSquare.getGroup().toLowerCase().equals("free parking")) {
            currentPlayer.setBalance(currentPlayer.getBalance() + board.getFreeParking());
            addToGameLog(currentPlayer.getName() + " collected £" + board.getFreeParking() + " from Free Parking");
            board.setFreeParking(0);
        }
        if (currentPlayer instanceof GameAgent) {
            buyAssetsGameAgent();
        }
        updateBalanceLabels();
    }

    /**
     * Called when an AI lands on a property
     * that is buyable. If they AI wants to
     * buy the property, then it tries to buy
     * it. If the transaction fails, an
     * auction is started.
     */
    public void buyPropertyGameAgent() {
        GameAgent currentPlayer = (GameAgent) board.getCurrentPLayer();
        Square currentSquare = board.getAllSquares().get(currentPlayer.getPosition());
        boolean bought = false;

        if (currentPlayer.getDecision()) {
            bought = currentPlayer.buyProperty(currentSquare);
        }
        if (bought) {
            addToGameLog(currentPlayer.getName() + " bought " + currentSquare.getName() + " for £" + currentSquare.getCost());
        } else {
            addToGameLog(currentPlayer.getName() + " did not purchase " + currentSquare.getName());
            addToGameLog("Starting an Auction");
            createAuctionWindow();
        }
    }

    /**
     * For every property the AI owns, it
     * decides whether it wants to purchase
     * a house/hotel on it. If yes, then they
     * try to buy a house/hotel on the property.
     */
    public void buyAssetsGameAgent() {
        GameAgent currentPlayer = (GameAgent) board.getCurrentPLayer();
        for (Square square : currentPlayer.getOwnedSquares()) {
            if (currentPlayer.getDecision() && board.ownsAllGroup(currentPlayer, square)) {
                currentPlayer.buyAssets(square);
            }
        }
    }

    /**
     * Called when a play draws a "go to jail"
     * card or lands on "go to jail" square.
     * Moves the player's token to the jail
     * and offers the player to pay a bail
     * of £50 to be released from jail. If
     * the player declines the offer, they miss
     * the next two turns and cannot collect rent.
     * If the bail is paid, then they are released
     * from prison and can take their next turn
     * as normal.
     */
    public void goToJail() {
        int freeJailCost = 50;
        Player currentPlayer = board.getCurrentPLayer();
        squareCoordinates.get(currentPlayer.getPosition()).getChildren().remove(currentPlayer.getCharacter());
        currentPlayer.setPosition(board.positionOfJail());
        squareCoordinates.get(currentPlayer.getPosition()).getChildren().add(currentPlayer.getCharacter());
        addToGameLog(currentPlayer.getName() + " has been sent to jail");

        if (currentPlayer.getJailFreeCard()) {
            currentPlayer.setJailFreeCard(false);
            addToGameLog(currentPlayer.getName() + " has used their \"Get out of Jail Free Card\" and has been released from jail.");
        } else if (currentPlayer.getBalance() >= freeJailCost) {
            if (currentPlayer instanceof GameAgent) {
                if (((GameAgent) currentPlayer).getDecision()) {
                    currentPlayer.payBank(freeJailCost);
                    addToGameLog(currentPlayer.getName() + " decided to pay bail and has been released the jail.");
                } else {
                    currentPlayer.spendDayInJail();
                    addToGameLog(currentPlayer.getName() + " decided not to pay bail and remains in jail for their next two turns.");
                }
            } else {
                createJailWindow();
            }
        } else {
            currentPlayer.spendDayInJail();
            addToGameLog(currentPlayer.getName() + " has been jailed. They miss their next 2 turns and cannot collect rent.");
        }
        rollDiceButton.setDisable(true);
        endTurnButton.setDisable(false);
        updateBalanceLabels();
    }

    /**
     * Called when a player lands on either
     * "Pot Luck" or "Opportunity Knocks".
     * The player automatically carries out the
     * instruction of the card based on the action.
     */
    public void cardAction() {
        boolean paid = true;
        Player currentPlayer = board.getCurrentPLayer();
        Square currentSquare = board.getAllSquares().get(board.getCurrentPLayer().getPosition());
        Card card;

        if (currentSquare.getGroup().toLowerCase().equals("opportunity knocks")) {
            card = board.getOpportunityKnocks().pickCard();
        } else {
            card = board.getPotLuck().pickCard();
        }
        addToGameLog(currentPlayer.getName() + " drew \"" + card.getDescription() + "\"");
        String action = card.getAction().split(" ")[0].toLowerCase();

        switch (action) {
            case "collect":
                board.collectAction(currentPlayer, card.getAction());
                break;
            case "pay":
                String[] pay = card.getAction().split(" ");
                int n = Integer.parseInt(pay[1].replaceAll("[^\\d.]", ""));
                if (!board.payAction(currentPlayer, card.getAction())) {//could not pay the amount specified.
                    if (currentPlayer.getNetWorth() >= n) {
                        if (currentPlayer instanceof GameAgent) {
                            ((GameAgent) currentPlayer).sellAssets(n);
                            currentPlayer.payBank(n);
                        } else {
                            createPayRentWindow(n);
                        }
                    } else {
                        board.declareBankruptcy(currentPlayer);
                        rollDiceButton.setDisable(false);
                        endTurnButton.setDisable(true);
                        paid = false;
                        addToGameLog(currentPlayer.getName() + " is unable to pay £" + n + " and is bankrupt.");
                    }
                    if (paid) {
                        addToGameLog(currentPlayer.getName() + " paid £" + n);
                    }
                }
                break;
            case "fine":
                String[] fine = card.getAction().split(" ");
                int i = Integer.parseInt(fine[1].replaceAll("[^\\d.]", ""));
                if (!board.fineAction(currentPlayer, card.getAction())) {//could not pay the amount specified.
                    if (currentPlayer.getNetWorth() >= i) {
                        if (currentPlayer instanceof GameAgent) {
                            ((GameAgent) currentPlayer).sellAssets(i);
                            currentPlayer.payBank(i);
                        } else {
                            createPayRentWindow(i);
                        }
                    } else {
                        board.declareBankruptcy(currentPlayer);
                        rollDiceButton.setDisable(false);
                        endTurnButton.setDisable(true);
                        paid = false;
                        addToGameLog(currentPlayer.getName() + " is unable to pay £" + i + " and is bankrupt.");
                    }
                    if (paid) {
                        addToGameLog(currentPlayer.getName() + " paid a fine of £" + i);
                    }
                }
                break;
            case "move":
                squareCoordinates.get(currentPlayer.getPosition()).getChildren().remove(currentPlayer.getCharacter());
                board.moveAction(currentPlayer, card.getAction());
                squareCoordinates.get(currentPlayer.getPosition()).getChildren().add(currentPlayer.getCharacter());
                addToGameLog(currentPlayer.getName() + " has been moved to " + board.getAllSquares().get(currentPlayer.getPosition()).getName());
                turnAction();
                break;
            case "jail":
                goToJail();
                break;
            case "free":
                currentPlayer.setJailFreeCard(true);
                addToGameLog(currentPlayer.getName() + " obtained a \"Get out of Jail Free\" card.");
                break;
            case "back":
                String[] back = card.getAction().split(" ");
                int spaces = Integer.parseInt(back[1]);

                squareCoordinates.get(currentPlayer.getPosition()).getChildren().remove(currentPlayer.getCharacter());
                board.backAction(currentPlayer, card.getAction());
                squareCoordinates.get(currentPlayer.getPosition()).getChildren().add(currentPlayer.getCharacter());
                addToGameLog(currentPlayer.getName() + " has moved back " + spaces + " spaces");
                break;
            case "repair":
                String[] costs = card.getAction().split(" ");
                int costToRepairHouse = Integer.parseInt(costs[1].replaceAll("[^\\d.]", ""));
                int costToRepairHotel = Integer.parseInt(costs[2].replaceAll("[^\\d.]", ""));
                int totalCost = 0;

                for (Square square : currentPlayer.getOwnedSquares()) {
                    if (square.getProperty() != null) {
                        totalCost += square.getProperty().numOfHouses() * costToRepairHouse;
                        totalCost += square.getProperty().numOfHotels() * costToRepairHotel;
                    }
                }
                if (!currentPlayer.payBank(totalCost)) {
                    if (currentPlayer.getNetWorth() >= totalCost) {
                        if (currentPlayer instanceof GameAgent) {
                            ((GameAgent) currentPlayer).sellAssets(totalCost);
                            currentPlayer.payBank(totalCost);
                        } else {
                            createPayRentWindow(totalCost);
                        }
                    } else {
                        paid = false;
                        board.declareBankruptcy(currentPlayer);
                        rollDiceButton.setDisable(false);
                        endTurnButton.setDisable(true);
                        addToGameLog(currentPlayer.getName() + " is unable to pay repair costs of £" + totalCost + " and is bankrupt.");
                    }
                }
                if (paid) {
                    addToGameLog(currentPlayer.getName() + " has paid repair costs of £" + totalCost);
                }
                break;
            case "receive":
                int receive = Integer.parseInt(card.getAction().split(" ")[1].replaceAll("[^\\d.]", ""));
                for (Player player : board.getAllPlayers()) {
                    if (!player.equals(currentPlayer)) {
                        player.setBalance(player.getBalance() - receive);
                        currentPlayer.setBalance(currentPlayer.getBalance() + receive);
                    }
                }
                addToGameLog(currentPlayer.getName() + " received a total of £" + receive + " from all players");
                break;
        }
        updateBalanceLabels();
    }

    /**
     * Handles the event when a player lands
     * on a square that belongs to the tax
     * group. The player must pay the tax,
     * selling assets if there is not
     * enough money in their balance. The
     * player goes bankrupt otherwise.
     */
    public void payTax() {
        Player currentPlayer = board.getCurrentPLayer();
        Square currentSquare = board.getAllSquares().get(currentPlayer.getPosition());
        int taxToPay = Integer.parseInt(currentSquare.getAction().split(" ")[1].replaceAll("[^\\d.]", ""));
        boolean paid = board.payAction(currentPlayer, currentSquare.getAction());

        if (paid) {
            addToGameLog(currentPlayer.getName() + " has paid a tax of £" + taxToPay + " to the bank.");
        } else {
            addToGameLog(currentPlayer.getName() + " does not have enough funds in their balance to pay the tax");
            if (currentPlayer.getNetWorth() >= taxToPay) {
                addToGameLog(currentPlayer.getName() + " has enough funds to pay the tax of £" + taxToPay + ". Sell houses/hotels/properties to pay the rent.");
                if (currentPlayer instanceof GameAgent) {
                    ((GameAgent) currentPlayer).sellAssets(taxToPay);
                    currentPlayer.payBank(taxToPay);
                    addToGameLog(currentPlayer.getName() + " has sold assets and has paid £" + taxToPay + " to the bank");
                } else {
                    createPayRentWindow(taxToPay);
                }
            } else {
                board.declareBankruptcy(currentPlayer);
                rollDiceButton.setDisable(false);
                endTurnButton.setDisable(true);
                addToGameLog(currentPlayer.getName() + " has gone bankrupt!");
            }
        }
        updateBalanceLabels();
    }

    /**
     * Handles the event when a player lands on
     * a property that is owned by a different
     * player. The current player must pay
     * the rent of the square, selling
     * assets if they have to. Otherwise, they
     * will go bankrupt.
     */
    public void payRent() {
        Player currentPlayer = board.getCurrentPLayer();
        Square currentSquare = board.getAllSquares().get(currentPlayer.getPosition());
        int rentToPay;
        boolean paid;

        if (currentSquare.getGroup().toLowerCase().equals("utilities")) {
            rentToPay = currentSquare.getProperty().getCurrentRent() * board.getDice().getTotal();
        } else {
            rentToPay = currentSquare.getProperty().getCurrentRent();
            if (!currentSquare.getGroup().toLowerCase().equals("station") && board.noImprovements(currentPlayer, currentSquare)) {//no houses/hotels on all squares belonging to the same colour group, double rent.
                rentToPay *= 2;
            }
        }

        paid = currentPlayer.payPlayer(currentSquare.getOwnedBy(), rentToPay);
        if (paid) {
            addToGameLog(currentPlayer.getName() + " has paid a rent of £" + rentToPay + " to Player " + currentSquare.getOwnedBy().getId());
        } else {
            addToGameLog(currentPlayer.getName() + " could not pay the rent of £" + rentToPay + " to Player " + currentSquare.getOwnedBy().getId());
            if (currentPlayer.getNetWorth() >= rentToPay) {
                addToGameLog(currentPlayer.getName() + " has enough funds to pay the rent of £" + rentToPay + " to Player:" + currentSquare.getOwnedBy().getId() + ". Sell houses/hotels/properties to pay the rent.");
                if (currentPlayer instanceof GameAgent) {
                    ((GameAgent) currentPlayer).sellAssets(rentToPay);
                    currentPlayer.payPlayer(currentSquare.getOwnedBy(), rentToPay);
                    addToGameLog(currentPlayer.getName() + " has sold assets and has paid £" + rentToPay + " to " + currentSquare.getOwnedBy().getName());
                } else {
                    createPayRentWindow(rentToPay);
                }
            } else {
                currentPlayer.payPlayer(currentSquare.getOwnedBy(), currentPlayer.getNetWorth());
                board.declareBankruptcy(currentPlayer);
                rollDiceButton.setDisable(false);
                endTurnButton.setDisable(true);
                addToGameLog(currentPlayer.getName() + " has gone bankrupt!");
            }
        }
        updateBalanceLabels();
    }

    /**
     * Handles the event when a player clicks
     * the "Leave Game" button. The player
     * is removed from the game, with all
     * their assets being sold back to the bank.
     */
    public void leaveGame() {
        Player currentPlayer = board.getCurrentPLayer();
        addToGameLog(currentPlayer.getName() + " has left the game and their assets are now owned by the bank");
        board.declareBankruptcy(currentPlayer);
        rollDiceButton.setDisable(false);
        endTurnButton.setDisable(true);
        updateBalanceLabels();
    }

    /**
     * Starts the timer for the game. With the
     * given minutes provided by the player in the
     * menu, the timer will countdown from that value
     * to 0. It updates and displays the time remaining
     * in minutes and seconds. The game will only have
     * a timer if the game mode is abridged.
     */
    public void startTimer() {
        if (!board.getFullGame()) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                int seconds = board.getTimeLeft();

                public void run() {
                    if (seconds != 0) {
                        Platform.runLater(() -> timeLeftLabel.setText("Time Left: " + seconds / 60 + " minute " + seconds % 60 + " seconds"));
                        seconds--;
                        board.decrementTime();
                    } else {
                        Platform.runLater(() -> timeLeftLabel.setText("Time is up! Waiting for all players to take equal number of turns"));
                        timer.cancel();
                        Platform.runLater(Controller.this::updateBalanceLabels);
                    }
                }
            }, 1000, 1000);
        }
    }

    /**
     * Handles the event when a user clicks "End Turn".
     * Sets the next player and updates the current player label.
     */
    public void endTheTurn() {
        board.getCurrentPLayer().incrementNumOfTurn();
        board.setNextPlayer();

        if (board.getCurrentPLayer().isJailed()) {
            board.getCurrentPLayer().spendDayInJail();
            if (!board.getCurrentPLayer().isJailed()) {
                addToGameLog(board.getCurrentPLayer().getName() + " has been freed from jail. They miss this turn, buy will take their next turn as normal.");
            } else {
                addToGameLog(board.getCurrentPLayer().getName() + " is jailed and misses this turn.");
            }
            endTheTurn();
        }
        rollDiceButton.setDisable(false);
        endTurnButton.setDisable(true);
        updateBalanceLabels();
    }

    /**
     * Updates the balance labels of all players.
     */
    public void updateBalanceLabels() {
        int maxPlayers = 6;
        int numOfPlayers = board.getAllPlayers().size();
        for (int i = 0; i < numOfPlayers; i++) {
            playerBalance.get(i).setText(board.getAllPlayers().get(i).getName() + ": £" + board.getAllPlayers().get(i).getBalance()
                    + "\nTurns taken: " + board.getAllPlayers().get(i).getNumOfTurns());
        }
        for (int i = 0; i < maxPlayers - numOfPlayers; i++) { //set labels of players not in the game to ""
            playerBalance.get(maxPlayers - 1 - i).setText("");
        }
        freeParkingLabel.setText("Amount of money in Free Parking: £" + board.getFreeParking());
        if (board.hasGameEnded()) {
            Stage stage = (Stage) leaveGameButton.getScene().getWindow();
            stage.close();
            createEndGameWindow();
        }
        showCurrentPlayer.setText("Current Player: " + board.getCurrentPLayer().getName());
        if (board.getCurrentPLayer() instanceof GameAgent) {
            leaveGameButton.setDisable(true);
            mortgageButton.setDisable(true);
            improveSellButton.setDisable(true);
        } else {
            leaveGameButton.setDisable(false);
            mortgageButton.setDisable(false);
            improveSellButton.setDisable(false);
        }
    }

    /**
     * Handles the event when a player either
     * lands on "Go to Jail" or draws the
     * "Go to Jail" card. Creates a window
     * offering the player to pay the bail
     * in order to be released from prison.
     */
    public void createJailWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Jail.fxml"));
        Parent jailView = null;
        try {
            jailView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JailController jc = loader.getController();
        jc.setController(this);
        jc.initialize(this.board);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pay bail?");
        stage.setScene(new Scene(jailView));
        stage.showAndWait();
    }

    /**
     * Handles the event when a player lands on a property
     * that can be bought. Creates a new window from
     * "BuyProperty.fxml".
     */
    public void createBuyPropertyWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BuyProperty.fxml"));
        Parent buyPropertyView = null;
        try {
            buyPropertyView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BuyPropertyController bpc = loader.getController();
        bpc.setController(this);
        bpc.initialize(this.board);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Buy Property?");
        stage.setScene(new Scene(buyPropertyView));
        stage.showAndWait();
    }

    /**
     * Handles the event when a player
     * lands on a square that requires
     * them to pay money. The player
     * has enough money to pay the amount
     * owed by selling assets. Creates
     * and loads a new window from
     * "BuySell.fxml".
     */
    public void createPayRentWindow(int rentToPay) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BuySell.fxml"));
        Parent payRentView = null;
        try {
            payRentView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BuySellController isc = loader.getController();
        isc.setController(this);
        isc.setPayingRent(true);
        isc.setRentToPay(rentToPay);
        isc.initialize(this.board);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pay Funds");
        stage.setScene(new Scene(payRentView));
        stage.showAndWait();
    }

    /**
     * Handles the event when a user clicks "Status".
     * Creates a new window from "Status.fxml".
     */
    public void createStatusWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Status.fxml"));
        Parent statusView = null;
        try {
            statusView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StatusController sc = loader.getController();
        sc.setController(this);
        sc.initialize(this.board);

        Stage stage = new Stage();
        stage.setTitle("State of all players");
        stage.setScene(new Scene(statusView));
        stage.show();
    }

    /**
     * Handles the event when a user clicks "Buy/Sell".
     * Creates a new window from "BuySell.fxml".
     */
    public void createBuySellWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BuySell.fxml"));
        Parent buyAndSellView = null;
        try {
            buyAndSellView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BuySellController isc = loader.getController();
        isc.setController(this);
        isc.initialize(this.board);

        Stage stage = new Stage();
        stage.setTitle("Improve and Sell Properties");
        stage.setScene(new Scene(buyAndSellView));
        stage.show();
    }

    /**
     * Handles the event when the user
     * clicks "Pay off Mortgage". Creates
     * a new window from "PayLoad.fmxl".
     */
    public void createMortgageWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("PayLoan.fxml"));
        Parent payLoanView = null;
        try {
            payLoanView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PayLoanController plc = loader.getController();
        plc.setController(this);
        plc.initialize(this.board);

        Stage stage = new Stage();
        stage.setTitle("Pay off Mortgage");
        stage.setScene(new Scene(payLoanView));
        stage.show();
    }

    /**
     * Handles the event when the game is over.
     * Opens a new window listing the winners
     * of the game.
     */
    public void createEndGameWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("EndGame.fxml"));
        Parent endGameView = null;
        try {
            endGameView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        EndGameController egc = loader.getController();
        egc.initialize(this.board);

        Stage stage = new Stage();
        stage.setTitle("Congratulations!");
        stage.setScene(new Scene(endGameView));
        stage.show();
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
        ac.setController(this);
        ac.initialize(this.getBoard());

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Auction");
        stage.setScene(new Scene(auctionView));
        stage.showAndWait();
    }

    /**
     * Adds the given message to the game log.
     *
     * @param message Message to be added
     */
    public void addToGameLog(String message) {
        gameLog.getItems().add(message);
    }

    /**
     * Populates the GUI with the names, background images of a square and
     * character sprites.
     */
    public void initializeSquares() {
        for (int i = 0; i < board.getAllSquares().size(); i++) {
            allNames.get(i).setText(board.getAllSquares().get(i).getName());
            if (board.getAllSquares().get(i).getGroup() != null) {
                allGroups.get(i).setImage(setGroupImage(board.getAllSquares().get(i).getGroup()));
            }
        }
        //add all player tokens to starting position and add characters
        for (Player p : board.getAllPlayers()) {
            setPlayerImage(p);
            squareCoordinates.get(0).getChildren().add(p.getCharacter());
        }
        updateBalanceLabels();
        showCurrentPlayer.setText("Current Player: Player " + (board.getCurrentPLayer().getId()));
        startTimer();
    }

    /**
     * Sets the character field of the specified player
     * based on their ID.
     *
     * @param player The player to set the character image
     */
    public void setPlayerImage(Player player) {
        switch (player.getId()) {
            case 1:
                player.setCharacter(new ImageView(new Image("SoftwareEngineering/Images/cat.png")));
                break;
            case 2:
                player.setCharacter(new ImageView(new Image("SoftwareEngineering/images/boot.png")));
                break;
            case 3:
                player.setCharacter(new ImageView(new Image("SoftwareEngineering/images/goblet.png")));
                break;
            case 4:
                player.setCharacter(new ImageView(new Image("SoftwareEngineering/images/hatstand.png")));
                break;
            case 5:
                player.setCharacter(new ImageView(new Image("SoftwareEngineering/images/smartphone.png")));
                break;
            case 6:
                player.setCharacter(new ImageView(new Image("SoftwareEngineering/images/spoon.png")));
                break;
        }
    }

    /**
     * Returns an image based on the group of the square.
     *
     * @param group The group of the square
     * @return Image
     */
    public Image setGroupImage(String group) {
        Image groupImage = null;
        switch (group.toLowerCase()) {
            case "go":
                groupImage = new Image("SoftwareEngineering/images/go.png");
                break;
            case "green":
                groupImage = new Image("SoftwareEngineering/images/green.png");
                break;
            case "deep blue":
                groupImage = new Image("SoftwareEngineering/images/deepblue.png");
                break;
            case "orange":
                groupImage = new Image("SoftwareEngineering/images/orange.png");
                break;
            case "red":
                groupImage = new Image("SoftwareEngineering/images/red.png");
                break;
            case "blue":
                groupImage = new Image("SoftwareEngineering/images/lightblue.png");
                break;
            case "purple":
                groupImage = new Image("SoftwareEngineering/images/purple.png");
                break;
            case "brown":
                groupImage = new Image("SoftwareEngineering/images/brown.png");
                break;
            case "yellow":
                groupImage = new Image("SoftwareEngineering/images/yellow.png");
                break;
            case "station":
                groupImage = new Image("SoftwareEngineering/images/station.png");
                break;
            case "utilities":
                groupImage = new Image("SoftwareEngineering/images/utilities.png");
                break;
            case "income tax":
                groupImage = new Image("SoftwareEngineering/images/incometax.png");
                break;
            case "super tax":
                groupImage = new Image("SoftwareEngineering/images/supertax.png");
                break;
            case "pot luck":
                groupImage = new Image("SoftwareEngineering/images/potluck.png");
                break;
            case "opportunity knocks":
                groupImage = new Image("SoftwareEngineering/images/opportunityknocks.png");
                break;
            case "jail":
                groupImage = new Image("SoftwareEngineering/images/jail.png");
                break;
            case "go to jail":
                groupImage = new Image("SoftwareEngineering/images/gotojail.png");
                break;
            case "free parking":
                groupImage = new Image("SoftwareEngineering/images/freeparking.png");
                break;
        }
        return groupImage;
    }

    /**
     * Creates a new instance of Board with the following parameters and sets the
     * board field of the controller to it.
     *
     * @param numOfPlayers      Number of players playing Property Tycoon
     * @param boardDataFilePath Path to the board data file
     * @param cardDataFilePath  Path to the card data file
     * @throws FileNotFoundException
     */
    public void setBoard(int numOfPlayers, String boardDataFilePath, String cardDataFilePath, String gameMode, int minutes, int numOfAI) throws FileNotFoundException {
        this.board = new Board(numOfPlayers, boardDataFilePath, cardDataFilePath, gameMode, minutes, numOfAI);
    }

    /**
     * Returns the board
     *
     * @return Board
     */
    public Board getBoard() {
        return this.board;
    }
}
