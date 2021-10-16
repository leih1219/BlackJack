import java.util.Scanner;

public class Player extends AbstractPlayer{
    private static Scanner sc = new Scanner(System.in);

    public Player(String name, int pricipal) {
        super(name);
        this.principal = pricipal;
    }

    public void startGame(Scanner sc) {
        System.out.println(name + "'s turn!");
        System.out.println(name + "'s cards are: " + hands.get(0).toString());
        bet = -1;
        System.out.println(name + " you have " + principal + " now.");
        System.out.println("Please input your bet:");
        System.out.println("Or you want to see your cards, please input \"s\":");
        while (bet < 0 || bet > principal) {
            System.out.println("Please input a valid number.");
            String currentBet = sc.nextLine();
            if (currentBet.equalsIgnoreCase("s")) {
                System.out.println(name + "'s cards are:");
                System.out.println(getHandsFaces());
                continue;
            }

            try {
                bet = Integer.parseInt(currentBet);
            } catch (Exception e) {
                bet = -1;
            }
        }
    }

    public void hit(Dealer dealer, Hand hand) {
        dealer.hit(this, hand);
    }

    public boolean split(Dealer dealer, Hand hand) {
        //Only available if the Player has two cards of the same rank
        //When the bet is over the principal, can't do that
        if (hand.size() > 2) {
            System.out.printf("Can't split, there are more than two cards in hand.");
            return false;
        } else if (hand.getCard(0).getValue() != (hand.getCard(1).getValue())) {
            System.out.printf("Can't split, they are not the same rank.");
            return false;
        } else if (bet * (hands.size() + 1) > principal) {
            System.out.printf("Can't split, total bet is greater than principal!");
            return false;
        }

        hands.remove(hand);

        System.out.printf("Split the hand: ");
        System.out.println(hand.toString());
        Hand newHand = new Hand();
        newHand.add(hand.removeCard(0));
        dealer.deal(this, hand);
        dealer.deal(this, newHand);
        //add a new hand
        hands.add(hand);
        hands.add(newHand);
        return true;
    }

    public boolean doubleUp(Dealer dealer, Hand hand) {
        //When the bet is over the principal, can't do that、
        if (bet * (hands.size() + 1) > principal) {
            System.out.printf("Can't double up, total bet is greater than principal!");
            return false;
        } else {
            dealer.hit(this, hand);
            hand.stand();
            bet *= 2;
            return true;
        }
    }

    public boolean isHandDone() {
        boolean doneFlag = true;
        for (Hand h : hands) {
            doneFlag = doneFlag && h.isHandDone();
        }
        return doneFlag;
    }
}