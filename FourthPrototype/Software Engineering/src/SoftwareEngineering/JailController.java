package SoftwareEngineering;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class JailController {
    private Controller controller;
    public Board board;
    public Button yesButton, noButton;
    private int freeJailCost;

    public void initialize(Board board) {
        this.board = board;
        this.freeJailCost = 50;
    }

    public void clickedYes() {
        board.getCurrentPLayer().payBank(freeJailCost);
        board.setFreeParking(board.getFreeParking() + freeJailCost);
        controller.addToGameLog("Player " + board.getCurrentPLayer().getId() + " paid a fine of £50 and has been released from prison.");
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
    }

    public void clickedNo() {
        board.getCurrentPLayer().spendDayInJail();
        controller.addToGameLog("Player " + board.getCurrentPLayer().getId() + " has declined to pay bail and has been jailed");
        Stage stage = (Stage) noButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the Controller field to the
     * controller given in the parameter.
     *
     * @param controller The controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
