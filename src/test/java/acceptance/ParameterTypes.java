package acceptance;

import java.util.HashMap;
import constant.*;
import constant.CardSuit;
import io.cucumber.java.ParameterType;

public class ParameterTypes {
  // == Parameter Type =======================
  @ParameterType(".*")
  public String CardSuit (String suit) {
    HashMap<String, String> cardSuitMap = new HashMap<>(){{
      put("C", CardSuit.CLUBS);
      put("D", CardSuit.DIAMONDS);
      put("H", CardSuit.HEARTS);
      put("S", CardSuit.SPADES);
    }};
    
    return cardSuitMap.get(suit);
  }

  @ParameterType(".*")
  public String CardValue (String suit) {
    HashMap<String, String> cardValueMap = new HashMap<>(){{
      put("A", constant.CardValue.A);
      put("2", constant.CardValue.TWO);
      put("3", constant.CardValue.THREE);
      put("4", constant.CardValue.FOUR);
      put("5", constant.CardValue.FIVE);
      put("6", constant.CardValue.SIX);
      put("7", constant.CardValue.SEVEN);
      put("8", constant.CardValue.EIGHT);
      put("9", constant.CardValue.NINE);
      put("10", constant.CardValue.TEN);
      put("J", constant.CardValue.JACK);
      put("Q", constant.CardValue.QUEEN);
      put("K", constant.CardValue.KING);
    }};
    
    return cardValueMap.get(suit);
  }

  @ParameterType(".*")
  public String Direction (String direction) {
    HashMap<String, String> directionMap = new HashMap<>(){{
      put("normal", Direction.NORMAL);
      put("opposite", Direction.REVERSE);
    }};

    return directionMap.get(direction);
  }
}
