package SoftwareEngineering;

import org.junit.Test;

import static org.junit.Assert.*;

public class DiceTest {
    /**
     * Test that a dice rolls a number greater or equal to 1
     * and less than or equal to 6. Test that the correct
     * numbers are stored in the dice.
     */
    @Test
    public void sumOfDice() {
        Dice dice = new Dice();
        dice.roll();
        int x = dice.getFirstRoll();
        assertTrue((x >= 1) && (x <= 6)); // check dice does not roll less than 1 or greater than 6
        int y = dice.getSecondRoll();
        assertTrue((y >= 1) && (y <= 6)); // check dice does not roll less than 1 or greater than 6
        assertEquals(x + y, dice.getFirstRoll() + dice.getSecondRoll());
    }
}