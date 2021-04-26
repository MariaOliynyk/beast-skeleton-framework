package skeleton_tests.configuration;

import org.springframework.context.annotation.*;


@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan("com.work.qa.common.services")
@PropertySources({
        @PropertySource("classpath:properties/env/${app_env}.properties"),
        @PropertySource("classpath:properties/api/${app_env}-endpoint.properties")
})

public class AppConfig {

}
