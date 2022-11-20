package acceptance;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;
import server.GameModule;
import server.ServerApplication;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ServerApplication.class)
@CucumberContextConfiguration
public class StepDefinitions {
  private static final String HOST_NAME = "http://127.0.0.1";

  // == Props ================================
  WebDriver driver;

  @Autowired GameModule game;

  @LocalServerPort int port;

  // == Hooks ================================
  @Before
  public void setup () {
    driver = WebDriverManager.chromedriver().create();
  }

  // == Step Defs - Given ====================
  @Given("game started")
  public void startGame () {
    driver.get(HOST_NAME + ":" + port);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
  }

  // == Step Defs - When =====================
  @When("play joins the game")
  public void join () {
    WebElement nameInput = driver.findElement(By.id("player-name-input-box"));
    nameInput.sendKeys("player1");
    WebElement joinButton = driver.findElement(By.id("join-button"));
    Assertions.assertNotNull(joinButton);
    joinButton.click();
  }

  // == Step Defs - And ======================

  // == Step Defs - Then =====================
  @Then("game ended")
  public void teardown () {
    driver.quit();
  }
}
