package acceptance;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;

@CucumberContextConfiguration
public class StepDefinitions {
  WebDriver driver;

  @Before
  public void setup () {
    driver = WebDriverManager.chromedriver().create();
  }

  // == Step Defs - Given ====================
  @Given("game started")
  public void startGame () {
    driver.get("http://localhost:8080");
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
  }

  // == Step Defs - When =====================

  // == Step Defs - And ======================

  // == Step Defs - Then =====================
  @Then("the join button exists")
  public void join () {
    WebElement nameInput = driver.findElement(By.id("player-name-input-box"));
    nameInput.sendKeys("player1");
    WebElement joinButton = driver.findElement(By.id("join-button"));
    Assertions.assertNotNull(joinButton);
  }

  @Then("game ended")
  public void teardown () {
    driver.quit();
  }
}
