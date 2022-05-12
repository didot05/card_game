package SoftwareEngineering;

import java.util.Random;

public class GameAgent extends Player {
    /**
     * Initializes an AI object with the given ID number.
     * The player will start on position 0, with an initial
     * £1500 in their balance.
     *
     * @param id The ID number to be assigned to the AI
     */

    public GameAgent(int id) {
        super(id);
    }

    /**
     * Returns the decision made by the AI.
     * Returns true for "Yes" and false for "No".
     *
     * @return boolean
     */
    public boolean getDecision() {
        Random randomGenerator = new Random();
        int choice = randomGenerator.nextInt(2);
        return choice != 0;
    }

    /**
     * Places a bid. The AI takes a random
     * percentage of their current balance
     * and returns it as a bid.
     *
     * @return int
     */
    public int makeBid() {
        Random random = new Random();
        double percent = random.nextInt(101) / (double) 100;
        return (int) (percent * getBalance());
    }

    /**
     * The AI loops decided whether
     * they want to improve the property
     * by buying houses/hotel.
     *
     * @param square The square
     */
    public void buyAssets(Square square) {
        if (getDecision() && !square.getGroup().toLowerCase().equals("station") && !square.getGroup().toLowerCase().equals("utilities")) {
            buyHouse(square);
        }
        if (getDecision() && !square.getGroup().toLowerCase().equals("station") && !square.getGroup().toLowerCase().equals("utilities")) {
            buyHotel(square);
        }
    }

    /**
     * The player sells assets until the
     * the their balance is greater than
     * or equal to the funds they need to
     * raise.
     *
     * @param fundsNeeded The amount of money needed in the balance
     */
    public void sellAssets(int fundsNeeded) {
        boolean raisedFunds = false;
        for (Square square : getOwnedSquares()) {
            if (!square.getGroup().toLowerCase().equals("station") && !square.getGroup().toLowerCase().equals("utilities")) {
                sellHotel(square);
            }
            if (getBalance() >= fundsNeeded) {
                raisedFunds = true;
                break;
            }
        }
        if (!raisedFunds) {
            for (Square square : getOwnedSquares()) {
                if (!square.getGroup().toLowerCase().equals("station") && !square.getGroup().toLowerCase().equals("utilities")) {
                    sellHouse(square);
                }
                if (getBalance() >= fundsNeeded) {
                    raisedFunds = true;
                    break;
                }
            }
        }
        if (!raisedFunds) {
            for (Square square : getOwnedSquares()) {
                sellProperty(square);
                if (getBalance() >= fundsNeeded) {
                    raisedFunds = true;
                    break;
                }
            }
        }
        if (!raisedFunds) {
            sellAssets(fundsNeeded);
        }
    }

    /**
     * Returns the name of the AI which
     * includes its ID.
     *
     * @return String
     */
    public String getName() {
        return "Agent " + getId();
    }
}