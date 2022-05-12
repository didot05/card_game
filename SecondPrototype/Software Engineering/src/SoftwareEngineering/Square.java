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

    public Square(int position, String name, String group, String action, boolean canBeBought, int cost, Property property) {
        this.position = position;
        this.name = name;
        this.group = group;
        this.canBeBought = canBeBought;
        this.cost = cost;
        this.property = property;
    }

    public int getPosition() {
        return this.position;
    }

    public String getName() {
        return this.name;
    }

    public String getGroup(){
        return this.group;
    }

    public String getAction() {
        return this.action;
    }

    public boolean getCanBeBought() {
        return this.canBeBought;
    }

    public int getCost(){
        return this.cost;
    }

    public Property getProperty(){
        return this.property;
    }
}
