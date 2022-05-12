package SoftwareEngineering;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameAgentTest {
    public Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(0, "src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv",
                "src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv", "Full Game", 0, 2);
    }

    /**
     * Tests the bid made by the Game Agent
     * to be lower or equal to its balance.
     */
    @Test
    public void makeBid() {
        GameAgent one = (GameAgent) board.getAllPlayers().get(0);
        assertTrue(one.makeBid() <= one.getBalance());

        one.setBalance(1);
        assertTrue(one.makeBid() <= one.getBalance());

        one.setBalance(0);
        assertTrue(one.makeBid() <= one.getBalance());
    }

    /**
     * Tests that the Game Agent sells
     * assets until the required funds
     * have been raised in their balance.
     */
    @Test
    public void sellAssets() {
        GameAgent one = (GameAgent) board.getAllPlayers().get(0);
        Square crapperStreet = board.getAllSquares().get(1);
        Square skywalkerDrive = board.getAllSquares().get(11);
        int balance = one.getBalance();

        assertTrue(one.buyProperty(crapperStreet));
        assertEquals(balance - crapperStreet.getCost(), one.getBalance());
        assertEquals(crapperStreet.getOwnedBy(), one);

        one.sellAssets(1489);

        assertEquals(balance, one.getBalance());
        assertNull(crapperStreet.getOwnedBy());

        assertTrue(one.buyProperty(skywalkerDrive));
        assertEquals(balance - skywalkerDrive.getCost(), one.getBalance());
        assertTrue(one.buyHouse(skywalkerDrive));
        assertEquals(skywalkerDrive.getProperty().numOfHouses(), 1);
        assertEquals(1260, one.getBalance());

        one.sellAssets(1360);

        assertEquals(skywalkerDrive.getOwnedBy(), one);
        assertEquals(skywalkerDrive.getProperty().numOfHouses(), 0);
    }
}