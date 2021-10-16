import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public abstract class CardGame implements Comparator<Hand> {
    public static Scanner sc;

    public static String userSelection;
    public ArrayList<Player> players;
    public Dealer dealer;
    public int round;
    public int maxValue;

    public CardGame() {
        players = new ArrayList<Player>();
        round = 0;
    }

    public CardGame(int maxValue) {
        this();
        this.maxValue = maxValue;
    }

    public int initialize() {
        System.out.println("Welcome to the table!");
        int numberOfPlayers = 0;

        //Here I set the max number of player to 5.
        while (numberOfPlayers < 1 || numberOfPlayers > 5) {
            System.out.println("Please input number between 1 and 5");
            String i = sc.nextLine();
            try {
                numberOfPlayers = Integer.parseInt(i);
            } catch (NumberFormatException e) {
                numberOfPlayers = 0;
            }
        }
        return numberOfPlayers;
    }

    public static void selectGame() {
        sc = new Scanner(System.in);
        System.out.println("Welcome! Choose a game you want to play.");
        System.out.println("1. BlackJack");
        System.out.println("2. Trianta Ena");
        System.out.println("Input any other key to quit.");

        while (true) {
            System.out.println("Please input the index of the game!");
            userSelection = sc.nextLine();
            if (userSelection.equals("1")) {
                BlackJack.start();
                break;
            } else if (userSelection.equals("2")) {
                TriantaEna.start();
                break;
            } else {
                break;
            }
        }

        System.out.println("Thank you, bye!");
        sc.close();
    }

    public void addDealer(String dealerName, int dealerPrincipal) {
        System.out.println("Welcome to the table!");
        dealer = new Dealer(dealerName, dealerPrincipal);

    }

    public abstract void deal();

    public void addPlayer(String playerName, int playerPrincipal) {
        int numberOfPlayers = 0;

        //Here I set the max number of player to 5.
        while (numberOfPlayers < 1 || numberOfPlayers > 9) {
            System.out.println("Please input number of the players between 1 and 9");
            String i = sc.nextLine();
            try {
                numberOfPlayers = Integer.parseInt(i);
            } catch (NumberFormatException e) {
                numberOfPlayers = 0;
            }
        }
        //set player
        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player(playerName + "_" + i, playerPrincipal));
        }
    }

    public abstract boolean startRound();

    public void printPrincipals() {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.principal < o2.principal) {
                    return 1;
                } else if (o1.principal == o2.principal) {
                    return 0;
                }
                return -1;
            }
        });

        for (int i = 0; i < players.size(); i++) {
            System.out.println("No. " + i+1 + " Player: " + players.get(i).name + "Principal: " + players.get(i).principal);
        }
    }
    public void play() {
        while (startRound()) {
            printPrincipals();
        }
    }

    public abstract void getDealerAction(Hand hand);

    public abstract void getPlayerAction(Player player, Hand hand);

    public boolean getHandResult(Hand hand) {
        int handValue = hand.getHandValue();
        if (handValue > maxValue) { //means bust
            hand.bust = true;
            System.out.println("Bust!");
        } else if (handValue == maxValue) {
            System.out.println("You got " + maxValue + "!!");
        }
        return !hand.isHandDone();
    }
    public boolean isAllLost() {
        boolean isBust = true;
        for (Player p : players) {
            isBust = isBust && (p.bust() || p.isBroke());
        }
        return isBust;
    }

    public boolean isAllBroke() {
        boolean isBroke = true;
        for (Player p : players) {
            isBroke = isBroke && p.isBroke();
        }
        return isBroke;
    }

    public void dealerRound() {
        if (dealer.start()) {
            while (true) {
                getDealerAction(dealer.getHand(0));
                if (dealer.isHandDone()) {
                    break;
                }
            }
        }
        getHandResult(dealer.getHand(0));

    }

    public void playerRound() {
        for (Player p : players) {
            while (!p.isHandDone()) {
                for (int i = 0; i < p.getHands().size(); i++) {
                    Hand hand = p.getHand(i);
                    if (hand.isHandDone()) {
                        continue;
                    }
                    getPlayerAction(p, hand);
                }
            }
        }
    }

    public void tie(AbstractPlayer player) {
        System.out.println("Tie!");
    }

    public void checkResult() {
        if (dealer.bust()) {
            System.out.println("Dealer BUST");
            for (Player p : players) {
                if (p.isBroke()) {
                    continue;
                }
                int totalWin = 0;
                for (Hand h : p.getHands()) {
                    h.getCard(0).setFace(Face.UP);
                    if (h.isBust()) {
                        continue;
                    }
                    totalWin += p.bet;
                    p.win();
                    dealer.lose();
                    System.out.println(p.name + "'s hand: " + h.toString() + "wins");
                }
                System.out.println(p.name + "wins: " + totalWin);
            }
        } else {
            for (Player p : players) {
                if (p.isBroke()) {
                    continue;
                }
                for (Hand h : p.getHands()) {
                    if (h.isBust()) {
                        p.lose();
                        dealer.win();
                        continue;
                    }
                    //compare the values
                    h.getCard(0).setFace(Face.UP);
                    int compare = 0;
                    compare = compare(h, dealer.getHand(0));
                    if (compare == -1) {
                        p.lose();
                        dealer.win();
                        System.out.println(p.name + "'s hand: " + h.toString() + "loses");
                        break;
                    } else if (compare == 0) {
                        tie(p);
                        break;
                    } else {
                        dealer.lose();
                        p.win();
                        System.out.println(p.name + "'s hand: " + h.toString() + "wins");
                        break;
                    }
                }
            }
        }
    }
}
