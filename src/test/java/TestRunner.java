
import acceptance.AcceptanceTestSuite;
import unit.UnitTestSuite;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;
import java.io.PrintWriter;
import java.util.List;

public class TestRunner {
  public static void main(String[] args) {
    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                                                                      .selectors(selectClass(AcceptanceTestSuite.class))
                                                                      .selectors(selectClass(UnitTestSuite.class))
                                                                      .build();

    Launcher launcher = LauncherFactory.create();
    SummaryGeneratingListener listener = new SummaryGeneratingListener();

    launcher.registerTestExecutionListeners(listener);
    launcher.execute(request);

    
    PrintWriter pWriter = new PrintWriter(System.out);
    TestExecutionSummary summary = listener.getSummary();

    summary.printTo(pWriter);

    List<Failure> failures = summary.getFailures();
    System.out.println("getTestsSucceededCount() - " + summary.getTestsSucceededCount());
    failures.forEach(failure -> System.out.println("failure - " + failure.getException()));
  }
}
