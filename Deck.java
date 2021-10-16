import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Deck {
    public boolean needShuffle;
    public static Rank[] ranks;
    public static Suit[] suits;
    public static ArrayList<Card> allCards = new ArrayList<Card>();

    private ArrayList<Card> cards = new ArrayList<Card>();

    static {
        for (Rank r : Rank.values()) {
            for (Suit s : Suit.values()) {
                allCards.add(new Card(r, s));
            }
        }
    }

    public Deck() {
        for (Card c : allCards) {
            c.setFace(Face.UP);
        }
        shuffle();
    }

//    private void addCards() {
//        for (Rank r : Rank.values()) {
//            for (Suit s : Suit.values()) {
//                cards.add(new Card(r, s));
//                allCards.add(new Card(r, s));
//            }
//        }
//    }

    public void shuffle() {
        cards.clear();
        cards.addAll(allCards);
        Collections.shuffle(cards);
        needShuffle = false;
    }

    public int size() {
        return cards.size();
    }

    public void addDiscards(ArrayList<Card> discards) {
        cards.addAll(discards);
        Collections.shuffle(cards);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Card getCard() {
        if (cards.size() == 1) {
            needShuffle = true;
        }
        return cards.remove(0);
    }
}
