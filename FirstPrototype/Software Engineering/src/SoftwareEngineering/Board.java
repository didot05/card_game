package SoftwareEngineering;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Board {
    private ArrayList<Player> allPlayers;
    private int currentPlayer;
    private Dice dice;

    public Board(int numOfPlayers) throws FileNotFoundException {
        this.allPlayers = new ArrayList<Player>();
        for (int id = 1; id <= numOfPlayers; id++) {
            this.allPlayers.add(new Player(id));
        }
        this.currentPlayer = 0;
        this.dice = new Dice();
    }

    public ArrayList<Player> getAllPlayers() {
        return this.allPlayers;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Dice getDice() {
        return this.dice;
    }
}
