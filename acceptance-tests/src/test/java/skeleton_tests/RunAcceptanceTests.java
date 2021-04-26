package skeleton_tests;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        plugin = {"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm","json:target/cucumber-report.json","junit:target/cucumber.xml","rerun:target/rerun.txt"},
        tags = {"not @Ignore", "not @InDev"}
)
public class RunAcceptanceTests extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @AfterSuite
    public static void teardown() {
        System.out.println("\n\n\n\n\n\n ***************************************Test Suite Completed!!!!******************************************");
    }
}
