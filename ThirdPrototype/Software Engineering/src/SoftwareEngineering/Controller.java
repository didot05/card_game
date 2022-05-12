package SoftwareEngineering;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for "WatsonGames.fxml".
 */
public class Controller implements Initializable {
    public GridPane theGrid;
    public VBox playerStatus;
    public ImageView diceImage1, diceImage2;
    public Button rollDiceButton, endTurnButton, statusButton, improveSellButton;
    public Label showCurrentPlayer;
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
            rollDiceButton.setDisable(false);
            endTurnButton.setDisable(true);
            addToGameLog("Player " + currentPlayer.getId() + " rolled a double!");
        } else {
            rollDiceButton.setDisable(true);
            endTurnButton.setDisable(false);
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
        addToGameLog("Player " + currentPlayer.getId() + " rolled a " + total + " and landed on " + board.getAllSquares().get(board.getCurrentPLayer().getPosition()).getName());
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
        if (currentSquare.getCanBeBought()) {
            createBuyPropertyWindow();
        } else if (!Objects.isNull(currentSquare.getOwnedBy()) && !currentSquare.getOwnedBy().equals(currentPlayer)) {//pay rent
            payRent();
        }
        updateBalanceLabels();
    }

    /**
     * Handles the event when a player lands on
     * a property that is owned by a different
     * player. The current player must pay
     * the rent of the square, otherwise they
     * will go bankrupt.
     */
    public void payRent() {
        Player currentPlayer = board.getCurrentPLayer();
        Square currentSquare = board.getAllSquares().get(currentPlayer.getPosition());
        int rentToPay;
        boolean paid;
        if (currentSquare.getGroup().equals("Utilities")) {
            rentToPay = currentSquare.getProperty().getCurrentRent() * board.getDice().getTotal();
        } else {
            rentToPay = currentSquare.getProperty().getCurrentRent();
        }
        paid = currentPlayer.payPlayer(currentSquare.getOwnedBy(), rentToPay);
        if (paid) {
            addToGameLog("Player " + currentPlayer.getId() + " has paid a rent of £" + rentToPay + " to Player " + currentSquare.getOwnedBy().getId());
        } else {
            addToGameLog("Player " + currentPlayer.getId() + " could not pay the rent of £" + rentToPay + " to Player " + currentSquare.getOwnedBy().getId());
            if (currentPlayer.getNetWorth() >= rentToPay) {
                addToGameLog("Player " + currentPlayer.getId() + "has enough funds to pay the rent of £" + rentToPay + " to Player:" + currentSquare.getOwnedBy().getId() + ". Sell houses/hotels/properties to pay the rent.");
            } else {
                currentPlayer.payPlayer(currentSquare.getOwnedBy(), currentPlayer.getNetWorth());
                board.declareBankruptcy(currentPlayer);
                addToGameLog("Player " + currentPlayer.getId() + " has gone bankrupt!");
            }
        }
    }

    /**
     * Handles the when a player lands on a property
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
     * Handles the event when a user clicks "Improve/Sell".
     * Creates a new window from "BuySell.fxml".
     */
    public void createImproveSellWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BuySell.fxml"));
        Parent improveAndSellView = null;
        try {
            improveAndSellView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BuySellController isc = loader.getController();
        isc.setController(this);
        isc.initialize(this.board);

        Stage stage = new Stage();
        stage.setTitle("Improve and Sell Properties");
        stage.setScene(new Scene(improveAndSellView));
        stage.show();
    }

    /**
     * Handles the event when a user clicks "End Turn".
     * Sets the next player and updates the current player label.
     */
    public void endTheTurn() {
        board.setNextPlayer();
        rollDiceButton.setDisable(false);
        endTurnButton.setDisable(true);
        showCurrentPlayer.setText("Current Player: Player " + (board.getCurrentPLayer().getId()));
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
     * Updates the balance labels of all players.
     */
    public void updateBalanceLabels() {
        int maxPlayers = 6;
        int numOfPlayers = board.getAllPlayers().size();
        for (int i = 0; i < numOfPlayers; i++) {
            playerBalance.get(i).setText("Player " + board.getAllPlayers().get(i).getId() + ": £" + board.getAllPlayers().get(i).getBalance());
        }
        for (int i = 0; i < maxPlayers - numOfPlayers; i++) { //set labels of players not in the game to ""
            playerBalance.get(maxPlayers - 1 - i).setText("");
        }
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
    public void setBoard(int numOfPlayers, String boardDataFilePath, String cardDataFilePath) throws FileNotFoundException {
        this.board = new Board(numOfPlayers, boardDataFilePath, cardDataFilePath);
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
