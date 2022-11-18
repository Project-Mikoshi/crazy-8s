package config;

import java.util.ArrayList;

import constant.CardColor;
import constant.CardSuit;
import constant.CardValue;
import game.Card;

public class GameConfig {
  public static final int NUM_OF_PLAYERS = 1;

  public static final int NUM_OF_INITIAL_CARDS = 5;

  public static final String GAME_ROOM = "crazy 8s";

  public static final String[] CARD_SUITS = {
    CardSuit.CLUBS,
    CardSuit.DIAMONDS,
    CardSuit.HEARTS,
    CardSuit.SPADES
  };

  public static final String[] CARD_COLORS = {
    CardColor.BLACK,
    CardColor.RED
  };

  public static final String[] CARD_VALUES = {
    CardValue.A,
    CardValue.TWO,
    CardValue.THREE,
    CardValue.FOUR,
    CardValue.FIVE,
    CardValue.SIX,
    CardValue.SEVEN,
    CardValue.EIGHT,
    CardValue.NINE,
    CardValue.TEN,
    CardValue.JACK,
    CardValue.QUEEN,
    CardValue.KING
  };

  public static final ArrayList<Card> CARDS = new ArrayList<>(){{
    for (String suit: CARD_SUITS) {
      for (String value: CARD_VALUES) {
        if (suit.equals(CardSuit.HEARTS) || suit.equals(CardSuit.DIAMONDS)) {
          add(new Card(suit, value, CardColor.RED));
        } else {
          add(new Card(suit, value, CardColor.BLACK));
        }
      }
    }
  }};
}
