package SoftwareEngineering;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public StackPane StackPane1, StackPane2, StackPane3, StackPane4, StackPane5, StackPane6, StackPane7, StackPane8, StackPane9, StackPane10,
            StackPane11, StackPane12, StackPane13, StackPane14, StackPane15, StackPane16, StackPane17, StackPane18, StackPane19, StackPane20,
            StackPane21, StackPane22, StackPane23, StackPane24, StackPane25, StackPane26, StackPane27, StackPane28, StackPane29, StackPane30,
            StackPane31, StackPane32, StackPane33, StackPane34, StackPane35, StackPane36, StackPane37, StackPane38, StackPane39, StackPane40;
    public ImageView Image1, Image2, Image3, Image4, Image5, Image6, Image7, Image8, Image9, Image10,
            Image11, Image12, Image13, Image14, Image15, Image16, Image17, Image18, Image19, Image20,
            Image21, Image22, Image23, Image24, Image25, Image26, Image27, Image28, Image29, Image30,
            Image31, Image32, Image33, Image34, Image35, Image36, Image37, Image38, Image39, Image40,
            diceImage1, diceImage2;
    public Label Label1, Label2, Label3, Label4, Label5, Label6, Label7, Label8, Label9, Label10,
            Label11, Label12, Label13, Label14, Label15, Label16, Label17, Label18, Label19, Label20,
            Label21, Label22, Label23, Label24, Label25, Label26, Label27, Label28, Label29, Label30,
            Label31, Label32, Label33, Label34, Label35, Label36, Label37, Label38, Label39, Label40,
            player1Label, player2Label, player3Label, player4Label, player5Label, player6Label;
    public Button rollDiceButton;
    private HashMap<Integer, Label> playerBalance;
    public HashMap<Integer, StackPane> squareCoordinates;
    private Board board;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            board = new Board(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        playerBalance = new HashMap<Integer, Label>();
        playerBalance.put(1, player1Label);
        playerBalance.put(2, player2Label);
        playerBalance.put(3, player3Label);
        playerBalance.put(4, player4Label);
        playerBalance.put(5, player5Label);
        playerBalance.put(6, player6Label);

        // A player has a position value that corresponds to the square they are in. Here we use a
        // HashMap to connect a Player's position value to the square they are associated with.
        squareCoordinates = new HashMap<Integer, StackPane>();
        squareCoordinates.put(1, StackPane1);
        squareCoordinates.put(2, StackPane2);
        squareCoordinates.put(3, StackPane3);
        squareCoordinates.put(4, StackPane4);
        squareCoordinates.put(5, StackPane5);
        squareCoordinates.put(6, StackPane6);
        squareCoordinates.put(7, StackPane7);
        squareCoordinates.put(8, StackPane8);
        squareCoordinates.put(9, StackPane9);
        squareCoordinates.put(10, StackPane10);
        squareCoordinates.put(11, StackPane11);
        squareCoordinates.put(12, StackPane12);
        squareCoordinates.put(13, StackPane13);
        squareCoordinates.put(14, StackPane14);
        squareCoordinates.put(15, StackPane15);
        squareCoordinates.put(16, StackPane16);
        squareCoordinates.put(17, StackPane17);
        squareCoordinates.put(18, StackPane18);
        squareCoordinates.put(19, StackPane19);
        squareCoordinates.put(20, StackPane20);
        squareCoordinates.put(21, StackPane21);
        squareCoordinates.put(22, StackPane22);
        squareCoordinates.put(23, StackPane23);
        squareCoordinates.put(24, StackPane24);
        squareCoordinates.put(25, StackPane25);
        squareCoordinates.put(26, StackPane26);
        squareCoordinates.put(27, StackPane27);
        squareCoordinates.put(28, StackPane28);
        squareCoordinates.put(29, StackPane29);
        squareCoordinates.put(30, StackPane30);
        squareCoordinates.put(31, StackPane31);
        squareCoordinates.put(32, StackPane32);
        squareCoordinates.put(33, StackPane33);
        squareCoordinates.put(34, StackPane34);
        squareCoordinates.put(35, StackPane35);
        squareCoordinates.put(36, StackPane36);
        squareCoordinates.put(37, StackPane37);
        squareCoordinates.put(38, StackPane38);
        squareCoordinates.put(39, StackPane39);
        squareCoordinates.put(40, StackPane40);
        try {
            initializeSquares();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void rolledTheDice() {
        Dice dice = board.getDice();
        int total = dice.roll();
        int face = dice.getFirstRoll(); // to check for a "double" roll, just do face*2 == total
        ImageView currentDice = diceImage1;
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                face = dice.getSecondRoll();
                currentDice = diceImage2;
            }
            switch (face) {
                case 1:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-1.png"));
                    break;
                case 2:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-2.png"));
                    break;
                case 3:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-3.png"));
                    break;
                case 4:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-4.png"));
                    break;
                case 5:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-5.png"));
                    break;
                case 6:
                    currentDice.setImage(new Image("SoftwareEngineering/images/dice-6.png"));
                    break;
            }
        }
        //remove player token from old square
        squareCoordinates.get(board.getAllPlayers().get(board.getCurrentPlayer()).getPosition()).getChildren().remove(board.getAllPlayers().get(board.getCurrentPlayer()));
        //set new position of player
        board.getAllPlayers().get(board.getCurrentPlayer()).newPosition(total);
        //move player token to new position
        squareCoordinates.get(board.getAllPlayers().get(board.getCurrentPlayer()).getPosition()).getChildren().add(board.getAllPlayers().get(board.getCurrentPlayer()).getCharacter());
    }

    public void initializeSquares() throws FileNotFoundException {
        //add all player tokens to starting position and add characters
        for (Player p : board.getAllPlayers()) {
            setPlayerImage(p);
            StackPane1.getChildren().add(p.getCharacter());
        }
        for (int i = 1; i <= board.getAllPlayers().size(); i++) {
            playerBalance.get(i).setText("Player " + i + ": £" + board.getAllPlayers().get(i - 1).getBalance());
        }
    }

    public void setPlayerImage(Player p) {
        switch (p.getId()) {
            case 1:
                p.setCharacter(new ImageView(new Image("SoftwareEngineering/Images/cat.png")));
                break;
            case 2:
                p.setCharacter(new ImageView(new Image("SoftwareEngineering/images/boot.png")));
                break;
            case 3:
                p.setCharacter(new ImageView(new Image("SoftwareEngineering/images/goblet.png")));
                break;
            case 4:
                p.setCharacter(new ImageView(new Image("SoftwareEngineering/images/hatstand.png")));
                break;
            case 5:
                p.setCharacter(new ImageView(new Image("SoftwareEngineering/images/smartphone.png")));
                break;
            case 6:
                p.setCharacter(new ImageView(new Image("SoftwareEngineering/images/spoon.png")));
                break;
        }
    }
}
