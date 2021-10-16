import java.util.ArrayList;

public class Hand implements Comparable<Hand> {
    public static int MAX_VAL;

    private ArrayList<Card> cards;
    public boolean bust;
    public boolean stand;

    public Hand() {
        cards = new ArrayList<Card>();
    }

    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void add(Card card) {
        cards.add(card);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public static void setMaxVal(int maxVal) {
        MAX_VAL = maxVal;
    }

    public int size() {
        return cards.size();
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public Card removeCard(int index) {
        return cards.remove(index);
    }

    public int getHandValue() {
        int value = 0;
        boolean hasAce = false;
        for (Card card : cards) {
            if (card.getRank() == Rank.ACE) {
                hasAce = true; // initial value of Ace is 0
            }
            value += card.getValue();
        }

        if (hasAce) {
            if (value + 11 > MAX_VAL) {
                value += 1;
            } else {
                value += 11;
            }
        }
        return value;
    }

    public void stand() {
        stand = true;
    }

    public boolean isBust() {
        return bust;
    }

    public boolean isHandDone() {
        return bust || stand;
    }

    public String getHandFaces() {
        StringBuffer sb = new StringBuffer();
        if (size() > 0) {
            for (Card c : cards) {
                sb.append(" " + c.cardFace());
            }
        } else {
            sb.append("Empty");
        }
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (size() > 0) {
            for (Card c : cards) {
                sb.append(" " + c.toString());
            }
        } else {
            sb.append("Empty");
        }
        return sb.toString().trim();
    }

    @Override
    public int compareTo(Hand o) {
        if (getHandValue() == o.getHandValue()) {
            return 0;
        } else if (getHandValue() > o.getHandValue()) {
            return 1;
        }
        return -1;
    }
}
