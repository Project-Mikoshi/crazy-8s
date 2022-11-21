package unit;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.GameUtil;
import constant.CardColor;
import constant.CardSuit;
import constant.CardValue;
import model.Card;

public class GameUtilTest {

  @Test
  public void shouldHaveCorrectAmountOfCardsInDeck () {
    Assertions.assertEquals(52, GameUtil.shuffleAndBuildCardsStack().size());
  }

  @Test
  public void shouldReturnCorrectScore () {
    Assertions.assertEquals(5, GameUtil.calculateScore(new ArrayList<>(){{
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
    }}));

    Assertions.assertEquals(6, GameUtil.calculateScore(new ArrayList<>(){{
      add(new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
    }}));

    Assertions.assertEquals(54, GameUtil.calculateScore(new ArrayList<>(){{
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED));
      add(new Card(CardSuit.CLUBS, CardValue.EIGHT, CardColor.RED));
    }}));
  }

  @Test
  public void shouldCorrectlyVerifyCard () {
    Card playerCard = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED);
    Card target = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED);

    Assertions.assertTrue(GameUtil.doesTwoCardsMatch(playerCard, target));

    playerCard.setValue(CardValue.EIGHT);
    playerCard.setSuit(CardSuit.DIAMONDS);
    Assertions.assertTrue(GameUtil.doesTwoCardsMatch(playerCard, target));

    playerCard.setValue(CardValue.TWO);
    playerCard.setSuit(CardSuit.DIAMONDS);
    Assertions.assertTrue(GameUtil.doesTwoCardsMatch(playerCard, target));

    playerCard.setValue(CardValue.QUEEN);
    playerCard.setSuit(CardSuit.CLUBS);
    Assertions.assertTrue(GameUtil.doesTwoCardsMatch(playerCard, target));

    playerCard.setValue(CardValue.THREE);
    playerCard.setSuit(CardSuit.DIAMONDS);
    Assertions.assertFalse(GameUtil.doesTwoCardsMatch(playerCard, target));
  }
}
