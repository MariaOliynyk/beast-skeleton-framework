package skeleton_tests.stepDef;

import com.work.qa.common.utils.helpers.JsonOperation;
import com.work.qa.common.utils.session.Key;
import com.work.qa.common.utils.session.Session;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import com.work.qa.common.services.api.models.UserResponse;
import com.work.qa.common.services.api.models.UsersResponse;
import ro.skyah.comparator.JSONCompare;

import java.util.Map;

import static com.work.qa.common.utils.helpers.FIlesUtility.readFileAsString;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class ApiStepDef extends AbstractStepsDef{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ApiStepDef.class);

    public static final String DATA_JSON_FOLDER = "src/test/resources/data/json/";

    @Value("${reqres.path.get.user.url}")
    String GET_USER_INFO_ENDPOINT;

    @Value("${reqres.path.get.allUsers.url}")
    String GET_ALL_USERS_ENDPOINT;



    @Given("API request for user with id equals {int} is sent to web service")
    public void apiRequestForUserWithIdEqualsIsSentToWebService(int id) {
        Response apiRequestForUserWithId =
                restService.sendGetRequest(BaseEndpoint + GET_USER_INFO_ENDPOINT + id);
        Session.getCurrentSession().put(Key.Keys.API_REQUEST,apiRequestForUserWithId);
    }

    @Then("response contain user with first name equal to {string}")
    public void responseContainUserWithFirstNameEqualToJanet(String name) {
        UserResponse userResponse = restService.getResponse().as(UserResponse.class);
        Session.getCurrentSession().put(Key.Keys.API_RESPONSE,userResponse);
        assertThat(userResponse.getData().getName()).isEqualTo(name);
    }

    @Given("get all users API request is sent to web service")
    public void getAllUsersAPIRequestIsSentToWebService() {
        restService.sendGetRequest(BaseEndpoint + GET_ALL_USERS_ENDPOINT);
    }

    @Then("response contain data for {int} users")
    public void responseContainDataForUsers(int numberOfUsers) {
        UsersResponse usersResponse = restService.getResponse().as(UsersResponse.class);
        assertThat(usersResponse.getData().size()).isEqualTo(numberOfUsers);
    }
    @Then("response is equal to expected result")
    public void responseIsEqualToExpectedResult(Map<String, String> exampleTable) {
        String expected = null;
        String actual = null;
        Response response = restService.getResponse();

        String expectedJsonFile = DATA_JSON_FOLDER + exampleTable.get("responseFileName");
        try {
            expected = readFileAsString(expectedJsonFile);
            actual = response.getBody().prettyPrint();
            Session.getCurrentSession().put(Key.Keys.EXPECTED_JSON_FILE, expected);
            Session.getCurrentSession().put(Key.Keys.ACTUAL_RESPONSE_JSON, actual);
            JSONCompare.assertEquals(expected, actual);

        } catch (AssertionError | Exception e) {
            log.error("\n\n\n\n***********Mismatch for response detected. See details below *********");
            log.error(
                    "Comparison failed for the following request : {}",
                    Session.getCurrentSession().get(Key.Keys.API_REQUEST));

            JsonOperation jsonOperation = new JsonOperation();
            jsonOperation.printJsonDiff(actual, expected);
            Assertions.assertThat(false).isTrue();

        }
    }
}
