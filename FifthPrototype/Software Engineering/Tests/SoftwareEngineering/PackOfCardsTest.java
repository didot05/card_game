package SoftwareEngineering;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class PackOfCardsTest {
    public ArrayList<Card> cards;
    public PackOfCards pack;

    @Before
    public void setUp() throws Exception {
        cards = new ArrayList<Card>();
        cards.add(new Card("d1", "a1"));
        cards.add(new Card("d2", "a2"));
        cards.add(new Card("d3", "a3"));
        cards.add(new Card("d4", "a4"));
        cards.add(new Card("d5", "a5"));
        cards.add(new Card("d6", "a6"));
        pack = new PackOfCards(cards);
    }

    /**
     * When a card is picked, test whether it returned the
     * card at the top of the pack
     */
    @Test
    public void topOfPack() {
        Card top = pack.getPack().get(0);
        Card testTop = pack.pickCard();

        assertEquals(top, testTop);
    }

    /**
     * Test that the pack is shuffled. Note that there is
     * a very small chance that shuffling the deck ends up
     * matching the original pack, in which case the test
     * would fail.
     */
    @Test
    public void shuffle() {
        PackOfCards original = pack;
        pack.shuffleDeck();

        assertNotEquals(pack.getPack(), original);
    }

    /**
     * After picking a card, test whether it is placecd
     * at the bottom of the pack. Then check that the
     * next card picked was the card at the top of
     * the pack.
     */
    @Test
    public void pickCard(){
        pack.shuffleDeck();
        Card first = pack.getPack().get(0);
        Card second = pack.getPack().get(1);
        Card testFirst = pack.pickCard();

        assertEquals(first,testFirst);
        assertNotEquals(first,pack.getPack().get(0));

        Card bottomCard = pack.getPack().get(pack.getPack().size()-1);
        assertEquals(first,bottomCard);
        assertEquals(pack.getPack().get(0),second);
    }
}