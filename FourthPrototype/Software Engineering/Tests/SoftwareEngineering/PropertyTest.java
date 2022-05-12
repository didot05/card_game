package SoftwareEngineering;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PropertyTest {
    public Board board;
    public ArrayList<Player> allPlayers;
    public ArrayList<Square> allSquares;
    public int housePointer = 0;
    public int hotelPointer = 1;

    @Before
    public void setUp() throws Exception {
        board = new Board(6, "src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv",
                "src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv");
        allPlayers = board.getAllPlayers();
        allSquares = board.getAllSquares();
    }

    /**
     * Tests whether numOfHouses and
     * numOfHotels returns the correct
     * value. The edge case is when the
     * rent pointer is 5, i.e. there is
     * one hotel, but also 4 houses.
     */
    @Test
    public void numOfHousesAndHotels() {
        Player one = board.getAllPlayers().get(0);
        Square crapperStreet = board.getAllSquares().get(1);
        Square gangstersParadise = board.getAllSquares().get(3);
        one.buyProperty(crapperStreet);
        one.buyProperty(gangstersParadise);

        assertEquals(crapperStreet.getProperty().numOfHouses(), 0);
        assertEquals(gangstersParadise.getProperty().numOfHotels(), 0);

        for (int i = 0; i < 4; i++) {
            one.buyHouse(crapperStreet);
            one.buyHouse(gangstersParadise);
        }
        one.buyHotel(crapperStreet);

        assertEquals(crapperStreet.getProperty().numOfHouses(), 4);
        assertEquals(crapperStreet.getProperty().numOfHotels(), 1);
        assertEquals(gangstersParadise.getProperty().numOfHouses(), 4);
        assertEquals(gangstersParadise.getProperty().numOfHotels(), 0);
    }

    /**
     * Tests if the worth of a property
     * is correct based on the number of
     * houses and hotels on it.
     */
    @Test
    public void calculatePropertyWorth() {
        Player one = board.getAllPlayers().get(0);
        Square crapperStreet = board.getAllSquares().get(1);
        Square gangstersParadise = board.getAllSquares().get(3);
        one.buyProperty(crapperStreet);
        one.buyProperty(gangstersParadise);
        for (int i = 0; i < 4; i++) {
            one.buyHouse(crapperStreet);
            one.buyHouse(gangstersParadise);
        }
        one.buyHotel(crapperStreet);

        assertEquals(crapperStreet.getProperty().calculatePropertyWorth(),
                crapperStreet.getProperty().getImprovementCosts().get(housePointer) * 4,
                +crapperStreet.getProperty().getImprovementCosts().get(hotelPointer));
        assertEquals(gangstersParadise.getProperty().calculatePropertyWorth(),
                gangstersParadise.getProperty().getImprovementCosts().get(housePointer) * 4);
    }
}