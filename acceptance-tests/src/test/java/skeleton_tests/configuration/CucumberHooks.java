package skeleton_tests.configuration;

import com.work.qa.common.TestStatusText;
import com.work.qa.common.utils.helpers.JsonOperation;
import com.work.qa.common.utils.session.Key;
import com.work.qa.common.utils.session.Session;
import cucumber.api.PickleStepTestStep;
import cucumber.api.Scenario;
import cucumber.api.TestCase;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.BeforeStep;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class CucumberHooks {
    int currentStepDefIndex;
    PickleStepTestStep currentStepDef;
    private static final TestStatusText testStatusText = new TestStatusText();
    private static int failedTests = 0;
    private static int passedTests = 0;
    private static int count = 0;

    @BeforeStep
    public void logCurrentStepInfo(Scenario scenario) throws Exception {

        Field f = scenario.getClass().getDeclaredField("testCase");
        f.setAccessible(true);
        TestCase r = (TestCase) f.get(scenario);

        List<PickleStepTestStep> stepDefs = r.getTestSteps()
                .stream()
                .filter(x -> x instanceof PickleStepTestStep)
                .map(x -> (PickleStepTestStep) x)
                .collect(Collectors.toList());

        currentStepDef = stepDefs
                .get(currentStepDefIndex);
        log.info("Current step is " + currentStepDef.getStepText());

    }

    @Before
    public void setScenarioInfoIntoLog(Scenario scenario) {
        log.info("Scenario " + scenario.getName());
        log.info(testStatusText.getTEXT_FOR_START());
        setIdIntoLog(scenario);
        setScenarioName(scenario);
    }

    @After
    public void logCountOfTest(Scenario scenario) {
        count++;
        if (scenario.isFailed()) {
            failedTests++;
        } else {
            passedTests++;
        }
        log.info("There are {} tests completed.", count);
        Session.getCurrentSession().put(Key.Keys.COUNT, String.valueOf(count));
        log.info("Status of last test is {}", scenario.getStatus());
        log.info("Passed tests: {}, Failed test: {}", passedTests, failedTests);
        Session.getCurrentSession().put(Key.Keys.PASSED_TESTS, String.valueOf(passedTests));
        Session.getCurrentSession().put(Key.Keys.FAILED_TESTS, String.valueOf(failedTests));
        if (scenario.isFailed()) {
            log.info(testStatusText.getTEXT_FOR_FAILED_TEST());
        } else {
            log.info(testStatusText.getTEXT_FOR_PASSED_TEST());
        }
    }

    public void setScenarioName(Scenario scenario) {
        MDC.put("scenarioName", scenario.getName());
    }

    public void setIdIntoLog(Scenario scenario) {
        final Pattern testCaseIdPattern = Pattern.compile("@TestCaseId\\(\"+?([^\"]+)\"+?\\)");
        for (String tag : scenario.getSourceTagNames()) {
            Matcher matcher = testCaseIdPattern.matcher(tag);
            if (matcher.matches()) {
                final String testCaseId = matcher.group(1);
                MDC.put("testCaseId", testCaseId);
            }
        }
    }

    @After("@api")
    public void attachResponse() {
        if (Session.getCurrentSession().get(Key.Keys.API_RESPONSE) != null) {
            String result = (Session.getCurrentSession().get(Key.Keys.API_RESPONSE)).toString(); // replace with what should be added as attachment
            Allure.addAttachment("Response info: ", result);
        }
    }

    @After("@AC2-DEMO8 or @api")
    public void attachJsonDiscrepancies() {
        if ((Session.getCurrentSession().get(Key.Keys.ACTUAL_RESPONSE_JSON))!=null) {
            JsonOperation jsonOperation = new JsonOperation();

            String expected = (String) Session.getCurrentSession().get(Key.Keys.EXPECTED_JSON_FILE);
            String actual = (String) Session.getCurrentSession().get(Key.Keys.ACTUAL_RESPONSE_JSON);
            Allure.addAttachment(
                    "Response Discrepancies",
                    "\n\nEntries only on right\n--------------------------\n"
                            + jsonOperation.printJsonDiff(actual, expected).entriesOnlyOnRight()
                            + "\n\nEntries only on left\n--------------------------\n"
                            + jsonOperation.printJsonDiff(actual, expected).entriesOnlyOnLeft()
                            + "\n\nEntries differing\n--------------------------\n"
                            + jsonOperation.printJsonDiff(actual, expected).entriesDiffering());
        }
    }
}
