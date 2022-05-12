package SoftwareEngineering;

import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for "Jail.fxml"
 */
public class JailController {
    private Controller controller;
    public Board board;
    public Button yesButton, noButton;
    private int freeJailCost;

    /**
     * Initialize the board and cost of bail
     * @param board The board
     */
    public void initialize(Board board) {
        this.board = board;
        this.freeJailCost = 50;
    }

    /**
     * Handles the event when the player
     * clicks "Yes". The player is released
     * from jail and £50 is taken from
     * their balance. Closes the jail
     * window.
     */
    public void clickedYes() {
        board.getCurrentPLayer().payBank(freeJailCost);
        board.setFreeParking(board.getFreeParking() + freeJailCost);
        controller.addToGameLog("Player " + board.getCurrentPLayer().getId() + " paid a fine of £50 and has been released from prison.");
        Stage stage = (Stage) yesButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the event when the player
     * clicks "No". The player remains in
     * jail for the next two turns.
     */
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
