public class Card {

    private Rank rank;
    private Suit suit;
    private Face face;

    public  Card(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
        this.face = Face.DOWN;
    }

    //regardless of the face is up or down
    public String cardFace() {
        return rank.getName() + suit.getName();
    }

    @Override
    public String toString() {
        if (face == Face.UP) {
            return rank.getName() + suit.getName();
        } else {
            return "**";
        }
    }

    public int getValue() {
        if (rank.getName() == "J" || rank.getName() == "Q" || rank.getName() == "K") {
            return 10;
        } else if (rank.getName() == "A") {
            return 0;
        } else {
            return Integer.parseInt(rank.getName());
        }
    }

    public Rank getRank() {
        return this.rank;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public void setFace(Face f) {
        this.face = f;
    }

    public Face getFace() {
        return this.face;
    }
}
