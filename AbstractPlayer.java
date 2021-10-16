import java.util.ArrayList;

public class AbstractPlayer {
    public String name;
    public int principal;
    public int bet;
    public ArrayList<Hand> hands; // in case of split

    public AbstractPlayer(String name) {
        this.name = name;
        hands = new ArrayList<Hand>();
    }

    public void lose() {
        principal -= bet;
    }

    public void win() {
        principal += bet;
    }

    public void stand(Hand hand) {
        hand.stand();
    }

    public boolean bust() {
        boolean isBust = false;
        for (Hand h : hands) {
            isBust = isBust && h.isBust();
        }
        return isBust;
    }

    public boolean isBroke() {
        if (principal <= 0) {
            return true;
        }
        return false;
    }

    public void newRound() {
        for (Hand h : hands) {
            for (Card c : h.getCards()) {
                c.setFace(Face.UP);
            }
        }
        hands.clear();
        hands.add(new Hand());
    }

    public ArrayList<Hand> getHands() {
        return hands;
    }

    public Hand getHand(int index) {
        return hands.get(index);
    }

    public String getHandsPrint() {
        String playerHands = "";
        for (Hand h : hands) {
            playerHands += h.toString() + " ";
        }
        playerHands += "\n";
        return playerHands;
    }

    public String getHandsFaces() {
        String playerHands = "";
        for (Hand h : hands) {
            playerHands += h.getHandFaces() + " ";
        }
        playerHands += "\n";
        return playerHands;
    }


}
