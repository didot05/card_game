package SoftwareEngineering;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {
    public Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(6, "src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv",
                "src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv");
    }

    /**
     * Test that the starting position of a player is 1 and if a
     * player's position is correct after they have rolled the dice
     */
    @Test
    public void newPosition() {
        Dice dice = new Dice();
        Player p1 = new Player(1);
        int rolled = dice.roll();

        assertEquals(0, p1.getPosition());
        p1.newPosition(rolled);
        assertEquals(rolled, p1.getPosition());
    }

    /**
     * Test the edge case where as the result of a roll, the
     * player's position goes above 40.
     */
    @Test
    public void newPositionOver40() {
        Dice dice = new Dice(); // create new instance of dice
        Player p1 = new Player(1); // create new instance of player

        int rolled = dice.roll(); // roll the dice
        assertEquals(0, p1.getPosition()); // check that player's position starts at 1
        p1.setPosition(39); // set player's position to 39
        assertEquals(39, p1.getPosition());
        p1.newPosition(rolled); // update player's position  based on number rolled
        assertEquals(39 + rolled - 40, p1.getPosition());
    }

    /**
     * If a player has enough money, they are
     * able to purchase the property. The
     * property should be added to the list
     * of owned properties of the player.
     * The square should be owned by the player
     * and have no houses or hotels on it.
     */
    @Test
    public void buyProperty() {
        Player one = board.getAllPlayers().get(0);
        Square skywalkerDrive = board.getAllSquares().get(11);
        Square reyLane = board.getAllSquares().get(14);

        assertTrue(one.buyProperty(skywalkerDrive));
        assertFalse(skywalkerDrive.getCanBeBought());
        assertEquals(skywalkerDrive.getOwnedBy(), one);
        assertEquals(skywalkerDrive.getProperty().getRentPointer(), 0);
        assertEquals(one.getBalance(), 1500 - skywalkerDrive.getCost());

        one.setBalance(0);
        assertFalse(one.buyProperty(reyLane));
    }

    /**
     * canBuyHouseHotel should return true
     * if the difference between all properties
     * of that group is not more than 1 house
     * after buying a house/hotel.
     */
    @Test
    public void canBuyHouseHotel() {
        Player one = board.getAllPlayers().get(0);
        Square skywalkerDrive = board.getAllSquares().get(11);
        Square reyLane = board.getAllSquares().get(14);
        Square wookieHole = board.getAllSquares().get(13);

        one.buyProperty(skywalkerDrive);
        one.buyProperty(reyLane);
        one.buyProperty(wookieHole);

        assertTrue(one.canBuyHouseHotel(skywalkerDrive));
        assertTrue(one.canBuyHouseHotel(reyLane));
        assertTrue(one.canBuyHouseHotel(wookieHole));

        one.buyHouse(skywalkerDrive);

        assertFalse(one.buyHotel(skywalkerDrive));
        assertTrue(one.buyHouse(wookieHole));
        assertTrue(one.buyHouse(reyLane));
    }

    /**
     * canSellHouseHotel should return true
     * if the difference between all properties
     * of that group is not more than 1 house
     * after selling a house/hotel.
     */
    @Test
    public void canSellHouseHotel() {
        Player one = board.getAllPlayers().get(0);
        one.setBalance(100000);
        Square skywalkderDrive = board.getAllSquares().get(11);
        Square wookieHole = board.getAllSquares().get(13);
        Square reyLane = board.getAllSquares().get(14);
        one.buyProperty(skywalkderDrive);
        one.buyProperty(wookieHole);
        one.buyProperty(reyLane);

        one.buyHouse(skywalkderDrive);
        one.buyHouse(reyLane);
        one.buyHouse(wookieHole);
        one.buyHouse(skywalkderDrive);

        assertTrue(one.canSellHouseHotel(skywalkderDrive));
        assertFalse(one.canSellHouseHotel(wookieHole));
        assertFalse(one.canSellHouseHotel(reyLane));

        one.sellHouse(skywalkderDrive);

        assertTrue(one.canSellHouseHotel(skywalkderDrive));
        assertTrue(one.canSellHouseHotel(wookieHole));
        assertTrue(one.canSellHouseHotel(reyLane));
    }

    /**
     * A house/hotel can only be bought if the
     * player has enough money to buy the house/hotel
     * and if, by buying the house/hotel, there would
     * not be a difference of more than 1 house
     * between properties of the group of the square
     * to be bought.
     */
    @Test
    public void buyHouseHotel() {
        Player one = board.getAllPlayers().get(0);
        one.setBalance(100000);
        Square crapperStreet = board.getAllSquares().get(1);
        Square gangstersParadise = board.getAllSquares().get(3);
        Square skywalkerDrive = board.getAllSquares().get(11);
        one.buyProperty(crapperStreet);
        one.buyProperty(gangstersParadise);

        assertTrue(one.buyHouse(crapperStreet));
        assertTrue(one.buyHouse(gangstersParadise));
        assertEquals(crapperStreet.getOwnedBy(), one);
        assertEquals(gangstersParadise.getOwnedBy(), one);
        assertFalse(crapperStreet.getCanBeBought());
        assertFalse(gangstersParadise.getCanBeBought());
        assertEquals(crapperStreet.getProperty().getRentPointer(), 1);
        assertEquals(gangstersParadise.getProperty().getRentPointer(), 1);

        assertTrue(one.buyHouse(crapperStreet));
        assertFalse(one.buyHouse(crapperStreet));
        assertTrue(one.buyHouse(gangstersParadise));
        assertFalse(one.buyHotel(crapperStreet));
        assertFalse(one.buyHotel(gangstersParadise));

        one.buyProperty(skywalkerDrive);
        for (int i = 0; i < 4; i++) {
            assertTrue(one.buyHouse(skywalkerDrive));
        }
        assertFalse(one.buyHouse(skywalkerDrive));
        assertTrue(one.buyHotel(skywalkerDrive));

        one.setBalance(0);
        assertFalse(one.buyHouse(gangstersParadise));
    }

    /**
     * A house or hotel can only be sold if
     * there is a house/hotel to sell and if,
     * by selling the house/hotel, there would
     * not be a difference of more than 1 house
     * between properties of the group of the square
     * to sell. Furthermore, a house can only be
     * sold by selling the hotel, if any, beforehand.
     */
    @Test
    public void sellHouseHotel() {
        Player one = board.getAllPlayers().get(0);
        one.setBalance(10000);
        Square crapperStreet = board.getAllSquares().get(1);
        Square gangstersParadise = board.getAllSquares().get(3);
        Square skywalkerDrive = board.getAllSquares().get(11);
        one.buyProperty(crapperStreet);
        one.buyProperty(gangstersParadise);

        assertFalse(one.sellHouse(crapperStreet));
        assertFalse(one.sellHouse(gangstersParadise));
        assertFalse(one.sellHotel(crapperStreet));
        assertFalse(one.sellHotel(gangstersParadise));
        assertTrue(one.buyHouse(crapperStreet));
        assertTrue(one.buyHouse(gangstersParadise));
        assertTrue(one.buyHouse(crapperStreet));
        assertFalse(one.sellHouse(gangstersParadise));

        one.buyProperty(skywalkerDrive);
        for (int i = 0; i < 4; i++) {
            assertTrue(one.buyHouse(skywalkerDrive));
        }
        assertFalse(one.sellHotel(skywalkerDrive));
        assertTrue(one.buyHotel(skywalkerDrive));
        assertFalse(one.sellHouse(skywalkerDrive));
        assertTrue(one.sellHotel(skywalkerDrive));
        assertTrue(one.sellHouse(skywalkerDrive));
    }

    /**
     * canSellProperty should return true if
     * there are no houses or hotels on the current
     * property and if there are no houses or
     * hotels on all other properties of the
     * group type.
     */
    @Test
    public void canSellProperty() {
        Player one = board.getAllPlayers().get(0);
        Square crapperStreet = board.getAllSquares().get(1);
        Square gangstersParadise = board.getAllSquares().get(3);
        one.buyProperty(crapperStreet);
        one.buyProperty(gangstersParadise);

        assertTrue(one.canSellProperty(crapperStreet));
        assertTrue(one.canSellProperty(gangstersParadise));

        one.buyHouse(crapperStreet);

        assertFalse(one.canSellProperty(crapperStreet));
        assertFalse(one.canSellProperty(gangstersParadise));
    }

    /**
     * Tests whether a player can sell the property.
     * A property can only be sold if if had no
     * houses or hotels on it, and if all other properties
     * of the group also have no houses or hotels.
     */
    @Test
    public void sellProperty() {
        Player one = board.getAllPlayers().get(0);
        Square crapperStreet = board.getAllSquares().get(1);
        one.buyProperty(crapperStreet);
        one.buyHouse(crapperStreet);

        assertFalse(one.sellProperty(crapperStreet));

        one.sellHouse(crapperStreet);
        one.sellProperty(crapperStreet);

        assertNull(crapperStreet.getOwnedBy());
        assertTrue(crapperStreet.getCanBeBought());
        assertEquals(crapperStreet.getProperty().getRentPointer(), 0);
    }

    /**
     * Test the value returned by
     * getNetWorth. The value is calculated
     * by summing the balance of the player
     * along with the worth of their properties.
     */
    @Test
    public void getNetWorthTest() {
        Player one = board.getAllPlayers().get(0);
        Square skywalkerDrive = board.getAllSquares().get(11);
        one.buyProperty(skywalkerDrive);

        assertEquals(one.getNetWorth(), one.getBalance() + skywalkerDrive.getCost());

        for (int i = 0; i < 4; i++) {
            one.buyHouse(skywalkerDrive);
            assertEquals(one.getNetWorth(), one.getBalance() + skywalkerDrive.getCost()
                    + skywalkerDrive.getProperty().calculatePropertyWorth());
        }
        one.buyHotel(skywalkerDrive);

        assertEquals(one.getNetWorth(), one.getBalance() + skywalkerDrive.getCost()
                + skywalkerDrive.getProperty().calculatePropertyWorth());
    }

    /**
     * After going bankrupt, the player's
     * character should be set to null.
     * All properties owned by the player
     * should be reset, i.e. have no
     * houses or hotels, and can be bought.
     */
    @Test
    public void bankrupt() {
        Player one = board.getAllPlayers().get(0);
        Square skywalkerDrive = board.getAllSquares().get(11);
        one.buyProperty(skywalkerDrive);
        one.buyHouse(skywalkerDrive);

        assertEquals(skywalkerDrive.getProperty().getRentPointer(), 1);
        assertEquals(skywalkerDrive.getOwnedBy(), one);
        assertFalse(skywalkerDrive.getCanBeBought());

        one.bankrupt();

        assertEquals(skywalkerDrive.getProperty().getRentPointer(), 0);
        assertNull(skywalkerDrive.getOwnedBy());
        assertTrue(skywalkerDrive.getCanBeBought());
        assertNull(one.getCharacter());
    }

    /**
     * Tests whether numOfOwnedGroup
     * returns the correct number of
     * groups.
     */
    @Test
    public void numOfOwnedGroup() {
        Player one = board.getAllPlayers().get(0);
        Square purple1 = board.getAllSquares().get(11);
        Square purple2 = board.getAllSquares().get(14);
        Square blue = board.getAllSquares().get(9);

        assertEquals(one.numOfOwnedGroup("purple"), 0);
        assertEquals(one.numOfOwnedGroup("blue"), 0);

        one.buyProperty(purple1);
        one.buyProperty(purple2);
        one.buyProperty(blue);

        assertEquals(one.numOfOwnedGroup("purple"), 2);
        assertEquals(one.numOfOwnedGroup("blue"), 1);
    }

    /**
     * Tests whether the player can pay an
     * amount to another player. Then checks
     * if the players' balances have been
     * updated correctly.
     */
    @Test
    public void payPlayer() {
        Player one = board.getAllPlayers().get(0);
        Player two = board.getAllPlayers().get(1);

        assertTrue(one.payPlayer(two, 1000));
        assertEquals(one.getBalance(), 500);
        assertEquals(two.getBalance(), 2500);

        assertFalse(one.payPlayer(two, 1000));
        assertEquals(one.getBalance(), 500);
        assertEquals(two.getBalance(), 2500);
    }
}