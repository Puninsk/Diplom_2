import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import project.UserRequests;
import project.UserData;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class TestCreateUser {
    private UserData userData;
    private UserRequests userRequests;
    private String accessToken;

    @Before
    public void generateDataUser() {
        userData = UserData.randomUser();
        userRequests = new UserRequests();
    }
    @Test
    @DisplayName("Create user")
    @Description("Create user with correct data")
    public void createUserWithCorrectData() {
        Response response = userRequests.createUser(userData);
        int actualStatusCode = response.getStatusCode();
        boolean isResponseSuccessful = response.jsonPath().getBoolean("success");
        String email = response.jsonPath().getString("user.email");
        String name = response.jsonPath().getString("user.name");
        accessToken = response.body().jsonPath().getString("accessToken");
        String refreshToken = response.body().jsonPath().getString("refreshToken");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
        assertEquals(userData.getEmail().toLowerCase(), email);
        assertEquals(userData.getName(), name);
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());
    }

    @Test
    @DisplayName("Create user with exist data")
    public void createUserWithExistData() {
        Response firstResponse = userRequests.createUser(userData);
        Response secondResponse = userRequests.createUser(userData);
        accessToken = firstResponse.body().jsonPath().getString("accessToken");
        int actualStatusCode = secondResponse.getStatusCode();
        boolean isResponseSuccessful = secondResponse.jsonPath().getBoolean("success");
        String responseMessage = secondResponse.jsonPath().getString("message");
        assertEquals(403, actualStatusCode);
        assertFalse(isResponseSuccessful);
        assertEquals("User already exists", responseMessage);
        assertThat(accessToken, notNullValue());
    }

    @Test
    @DisplayName("Create user without email")
    public void createUserWithoutEmail() {
        UserData userData = new UserData(null, RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6));
        Response response = userRequests.createUser(userData);
        int actualStatusCode = response.statusCode();
        boolean isResponseSuccessful = response.jsonPath().getBoolean("success");
        String responseMessage = response.jsonPath().getString("message");
        accessToken = response.body().jsonPath().getString("accessToken");
        assertEquals(403, actualStatusCode);
        assertFalse(isResponseSuccessful);
        assertEquals("Email, password and name are required fields", responseMessage);
    }

    @Test
    @DisplayName("Create user without password")
    public void createUserWithoutPassword() {
        UserData userData = new UserData(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", null, RandomStringUtils.randomAlphanumeric(6));
        Response response = userRequests.createUser(userData);
        int actualStatusCode = response.statusCode();
        boolean isResponseSuccessful = response.jsonPath().getBoolean("success");
        String responseMessage = response.jsonPath().getString("message");
        accessToken = response.body().jsonPath().getString("accessToken");
        assertEquals(403, actualStatusCode);
        assertFalse(isResponseSuccessful);
        assertEquals("Email, password and name are required fields", responseMessage);
    }

    @Test
    @DisplayName("Create user without name")
    public void createUserWithoutName() {
        UserData userData = new UserData(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(6), null);
        Response response = userRequests.createUser(userData);
        int actualStatusCode = response.statusCode();
        boolean isResponseSuccessful = response.jsonPath().getBoolean("success");
        String responseMessage = response.jsonPath().getString("message");
        accessToken = response.body().jsonPath().getString("accessToken");
        assertEquals(403, actualStatusCode);
        assertFalse(isResponseSuccessful);
        assertEquals("Email, password and name are required fields", responseMessage);
    }
    @Test
    @DisplayName("Create user with empty email")
    public void createUserWithEmptyEmail() {
        UserData userData = new UserData("", RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6));
        Response response = userRequests.createUser(userData);
        int actualStatusCode = response.statusCode();
        boolean isResponseSuccessful = response.jsonPath().getBoolean("success");
        String responseMessage = response.jsonPath().getString("message");
        accessToken = response.body().jsonPath().getString("accessToken");
        assertEquals(403, actualStatusCode);
        assertFalse(isResponseSuccessful);
        assertEquals("Email, password and name are required fields", responseMessage);

    }
    @Test
    @DisplayName("Create user with empty password")
    public void createUserWithEmptyPassword() {
        UserData userData = new UserData(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", "", RandomStringUtils.randomAlphanumeric(6));
        Response response = userRequests.createUser(userData);
        int actualStatusCode = response.statusCode();
        boolean isResponseSuccessful = response.jsonPath().getBoolean("success");
        String responseMessage = response.jsonPath().getString("message");
        accessToken = response.body().jsonPath().getString("accessToken");
        assertEquals(403, actualStatusCode);
        assertFalse(isResponseSuccessful);
        assertEquals("Email, password and name are required fields", responseMessage);
    }
    @Test
    @DisplayName("Create user with empty name")
    public void createUserWithEmptyName() {
        UserData userData = new UserData(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(6), "");
        Response response = userRequests.createUser(userData);
        int actualStatusCode = response.statusCode();
        boolean isResponseSuccessful = response.jsonPath().getBoolean("success");
        String responseMessage = response.jsonPath().getString("message");
        accessToken = response.body().jsonPath().getString("accessToken");
        assertEquals(403, actualStatusCode);
        assertFalse(isResponseSuccessful);
        assertEquals("Email, password and name are required fields", responseMessage);
    }

    @After
    public void deleteUser() {
        accessToken = userRequests.loginUserReturnAccessToken(userData);
        if (!(accessToken == null)) {
            userRequests.deleteUser(accessToken);
        }
    }

}