package SoftwareEngineering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Converts board data in CSV format into game logic by creating a Square
 * with respect to the values in the CSV file. The resulting Square is then
 * appended to an ArrayList holding all the Squares, squareData.
 */
public class BoardCSVConverter {
    private ArrayList<String> boardCSVData;
    private ArrayList<Square> squareData;
    private HashMap<String, Integer> numOfGroups;
    private File file;
    private Scanner inputStream;

    /**
     * Instantiates the converter using the path of the CSV board data file
     *
     * @param filePath Path of the CSV board data file
     * @throws FileNotFoundException
     */
    public BoardCSVConverter(String filePath) throws FileNotFoundException {
        this.file = new File(filePath);
        this.inputStream = new Scanner(new BufferedReader(new FileReader(file)));
        boardCSVData = new ArrayList<String>();
        squareData = new ArrayList<Square>();
        numOfGroups = new HashMap<String,Integer>();
    }

    /**
     * Stores all information contained in the CSV file into an ArrayList of type String.
     * This ArrayList can now be manipulated into game logic. Every line in the CSV file
     * is stored as a single element in the ArrayList boardCSVData
     */
    public void initializeBoardData() {
        while (inputStream.hasNext()) {
            String data = inputStream.nextLine();
            boardCSVData.add(data);
        }
        inputStream.close();
    }

    /**
     * Converts every element in boardCSVData into Squares and adds these Squares to squareData
     */
    public void setBoardData() {
        int startOfBoardData = 4;
        int endOfBoardData = 43;
        for (int i = 0; i < boardCSVData.size(); i++) {
            if (i >= startOfBoardData && i <= endOfBoardData) {
                String[] temp = boardCSVData.get(i).split(",");
                numOfGroups.put(temp[3].toLowerCase(), numOfGroups.getOrDefault(temp[3].toLowerCase(), 0) + 1);
                boolean canBeBought = true;
                if (temp[5].toLowerCase().equals("no")) {
                    canBeBought = false;
                }
                if (temp.length == 6) {
                    squareData.add(new Square(Integer.parseInt(temp[0]), temp[1], temp[3], temp[4], canBeBought, 0, null));
                } else if (temp.length == 9) {
                    squareData.add(new Square(Integer.parseInt(temp[0]), temp[1], temp[3], "", canBeBought,
                            Integer.parseInt(temp[7]), new Property(temp[3])));
                } else if (temp.length == 15) {
                    squareData.add(new Square(Integer.parseInt(temp[0]), temp[1], temp[3], "", canBeBought, Integer.parseInt(temp[7]),
                            new Property(temp[3], Integer.parseInt(temp[8]), Integer.parseInt(temp[10]), Integer.parseInt(temp[11]),
                                    Integer.parseInt(temp[12]), Integer.parseInt(temp[13]), Integer.parseInt(temp[14]))));
                }
            }
        }
    }

    /**
     * Returns an ArrayList holding the Squares of Property Tycoon
     *
     * @return ArrayList of type Square
     */
    public ArrayList<Square> getSquareData() {
        return this.squareData;
    }

    /**
     * Returns a HashMap mapping the group to the number of squares
     * belonging to that group.
     * @return HashMap<String,Integer>
     */
    public HashMap<String,Integer> getNumOfGroups(){
        return this.numOfGroups;
    }

    public static void main(String[] args) throws FileNotFoundException {
        BoardCSVConverter convert = new BoardCSVConverter("src/SoftwareEngineering/CSVData/PropertyTycoonBoardData.csv");
        convert.initializeBoardData();
        convert.setBoardData();
        for (Square s : convert.getSquareData()) {
            System.out.println(s.toString());
        }
        for (String key : convert.getNumOfGroups().keySet()) {
            System.out.println(key + " " + convert.getNumOfGroups().get(key));
        }
    }
}
