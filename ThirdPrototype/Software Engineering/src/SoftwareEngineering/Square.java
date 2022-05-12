package SoftwareEngineering;

import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;

import java.util.ArrayList;

public class Square {
    private int position;
    private String name;
    private String group;
    private String action;
    private boolean canBeBought;
    private int cost;
    private Property property;
    private Player ownedBy;
    private boolean mortgaged;

    /**
     * Constructor for a Square
     * @param position Position of the square on the board
     * @param name Name of the square
     * @param group The group the square belongs to
     * @param action The action of the square
     * @param canBeBought Whether the square can be bought
     * @param cost Cost of buying the square
     * @param property The property on the square
     */
    public Square(int position, String name, String group, String action, boolean canBeBought, int cost, Property property) {
        this.position = position;
        this.name = name;
        this.group = group;
        this.action = action;
        this.canBeBought = canBeBought;
        this.cost = cost;
        this.property = property;
        this.ownedBy = null;
        this.mortgaged = false;
    }

    /**
     * Helpful method for debugging and testing.
     * Lists all relevant information of the square.
     * @return String
     */
    @Override
    public String toString() {
        String info = "";
        info += "Position: " + this.position + ", ";
        info += "Name: " + this.name + ", ";
        info += "Group: " + this.group + ", ";
        if (!this.action.equals("")) {
            info += "Action: " + this.action + ", ";
        }
        if (this.canBeBought) {
            info += "Buyable: Yes, ";
            info += "Cost: " + this.cost + ", ";
        } else {
            info += "Buyable: No";
        }
        if (property != null) {
            info += "Property: " + this.property.getType();
        }
        return info;
    }

    /**
     * Returns the total worth of the square by
     * summing the value of the property on the square
     * and the cost of buying the square.
     * @return int
     */
    public int calculateSquareWorth() {
        int worth = property.calculatePropertyWorth();
        if (!mortgaged) {
            worth += this.cost;
        } else {
            worth += this.cost / 2;
        }
        return worth;
    }

    /**
     * Returns the position the square has on the board.
     * @return int
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Returns the name of the square
     * @return Stirng
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the group the square belongs to.
     * @return String
     */
    public String getGroup() {
        return this.group;
    }

    /**
     * Returns the action to be carried out of the square.
     * @return String
     */
    public String getAction() {
        return this.action;
    }

    /**
     * Set whether the square can be bought or not to
     * the boolean provided.
     * @param canBeBought Whether the square can be bought
     */
    public void setCanBeBought(boolean canBeBought) {
        this.canBeBought = canBeBought;
    }

    /**
     * Returns true if the square can be bought, false otherwise.
     * @return boolean
     */
    public boolean getCanBeBought() {
        return this.canBeBought;
    }

    /**
     * Returns the cost of buying the square.
     * @return int
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Returns the property on the square
     * @return Property
     */
    public Property getProperty() {
        return this.property;
    }

    /**
     * Sets the ownership of the square to the specified player.
     * @param player The player to own the square
     */
    public void setOwnedBy(Player player) {
        this.ownedBy = player;
    }

    /**
     * Returns the player that owns the square.
     * @return Player
      */
    public Player getOwnedBy() {
        return this.ownedBy;
    }
}
