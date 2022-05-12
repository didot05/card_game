package SoftwareEngineering;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller class for "Auction.fxml".
 */
public class AuctionController implements Initializable {
    private Controller controller;
    public Button confirmBidsButton;
    public HBox root;
    public Label squareInfo;
    private ArrayList<Label> playerLabels;
    private ArrayList<PasswordField> playerBids;
    private Square squareToBuy;
    private Board board;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerBids = new ArrayList<PasswordField>();
        playerLabels = new ArrayList<Label>();
        for (Node n : root.getChildren()) {
            if (n instanceof VBox) {
                for (Node element : ((VBox) n).getChildren()) {
                    if (element instanceof Label) {
                        playerLabels.add((Label) element);
                    } else if (element instanceof PasswordField) {
                        playerBids.add((PasswordField) element);
                        ((PasswordField) element).textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                ((PasswordField) element).setText(newValue.replaceAll("[^\\d]", ""));
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * Handles the event when the user clicks the
     * "Confirm Bids" button. If there are no
     * valid bids, the auction is cancelled.
     * Bids will only be accepted if the player
     * has enough money in their balance to pay
     * the proposed bid. The highest bidder will
     * buy the square for the specified bid. If
     * there is a tie, the auction is restarted.
     */
    public void confirmBids() {
        Stage stage = (Stage) confirmBidsButton.getScene().getWindow();
        boolean tie = false;
        Map.Entry<Player, String> maxBid = null;
        HashMap<Player, String> bids = new HashMap<Player, String>();

        for (int i = 0; i < board.getAllPlayers().size(); i++) { //obtain bids
            bids.put(board.getAllPlayers().get(i), playerBids.get(i).getText());
        }
        for (Map.Entry<Player, String> entry : bids.entrySet()) {
            if (!entry.getValue().equals("") && entry.getKey().getBalance() >= Integer.parseInt(entry.getValue()) && Integer.parseInt(entry.getValue()) != 0) {
                //valid bid
                if (maxBid == null) {
                    maxBid = entry;
                }
                if (Integer.parseInt(entry.getValue()) >= Integer.parseInt(maxBid.getValue())) {
                    maxBid = entry;
                }
            }
        }
        for (Map.Entry<Player, String> entry : bids.entrySet()) {
            if (maxBid != null && !maxBid.getKey().equals(entry.getKey()) && maxBid.getValue().equals(entry.getValue())) {
                tie = true;
                break;
            }
        }
        if (maxBid == null) {
            controller.addToGameLog("No bids or valid bids were made, the auction has been cancelled");
            stage.close();
        } else if (tie) {
            for (PasswordField p : playerBids) {
                p.setText("");
                insertGameAgentBids();
            }
            controller.addToGameLog("Auction finished in a tie, restarting auction");
        } else {
            controller.addToGameLog("Sold! " + maxBid.getKey().getName() + " has bought " + squareToBuy.getName() + " for £" + maxBid.getValue());
            maxBid.getKey().buyProperty(squareToBuy, Integer.parseInt(maxBid.getValue()));
            stage.close();
        }
    }

    /**
     * Populates the GUI, displaying fields to
     * enter bids for every player and information
     * of the square on auction. Disables PasswordField
     * objects for AIs.
     *
     * @param board The board
     */
    public void initialize(Board board) {
        this.board = board;
        int maxPlayers = 6;
        int numOfPlayers = board.getAllPlayers().size();
        squareToBuy = board.getAllSquares().get(board.getCurrentPLayer().getPosition());
        squareInfo.setText("Auction to Buy Square: " + squareToBuy.getName() + ",  Original Price: £" + squareToBuy.getCost());

        for (int i = 0; i < numOfPlayers; i++) {
            if (board.getAllPlayers().get(i).getPassedGo()) {
                playerLabels.get(i).setText(board.getAllPlayers().get(i).getName());
                if(board.getAllPlayers().get(i) instanceof GameAgent){
                    playerBids.get(i).setDisable(true);
                }
            } else {
                ((VBox) playerLabels.get(i).getParent()).getChildren().remove(playerLabels.get(i));
                ((VBox) playerBids.get(i).getParent()).getChildren().remove(playerBids.get(i));
            }
        }
        for (int i = 0; i < maxPlayers - numOfPlayers; i++) {
            ((VBox) playerLabels.get(maxPlayers - 1 - i).getParent()).getChildren().remove(playerLabels.get(maxPlayers - 1 - i));
            ((VBox) playerBids.get(maxPlayers - 1 - i).getParent()).getChildren().remove(playerBids.get(maxPlayers - 1 - i));
        }
        insertGameAgentBids();
    }

    /**
     * Inserts bids if the AI player wants to
     * place a bid.
     */
    public void insertGameAgentBids(){
        for (int i = 0; i < board.getAllPlayers().size(); i++) {
            if (board.getAllPlayers().get(i) instanceof GameAgent) {
                GameAgent currentGameAgent = (GameAgent) board.getAllPlayers().get(i);
                if (currentGameAgent.getDecision()&& currentGameAgent.getPassedGo()) {
                    playerBids.get(i).setText(Integer.toString(currentGameAgent.makeBid()));
                }
            }
        }
    }
    /**
     * Sets the controller field of the AuctionController object
     * to the controller given in the parameter.
     *
     * @param controller The controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
