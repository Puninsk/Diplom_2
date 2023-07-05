
import org.junit.Before;
import project.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestChangeUser {

    private User user;
    private String accessToken;

    @Before
    public void createRandomUser() {
        user = User.randomUser();
        user.createUser(user);
        accessToken = user.loginUserReturnAccessToken(user);
    }

    @Test
    @DisplayName("Update user data")
    public void updateUserDateSuccessful() {
        User updatedUser = new User(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6));
        Response responseNew = user.updateUser(updatedUser, accessToken);
        int actualStatusCode = responseNew.getStatusCode();
        boolean isResponseSuccessful = responseNew.jsonPath().getBoolean("success");
        String email = responseNew.jsonPath().getString("user.email");
        String name = responseNew.jsonPath().getString("user.name");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
        assertEquals(updatedUser.getEmail().toLowerCase(), email);
        assertEquals(updatedUser.getName(), name);
    }

    @Test
    @DisplayName("Update email")
    public void userUpdateEmailFieldTest() {
        String newEmail = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        User updatedEmailUser = new User(newEmail, user.getPassword(), user.getName());
        Response responseNew = user.updateUser(updatedEmailUser, accessToken);
        int actualStatusCode = responseNew.getStatusCode();
        boolean isResponseSuccessful = responseNew.jsonPath().getBoolean("success");
        String email = responseNew.jsonPath().getString("user.email");
        String name = responseNew.jsonPath().getString("user.name");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
        assertEquals(updatedEmailUser.getEmail().toLowerCase(), email);
        assertEquals(updatedEmailUser.getName(), name);
    }

    @Test
    @DisplayName("Update name")
    public void userUpdateNameFieldTest() {
        String newName = RandomStringUtils.randomAlphanumeric(6);
        User updatedNameUser = new User(user.getEmail(), user.getPassword(), newName);
        Response responseNew = user.updateUser(updatedNameUser, accessToken);
        int actualStatusCode = responseNew.getStatusCode();
        boolean isResponseSuccessful = responseNew.jsonPath().getBoolean("success");
        String email = responseNew.jsonPath().getString("user.email");
        String name = responseNew.jsonPath().getString("user.name");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
        assertEquals(updatedNameUser.getEmail().toLowerCase(), email);
        assertEquals(updatedNameUser.getName(), name);
    }

    @Test
    @DisplayName("Update user data without authorization")
    public void updateUserDataWithoutAuthorization() {
        User updatedUser = new User(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6));
        Response responseNew = user.updateUser(updatedUser, "");
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", responseMessage);
    }

    @Test
    @DisplayName("Update email without authorization")
    public void userUpdateEmailFieldWithoutAuthorizationTest() {
        String newEmail = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        User updatedEmailUser = new User(newEmail, user.getPassword(), user.getName());
        Response responseNew = user.updateUser(updatedEmailUser, "");
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", responseMessage);
    }

    @Test
    @DisplayName("Update password without authorization")
    public void userUpdatePasswordFieldWithoutAuthorizationTest() {
        String newPassword = RandomStringUtils.randomAlphanumeric(6);
        User updatedPasswordUser = new User(user.getEmail(), newPassword, user.getName());
        Response responseNew = user.updateUser(updatedPasswordUser, "");
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", responseMessage);
    }

    @Test
    @DisplayName("Update name without authorization")
    public void userUpdateNameFieldWithoutAuthorizationTest() {
        String newName = RandomStringUtils.randomAlphanumeric(6);
        User updatedNameUser = new User(user.getEmail(), user.getPassword(), newName);
        Response responseNew = user.updateUser(updatedNameUser, "");
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", responseMessage);
    }

    @Test
    @DisplayName("Update email to email already exist")
    public void userUpdateEmailFieldWithTakenEmailTest() {
        User newUser = new User(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6));
        user.createUser(newUser);
        Response responseLoginNewUser = user.loginUser(newUser);
        String emailNewUser = responseLoginNewUser.body().jsonPath().getString("user.email");
        User updateUserWithExistEmail = new User(emailNewUser, user.getPassword(), user.getName());
        Response responseNew = user.updateUser(updateUserWithExistEmail, accessToken);
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(403, actualStatusCode);
        assertEquals("User with such email already exists", responseMessage);
    }

    @After
    public void deleteUser() {
        if (!(accessToken == null)) {
            user.deleteUser(accessToken);
        }
    }
}



