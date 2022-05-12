package SoftwareEngineering;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Board {
    private ArrayList<Square> allSquares;
    private ArrayList<Player> allPlayers;
    private PackOfCards potLuck;
    private PackOfCards oppurtunityKnocks;
    private int currentPlayer;
    private Dice dice;

    public Board(int numOfPlayers, String boardDataFilePath, String cardDataFilePath) throws FileNotFoundException {
        this.allPlayers = new ArrayList<Player>();
        for (int id = 1; id <= numOfPlayers; id++) {
            this.allPlayers.add(new Player(id));
        }
        this.currentPlayer = 0;
        this.dice = new Dice();
        BoardCSVConverter convertBoard = new BoardCSVConverter(boardDataFilePath);
        convertBoard.initializeBoardData();
        convertBoard.setBoardData();
        this.allSquares = convertBoard.getSquareData();

        CardCSVConverter convertCards = new CardCSVConverter(cardDataFilePath);
        convertCards.initializeCardData();
        convertCards.setCardData();

        this.potLuck = convertCards.getPotLuckData();
        this.oppurtunityKnocks = convertCards.getOppurtunityKnocksData();
        this.potLuck.shuffleDeck();
        this.oppurtunityKnocks.shuffleDeck();

    }

    public void setNextPlayer() {
        if (currentPlayer == allPlayers.size() - 1) {
            currentPlayer = 0;
        } else {
            currentPlayer++;
        }
    }

    public ArrayList<Square> getAllSquares() {
        return this.allSquares;
    }

    public ArrayList<Player> getAllPlayers() {
        return this.allPlayers;
    }

    public PackOfCards getPotLuck() {
        return this.potLuck;
    }

    public PackOfCards getOppurtunityKnocks() {
        return this.oppurtunityKnocks;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Dice getDice() {
        return this.dice;
    }
}
