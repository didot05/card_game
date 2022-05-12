package SoftwareEngineering;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * The pack of cards are unshuffled. These tests are for
 * the original, unaltered card data.
 */
public class CardCSVConverterTest {
    CardCSVConverter converter;
    PackOfCards potLuck;
    PackOfCards oppurtunityKnocks;

    @Before
    public void setUp() throws Exception {
        converter = new CardCSVConverter("src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv");
        converter.initializeCardData();
        converter.setCardData();
        potLuck = converter.getPotLuckData();
        oppurtunityKnocks = converter.getOppurtunityKnocksData();
    }

    /**
     * Each pack of cards should consist of 16 cards.
     */
    @Test
    public void sizeOfPacks() {
        ArrayList<Card> potLuckPack = potLuck.getPack();
        ArrayList<Card> oppurtunityKnocksPack = oppurtunityKnocks.getPack();

        assertEquals(potLuckPack.size(), 16);
        assertEquals(oppurtunityKnocksPack.size(), 16);
    }

    /**
     * Test whether every description and action in the
     * Pot Luck data has been correctly loaded into a
     * Card.
     */
    @Test
    public void potLuck() {
        Card first = potLuck.getPack().get(0);
        assertEquals("You inherit £100", first.getDescription());
        assertEquals("Bank pays player £100", first.getAction());
        Card second = potLuck.getPack().get(1);
        assertEquals("You have won 2nd prize in a beauty contest, collect £20", second.getDescription());
        assertEquals("Bank pays player £20", second.getAction());
        Card third = potLuck.getPack().get(2);
        assertEquals("Go back to Crapper Street", third.getDescription());
        assertEquals("Player token moves backwards to Crapper Street", third.getAction());
        Card fourth = potLuck.getPack().get(3);
        assertEquals("Student loan refund. Collect £20", fourth.getDescription());
        assertEquals("Bank pays player £20", fourth.getAction());
        Card fifth = potLuck.getPack().get(4);
        assertEquals("Bank error in your favour. Collect £200", fifth.getDescription());
        assertEquals("Bank pays player £200", fifth.getAction());
        Card sixth = potLuck.getPack().get(5);
        assertEquals("Pay bill for text books of £100", sixth.getDescription());
        assertEquals("Player pays £100 to the bank", sixth.getAction());
        Card seventh = potLuck.getPack().get(6);
        assertEquals("Mega late night taxi bill pay £50", seventh.getDescription());
        assertEquals("Player pays £50 to the bank", seventh.getAction());
        Card eight = potLuck.getPack().get(7);
        assertEquals("Advance to go", eight.getDescription());
        assertEquals("Player moves forwards to GO", eight.getAction());
        Card ninth = potLuck.getPack().get(8);
        assertEquals("From sale of Bitcoin you get £50", ninth.getDescription());
        assertEquals("Bank pays player £50", ninth.getAction());
        Card ten = potLuck.getPack().get(9);
        assertEquals("Pay a £10 fine or take opportunity knocks", ten.getDescription());
        assertEquals("If fine paid, player puts £10 on free parking", ten.getAction());
        Card eleven = potLuck.getPack().get(10);
        assertEquals("Pay insurance fee of £50", eleven.getDescription());
        assertEquals("Player puts £50 on free parking", eleven.getAction());
        Card twelve = potLuck.getPack().get(11);
        assertEquals("Savings bond matures, collect £100", twelve.getDescription());
        assertEquals("Bank pays £100 to the player", twelve.getAction());
        Card thirteen = potLuck.getPack().get(12);
        assertEquals("Go to jail. Do not pass GO, do not collect £200", thirteen.getDescription());
        assertEquals("As the card says", thirteen.getAction());
        Card fourteen = potLuck.getPack().get(13);
        assertEquals("Received interest on shares of £25", fourteen.getDescription());
        assertEquals("Bank pays player £25", fourteen.getAction());
        Card fifteen = potLuck.getPack().get(14);
        assertEquals("It's your birthday. Collect £10 from each player", fifteen.getDescription());
        assertEquals("Player receives £10 from each player", fifteen.getAction());
        Card sixteen = potLuck.getPack().get(15);
        assertEquals("Get out of jail free", sixteen.getDescription());
        assertEquals("Retained by the player until needed. No resale or trade value", sixteen.getAction());
    }

    /**
     * Test whether every description and action in the
     * Oppurtunity Knocks data has been correctly loaded into a
     * Card.
     */
    @Test
    public void opportunityKnocks() {
        Card first = oppurtunityKnocks.getPack().get(0);
        assertEquals("Bank pays you divided of £50", first.getDescription());
        assertEquals("Bank pays player £50", first.getAction());
        Card second = oppurtunityKnocks.getPack().get(1);
        assertEquals("You have won a lip sync battle. Collect £100", second.getDescription());
        assertEquals("Bank pays player £100", second.getAction());
        Card third = oppurtunityKnocks.getPack().get(2);
        assertEquals("Advance to Turing Heights", third.getDescription());
        assertEquals("Player token moves forwards to Turing Heights", third.getAction());
        Card fourth = oppurtunityKnocks.getPack().get(3);
        assertEquals("Advance to Han Xin Gardens. If you pass GO, collect £200", fourth.getDescription());
        assertEquals("Player moves token", fourth.getAction());
        Card fifth = oppurtunityKnocks.getPack().get(4);
        assertEquals("Fined £15 for speeding", fifth.getDescription());
        assertEquals("Player puts £15 on free parking", fifth.getAction());
        Card sixth = oppurtunityKnocks.getPack().get(5);
        assertEquals("Pay university fees of £150", sixth.getDescription());
        assertEquals("Player pays £150 to the bank", sixth.getAction());
        Card seventh = oppurtunityKnocks.getPack().get(6);
        assertEquals("Take a trip to Hove station. If you pass GO collect £200", seventh.getDescription());
        assertEquals("Player moves token", seventh.getAction());
        Card eight = oppurtunityKnocks.getPack().get(7);
        assertEquals("Loan matures, collect £150", eight.getDescription());
        assertEquals("Bank pays £150 to the player", eight.getAction());
        Card ninth = oppurtunityKnocks.getPack().get(8);
        assertEquals("You are assessed for repairs, £40/house, £115/hotel", ninth.getDescription());
        assertEquals("Player pays money to the bank", ninth.getAction());
        Card ten = oppurtunityKnocks.getPack().get(9);
        assertEquals("Advance to GO", ten.getDescription());
        assertEquals("Player moves token", ten.getAction());
        Card eleven = oppurtunityKnocks.getPack().get(10);
        assertEquals("You are assessed for repairs, £25/house, £100/hotel", eleven.getDescription());
        assertEquals("Player pays money to the bank", eleven.getAction());
        Card twelve = oppurtunityKnocks.getPack().get(11);
        assertEquals("Go back 3 spaces", twelve.getDescription());
        assertEquals("Player moves token", twelve.getAction());
        Card thirteen = oppurtunityKnocks.getPack().get(12);
        assertEquals("Advance to Skywalker Drive. If you pass GO collect £200", thirteen.getDescription());
        assertEquals("Player moves token", thirteen.getAction());
        Card fourteen = oppurtunityKnocks.getPack().get(13);
        assertEquals("Go to jail. Do not pass GO, do not collect £200", fourteen.getDescription());
        assertEquals("As the card says", fourteen.getAction());
        Card fifteen = oppurtunityKnocks.getPack().get(14);
        assertEquals("Drunk in charge of a skateboard. Fine £20", fifteen.getDescription());
        assertEquals("Player puts £20 on free parking", fifteen.getAction());
        Card sixteen = oppurtunityKnocks.getPack().get(15);
        assertEquals("Get out of jail free", sixteen.getDescription());
        assertEquals("Retained by the player until needed. No resale or trade value", sixteen.getAction());
    }
}




