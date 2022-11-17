package game;

import java.util.Collections;
import java.util.List;
import java.util.Stack;
import config.GameConfig;

public class Util {
  public static Stack<Card> shuffleAndBuildCardsStack () {
    return new Stack<Card>(){{
      List<Card> cards = GameConfig.CARDS;
      Collections.shuffle(cards);
      addAll(cards);
    }};
  }
}
