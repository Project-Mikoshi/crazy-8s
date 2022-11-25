package unit;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import config.GameConfig;
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

  @Test
  public void shouldCorrectlyCheckIfAnyPlayerHasPlayableCard () {
    Card playableCard = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED, true);
    Card nonPlayableCard = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED, false);

    Player player1 = new Player(null, "test");
    Player player2 = new Player(null, "test");
    Player player3 = new Player(null, "test");
    player1.setCardsHeld(new ArrayList<Card>(){{
      add(playableCard);
      add(nonPlayableCard);
    }});
    player2.setCardsHeld(new ArrayList<Card>(){{
      add(nonPlayableCard);
      add(nonPlayableCard);
    }});
    player3.setCardsHeld(new ArrayList<Card>(){{
      add(nonPlayableCard);
      add(nonPlayableCard);
    }});

    Assertions.assertTrue(GameUtil.somePlayerHasPlayableCards(new ArrayList<>(){{
      add(player1);
      add(player2);
      add(player3);
    }}));

    player1.setCardsHeld(new ArrayList<Card>(){{
      add(nonPlayableCard);
      add(nonPlayableCard);
    }});

    Assertions.assertFalse(GameUtil.somePlayerHasPlayableCards(new ArrayList<>(){{
      add(player1);
      add(player2);
      add(player3);
    }}));
  }

  @Test
  public void shouldReturnCorrectlyWhetherPlayerDrawAbilityBeDisabled () {
    Card cardOnTopOfDiscardedPile = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED, true);
    Card playableCard = new Card(CardSuit.CLUBS, CardValue.TWO, CardColor.RED, true);
    Card nonPlayableCard = new Card(CardSuit.HEARTS, CardValue.THREE, CardColor.RED, false);

    Player player = new Player(null, "test");

    player.setCardsHeld(new ArrayList<>(){{
      add(nonPlayableCard);
      add(nonPlayableCard);
    }});

    Assertions.assertTrue(GameUtil.shouldDrawAbilityBeDisabled(player, playableCard, cardOnTopOfDiscardedPile));
    Assertions.assertFalse(GameUtil.shouldDrawAbilityBeDisabled(player, nonPlayableCard, cardOnTopOfDiscardedPile));

    player.setDrawnCardCount(GameConfig.MAX_DRAW_PER_TURN - 1);

    Assertions.assertFalse(GameUtil.shouldDrawAbilityBeDisabled(player, nonPlayableCard, cardOnTopOfDiscardedPile));

    player.setDrawnCardCount(GameConfig.MAX_DRAW_PER_TURN);

    Assertions.assertTrue(GameUtil.shouldDrawAbilityBeDisabled(player, nonPlayableCard, cardOnTopOfDiscardedPile));
  }
}
