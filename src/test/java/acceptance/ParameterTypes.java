package acceptance;

import java.util.HashMap;
import constant.*;
import constant.CardSuit;
import io.cucumber.java.ParameterType;

public class ParameterTypes {
  public static final HashMap<String, String> CARD_SUIT_MAP = new HashMap<>(){{
    put("C", CardSuit.CLUBS);
    put("D", CardSuit.DIAMONDS);
    put("H", CardSuit.HEARTS);
    put("S", CardSuit.SPADES);
  }};

  public static final HashMap<String, String> CARD_VALUE_MAP = new HashMap<>(){{
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

  public static final HashMap<String, String> DIRECTION_MAP = new HashMap<>(){{
    put("normal", Direction.NORMAL);
    put("opposite", Direction.REVERSE);
  }};

  public static final HashMap<String, Boolean> OUTCOME_MAP = new HashMap<>(){{
    put("success", true);
    put("failure", false);
  }};

  // == Parameter Type =======================
  @ParameterType(".*")
  public String CardSuit (String suit) {
    return CARD_SUIT_MAP.get(suit);
  }

  @ParameterType(".*")
  public String CardValue (String suit) {
    return CARD_VALUE_MAP.get(suit);
  }

  @ParameterType(".*")
  public String Direction (String direction) {
    return DIRECTION_MAP.get(direction);
  }

  @ParameterType(".*")
  public boolean Outcome (String outcome) {
    return OUTCOME_MAP.get(outcome);
  }
}
