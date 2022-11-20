package acceptance;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.setHeadless(true);
    options.addArguments("window-size=1920,1080");
    driver = new ChromeDriver(options);
  }
  // == Step Defs - Given ====================
  @Given("game started")
  public void startGame () {
    driver.get(HOST_NAME + ":" + port);
  }

  // == Step Defs - When =====================

  // == Step Defs - And ======================

  // == Step Defs - Then =====================
  @Then("game ended")
  public void teardown () {
    driver.quit();
  }
}
