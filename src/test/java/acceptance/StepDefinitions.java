package acceptance;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import constant.CardSuit;
import constant.CardValue;
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
  private static final By DRAW_CARD_BUTTON = By.id("draw-card-button");
  private static final By USER_STATUS_PLAYING = By.cssSelector("[data-testid='user-status-playing']");
  private static final By USER_STATUS_WAITING = By.cssSelector("[data-testid='user-status-waiting']");
  private static final By USER_NAME_DISPLAY_TEXT = By.cssSelector("[data-testid='user-name-display-text'");
  private static final By MODAL_CHOOSE_SUIT_PROMPT = By.cssSelector("[data-testid='modal-choose-suit-prompt'");
  private static final By GAME_STATUS_DIRECTION = By.cssSelector("[data-testid='game-status-direction'");
  private static final By GAME_STATUS_ROUND = By.cssSelector("[data-testid='game-status-round'");
  private static final By GAME_STATUS_WINNER = By.cssSelector("[data-testid='game-status-winner'");

  // == Props ================================
  @Autowired GameModule game;

  @LocalServerPort int port;
  
  ChromeOptions driverOptions;

  HashMap<Integer, WebDriver> drivers;
  HashMap<Integer, UUID> playerIds;

  // == Hooks ================================
  @Before
  public void setup () {
    game.reset();
    driverOptions = new ChromeOptions();
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

    wait.until(ExpectedConditions.presenceOfElementLocated(USER_NAME_DISPLAY_TEXT));

    UUID playerId = playerIds.get(id);
    Card cardToPlay = new Card(cardSuit, cardValue, CardColor.BLACK, false);
    Card fillerCard = new Card(CardSuit.TEST, CardValue.TEST, CardColor.RED, false);

    game.getPlayers().get(playerId).setCardsHeld(new ArrayList<>(){{
      add(cardToPlay);
      add(fillerCard);
    }});
    game.updateCardsOnPlayersHand();

    if (isSuccessful) {
      By cardSelector = By.cssSelector("[data-testid='%s-%s-enabled']".formatted(cardValue, cardSuit));
      wait.until(ExpectedConditions.presenceOfElementLocated(cardSelector));
      driver.findElement(cardSelector).click();

      if (!cardValue.equalsIgnoreCase(CardValue.EIGHT)) {
        wait.until(ExpectedConditions.presenceOfElementLocated(USER_STATUS_WAITING));
      }
    } else {
      By cardSelector = By.cssSelector("[data-testid='%s-%s-disabled']".formatted(cardValue, cardSuit));
      wait.until(ExpectedConditions.presenceOfElementLocated(cardSelector));
      Assertions.assertNotNull(driver.findElement(cardSelector));
    }
  }
  
  @When("player {int} plays {CardValue}-{CardSuit}")
  public void playerPlayCard (int id, String cardValue, String cardSuit) {
    changeCardOnTopOfDiscardedPile(cardValue, cardSuit);
    playerAttemptPlayCard(id, cardValue, cardSuit, true);
  }

  @When("from existing cards, player {int} plays {CardValue}-{CardSuit}")
  public void playerPlayFromExistingCard (int id, String cardValue, String cardSuit) {
    WebDriver driver = drivers.get(id);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    wait.until(ExpectedConditions.presenceOfElementLocated(USER_NAME_DISPLAY_TEXT));

    By cardSelector = By.cssSelector("[data-testid='%s-%s-enabled']".formatted(cardValue, cardSuit));
    wait.until(ExpectedConditions.presenceOfElementLocated(cardSelector));
    driver.findElement(cardSelector).click();
  }

  @When("player {int} has following cards:")
  public void playerHasCards (int id, List<String> cards) {
    ArrayList<Card> playerCards = new ArrayList<>(){{
      for (String card: cards) {
        String value = card.split("-")[0];
        String suit = card.split("-")[1];

        add(new Card(ParameterTypes.CARD_SUIT_MAP.get(suit), ParameterTypes.CARD_VALUE_MAP.get(value), CardColor.RED, false));
      }
    }};

    UUID playerId = playerIds.get(id);

    game.getPlayers().get(playerId).setCardsHeld(playerCards);
    game.updateCardsOnPlayersHand();
  }
  
  @When("player {int} draw and get {CardValue}-{CardSuit}")
  public void playerDrawAndHasCards (int id, String cardValue, String cardSuit) {
    Card card = new Card(cardSuit, cardValue, CardColor.BLACK, false);
    game.getDeck().push(card);
    WebDriver driver = drivers.get(id);
    driver.findElement(DRAW_CARD_BUTTON).click();
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

    while (game.getPlayers().size() != drivers.size()) {}

    playerIds = new HashMap<>(){{
      drivers.forEach((id, driver) -> {
        put(id, game.getPlayerOrder().get(id - 1));
      });
    }};
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
      WebElement directionText = driver.findElement(GAME_STATUS_DIRECTION);

      Assertions.assertNotNull(directionText);
      Assertions.assertTrue(directionText.getText().equalsIgnoreCase(direction));
    });
  }

  @Then("player {int} is prompted to chose a suit")
  public void checkForModalPrompt (int id) {
    WebDriver driver = drivers.get(id);
    WebElement modal = driver.findElement(MODAL_CHOOSE_SUIT_PROMPT);

    Assertions.assertNotNull(modal);
  }

  @Then("top of discard pile is now {CardValue}-{CardSuit}")
  public void checkTopCard (String cardValue, String cardSuit) {
    Card topCard = game.getDiscardPile().peek();

    Assertions.assertTrue(topCard.getSuit().equalsIgnoreCase(cardSuit) && topCard.getValue().equalsIgnoreCase(cardValue));
  }

  @Then("player {int} must draw")
  public void checkDrawButton (int id) {
    WebDriver driver = drivers.get(id);
    WebElement drawButton = driver.findElement(DRAW_CARD_BUTTON);

    Assertions.assertNotNull(drawButton);
  }

  @Then("player {int} must play a card")
  public void checkPlayingCard (int id) {
    WebDriver driver = drivers.get(id);

    Assertions.assertNotNull(driver.findElement(USER_STATUS_PLAYING));
    Assertions.assertTrue(driver.findElements(DRAW_CARD_BUTTON).isEmpty());
  }

  @Then("player {int} turn ended")
  public void checkPlayerTurnEnded (int id) {
    WebDriver driver = drivers.get(id);

    Assertions.assertNotNull(driver.findElement(USER_STATUS_WAITING));
  }

  @Then("game round advanced to {int}")
  public void checkGameRound (int round) {
    drivers.forEach((id, driver) -> {
      WebElement roundNumber = driver.findElement(GAME_STATUS_ROUND);
      Assertions.assertTrue(roundNumber.getText().equalsIgnoreCase(String.valueOf(round)));
    });
  }

  @Then("player {int} scores is {int}")
  public void checkScores(int id, int score) {
    WebDriver driver = drivers.get(id);
    UUID playerId = playerIds.get(id);

    WebElement scoreText = driver.findElement(By.cssSelector("[data-testid='player-score-%s'".formatted(playerId.toString())));
    Assertions.assertTrue(scoreText.getText().equalsIgnoreCase(String.valueOf(score)));
  }

  @Then("game is over with player {int} being the winner")
  public void checkGameOver (int id) {
    UUID playerId = playerIds.get(id);
    String winnerName = game.getPlayers().get(playerId).getName();
    
    drivers.forEach((driverId, driver) -> {
      WebElement winner = driver.findElement(GAME_STATUS_WINNER);
      Assertions.assertTrue(winner.getText().equalsIgnoreCase(winnerName));
    });
  }
}
