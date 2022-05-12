package SoftwareEngineering;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A property object holds the number of houses/hotel there
 * are on a square. Including the current rent and cost of
 * improving the property.
 */
public class Property {
    private int[] rents;
    private int rentPointer;
    private ArrayList<Integer> improvementCosts;
    private String type;

    /**
     * Constructor of a property if it belongs to a colour group
     *
     * @param group      The group the property belongs to
     * @param unimproved Price of rent with 0 houses
     * @param house1     Price of rent with 1 house
     * @param house2     Price of rent with 2 houses
     * @param house3     Price of rent with 3 houses
     * @param house4     Price of rent with 4 houses
     * @param hotel      Price of rent with 1 hotel
     */
    public Property(String group, int unimproved, int house1, int house2, int house3, int house4, int hotel) {
        this.rents = new int[]{unimproved, house1, house2, house3, house4, hotel};
        this.type = group;
        this.rentPointer = 0;
        this.improvementCosts = new ArrayList<Integer>();
        setImprovementCost(group);
    }

    /**
     * Constructor of a property if the property
     * is a station or utility.
     *
     * @param group The group the property belongs to
     */
    public Property(String group) {
        this.improvementCosts = new ArrayList<Integer>();
        if (group.toLowerCase().equals("station")) {
            this.rents = new int[]{25, 50, 100, 200};
            this.type = "Station";
        } else if (group.toLowerCase().equals("utilities")) {
            this.rents = new int[]{4, 10};
            this.type = "Utilities";
        }
        this.rentPointer = 0;
    }

    /**
     * Sets the cost of improving the property by
     * buying a house/hotel based on the group
     * the property belongs to.
     *
     * @param group The group the property belongs to
     */
    public void setImprovementCost(String group) {
        switch (group.toLowerCase()) {
            case "brown":
            case "blue":
                improvementCosts.addAll(Arrays.asList(50, 250));
                break;
            case "purple":
            case "orange":
                improvementCosts.addAll(Arrays.asList(100, 500));
                break;
            case "red":
            case "yellow":
                improvementCosts.addAll(Arrays.asList(150, 750));
                break;
            case "green":
            case "deep blue":
                improvementCosts.addAll(Arrays.asList(200, 1000));
                break;
        }
    }

    /**
     * Returns the total value of the property
     * by summing the value of all houses and
     * hotels.
     *
     * @return int
     */
    public int calculatePropertyWorth() {
        int worth = 0;
        int housePointer = 0;
        int hotelPointer = 1;
        int numOfHouses = numOfHouses();
        int numOfHotels = numOfHotels();
        for (int i = 0; i < numOfHouses; i++) {
            worth += improvementCosts.get(housePointer);
        }
        if (numOfHotels == 1) {
            worth += improvementCosts.get(hotelPointer);
        }
        return worth;
    }

    /**
     * Returns the number of houses on the property
     *
     * @return int
     */
    public int numOfHouses() {
        int hotelPointer = 5;
        if (this.rentPointer < hotelPointer) {
            return this.rentPointer;
        } else {
            return this.rentPointer - 1;
        }
    }

    /**
     * Returns the number of hotels on the property
     *
     * @return int
     */
    public int numOfHotels() {
        int hotelPointer = 5;
        if (this.rentPointer == hotelPointer) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Sets the pointer of int[] rents to newRentPointer
     *
     * @param newRentPointer The new pointer value
     */
    public void setRentPointer(int newRentPointer) {
        this.rentPointer = newRentPointer;
    }

    /**
     * Returns the pointer of int[] rents
     *
     * @return int
     */
    public int getRentPointer() {
        return this.rentPointer;
    }

    /**
     * Returns the current rent price
     *
     * @return int
     */
    public int getCurrentRent() {
        return rents[rentPointer];
    }

    /**
     * Returns the type of the property
     *
     * @return String
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns an ArrayList holding the costs of
     * improving the property by a house/hotel
     *
     * @return ArrayList holing Integers
     */
    public ArrayList<Integer> getImprovementCosts() {
        return this.improvementCosts;
    }

    /**
     * Returns the cost to buy a house on the property.
     *
     * @return int
     */
    public int getHouseCost() {
        return this.improvementCosts.get(0);
    }

    /**
     * Returns the cost to buy a hotel on the property.
     *
     * @return
     */
    public int getHotelCost() {
        return this.improvementCosts.get(1);
    }

    /**
     * Returns an ArrayList holding possible rents the
     * property can have.
     *
     * @return An array of type int
     */
    public int[] getRentPrices() {
        return this.rents;
    }
}
