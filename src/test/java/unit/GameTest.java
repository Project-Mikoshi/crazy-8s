package unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.Game;

public class GameTest {
  Game game;

  @BeforeEach
  public void beforeAll () {
    game = new Game(null);
  }

  @Test
  public void shouldHaveCorrectAmountOfCardsInDeck () {
    Assertions.assertEquals(52, game.getDeck().size());
  }
}
