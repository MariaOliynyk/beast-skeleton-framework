package skeleton_tests.stepDef;

import com.work.qa.common.services.api.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Value;

public class AbstractStepsDef {

    @Autowired
    @Lazy
    public RestClient restService;

    @Value("${reqres.path.baseUrl}")
    String BaseEndpoint;
}
