package SoftwareEngineering;

import java.util.ArrayList;

/**
 * An instance of Card represents a card of Property Tycoon. It has two
 * features; the description and action on the card.
 */
public class Card {
    private String description;
    private String action;

    /**
     * Creates an instance of Card with the description and action of the card
     *
     * @param description desciption of the card
     * @param action      action of the card
     */
    public Card(String description, String action) {
        this.description = description;
        this.action = action;
    }

    /**
     * Returns the description of the Card
     *
     * @return description of the Card
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the action of the Card
     *
     * @return action of the Card
     */
    public String getAction() {
        return this.action;
    }
}
