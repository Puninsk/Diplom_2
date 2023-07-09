
import org.junit.Before;
import project.UserRequests;
import project.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestChangeUser {

    private UserData userData;

    private UserRequests userRequests;
    private String accessToken;

    @Before
    public void createRandomUser() {
        userData = UserData.randomUser();
        userRequests = new UserRequests();
        userRequests.createUser(userData);
        accessToken = userRequests.loginUserReturnAccessToken(userData);
    }

    @Test
    @DisplayName("Update user data")
    public void updateUserDateSuccessful() {
        UserData updatedUser = new UserData(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6));
        Response responseNew = userRequests.updateUser(updatedUser, accessToken);
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
        UserData updatedEmailUser = new UserData(newEmail, userData.getPassword(), userData.getName());
        Response responseNew = userRequests.updateUser(updatedEmailUser, accessToken);
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
        UserData updatedNameUser = new UserData(userData.getEmail(), userData.getPassword(), newName);
        Response responseNew = userRequests.updateUser(updatedNameUser, accessToken);
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
        UserData updatedUser = new UserData(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6));
        Response responseNew = userRequests.updateUser(updatedUser, "");
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", responseMessage);
    }

    @Test
    @DisplayName("Update email without authorization")
    public void userUpdateEmailFieldWithoutAuthorizationTest() {
        String newEmail = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        UserData updatedEmailUser = new UserData(newEmail, userData.getPassword(), userData.getName());
        Response responseNew = userRequests.updateUser(updatedEmailUser, "");
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", responseMessage);
    }

    @Test
    @DisplayName("Update password without authorization")
    public void userUpdatePasswordFieldWithoutAuthorizationTest() {
        String newPassword = RandomStringUtils.randomAlphanumeric(6);
        UserData updatedPasswordUser = new UserData(userData.getEmail(), newPassword, userData.getName());
        Response responseNew = userRequests.updateUser(updatedPasswordUser, "");
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", responseMessage);
    }

    @Test
    @DisplayName("Update name without authorization")
    public void userUpdateNameFieldWithoutAuthorizationTest() {
        String newName = RandomStringUtils.randomAlphanumeric(6);
        UserData updatedNameUser = new UserData(userData.getEmail(), userData.getPassword(), newName);
        Response responseNew = userRequests.updateUser(updatedNameUser, "");
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertEquals("You should be authorised", responseMessage);
    }

    @Test
    @DisplayName("Update email to email already exist")
    public void userUpdateEmailFieldWithTakenEmailTest() {
        UserData newUser = new UserData(RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(6), RandomStringUtils.randomAlphanumeric(6));
        userRequests.createUser(newUser);
        Response responseLoginNewUser = userRequests.loginUser(newUser);
        String emailNewUser = responseLoginNewUser.body().jsonPath().getString("user.email");
        UserData updateUserWithExistEmail = new UserData(emailNewUser, userData.getPassword(), userData.getName());
        Response responseNew = userRequests.updateUser(updateUserWithExistEmail, accessToken);
        int actualStatusCode = responseNew.getStatusCode();
        String responseMessage = responseNew.jsonPath().getString("message");
        assertEquals(403, actualStatusCode);
        assertEquals("User with such email already exists", responseMessage);
    }

    @After
    public void deleteUser() {
        if (!(accessToken == null)) {
            userRequests.deleteUser(accessToken);
        }
    }

}



