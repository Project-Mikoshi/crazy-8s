package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import config.GameConfig;
import model.Card;

public class Util {
  public static Stack<Card> shuffleAndBuildCardsStack () {
    return new Stack<Card>(){{
      List<Card> cards = GameConfig.CARDS;
      Collections.shuffle(cards);
      addAll(cards);
    }};
  }

  public static int calculateScore (ArrayList<Card> cards) {
    return Arrays.asList(GameConfig.CARD_VALUES).stream().reduce(0, (subTotal, value) -> {
      int count = (int) cards.stream().filter(card -> card.getValue().equalsIgnoreCase(value)).count();

      int pointsPerCard = GameConfig.POINTS_BY_CARD_VALUE.get(value);

      subTotal += count * pointsPerCard;

      return subTotal;
    }, Integer::sum);
  }
}
