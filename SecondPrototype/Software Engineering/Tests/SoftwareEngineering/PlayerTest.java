package SoftwareEngineering;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    /**
     * Test that the starting position of a player is 1 and if a
     * player's position is correct after they have rolled the dice
     */
    @Test
    public void newPosition() {
        Dice dice = new Dice();
        Player p1 = new Player(1);
        int rolled = dice.roll();

        assertEquals(1, p1.getPosition());
        p1.newPosition(rolled);
        assertEquals(1 + rolled, p1.getPosition());
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
        assertEquals(1, p1.getPosition()); // check that player's position starts at 1
        p1.setPosition(39); // set player's position to 39
        assertEquals(39, p1.getPosition());
        p1.newPosition(rolled); // update player's position  based on number rolled
        assertEquals(39 + rolled - 40, p1.getPosition());
    }
}