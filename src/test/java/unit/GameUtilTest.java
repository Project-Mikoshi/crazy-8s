package unit;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.GameUtil;
import constant.CardColor;
import constant.CardSuit;
import constant.CardValue;
import model.Card;
import model.Player;

public class GameUtilTest {

  @Test
  public void shouldHaveCorrectAmountOfCardsInDeck () {
    Assertions.assertEquals(52, GameUtil.shuffleAndBuildCardsStack().size());
  }

  @Test
  public void shouldReturnCorrectScore () {
    Assertions.assertEquals(5, GameUtil.calculateScore(new ArrayList<>(){{
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
    }}));

    Assertions.assertEquals(6, GameUtil.calculateScore(new ArrayList<>(){{
      add(new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
    }}));

    Assertions.assertEquals(54, GameUtil.calculateScore(new ArrayList<>(){{
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.A, CardColor.RED, true));
      add(new Card(CardSuit.CLUBS, CardValue.EIGHT, CardColor.RED, true));
    }}));
  }

  @Test
  public void shouldCorrectlyVerifyCard () {
    Card playerCard = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED, true);
    Card target = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED, true);

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

  @Test
  public void shouldCorrectlyCheckIfPlayerHasPlayableCard () {
    Card playableCard = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED, true);
    Card nonPlayableCard = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED, false);

    Player player = new Player(null, "test");
    player.setCardsHeld(new ArrayList<Card>(){{
      add(playableCard);
      add(nonPlayableCard);
    }});

    Assertions.assertTrue(GameUtil.playerHasPlayableCards(player));

    player.setCardsHeld(new ArrayList<Card>(){{
      add(playableCard);
      add(playableCard);
    }});

    Assertions.assertTrue(GameUtil.playerHasPlayableCards(player));

    player.setCardsHeld(new ArrayList<Card>(){{
      add(nonPlayableCard);
      add(nonPlayableCard);
    }});

    Assertions.assertFalse(GameUtil.playerHasPlayableCards(player));
  }
}
