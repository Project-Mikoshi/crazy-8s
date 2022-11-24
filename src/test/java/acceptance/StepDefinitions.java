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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;
import constant.CardColor;
import constant.SocketEvent;
import model.Card;
import server.GameModule;
import server.ServerApplication;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ServerApplication.class)
@CucumberContextConfiguration
public class StepDefinitions {
  private static final String HOST_NAME = "http://127.0.0.1";

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
    driverOptions.setHeadless(true);
    driverOptions.addArguments("window-size=3840,2160");
    driverOptions.setImplicitWaitTimeout(Duration.ofSeconds(5));
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

  // == Step Defs - When =====================
  @When("player {int} plays {CardValue}-{CardSuit}")
  public void playerPlayCard (int id, String cardValue, String cardSuit) throws InterruptedException {
    WebDriver driver = drivers.get(id);

    Thread.sleep(500);

    UUID playerId = game.getCurrentPlayer();

    Card cardToPlay = new Card(cardSuit, cardValue, CardColor.BLACK, true);
    Card fillerCard = new Card("test", "test", CardColor.RED, false);

    game.getServer().getClient(playerId).sendEvent(SocketEvent.GAME_UPDATE_CARDS, new ArrayList<Card>(){{
      add(cardToPlay);
      add(fillerCard);
    }});

    Thread.sleep(500);

    WebElement cardElement = driver.findElement(By.cssSelector("[data-testid='%s-%s']".formatted(cardToPlay.getValue(), cardToPlay.getSuit())));

    cardElement.click();

    Thread.sleep(500);
  }

  // == Step Defs - And ======================
  @And("all player joined the game room and game started")
  public void joinGameRoom () {
    drivers.forEach((id, driver) -> {
      WebElement nameInput = driver.findElement(By.id("player-name-input-box"));
      nameInput.sendKeys("player %d".formatted(id));
      WebElement joinButton = driver.findElement(By.id("join-button"));
      joinButton.click();
    });
  }

  // == Step Defs - Then =====================
  @Then("next player is player {int}")
  public void checkPlayerOrder (int id) {
    WebDriver driver = drivers.get(id);

    WebElement playerStatusText = driver.findElement(By.cssSelector("[data-testid='user-status-playing']"));

    Assertions.assertNotNull(playerStatusText);
  }

  @Then("test finished")
  public void teardown () {
    drivers.forEach((id, driver) -> {
      driver.quit();
    });
  }
}
