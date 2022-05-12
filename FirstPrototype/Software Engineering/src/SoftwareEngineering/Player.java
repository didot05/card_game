package SoftwareEngineering;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Player {
    private int position;
    private int id;
    private int balance;
    private ImageView character;

    public Player(int id) {
        this.position = 1;
        this.id = id;
        this.balance = 1500;
        this.character = null;
    }

    public void newPosition(int rolledNumber) {
        position = position + rolledNumber;
        if (position > 40) {
            position -= 40;
        }
    }

    public int getPosition() {
        return this.position;
    }

    public int getBalance() {
        return this.balance;
    }

    public int getId() {
        return this.id;
    }

    public void setCharacter(ImageView character) {
        this.character = character;
    }

    public ImageView getCharacter() {
        return this.character;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
