package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import config.GameConfig;
import constant.CardValue;
import model.Card;
import model.Player;

public class GameUtil {
  public static Stack<Card> shuffleAndBuildCardsStack () {
    return new Stack<Card>(){{
      List<Card> cards = GameConfig.CARDS;
      Collections.shuffle(cards);
      addAll(cards);
    }};
  }

  @SuppressWarnings("null")
  public static int calculateScore (ArrayList<Card> cards) {
    return cards.stream().reduce(0, (subTotal, card) -> {
      int points = GameConfig.POINTS_BY_CARD_VALUE.get(card.getValue());

      return subTotal + points;
    }, Integer::sum);
  }

  public static boolean doesTwoCardsMatch (Card playerCard, Card target) {
    String value = playerCard.getValue();
    String suit = playerCard.getSuit();

    return value.equals(CardValue.EIGHT) || value.equals(target.getValue()) || suit.equals(target.getSuit());
  }

  public static boolean somePlayerHasPlayableCards (ArrayList<Player> players) {
    return players.stream().anyMatch(player -> playerHasPlayableCards(player));
  }

  public static boolean playerHasPlayableCards (Player player) {
    return player.getCardsHeld().stream().anyMatch(card -> card.getIsPlayable());
  }
}
