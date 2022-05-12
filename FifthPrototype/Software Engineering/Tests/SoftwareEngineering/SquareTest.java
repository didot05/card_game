package SoftwareEngineering;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SquareTest {
    public Board board;
    public ArrayList<Player> allPlayers;
    public ArrayList<Square> allSquares;

    @Before
    public void setUp() throws Exception {
        board = new Board(6, "src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv",
                "src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv", "Full Game", 0, 0);
        allPlayers = board.getAllPlayers();
        allSquares = board.getAllSquares();
    }

    /**
     * Tests if the calculateSquareWorth
     * returns the correct worth of a
     * property, based upon the cost
     * of the square and number of
     * houses and hotels on the property.
     */
    @Test
    public void calculateSquareWorth() {
        int housePointer = 0;
        int hotelPointer = 1;
        Player one = board.getAllPlayers().get(0);
        one.setBalance(1000000);
        Square crapperStreet = board.getAllSquares().get(1);
        Square gangstersParadise = board.getAllSquares().get(3);
        one.buyProperty(crapperStreet);
        one.buyProperty(gangstersParadise);

        assertEquals(crapperStreet.calculateSquareWorth(), crapperStreet.getCost());
        assertEquals(gangstersParadise.calculateSquareWorth(), gangstersParadise.getCost());

        for (int i = 0; i < 4; i++) {
            one.buyHouse(crapperStreet);
            one.buyHouse(gangstersParadise);
        }
        one.buyHotel(crapperStreet);

        assertEquals(crapperStreet.calculateSquareWorth(), crapperStreet.getCost()
                + crapperStreet.getProperty().getImprovementCosts().get(housePointer) * 4
                + crapperStreet.getProperty().getImprovementCosts().get(hotelPointer));
        assertEquals(gangstersParadise.calculateSquareWorth(), gangstersParadise.getCost()
                + gangstersParadise.getProperty().getImprovementCosts().get(housePointer) * 4);
    }
}
