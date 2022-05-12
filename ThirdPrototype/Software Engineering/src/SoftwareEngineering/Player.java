package SoftwareEngineering;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.PropertyPermission;

/**
 * A player represents a single user of the game. It holds all relevant
 * information such as the balance and owned properties of a user.
 */
public class Player {
    private int position;
    private int id;
    private int balance;
    private ImageView character;
    private boolean passedGo;
    private ArrayList<Square> ownedSquares;

    /**
     * Initialize a player object with the given ID number.
     * The player will start on position 0, with an initial
     * £1500 in their balance.
     *
     * @param id The ID number to be assigned to the player
     */
    public Player(int id) {
        this.position = 0;
        this.id = id;
        this.balance = 1500;
        this.character = null;
        this.passedGo = false;
        this.ownedSquares = new ArrayList<Square>();
    }

    /**
     * Updates the player's position based on the number
     * provided.
     *
     * @param rolledNumber The number of places to move the player from their current position.
     */
    public void newPosition(int rolledNumber) {
        int endOfBoard = 39;
        this.position += rolledNumber;
        if (this.position > endOfBoard) {
            this.position -= 40;
            this.balance += 200;
            this.passedGo = true;
        }
    }

    /**
     * Returns true if the player has bought the
     * square. Returns false if the player does
     * not have enough money to purchase the square.
     *
     * @param square The square to buy
     * @return boolean
     */
    public boolean buyProperty(Square square) {
        boolean bought = false;
        if (this.balance >= square.getCost()) {
            this.balance -= square.getCost();
            this.ownedSquares.add(square);
            square.setOwnedBy(this);
            square.setCanBeBought(false);
            bought = true;
        }
        for (Square s : this.ownedSquares) {
            if (s.getGroup().equals("Station") || s.getGroup().equals("Utilities")) {
                s.getProperty().setRentPointer(numOfOwnedGroup(s.getGroup()) - 1);
            }
        }
        return bought;
    }

    /**
     * Returns true if the property was sold. Otherwise,
     * returns false since not all houses/hotel on the
     * property has been sold.
     *
     * @param square The square to sell
     * @return boolean
     */
    public boolean sellProperty(Square square) {
        int unimproved = 0;
        boolean sold = false;
        if (canSellProperty(square)) {
            this.balance += square.getCost();
            this.ownedSquares.remove(square);
            square.setOwnedBy(null);
            square.setCanBeBought(true);
            square.getProperty().setRentPointer(unimproved);
            sold = true;
        }
        return sold;
    }


    /**
     * Returns true if the player is able to sell
     * the property. Returns false if there
     * any houses/hotels on the properties in the same
     * group as the square trying to be sold.
     *
     * @param square The square to be sold
     * @return boolean
     */
    public boolean canSellProperty(Square square) {
        boolean canSellProperty = true;
        for (Square s : this.ownedSquares) {
            if (s.getGroup().equals(square.getGroup())) {
                if (s.getProperty().getRentPointer() != 0) {
                    canSellProperty = false;
                }
            }
        }
        return canSellProperty;
    }

    /**
     * Returns true if the player is able to
     * buy a house/hotel on the square. Returns
     * false if there would be a difference of more
     * than 1 house between properties in the group
     * of the square, should the player try to
     * improve the square.
     *
     * @param square The square to improve
     * @return boolean
     */
    public boolean canBuyHouseHotel(Square square) {
        ArrayList<Integer> allRentPointers = new ArrayList<>();
        int newRank = square.getProperty().getRentPointer() + 1;
        allRentPointers.add(newRank);

        for (Square s : this.ownedSquares) {
            if (!s.equals(square) && square.getGroup().equals(s.getGroup())) {
                allRentPointers.add(s.getProperty().getRentPointer());
            }
        }
        int min = allRentPointers.get(0);
        int max = allRentPointers.get(0);
        for (int i = 1; i < allRentPointers.size(); i++) {
            min = Math.min(min, allRentPointers.get(i));
            max = Math.max(max, allRentPointers.get(i));
        }
        return max - min <= 1;
    }

    /**
     * Returns true if the player is able to
     * sell a house/hotel on the property. Returns
     * false if there would be a difference of more
     * than 1 house between properties in the group
     * of the square, should the player try to
     * sell houses/hotel on the square.
     *
     * @param square The square to improve
     * @return boolean
     */
    public boolean canSellHouseHotel(Square square) {
        ArrayList<Integer> allRentPointers = new ArrayList<>();
        int newRank = square.getProperty().getRentPointer() - 1;
        allRentPointers.add(newRank);

        for (Square s : this.ownedSquares) {
            if (!s.equals(square) && square.getGroup().equals(s.getGroup())) {
                allRentPointers.add(s.getProperty().getRentPointer());
            }
        }
        int min = allRentPointers.get(0);
        int max = allRentPointers.get(0);
        for (int i = 1; i < allRentPointers.size(); i++) {
            min = Math.min(min, allRentPointers.get(i));
            max = Math.max(max, allRentPointers.get(i));
        }
        return max - min <= 1;
    }

    /**
     * If the transaction is successful, return true
     * and update the player's balance and property's
     * rent. Returns false if the player
     * does not have enough money to buy a house,
     * already has the maximum number of houses, or
     * if there would be a difference of more than
     * 1 house between the properties of the
     * group should a house be bought.
     *
     * @param square The square to buy a house on
     * @return boolean
     */
    public boolean buyHouse(Square square) {
        boolean bought = false;
        int house = 0;
        int maxNumOfHouses = 4;
        Property property = square.getProperty();
        if (this.balance >= property.getImprovementCosts().get(house) && canBuyHouseHotel(square) && property.getRentPointer() < maxNumOfHouses) {
            this.balance -= property.getImprovementCosts().get(house);
            property.setRentPointer(property.getRentPointer() + 1);
            bought = true;
        }
        return bought;
    }

    /**
     * If the transaction is successful, return true
     * and update the player's balance and property's
     * rent accordingly. Returns false if the player
     * does not have enough money to buy a hotel,
     * does not have enough houses, or if there
     * would be a difference of more than one
     * house between the properties of the group
     * should a hotel be bought.
     *
     * @param square The square to buy a hotel on
     * @return boolean
     */
    public boolean buyHotel(Square square) {
        boolean bought = false;
        int hotel = 1;
        int maxNumOfHouses = 4;
        Property property = square.getProperty();
        if (this.balance >= property.getImprovementCosts().get(hotel) && canBuyHouseHotel(square) && property.getRentPointer() == maxNumOfHouses) {
            this.balance -= property.getImprovementCosts().get(hotel);
            property.setRentPointer(property.getRentPointer() + 1);
            bought = true;
        }
        return bought;
    }

    /**
     * If the transaction is successful, return true
     * and update the player's balance and the property's
     * rent. Returns false if the player has no
     * houses, has not yet sold their hotel or if
     * there would be a difference of more than 1
     * house between the properties of the group
     * should a house be sold.
     *
     * @param square The square to sell a house from
     * @return boolean
     */
    public boolean sellHouse(Square square) {
        boolean sold = false;
        int house = 0;
        int hotel = 5;
        Property property = square.getProperty();
        if (property.getRentPointer() != 0 && property.getRentPointer() != hotel && canSellHouseHotel(square)) {
            this.balance += property.getImprovementCosts().get(house);
            property.setRentPointer(property.getRentPointer() - 1);
            sold = true;
        }
        return sold;
    }

    /**
     * Returns true if the transaction was successful.
     * Returns false if the player does not have a
     * hotel, or if there would be a difference of more
     * than 1 house between the properties of the
     * group should the hotel be sold.
     *
     * @param square The square to sell a hotel from
     * @return boolean
     */
    public boolean sellHotel(Square square) {
        boolean sold = false;
        int hotelPointer = 5;
        int hotel = 1;
        Property property = square.getProperty();
        if (property.getRentPointer() == hotelPointer && canSellHouseHotel(square)) {
            this.balance += property.getImprovementCosts().get(hotel);
            property.setRentPointer(property.getRentPointer() - 1);
            sold = true;
        }
        return sold;
    }

    /**
     * Returns true if the player has paid the
     * specified amount to the given player.
     * Returns false if the player does not
     * have enough money to pay the amount.
     *
     * @param player The player to give money to
     * @param amount The amount of money to give
     * @return boolean
     */
    public boolean payPlayer(Player player, int amount) {
        boolean paid = false;
        if (this.balance >= amount) {
            this.balance -= amount;
            player.setBalance(player.getBalance() + amount);
            paid = true;
        }
        return paid;
    }

    /**
     * Returns the number of squares the player owns
     * of a certain group.
     *
     * @param group Group of square to be queried
     * @return int
     */
    public int numOfOwnedGroup(String group) {
        int n = 0;
        for (Square square : this.ownedSquares) {
            if (square.getGroup().toLowerCase().equals(group.toLowerCase())) {
                n++;
            }
        }
        return n;
    }

    /**
     * The player will go bankrupt, i.e. is out
     * of the game. All properties the player
     * owned will be reset and can be bought
     * again.
     */
    public void bankrupt() {
        for (Square square : this.ownedSquares) {
            square.getProperty().setRentPointer(0);
            square.setOwnedBy(null);
            square.setCanBeBought(true);
        }
        this.character = null;
    }

    /**
     * Returns the net worth of the player, which
     * includes the current balance and the worth
     * of all properties the player owns.
     *
     * @return int
     */
    public int getNetWorth() {
        int worth = this.balance;
        for (Square square : this.ownedSquares) {
            worth += square.calculateSquareWorth();
        }
        return worth;
    }

    /**
     * Returns the current position of the player
     *
     * @return int
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Sets the postion of the player to the specified position
     *
     * @param newPosition New postion of player
     */
    public void setPosition(int newPosition) {
        this.position = newPosition;
    }

    /**
     * Sets the balance of the player to the specified amount
     *
     * @param newBalance New balance of player
     */
    public void setBalance(int newBalance) {
        this.balance = newBalance;
    }

    /**
     * Returns the balance of the player
     *
     * @return int
     */
    public int getBalance() {
        return this.balance;
    }

    /**
     * Returns the ID of the player
     *
     * @return int
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the character of the player
     *
     * @param character ImageView containing a character image
     */
    public void setCharacter(ImageView character) {
        this.character = character;
    }

    /**
     * Returns an ImageView object containing the character of the player
     *
     * @return ImageView object
     */
    public ImageView getCharacter() {
        return this.character;
    }

    /**
     * Returns all properties the player owns.
     *
     * @return ArrayList holding Square objects
     */
    public ArrayList<Square> getOwnedSquares() {
        return this.ownedSquares;
    }
}
