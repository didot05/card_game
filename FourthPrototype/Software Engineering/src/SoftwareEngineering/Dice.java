package SoftwareEngineering;

import java.util.Random;

/**
 * Dice represents two die.
 */
public class Dice {

    private Random randomGenerator;
    private int firstRoll;
    private int secondRoll;

    /**
     * Create an instance of Dice and sets the random number generator.
     */
    public Dice() {
        randomGenerator = new Random();
        firstRoll = 0;
        secondRoll = 0;
    }

    /**
     * Rolls the dice and returns the sum of both dice.
     *
     * @return int
     */
    public int roll() {
        int numberOfSides = 6;
        firstRoll = randomGenerator.nextInt(numberOfSides) + 1;
        secondRoll = randomGenerator.nextInt(numberOfSides) + 1;
        return firstRoll + secondRoll;
    }

    /**
     * Returns the number rolled by the first die.
     *
     * @return int
     */
    public int getFirstRoll() {
        return firstRoll;
    }

    /**
     * Returns the number rolled by the second die.
     *
     * @return int
     */
    public int getSecondRoll() {
        return secondRoll;
    }

    /**
     * Returns the sum of both die
     * @return int
     */
    public int getTotal(){
        return firstRoll+secondRoll;
    }
}
