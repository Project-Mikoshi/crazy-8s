package unit;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import constant.CardColor;
import constant.CardSuit;
import constant.CardValue;
import model.Card;
import util.Util;

public class UtilTest {

  @Test
  public void shouldHaveCorrectAmountOfCardsInDeck () {
    Assertions.assertEquals(52, Util.shuffleAndBuildCardsStack().size());
  }

  @Test
  public void shouldReturnCorrectScore () {
    Assertions.assertEquals(5, Util.calculateScore(new ArrayList<>(){{
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
    }}));
  }
}
