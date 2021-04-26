package skeleton_tests.stepDef;

import cucumber.api.java.en.And;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonStepsDef extends AbstractStepsDef {
    @And("User wait for {int} seconds")
    public void iWaitSeconds(int sec) throws InterruptedException {
        sleep(sec * 1000);
    }

    @And("The status code is {int}")
    public void theStatusCodeIs(int code) {
        int actualStatusCode = restService.getResponse().getStatusCode();
        assertThat(actualStatusCode).isEqualTo(code);
    }

}
