package SoftwareEngineering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Converts card data in CSV format to game logic by creating instances
 * of PackOfCards, storing Pot Luck and Oppurtuninty Knocks in separate
 * instances.
 */
public class CardCSVConverter {
    private ArrayList<String> cardCSVData;
    private File file;
    private Scanner inputStream;
    private PackOfCards potLuck;
    private PackOfCards oppurtunityKnocks;

    /**
     * Instantiates the converter using the path of the CSV card data file
     *
     * @param filePath path of the CSV card data file
     * @throws FileNotFoundException
     */
    public CardCSVConverter(String filePath) throws FileNotFoundException {
        this.file = new File(filePath);
        this.inputStream = new Scanner(new BufferedReader(new FileReader(file)));
        cardCSVData = new ArrayList<String>();
        potLuck = new PackOfCards(new ArrayList<Card>());
        oppurtunityKnocks = new PackOfCards(new ArrayList<Card>());
    }

    /**
     * Stores all information contained in the CSV file into an ArrayList of type String.
     * This ArrayList can now be manipulated into game logic. Every line in the CSV file
     * is stored as a single element in the ArrayList boardCSVData
     */
    public void initializeCardData() {
        while (inputStream.hasNext()) {
            String data = inputStream.nextLine();
            cardCSVData.add(data);
        }
        inputStream.close();
    }

    /**
     * Converts every element in cardCSVData into an instance of Card, then adds
     * to the corresponding PackOfCards that it belongs to (potLuck or oppurtunityKnocks).
     */
    public void setCardData() {
        for (int i = 0; i < cardCSVData.size(); i++) {
            String[] temp = cardCSVData.get(i).split(",,,");
            if (i >= 5 && i <= 20) {
                potLuck.getPack().add(new Card(temp[0].replaceAll("\"", ""), temp[1].replaceAll("\"", "")));
            } else if (i >= 25) {
                oppurtunityKnocks.getPack().add(new Card(temp[0].replaceAll("\"", ""), temp[1].replaceAll("\"", "")));
            }
        }
    }

    /**
     * Returns a PackOfCards holding Cards belonging to Pot Luck
     *
     * @return PackOfCards
     */
    public PackOfCards getPotLuckData() {
        return this.potLuck;
    }

    /**
     * Returns a PackOfCards holding Cards belonging to Oppurtunity Knocks
     *
     * @return PackOfCards
     */
    public PackOfCards getOppurtunityKnocksData() {
        return this.oppurtunityKnocks;
    }

    public static void main(String[] args) throws FileNotFoundException {
        CardCSVConverter converter = new CardCSVConverter("src/SoftwareEngineering/CSVData/PropertyTycoonCardData.csv");
        converter.initializeCardData();
        converter.setCardData();
    }
}

