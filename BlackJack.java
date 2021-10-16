public class BlackJack extends CardGame {

    public static BlackJack newGame;
    public static final int PRINCIPAL = 1000;
    public static final int MAX_VALUE = 21;
    public static final int DEALER_VALUE = 17;

    private BlackJack() {
        super(MAX_VALUE);
    }

    public static void start() {
        Hand.setMaxVal(MAX_VALUE);
        newGame = new BlackJack();
        newGame.addDealer("Dealer", PRINCIPAL);
        newGame.addPlayer("Player", PRINCIPAL);
        newGame.play();
    }

    @Override
    public boolean startRound() {
        System.out.println("Round:" + round++);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dealer.newRound();
        dealer.shuffle();
        for (Player p : players) {
            if (p.isBroke()) {
                continue;
            }
            p.newRound();
        }
        deal();

        for (Player p : players) {
            p.startGame(sc);
        }
        playerRound();

        if (!isAllLost()) {
            dealerRound();
        }
        checkResult();
        if (isAllLost()) {
            System.out.println("All players Bust");
        }

        if (!isAllBroke()) {
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
    //similar logic with dealer, but added split and double up
    public void getPlayerAction(Player player, Hand hand) {
        boolean action;
        do {
            action = false;
            System.out.println(player.name + ", enter the number of your action:");
            System.out.println("1. Hit; 2. Stand; 3. Split; 4. Double Up");
            int input = sc.nextInt();

            switch (input) {
                case 1:
                    player.hit(dealer, hand);
                    break;
                case 2:
                    player.stand(hand);
                    break;
                case 3:
                    boolean isSplit = player.split(dealer, hand);
                    if (!isSplit) {
                        System.out.println("Can't split, choose another action!");
                    }
                    break;
                case 4:
                    boolean isDoubleUp = player.doubleUp(dealer, hand);
                    if (!isDoubleUp) {
                        System.out.println("Can't double up, choose another action!");
                    }
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
                if (o1.size() == 2 && o2.size() != 2) {
                    return 1;
                } else if (o1.size() != 2 && o2.size() == 2) {
                    return -1;
                }
            }
        }
        return o1.compareTo(o2);
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

    public static void exit() {
        System.out.println("Thanks for playing, bye!");
        newGame = null;
    }


    @Override
    public void deal() {
        System.out.println("Now dealer will deal the card");
        try {
            Thread.sleep(500);
            //will give two cards, one face up and one face down
            for (Player p : players) {
                if (p.isBroke()) {
                    continue;
                }
                dealer.deal(p, p.getHand(0));
                Thread.sleep(500);
            }
            dealer.deal(dealer, dealer.getHand(0), Face.UP);
            Thread.sleep(500);

            for (Player p : players) {
                if (p.isBroke()) {
                    continue;
                }
                dealer.deal(p, p.getHand(0));
                Thread.sleep(500);
            }
            //Notice second time should be face down
            dealer.deal(dealer, dealer.getHand(0), Face.DOWN);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
