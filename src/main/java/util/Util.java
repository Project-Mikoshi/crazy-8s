package util;

import java.util.ArrayList;
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
    return 0;
  }
}
