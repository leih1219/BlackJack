import javax.swing.*;
import java.awt.image.CropImageFilter;
import java.util.ArrayList;

public class Dealer extends AbstractPlayer{

    public Deck deck;
    public ArrayList<Card> discards;

    public Dealer(String name) {
        super(name);
        deck = new Deck();
    }

    public Dealer(String name, int principal) {
        this(name);
        this.principal = principal;
        discards = new ArrayList<Card>();
    }

    public String getName() {
        return name;
    }

    public int getPrincipal() {
        return principal;
    }

    public boolean start() {
        System.out.println(name + "\'s turn:");
        Hand hand = hands.get(0);
        if (hand.size() > 1) {
            hand.getCard(1).setFace(Face.UP);
        }
        return true;
    }

    public void shuffle() {
        deck.shuffle();
    }

    public void discardAll(ArrayList<Player> players) {
        for (Player p : players) {
            for (Hand h : hands) {
                discards.addAll(h.getCards());
            }
        }
    }

    public void check(ArrayList<Player> players) {
        if (deck.needShuffle) {
            deck.shuffle();
            discards.clear();
        } else {
            if (hands.size() > 0) {
                discards.addAll(hands.get(0).getCards());
                discardAll(players);
            }
        }
    }

    public void checkDeck() {
        if (deck.size() == 0) {
            deck.addDiscards(discards);
            discards.clear();
        }
    }

    public void deal(AbstractPlayer player, Hand hand) {
        checkDeck();
        System.out.println(name + " deals a card to " + player.name);
        hand.add(deck.getCard());
        System.out.println(player.name + "'s cards are: " + player.getHandsPrint());
    }

    //Overloading the method
    public void deal(AbstractPlayer player, Hand hand, Face face) {
        checkDeck();
        Card card = deck.getCard();
        if (face == Face.UP) {
            card.setFace(Face.UP);
            System.out.println(name + " deals a card to " + player.name + " with face up");
        } else {
            card.setFace(Face.DOWN);
            System.out.println(name + " deals a card to " + player.name + " with face down");
        }
        hand.add(card);
        System.out.println(player.name + "'s cards are: ");
        System.out.println(player.getHandsPrint());
    }

    public void hit(AbstractPlayer player, Hand hand) {
        System.out.println(player.name + " Hits!");
//        System.out.println("Hits!");
        deal(player,hand);
    }

    public boolean isHandDone() {
        return hands.get(0).isHandDone();
    }

}
