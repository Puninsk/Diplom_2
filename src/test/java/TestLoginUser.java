
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.After;
import project.User;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestLoginUser {

    private User user;
    private String accessToken;
    @Before
    public void generateDataForNewUser() {
        user = User.randomUser();
        user.createUser(user);
    }
    @Test
    @DisplayName("Login Successful")
    public void loginSuccessful() {
        Response responseNew = user.loginUser(user);
        int actualStatusCode = responseNew.getStatusCode();
        boolean isResponseSuccessful = responseNew.jsonPath().getBoolean("success");
        String email = responseNew.jsonPath().getString("user.email");
        String name = responseNew.jsonPath().getString("user.name");
        accessToken = responseNew.body().jsonPath().getString("accessToken");
        String refreshToken = responseNew.body().jsonPath().getString("refreshToken");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
        assertEquals(user.getEmail().toLowerCase(), email);
        assertEquals(user.getName(), name);
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());
    }
    @Test
    @DisplayName("Login with wrong login and password")
    public void loginWithWrongUserData() {
        User invalidCreds = new User(RandomStringUtils.randomAlphabetic(5) + "@yandex.ru", RandomStringUtils.randomNumeric(6), user.getName());
        Response responseNew = user.loginUser(invalidCreds);
        int statusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.body().jsonPath().getString("message");
        accessToken = responseNew.body().jsonPath().getString("accessToken");
        assertThat(statusCode, equalTo(401));
        assertThat(responseMessage, equalTo("email or password are incorrect"));
    }
    @After
    public void deleteUser() {
        User credentials = new User(user.getEmail(), user.getPassword(), null);
        accessToken = user.loginUserReturnAccessToken(credentials);
        if (!(accessToken == null)) {
            user.deleteUser(accessToken);
        }
    }


}