package acceptance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import io.cucumber.java.Before;
import server.ServerApplication;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = ServerApplication.class, loader = SpringBootContextLoader.class)
public class CucumberSpringContextConfiguration {
  private static final Logger LOG = LoggerFactory.getLogger(CucumberSpringContextConfiguration.class);

  @Before
  public void setup() {
    LOG.info("Spring Context Initialized For Executing Cucumber Tests");
  }
}
