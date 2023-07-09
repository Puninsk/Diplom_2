import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.After;

import org.junit.Test;
import project.UserData;
import project.UserRequests;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestLoginUser {

    private UserData userData;

    private UserRequests userRequests;
    private String accessToken;
    @Before
    public void generateDataForNewUser() {
        userData = UserData.randomUser();
        userRequests = new UserRequests();
        userRequests.createUser(userData);
    }
    @Test
    @DisplayName("Login Successful")
    public void loginSuccessful() {
        Response responseNew = userRequests.loginUser(userData);
        int actualStatusCode = responseNew.getStatusCode();
        boolean isResponseSuccessful = responseNew.jsonPath().getBoolean("success");
        String email = responseNew.jsonPath().getString("user.email");
        String name = responseNew.jsonPath().getString("user.name");
        accessToken = responseNew.body().jsonPath().getString("accessToken");
        String refreshToken = responseNew.body().jsonPath().getString("refreshToken");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
        assertEquals(userData.getEmail().toLowerCase(), email);
        assertEquals(userData.getName(), name);
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());
    }
    @Test
    @DisplayName("Login with wrong login and password")
    public void loginWithWrongUserData() {
        UserData invalidCreds = new UserData(RandomStringUtils.randomAlphabetic(5) + "@yandex.ru", RandomStringUtils.randomNumeric(6), userData.getName());
        Response responseNew = userRequests.loginUser(invalidCreds);
        int statusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.body().jsonPath().getString("message");
        accessToken = responseNew.body().jsonPath().getString("accessToken");
        assertThat(statusCode, equalTo(401));
        assertThat(responseMessage, equalTo("email or password are incorrect"));
    }
    @After
    public void deleteUser() {
        UserData credentials = new UserData(userData.getEmail(), userData.getPassword(), null);
        accessToken = userRequests.loginUserReturnAccessToken(credentials);
        if (!(accessToken == null)) {
            userRequests.deleteUser(accessToken);
        }
    }


}
