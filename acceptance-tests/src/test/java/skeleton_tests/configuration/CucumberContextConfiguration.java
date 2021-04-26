package skeleton_tests.configuration;

import cucumber.api.java.Before;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {AppConfig.class})
@DirtiesContext
public class CucumberContextConfiguration {

    @Before
    public void setup_cucumber_spring_context() {

    }
}
