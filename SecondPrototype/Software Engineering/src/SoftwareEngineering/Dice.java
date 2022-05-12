package SoftwareEngineering;

import java.util.Random;

public class Dice {
    private Random randomGenerator;
    private int firstRoll;
    private int secondRoll;

    public Dice() {
        randomGenerator = new Random();
        firstRoll = 0;
        secondRoll = 0;
    }

    public int roll() {
        firstRoll = randomGenerator.nextInt(6) + 1;
        secondRoll = randomGenerator.nextInt(6) + 1;
        return firstRoll + secondRoll;
    }

    public int getFirstRoll() {
        return firstRoll;
    }

    public int getSecondRoll() {
        return secondRoll;
    }
}
