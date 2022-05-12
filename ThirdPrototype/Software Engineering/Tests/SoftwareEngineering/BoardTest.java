package SoftwareEngineering;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BoardTest {
    public Board board;
    public ArrayList<Player> allPlayers;
    public int housePointer = 0;

    @Before
    public void setUp() throws Exception {
        board = new Board(6, "src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv",
                "src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv");
        allPlayers = board.getAllPlayers();
    }

    /**
     * Test whether every player has a unique ID
     */
    @Test
    public void uniquePlayerId() {
        ArrayList<Integer> unique = new ArrayList<Integer>();
        boolean allUnique = true;
        for (Player p : allPlayers) {
            if (!unique.contains(p.getId())) {
                unique.add(p.getId());
            } else {
                allUnique = false;
            }
        }
        assertTrue(allUnique);
    }

    /**
     * Test whether the board updates to the
     * correct player after a player has ended
     * their turn. This test is for a board with
     * 6 players.
     */
    @Test
    public void nextPlayer() {
        assertEquals(board.getCurrentPlayerPointer(), 0);
        board.setNextPlayer();
        assertEquals(board.getCurrentPlayerPointer(), 1);
        board.setNextPlayer();
        assertEquals(board.getCurrentPlayerPointer(), 2);
        board.setNextPlayer();
        assertEquals(board.getCurrentPlayerPointer(), 3);
        board.setNextPlayer();
        assertEquals(board.getCurrentPlayerPointer(), 4);
        board.setNextPlayer();
        assertEquals(board.getCurrentPlayerPointer(), 5);
        board.setNextPlayer();
        assertEquals(board.getCurrentPlayerPointer(), 0);
    }

    /**
     * Test whether a player is removed fromm
     * the game after being declared bankrupt.
     * All properties owned by the player
     * should be buyable. The properties
     * should also be reset, i.e. there
     * should be no houses or hotels
     * on the properties.
     */
    @Test
    public void declareBankruptcy() {
        Player one = board.getAllPlayers().get(0);
        Square crapperStreet = board.getAllSquares().get(1);
        Square gangstersParadise = board.getAllSquares().get(3);
        one.buyProperty(crapperStreet);
        one.buyProperty(gangstersParadise);

        assertFalse(crapperStreet.getCanBeBought());
        assertFalse(gangstersParadise.getCanBeBought());
        assertEquals(one.getOwnedSquares().size(), 2);
        assertTrue(one.getOwnedSquares().contains(crapperStreet));
        assertTrue(one.getOwnedSquares().contains(gangstersParadise));
        assertEquals(one.getBalance(), 1500 - crapperStreet.getCost() - gangstersParadise.getCost());

        one.buyHouse(crapperStreet);

        assertEquals(crapperStreet.getProperty().getRentPointer(), 1);
        assertEquals(one.getBalance(), 1500 - crapperStreet.getCost()
                - gangstersParadise.getCost() - crapperStreet.getProperty().getImprovementCosts().get(housePointer));

        board.declareBankruptcy(one);

        assertEquals(board.getAllPlayers().size(), 5);
        assertFalse(board.getAllPlayers().contains(one));
        assertTrue(crapperStreet.getCanBeBought());
        assertTrue(gangstersParadise.getCanBeBought());
        assertEquals(crapperStreet.getProperty().getRentPointer(), 0);
    }
}