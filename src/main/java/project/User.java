package project;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import static io.restassured.RestAssured.given;

public class User {

    private static final String USER_CREATE = "/api/auth/register";
    private static final String USER_LOGIN = "/api/auth/login";
    private static final String USER_INFO_CHANGE = "/api/auth/user";

    private String email;
    private String password;
    private String name;
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public User() {
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static User randomUser() {
        String email = RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphanumeric(6);
        String name = RandomStringUtils.randomAlphanumeric(6);
        return new User(email, password, name);
    }
    @Step("Create user")
    public Response createUser(User user) {
        Response response = given()
                .spec(Configuration.configuration())
                .body(user)
                .when()
                .post(USER_CREATE);
        return response;
    }

    @Step("User login to extract Token")
    public String loginUserReturnAccessToken(User data) {
        return given()
                .spec(Configuration.configuration())
                .body(data)
                .when()
                .post(USER_LOGIN).then().extract().path("accessToken");
    }
    @Step("Change user info")
    public Response updateUser(User user, String accessToken) {
        Response response = given()
                .spec(Configuration.configuration())
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_INFO_CHANGE);
        return response;
    }
    @Step("Login user")
    public Response loginUser(User data) {
        Response response = given()
                .spec(Configuration.configuration())
                .body(data)
                .when()
                .post(USER_LOGIN);
        return response;
    }

    @Step("User delete")
    public Response deleteUser(String accessToken) {
        Response response = given()
                .spec(Configuration.configuration())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_INFO_CHANGE)
                .then()
                .assertThat()
                .statusCode(202)
                .extract()
                .path("ok");
        return response;
    }
}
