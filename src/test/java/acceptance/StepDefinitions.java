package acceptance;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;
import constant.CardColor;
import model.Card;
import server.GameModule;
import server.ServerApplication;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ServerApplication.class)
@CucumberContextConfiguration
public class StepDefinitions {
  private static final String HOST_NAME = "http://127.0.0.1";
  private static final By PLAYER_NAME_INPUT = By.id("player-name-input-box");
  private static final By JOIN_GAME_BUTTON = By.id("join-button");
  private static final By USER_STATUS_PLAYING = By.cssSelector("[data-testid='user-status-playing']");
  private static final By USER_STATUS_WAITING = By.cssSelector("[data-testid='user-status-waiting']");
  private static final By USER_NAME_DISPLAY_TEXT = By.cssSelector("[data-testid='user-name-display-text'");

  // == Props ================================
  @Autowired GameModule game;

  @LocalServerPort int port;
  
  ChromeOptions driverOptions;

  HashMap<Integer, WebDriver> drivers;

  // == Hooks ================================
  @Before
  public void setup () {
    game.reset();
    driverOptions = new ChromeOptions();
    // driverOptions.setHeadless(true);
    driverOptions.addArguments("window-size=3840,2160");
    driverOptions.setImplicitWaitTimeout(Duration.ofSeconds(5));
    driverOptions.setPageLoadTimeout(Duration.ofSeconds(5));
  }

  @After
  public void tearDown () {
    drivers.forEach((id, driver) -> {
      driver.quit();
    });
  }

  // == Step Defs - Given ====================
  @Given("there are {int} players")
  public void startGame (int count) {
    drivers = new HashMap<>(){{
      for (int i = 1; i <= count; i++) {
        WebDriver driver = WebDriverManager.chromedriver().capabilities(driverOptions).create();
        put(i, driver);
      }
    }};

    drivers.forEach((id, driver) -> {
      driver.get(HOST_NAME + ":" + port);
    });
  }

  @Given("top card is {CardValue}-{CardSuit}")
  public void changeCardOnTopOfDiscardedPile (String cardValue, String cardSuit) {
    game.getDiscardPile().push(new Card(cardSuit, cardValue, CardColor.BLACK, false));
  }

  // == Step Defs - When =====================
  @When("player {int} try to play {CardValue}-{CardSuit} with {Outcome}")
  public void playerAttemptPlayCard (int id, String cardValue, String cardSuit, boolean isSuccessful) {
    WebDriver driver = drivers.get(id);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    By cardSelector = By.cssSelector("[data-testid='%s-%s-enabled']".formatted(cardValue, cardSuit));

    wait.until(ExpectedConditions.presenceOfElementLocated(USER_NAME_DISPLAY_TEXT));

    UUID playerId = game.getCurrentPlayer();
    Card cardToPlay = new Card(cardSuit, cardValue, CardColor.BLACK, false);
    Card fillerCard = new Card("test", "test", CardColor.RED, false);

    game.getPlayers().get(playerId).setCardsHeld(new ArrayList<>(){{
      add(cardToPlay);
      add(fillerCard);
    }});
    game.updateCardsOnPlayersHand();

    if (isSuccessful) {
      wait.until(ExpectedConditions.presenceOfElementLocated(cardSelector));
      driver.findElement(cardSelector).click();
      wait.until(ExpectedConditions.presenceOfElementLocated(USER_STATUS_WAITING));
    }
  }
  
  @When("player {int} plays {CardValue}-{CardSuit}")
  public void playerPlayCard (int id, String cardValue, String cardSuit) {
    changeCardOnTopOfDiscardedPile(cardValue, cardSuit);
    playerAttemptPlayCard(id, cardValue, cardSuit, true);
  }

  // == Step Defs - And ======================
  @And("all player joined the game room and game started")
  public void joinGameRoom () {
    drivers.forEach((id, driver) -> {
      WebElement nameInput = driver.findElement(PLAYER_NAME_INPUT);
      nameInput.sendKeys("player %d".formatted(id));
      WebElement joinButton = driver.findElement(JOIN_GAME_BUTTON);
      joinButton.click();
    });
  }

  // == Step Defs - Then =====================
  @Then("next player is player {int}")
  public void checkPlayerOrder (int id) {
    WebDriver driver = drivers.get(id);
    WebElement playerStatusText = driver.findElement(USER_STATUS_PLAYING);

    Assertions.assertNotNull(playerStatusText);
  }

  @Then("game is playing in {Direction} direction")
  public void checkGameDirection (String direction) {
    drivers.forEach((id, driver) -> {
      WebElement directionText = driver.findElement(By.cssSelector("[data-testid='game-status-direction'"));

      Assertions.assertNotNull(directionText);
      Assertions.assertTrue(directionText.getText().equalsIgnoreCase(direction));
    });
  }

  @Then("top of discard pile is now {CardValue}-{CardSuit}")
  public void checkTopCard (String cardValue, String cardSuit) {
    Card topCard = game.getDiscardPile().peek();

    Assertions.assertTrue(topCard.getSuit().equalsIgnoreCase(cardSuit) && topCard.getValue().equalsIgnoreCase(cardValue));
  }
}
