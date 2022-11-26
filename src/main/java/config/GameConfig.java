package config;

import java.util.ArrayList;
import java.util.HashMap;

import constant.CardColor;
import constant.CardSuit;
import constant.CardValue;
import model.Card;

public class GameConfig {
  public static final int NUM_OF_PLAYERS = 4;

  public static final int NUM_OF_INITIAL_CARDS = 5;

  public static final int SCORE_THRESHOLD = 100;

  public static final int MAX_DRAW_PER_TURN = 3;

  public static final int DRAW_OR_PLAY_REQUIRED_FOR_CARD_TWO = 2;

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

  public static final HashMap<String, Integer> POINTS_BY_CARD_VALUE = new HashMap<>(){{
    put(CardValue.A, 1);
    put(CardValue.TWO, 2);
    put(CardValue.THREE, 3);
    put(CardValue.FOUR, 4);
    put(CardValue.FIVE, 5);
    put(CardValue.SIX, 6);
    put(CardValue.SEVEN, 7);
    put(CardValue.EIGHT, 50);
    put(CardValue.NINE, 9);
    put(CardValue.TEN, 10);
    put(CardValue.QUEEN, 10);
    put(CardValue.KING, 10);
    put(CardValue.JACK, 10);
  }};

  public static final ArrayList<Card> CARDS = new ArrayList<>(){{
    for (String suit: CARD_SUITS) {
      for (String value: CARD_VALUES) {
        if (suit.equals(CardSuit.HEARTS) || suit.equals(CardSuit.DIAMONDS)) {
          add(new Card(suit, value, CardColor.RED, true));
        } else {
          add(new Card(suit, value, CardColor.BLACK, true));
        }
      }
    }
  }};
}
