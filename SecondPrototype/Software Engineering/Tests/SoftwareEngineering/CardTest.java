package SoftwareEngineering;

import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {
    /**
     * Test that the description and action of a card are loaded correctly
     */
    @Test
    public void descriptionAndAction(){
        Card c = new Card("hello", "i like cats");
        assertEquals("hello", c.getDescription());
        assertEquals("i like cats", c.getAction());
    }
}