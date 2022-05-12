package SoftwareEngineering;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.*;

public class BoardTest {
    public Board board;
    public ArrayList<Player> allPlayers;
    public int housePointer = 0;

    @Before
    public void setUp() throws Exception {
        board = new Board(6, "src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv",
                "src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv", "Full Game", 0, 0);
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

    /**
     * Test whether the player correctly
     * receives the specified amount in the
     * action of a "collect" card.
     */
    @Test
    public void collectAction() {
        Player one = board.getAllPlayers().get(0);
        int balance = one.getBalance();
        board.collectAction(one, "Collect £100");
        assertEquals(balance + 100, one.getBalance());
        board.collectAction(one, "Collect £500");
        assertEquals(balance + 600, one.getBalance());
    }

    /**
     * Tests whether the player correctly
     * pays the specified amount in the
     * action of a "pay" card.
     */
    @Test
    public void payAction() {
        Player one = board.getAllPlayers().get(0);
        int balance = one.getBalance();

        assertTrue(board.payAction(one, "Pay £1000"));
        assertEquals(balance - 1000, one.getBalance());
        assertFalse(board.payAction(one, "Pay £1000"));
        assertEquals(balance - 1000, one.getBalance());
    }

    /**
     * Tests whether the player moves
     * to the square named in the action
     * of a "move" card.
     */
    @Test
    public void moveAction() {
        Player one = board.getAllPlayers().get(0);
        Square crapperStreet = board.getAllSquares().get(1);
        one.setPosition(39);
        int balance = one.getBalance();

        assertTrue(board.moveAction(one, "Move to Crapper Street"));
        assertEquals(balance + 200, one.getBalance());
        assertEquals(crapperStreet.getPosition() - 1, one.getPosition());
    }

    /**
     * Tests whether the player pays the
     * correct amount as specified in the action.
     * Checks whether the amount of money on
     * "Free Parking" has been updated.
     */
    @Test
    public void fineAction() {
        Player one = board.getAllPlayers().get(0);
        int balance = one.getBalance();
        int freeParking = board.getFreeParking();

        assertTrue(board.fineAction(one, "Fine £400"));
        assertEquals(balance - 400, one.getBalance());
        assertEquals(freeParking + 400, board.getFreeParking());
    }

    /**
     * Tests whether the player moves
     * backwards by the amount specified
     * in the action. Tests the edge case
     * where a negative number is produced
     * by going past "GO".
     */
    @Test
    public void backAction() {
        Player one = board.getAllPlayers().get(0);
        board.backAction(one, "back 3");

        assertEquals(37, one.getPosition());
        one.setPosition(4);
        board.backAction(one, "back 2");
        assertEquals(2, one.getPosition());
    }

    /**
     * Tests whether the checking of a player
     * owning all squares belonging to a colour
     * group, and whether there are any houses/hotels
     * on those squares, functions properly.
     */
    @Test
    public void noImprovements() {
        Player one = board.getAllPlayers().get(0);
        Square crapperStreet = board.getAllSquares().get(1);
        Square gangstersParadise = board.getAllSquares().get(3);

        assertTrue(one.buyProperty(crapperStreet));
        assertFalse(board.noImprovements(one, crapperStreet));
        assertTrue(one.buyProperty(gangstersParadise));
        assertTrue(board.noImprovements(one, crapperStreet));
        assertTrue(board.noImprovements(one, gangstersParadise));
        assertTrue(one.buyHouse(crapperStreet));
        assertFalse(board.noImprovements(one, crapperStreet));
        assertFalse(board.noImprovements(one, gangstersParadise));
        assertTrue(one.sellHouse(crapperStreet));
        assertTrue(board.noImprovements(one, crapperStreet));
        assertTrue(board.noImprovements(one, gangstersParadise));
    }
}