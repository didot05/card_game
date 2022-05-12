package SoftwareEngineering;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * These tests are for the original, unaltered board data.
 */
public class BoardCSVConverterTest {
    public BoardCSVConverter converter;
    public ArrayList<Square> allSquares;
    public HashMap<String, Integer> numOfGroups;

    @Before
    public void setUp() throws Exception {
        converter = new BoardCSVConverter("src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv");
        converter.initializeBoardData();
        converter.setBoardData();
        allSquares = converter.getSquareData();
        numOfGroups = converter.getNumOfGroups();
    }

    /**
     * The board should consist of 40 squares
     */
    @Test
    public void sizeOfBoard() {
        assertEquals(allSquares.size(), 40);
    }

    /**
     * Test whether every data in the BoardCSVData
     * has been correctly transformed into Squares.
     */
    @Test
    public void squares() {
        assertEquals(allSquares.get(0).toString(), "Position: 1, Name: Go, Group: Go, Action: Collect £200, Buyable: No");
        assertEquals(allSquares.get(1).toString(), "Position: 2, Name: Crapper Street, Group: Brown, Buyable: Yes, Cost: 60, Property: Brown");
        assertEquals(allSquares.get(2).toString(), "Position: 3, Name: Pot Luck, Group: Pot Luck, Action: Take card, Buyable: No");
        assertEquals(allSquares.get(3).toString(), "Position: 4, Name: Gangsters Paradise, Group: Brown, Buyable: Yes, Cost: 60, Property: Brown");
        assertEquals(allSquares.get(4).toString(), "Position: 5, Name: Income Tax, Group: Income Tax, Action: Pay £200, Buyable: No");
        assertEquals(allSquares.get(5).toString(), "Position: 6, Name: Brighton Station, Group: Station, Buyable: Yes, Cost: 200, Property: Station");
        assertEquals(allSquares.get(6).toString(), "Position: 7, Name: Weeping Angel, Group: Blue, Buyable: Yes, Cost: 100, Property: Blue");
        assertEquals(allSquares.get(7).toString(), "Position: 8, Name: Opportunity Knocks, Group: Opportunity Knocks, Action: Take card, Buyable: No");
        assertEquals(allSquares.get(8).toString(), "Position: 9, Name: Potts Avenue, Group: Blue, Buyable: Yes, Cost: 100, Property: Blue");
        assertEquals(allSquares.get(9).toString(), "Position: 10, Name: Nardole Drive, Group: Blue, Buyable: Yes, Cost: 120, Property: Blue");
        assertEquals(allSquares.get(10).toString(), "Position: 11, Name: Jail/Just visiting, Group: Jail, Buyable: No");
        assertEquals(allSquares.get(11).toString(), "Position: 12, Name: Skywalker Drive, Group: Purple, Buyable: Yes, Cost: 140, Property: Purple");
        assertEquals(allSquares.get(12).toString(), "Position: 13, Name: Tesla Power Co, Group: Utilities, Buyable: Yes, Cost: 150, Property: Utilities");
        assertEquals(allSquares.get(13).toString(), "Position: 14, Name: Wookie Hole, Group: Purple, Buyable: Yes, Cost: 140, Property: Purple");
        assertEquals(allSquares.get(14).toString(), "Position: 15, Name: Rey Lane, Group: Purple, Buyable: Yes, Cost: 160, Property: Purple");
        assertEquals(allSquares.get(15).toString(), "Position: 16, Name: Hove Station, Group: Station, Buyable: Yes, Cost: 200, Property: Station");
        assertEquals(allSquares.get(16).toString(), "Position: 17, Name: Cooper Drive, Group: Orange, Buyable: Yes, Cost: 180, Property: Orange");
        assertEquals(allSquares.get(17).toString(), "Position: 18, Name: Pot Luck, Group: Pot Luck, Action: Take card, Buyable: No");
        assertEquals(allSquares.get(18).toString(), "Position: 19, Name: Wolowitz Street, Group: Orange, Buyable: Yes, Cost: 180, Property: Orange");
        assertEquals(allSquares.get(19).toString(), "Position: 20, Name: Penny Lane, Group: Orange, Buyable: Yes, Cost: 200, Property: Orange");
        assertEquals(allSquares.get(20).toString(), "Position: 21, Name: Free Parking, Group: Free Parking, Action: Collect fines, Buyable: No");
        assertEquals(allSquares.get(21).toString(), "Position: 22, Name: Yue Fei Square, Group: Red, Buyable: Yes, Cost: 220, Property: Red");
        assertEquals(allSquares.get(22).toString(), "Position: 23, Name: Opportunity Knocks, Group: Opportunity Knocks, Action: Take card, Buyable: No");
        assertEquals(allSquares.get(23).toString(), "Position: 24, Name: Mulan Rouge, Group: Red, Buyable: Yes, Cost: 220, Property: Red");
        assertEquals(allSquares.get(24).toString(), "Position: 25, Name: Han Xin Gardens, Group: Red, Buyable: Yes, Cost: 240, Property: Red");
        assertEquals(allSquares.get(25).toString(), "Position: 26, Name: Falmer Station, Group: Station, Buyable: Yes, Cost: 200, Property: Station");
        assertEquals(allSquares.get(26).toString(), "Position: 27, Name: Kirk Close, Group: Yellow, Buyable: Yes, Cost: 260, Property: Yellow");
        assertEquals(allSquares.get(27).toString(), "Position: 28, Name: Picard Avenue, Group: Yellow, Buyable: Yes, Cost: 260, Property: Yellow");
        assertEquals(allSquares.get(28).toString(), "Position: 29, Name: Edison Water, Group: Utilities, Buyable: Yes, Cost: 150, Property: Utilities");
        assertEquals(allSquares.get(29).toString(), "Position: 30, Name: Crusher Creek, Group: Yellow, Buyable: Yes, Cost: 280, Property: Yellow");
        assertEquals(allSquares.get(30).toString(), "Position: 31, Name: Go to Jail, Group: Go to jail, Buyable: No");
        assertEquals(allSquares.get(31).toString(), "Position: 32, Name: Sirat Mews, Group: Green, Buyable: Yes, Cost: 300, Property: Green");
        assertEquals(allSquares.get(32).toString(), "Position: 33, Name: Ghengis Crescent, Group: Green, Buyable: Yes, Cost: 300, Property: Green");
        assertEquals(allSquares.get(33).toString(), "Position: 34, Name: Pot Luck, Group: Pot Luck, Action: Take card, Buyable: No");
        assertEquals(allSquares.get(34).toString(), "Position: 35, Name: Ibis Close, Group: Green, Buyable: Yes, Cost: 320, Property: Green");
        assertEquals(allSquares.get(35).toString(), "Position: 36, Name: Lewes Station, Group: Station, Buyable: Yes, Cost: 200, Property: Station");
        assertEquals(allSquares.get(36).toString(), "Position: 37, Name: Opportunity Knocks, Group: Opportunity Knocks, Action: Take card, Buyable: No");
        assertEquals(allSquares.get(37).toString(), "Position: 38, Name: Hawking Way, Group: Deep blue, Buyable: Yes, Cost: 350, Property: Deep blue");
        assertEquals(allSquares.get(38).toString(), "Position: 39, Name: Super Tax, Group: Super Tax, Action: Pay £100, Buyable: No");
        assertEquals(allSquares.get(39).toString(), "Position: 40, Name: Turing Heights, Group: Deep blue, Buyable: Yes, Cost: 400, Property: Deep blue");
    }

    @Test
    public void numOfGroups() {
        assertEquals(numOfGroups.get("red").intValue(),3);
        assertEquals(numOfGroups.get("blue").intValue(),3);
        assertEquals(numOfGroups.get("orange").intValue(),3);
        assertEquals(numOfGroups.get("purple").intValue(),3);
        assertEquals(numOfGroups.get("yellow").intValue(),3);
        assertEquals(numOfGroups.get("green").intValue(),3);
        assertEquals(numOfGroups.get("deep blue").intValue(),2);
        assertEquals(numOfGroups.get("brown").intValue(),2);
        assertEquals(numOfGroups.get("pot luck").intValue(),3);
        assertEquals(numOfGroups.get("opportunity knocks").intValue(),3);
        assertEquals(numOfGroups.get("station").intValue(),4);
        assertEquals(numOfGroups.get("utilities").intValue(),2);
        assertEquals(numOfGroups.get("income tax").intValue(),1);
        assertEquals(numOfGroups.get("super tax").intValue(),1);
        assertEquals(numOfGroups.get("jail").intValue(),1);
        assertEquals(numOfGroups.get("go to jail").intValue(),1);
        assertEquals(numOfGroups.get("free parking").intValue(),1);
        assertEquals(numOfGroups.get("go").intValue(),1);
    }
}
