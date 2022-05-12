package SoftwareEngineering;

import java.util.ArrayList;
import java.util.Collections;

/**
 * PackOfCards holds a list of cards. This pack can be shuffled. The card
 * at the top of the pack can be picked, where it will then be placed at
 * the bottom of the pack.
 */
public class PackOfCards {
    private ArrayList<Card> pack;

    /**
     * Creates an Instance of PackOfCards using an ArrayList of type Card
     * @param packOfCards An ArrayList of type Card
     */
    public PackOfCards(ArrayList<Card> packOfCards) {
        this.pack = packOfCards;
    }

    /**
     * Randomly rearranges the elements of the pack
     */
    public void shuffleDeck() {
        Collections.shuffle(this.pack);
    }

    /**
     * Returns the card at the top of the pack, then places it at the bottom of
     * the pack
     * @return Card
     */
    public Card pickCard() {
        Card card = pack.get(0);
        Collections.rotate(pack, -1);
        return card;
    }

    public ArrayList<Card> getPack(){
        return this.pack;
    }

}
