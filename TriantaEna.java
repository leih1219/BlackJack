import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TriantaEna extends CardGame {
    public static TriantaEna newGame;

    public static final int BANK_PRINCIPAL = 500;
    public static final int PLAYER_PRINCIPAL = 200;
    public static final int MAX_VALUE = 31;
    public static final int DEALER_VALUE = 27;

    public TriantaEna() {
        super(MAX_VALUE);
    }

    public static void start() {
        Hand.setMaxVal(MAX_VALUE);
        newGame = new TriantaEna();
        newGame.addDealer("Dealer", BANK_PRINCIPAL);
        newGame.addPlayer("Player", PLAYER_PRINCIPAL);
        newGame.play();
    }

    @Override
    public void play() {
        super.play();

        if (isAllBroke()) {
            System.out.println("All players broke, do you want to play again?");
            System.out.println("Press Y / N:");
        }
        if (sc.nextLine().equalsIgnoreCase("y")) {
            start();
        } else {
            exit();
        }
    }

    @Override
    public void printPrincipals() {
        ArrayList<AbstractPlayer> allPlayers = new ArrayList<AbstractPlayer>();
        allPlayers.addAll(players);
        allPlayers.add(dealer);
        Collections.sort(allPlayers, new Comparator<AbstractPlayer>() {
            @Override
            public int compare(AbstractPlayer o1, AbstractPlayer o2) {
                if (o1.principal < o2.principal) {
                    return 1;
                } else if (o1.principal == o2.principal) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        for (int i = 0; i < allPlayers.size(); i++) {
            System.out.println("No. " + i+1 + " Player: " + allPlayers.get(i).name + "Principal: " + players.get(i).principal);
        }

        convertToBank(allPlayers);

    }

    private void convertToBank(ArrayList<AbstractPlayer> allPlayers) {
        for (int i = 0; i < allPlayers.size(); i++) {
            AbstractPlayer ap = allPlayers.get(i);
            if (ap.isBroke()) {
                continue;
            }
            if (ap.equals(dealer)) {
                break;
            }
            if (ap.principal <= dealer.principal) {
                break;
            } else {
                String choice;
                System.out.println(ap.name + " has more money than dealer!");
                while (true) {
                    System.out.println("Do you want to be the dealer? Press Y / N");
                    choice = sc.nextLine();
                    if (choice.equalsIgnoreCase("Y")) { // want to be the dealer
                        players.remove(ap);
                        players.add(new Player(dealer.name, dealer.principal));
                        dealer = new Dealer(ap.name, ap.principal);
                        return;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void deal() {
        System.out.println("Now dealer will deal the card");
        try {
            Thread.sleep(500);

            //will give three cards
            for (Player p : players) {
                if (p.isBroke()) {
                    continue;
                }
                dealer.deal(p, p.getHand(0), Face.DOWN);
                Thread.sleep(500);
            }
            dealer.deal(dealer, dealer.getHand(0), Face.UP);
            Thread.sleep(500);

            for (Player p : players) {
                if (p.isBroke()) {
                    continue;
                }
                p.startGame(sc);
            }

            for (Player p : players) {
                if (p.isBroke()) {
                    continue;
                }
                dealer.deal(p, p.getHand(0));
                Thread.sleep(500);
            }
            //Notice second time should be face down
            for (Player p : players) {
                if (p.isBroke()) {
                    continue;
                }
                dealer.deal(p, p.getHand(0));
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean startRound() {
        System.out.println("Round:" + round++);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dealer.check(players);
        dealer.newRound();
        for (Player p : players) {
            if (p.isBroke()) {
                continue;
            }
            p.newRound();
        }
        deal();

        playerRound();
        //if player lose, no dealer round then
        if (!isAllLost()) {
            dealerRound();
        }
        checkResult();
        if (isAllLost()) {
            System.out.println("All players Bust");
        }

        boolean hasWinner;
        if (isAllBroke()) {
            hasWinner = true;
        } else {
            if (!dealer.isBroke()) {
                hasWinner = false;
            } else {
                int numberOfWinner = 0;
                for (Player player : players) {
                    if (!player.isBroke()) {
                        numberOfWinner += 1;
                    }
                }
                hasWinner = (numberOfWinner == 1);
            }
        }

        if (!hasWinner) {
            System.out.println("Do you want to continue? Press Y / N:");
            if (sc.nextLine().equalsIgnoreCase("Y")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void getDealerAction(Hand hand) {
        boolean action;
        do {
            action = false;
            System.out.println(dealer.name + ", enter the number of your action:");
            System.out.println("1. Hit; 2. Stand");
            int input = sc.nextInt();
            switch (input) {
                case 1:
                    dealer.hit(dealer, hand);
                    break;
                case 2:
                    //check the total value has reached 17 first.
                    if (dealer.getHand(0).getHandValue() < DEALER_VALUE) {
                        System.out.println("Can't stand now, total value is less than " + DEALER_VALUE);
                    } else {
                        dealer.stand(hand);
                    }
                    break;
                default:
                    System.out.println("Please input number from 1 - 2");
                    break;
            }
            action = getHandResult(hand);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (action);
    }

    @Override
    public void getPlayerAction(Player player, Hand hand) {
        boolean action;
        do {
            action = false;
            System.out.println(player.name + ", enter the number of your action:");
            System.out.println("1. Hit; 2. Stand; 3. Show Cards");
            int input = sc.nextInt();

            switch (input) {
                case 1:
                    player.hit(dealer, hand);
                    break;
                case 2:
                    player.stand(hand);
                    break;
                case 3:
                    System.out.println(player.name + "'s Cards: " + player.getHandsFaces());
                    break;
                default:
                    System.out.println("Please input number from 1 - 4");
                    break;
            }
            action = getHandResult(hand);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (action);
    }

    @Override
    public int compare(Hand o1, Hand o2) {
        if (o1.compareTo(o2) == 0) {
            if (o1.getHandValue() == MAX_VALUE) {
                if (o1.size() == 3 && o2.size() != 3) {
                    return 1;
                } else if (o1.size() != 2 && o2.size() == 2) {
                    return -1;
                }
            }
        } else if (o1.compareTo(o2) == -1) {
            if (o1.getHandValue() == 14) {
                Suit suit = o1.getCard(0).getSuit();
                for (Card c : o1.getCards()) {
                    if (!c.getSuit().equals(suit)) {
                        return -1;
                    }
                }
                return 1;
            }
        }
        return o1.compareTo(o2);
    }

    @Override
    public void tie(AbstractPlayer player) {
        super.tie(player);
        dealer.win();
        player.lose();
    }

    public void exit() {
        System.out.println("Thanks for playing, bye!");
        newGame = null;
    }
}
