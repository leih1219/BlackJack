# BlackJack & TriantaEna
***

# Classes:

## Launch Classes

***GameStarter.java*** - The Launcher of the game

## GameClasses

***CardGame.java*** - Defines an abstract class for the card game

***BlackJack.java*** - Defines a BlackJack Game that extends CardGame

***TriantaEna.java*** - Defines a TriantaEna Game that extends CardGame

## PlayerClasses

***AbstractPlayer.java*** - Defines an abstract Player object for a CardGame, allows basic functionality and has a collection of Hands

***Player.java*** - Defines a player class extends AbstractPlayer, defines unique methods: Hit, Split, Double Up...

***Dealer.java*** - Defines a dealer class extends AbstractPlayer, defines it's unique methods: Deal, Hit, CheckDeck...

***Hand.java*** - Defines a class for the Hands of Card object, which will be used by the AbstractPlayers

## Deck Classes

***Deck.java*** - Defines a class for holding a stack of cards and has basic Deck logic

### Card Classes

***Card.java*** - Defines a Card class having basic functionality like: setFace(), getValue()...

***Face.java*** - An enum class defines the Face is UP or DOWN for a Card

***Suit.java*** - An enum class defines the Suit for a Card

***Rank.java*** - An enum class defines the Rank for a Card

***

# Improvement 
- Support multiple players: up to 10
- Use Comparable in Hand class

