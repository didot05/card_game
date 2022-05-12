package SoftwareEngineering;

import java.util.ArrayList;
import java.util.Arrays;

public class Property {
    private int[] rent;
    private int rentPointer;
    private ArrayList<Integer> improvementCost;
    private int improvementPointer;
    private String type;

    public Property(String group, int unimproved, int house1, int house2, int house3, int house4, int hotel) {
        this.rent = new int[]{unimproved, house1, house2, house3, house4, hotel};
        this.type = group;
        this.rentPointer = 0;
        this.improvementCost = new ArrayList<Integer>();
        this.improvementPointer = 0;
        setCost(group);
    }

    public Property(String stationOrUtility) {
        if (stationOrUtility.toLowerCase().equals("station")) {
            this.rent = new int[]{25, 50, 100, 200};
            this.type = "station";
        } else if (stationOrUtility.toLowerCase().equals("utilities")) {
            this.rent = new int[]{4, 10};
            this.type = "utilities";
        }
        this.rentPointer = 0;
    }

    public void setCost(String group) {
        if (group.equals("brown") || group.equals("blue")) {
            improvementCost.addAll(Arrays.asList(50,250));
        } else if (group.equals("purple") || group.equals("orange")) {
            improvementCost.addAll(Arrays.asList(100,500));
        } else if (group.equals("red") || group.equals("yellow")) {
            improvementCost.addAll(Arrays.asList(150,750));
        } else if (group.equals("green") || group.equals("deep blue")) {
            improvementCost.addAll(Arrays.asList(200,1000));
        }
    }

    public int currentRent(){
        return rent[rentPointer];
    }

    public int currentImprovementCost(){
        return improvementCost.get(improvementPointer);
    }

    public int[] getRent() {
        return this.rent;
    }
}
