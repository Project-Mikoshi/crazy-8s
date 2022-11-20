package unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.Util;

public class UtilTest {

  @Test
  public void shouldHaveCorrectAmountOfCardsInDeck () {
    Assertions.assertEquals(52, Util.shuffleAndBuildCardsStack().size());
  }
}
