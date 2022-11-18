package acceptance;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@SelectClasses({
  CucumberTest.class
})
@Suite
public class AcceptanceTestSuite {}
