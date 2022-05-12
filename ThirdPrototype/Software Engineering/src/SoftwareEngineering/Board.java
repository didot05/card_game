package SoftwareEngineering;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    private ArrayList<Square> allSquares;
    private ArrayList<Player> allPlayers;
    private HashMap<String, Integer> numOfGroups;
    private PackOfCards potLuck;
    private PackOfCards opportunityKnocks;
    private int currentPlayerPointer;
    private Player currentPLayer;
    private Dice dice;

    /**
     * Creates a Board object with the following parameters.
     * @param numOfPlayers The number of player playing Property Tycoon
     * @param boardDataFilePath The path to the board data file
     * @param cardDataFilePath The path to the card data file
     * @throws FileNotFoundException
     */
    public Board(int numOfPlayers, String boardDataFilePath, String cardDataFilePath) throws FileNotFoundException {
        this.allPlayers = new ArrayList<Player>();
        for (int id = 1; id <= numOfPlayers; id++) {
            this.allPlayers.add(new Player(id));
        }
        this.currentPlayerPointer = 0;
        this.currentPLayer = allPlayers.get(currentPlayerPointer);
        this.dice = new Dice();

        BoardCSVConverter convertBoard = new BoardCSVConverter(boardDataFilePath);
        convertBoard.initializeBoardData();
        convertBoard.setBoardData();
        this.allSquares = convertBoard.getSquareData();
        this.numOfGroups = convertBoard.getNumOfGroups();

        CardCSVConverter convertCards = new CardCSVConverter(cardDataFilePath);
        convertCards.initializeCardData();
        convertCards.setCardData();

        this.potLuck = convertCards.getPotLuckData();
        this.opportunityKnocks = convertCards.getOppurtunityKnocksData();
        this.potLuck.shuffleDeck();
        this.opportunityKnocks.shuffleDeck();
    }

    /**
     * Sets the next player to take their turn.
     */
    public void setNextPlayer() {
        if (currentPlayerPointer == allPlayers.size() - 1) {
            currentPlayerPointer = 0;
            currentPLayer = allPlayers.get(currentPlayerPointer);
        } else {
            currentPlayerPointer++;
            currentPLayer = allPlayers.get(currentPlayerPointer);
        }
    }

    public void declareBankruptcy(Player player){
        player.bankrupt();
        allPlayers.remove(player);
    }

    /**
     * Returns an ArrayList containing all the squares of the board
     * @return ArrayList<Square>
     */
    public ArrayList<Square> getAllSquares() {
        return this.allSquares;
    }

    /**
     * Returns an ArrayList containing all the players in the game.
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getAllPlayers() {
        return this.allPlayers;
    }

    /**
     * Returns the total number of squares that belong
     * to the given group.
     * @param group The group to query
     * @return int
     */
    public int getNumOfGroup(String group) {
        return this.numOfGroups.get(group);
    }

    /**
     * Returns the PackOfCards of Pot Luck.
     * @return PackOfCards
     */
    public PackOfCards getPotLuck() {
        return this.potLuck;
    }

    /**
     * Returns the PackOfCards of Opportunity Knocks.
     * @return PackOfCards
     */
    public PackOfCards getOpportunityKnocks() {
        return this.opportunityKnocks;
    }

    /**
     * Returns the pointer to currentPlayerPointer
     * @return int
     */
    public int getCurrentPlayerPointer() {
        return this.currentPlayerPointer;
    }

    /**
     * Returns the current player taking their turn
     * @return Player
     */
    public Player getCurrentPLayer() {
        return currentPLayer;
    }

    /**
     * Returns the Dice
     * @return Dice
     */
    public Dice getDice() {
        return this.dice;
    }
}
