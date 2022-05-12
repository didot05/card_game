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
    private int freeParking;
    private boolean fullGame;
    private int timeLeft;

    /**
     * Creates a Board object with the following parameters.
     *
     * @param numOfPlayers      The number of player playing Property Tycoon
     * @param boardDataFilePath The path to the board data file
     * @param cardDataFilePath  The path to the card data file
     * @throws FileNotFoundException
     */
    public Board(int numOfPlayers, String boardDataFilePath, String cardDataFilePath, String gameMode, int minutes, int numOfAI) throws FileNotFoundException {
        this.allPlayers = new ArrayList<Player>();
        for (int id = 1; id <= numOfPlayers + numOfAI; id++) {
            if (id <= numOfPlayers) {
                this.allPlayers.add(new Player(id));
            } else {
                this.allPlayers.add(new GameAgent(id));
            }
        }
        this.currentPlayerPointer = 0;
        this.currentPLayer = allPlayers.get(currentPlayerPointer);
        this.dice = new Dice();
        this.freeParking = 0;
        this.timeLeft = minutes * 60;
        this.fullGame = gameMode.toLowerCase().equals("full game");

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
        } else {
            currentPlayerPointer++;
        }
        currentPLayer = allPlayers.get(currentPlayerPointer);
    }

    /**
     * A player declared bankrupt is removed
     * from the game. All properties that
     * player owned are reset and can be
     * bought again by the remaining players.
     * Their token is removed from the game.
     *
     * @param player The player to remove from game
     */
    public void declareBankruptcy(Player player) {
        player.bankrupt();
        allPlayers.remove(player);
        if (getCurrentPlayerPointer() == getAllPlayers().size()) {
            setCurrentPLayer(0);
            setCurrentPlayerPointer(0);
        } else {
            setCurrentPLayer(getCurrentPlayerPointer());
        }
    }

    /**
     * Called when a player chooses a card
     * that has the action "Collect". The
     * player receives the amount specified
     * in the action.
     *
     * @param player The player who drew the card
     * @param action Action of the card
     */
    public void collectAction(Player player, String action) {
        String[] temp = action.split(" ");
        int n = Integer.parseInt(temp[1].replaceAll("[^\\d.]", ""));
        player.setBalance(player.getBalance() + n);
    }

    /**
     * Called when a player chooses a card
     * that has the action "Pay". The player
     * pays the specified amount in the action
     * to the bank. Returns true if the player
     * has paid the amount, false otherwise.
     *
     * @param player The player who drew the card
     * @param action Action of the card
     * @return boolean
     */
    public boolean payAction(Player player, String action) {
        boolean fin = false;
        String[] temp = action.split(" ");
        int n = Integer.parseInt(temp[1].replaceAll("[^\\d.]", ""));
        if (player.payBank(n)) {
            fin = true;
        }
        return fin;
    }

    /**
     * Called when a player chooses a card
     * that has the action "Move". The
     * player moves to the square named
     * in the action. Returns true if
     * the player has moved to the square,
     * false otherwise. If a player passes
     * GO, £200 is added to their account.
     *
     * @param player The player who drew the card
     * @param action Action of the card
     * @return boolean
     */
    public boolean moveAction(Player player, String action) {
        int endOfBoard = 39;
        boolean fin = false;
        String[] temp = action.split("Move to ");
        String nameOfSquare = temp[1].toLowerCase();
        for (Square square : this.allSquares) {
            if (square.getName().toLowerCase().equals(nameOfSquare)) {
                while (player.getPosition() != square.getPosition() - 1) {
                    if (player.getPosition() == endOfBoard) {
                        player.setPosition(0);
                        player.setPassedGo(true);
                        player.setBalance(player.getBalance() + 200);
                    } else {
                        player.setPosition(player.getPosition() + 1);
                    }
                }
                fin = true;
            }
        }
        return fin;
    }

    /**
     * Called when a player draws a card with
     * action "fine". Returns true if the player
     * pays the specified amount, false otherwise.
     * If the player has paid the fine, the amount
     * paid is added to the "Free Parking".
     *
     * @param player The player who drew the card
     * @param action Action of the card
     * @return boolean
     */
    public boolean fineAction(Player player, String action) {
        boolean fin = false;
        String[] temp = action.split(" ");
        int n = Integer.parseInt(temp[1].replaceAll("[^\\d.]", ""));
        if (player.payBank(n)) {
            fin = true;
        }
        freeParking += n;
        return fin;
    }

    /**
     * Called when a player draws a card with
     * action "back". Moves the player backwards
     * by the number of positions specified in the
     * action.
     *
     * @param player THe player who drew the card
     * @param action Action of the card
     */
    public void backAction(Player player, String action) {
        String[] back = action.split(" ");
        int spaces = Integer.parseInt(back[1]);
        player.newPosition(-spaces);
    }

    /**
     * Returns the index of jail in allSquares,
     * returns -1 if it was not found
     *
     * @return int
     */
    public int positionOfJail() {
        for (Square square : this.allSquares) {
            if (square.getGroup().toLowerCase().equals("jail")) {
                return square.getPosition() - 1;
            }
        }
        return -1;
    }

    /**
     * Returns true if the player has no
     * houses, and hence no hotels, on all
     * squares belonging to the colour
     * group of the specified square. The
     * player must also own all the squares
     * belonging to the
     * Otherwise, return false.
     *
     * @param player The player to check
     * @param square The square to check
     * @return boolean
     */
    public boolean noImprovements(Player player, Square square) {
        boolean noImprovements = true;
        String group = square.getGroup().toLowerCase();
        if (player.numOfOwnedGroup(group) == numOfGroups.get(group)) {
            for (Square s : player.getOwnedSquares()) {
                if (s.getGroup().toLowerCase().equals(group)) {
                    if (s.getProperty().numOfHouses() != 0) {
                        noImprovements = false;
                        break;
                    }
                }
            }
        } else {
            noImprovements = false;
        }
        return noImprovements;
    }

    /**
     * Returns the list of winners. Winners are
     * determined by the cash held by the player,
     * the worth of properties they own and the
     * worth of houses/hotels on those properties.
     *
     * @return ArrayList holding Player objects
     */
    public ArrayList<Player> getWinners() {
        ArrayList<Player> winners = new ArrayList<Player>();
        int highestNetWorth = -1;
        for (Player player : allPlayers) {
            if (player.getNetWorth() > highestNetWorth) {
                highestNetWorth = player.getNetWorth();
            }
        }
        for (Player player : allPlayers) {
            if (player.getNetWorth() == highestNetWorth) {
                winners.add(player);
            }
        }
        return winners;
    }

    /**
     * Returns true if the game has ended,
     * false otherwise. If the game mode
     * is abridged, then the game will end
     * when the timer hits 0 and all players
     * have taken the same number of turns.
     *
     * @return boolean
     */
    public boolean hasGameEnded() {
        boolean end = false;
        if (allPlayers.size() == 1) {
            end = true;
        }
        if (!fullGame) {
            if (timeLeft == 0 && sameNumOfTurns()) {
                end = true;
            }
        }
        return end;
    }

    /**
     * Returns true if all players have taken
     * the same number of turns, false otherwise.
     *
     * @return boolean
     */
    public boolean sameNumOfTurns() {
        boolean sameNumOfTurns = true;
        int numOfTurns = allPlayers.get(0).getNumOfTurns();
        for (Player player : allPlayers) {
            if (player.getNumOfTurns() != numOfTurns) {
                sameNumOfTurns = false;
                break;
            }
        }
        return sameNumOfTurns;
    }

    /**
     * Returns true if the player owns all properties of the
     * colour group, false otherwise.
     *
     * @return boolean
     */
    public boolean ownsAllGroup(Player player, Square square) {
        return numOfGroups.get(square.getGroup().toLowerCase()).equals(player.numOfOwnedGroup(square.getGroup().toLowerCase()));
    }

    /**
     * Returns an ArrayList containing all the squares of the board
     *
     * @return ArrayList<Square>
     */
    public ArrayList<Square> getAllSquares() {
        return this.allSquares;
    }

    /**
     * Returns an ArrayList containing all the players in the game.
     *
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getAllPlayers() {
        return this.allPlayers;
    }

    /**
     * Returns the total number of squares that belong
     * to the given group.
     *
     * @param group The group to query
     * @return int
     */
    public int getNumOfGroup(String group) {
        return this.numOfGroups.get(group);
    }

    /**
     * Returns the PackOfCards of Pot Luck.
     *
     * @return PackOfCards
     */
    public PackOfCards getPotLuck() {
        return this.potLuck;
    }

    /**
     * Returns the PackOfCards of Opportunity Knocks.
     *
     * @return PackOfCards
     */
    public PackOfCards getOpportunityKnocks() {
        return this.opportunityKnocks;
    }

    /**
     * Returns the pointer to currentPlayerPointer
     *
     * @return int
     */
    public int getCurrentPlayerPointer() {
        return this.currentPlayerPointer;
    }

    /**
     * Sets the pointer to the current player
     * to the given pointer index
     *
     * @param pointer The index
     */
    public void setCurrentPlayerPointer(int pointer) {
        this.currentPlayerPointer = pointer;
    }

    /**
     * Returns the current player taking their turn
     *
     * @return Player
     */
    public Player getCurrentPLayer() {
        return currentPLayer;
    }

    /**
     * Returns the Dice
     *
     * @return Dice
     */
    public Dice getDice() {
        return this.dice;
    }

    /**
     * Sets the amount of money available
     * in free parking to the given total.
     *
     * @param total The amount of money in free parking
     */
    public void setFreeParking(int total) {
        this.freeParking = total;
    }

    /**
     * Returns the amount of fines collected
     * in free parking.
     *
     * @return int
     */
    public int getFreeParking() {
        return this.freeParking;
    }

    /**
     * Returns true if the game is abridged,
     * false otherwise.
     *
     * @return boolean
     */
    public boolean getFullGame() {
        return this.fullGame;
    }

    /**
     * Sets the current player of the board
     * to the specified player
     *
     * @param pointer The pointer to the player
     */
    public void setCurrentPLayer(int pointer) {
        this.currentPLayer = allPlayers.get(pointer);
    }

    /**
     * Returns the time left to play the
     * game. Always returns 0 if the game
     * mode is "full game". If the game
     * mode is "abridged", then it returns
     * the time left in seconds.
     *
     * @return int
     */
    public int getTimeLeft() {
        return this.timeLeft;
    }

    /**
     * Decrements the time left in the
     * game by 1 second.
     */
    public void decrementTime() {
        this.timeLeft--;
    }

}
