package project;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserRequests {


    private static final String USER_CREATE = "/api/auth/register";
    private static final String USER_LOGIN = "/api/auth/login";
    private static final String USER_INFO_CHANGE = "/api/auth/user";

    @Step("Create user")
    public Response createUser(UserData userData) {
        Response response = given()
                .spec(Configuration.configuration())
                .body(userData)
                .when()
                .post(USER_CREATE);
        return response;
    }

    @Step("User login to extract Token")
    public String loginUserReturnAccessToken(UserData data) {
        return given()
                .spec(Configuration.configuration())
                .body(data)
                .when()
                .post(USER_LOGIN).then().extract().path("accessToken");
    }
    @Step("Change user info")
    public Response updateUser(UserData userData, String accessToken) {
        Response response = given()
                .spec(Configuration.configuration())
                .header("authorization", accessToken)
                .body(userData)
                .when()
                .patch(USER_INFO_CHANGE);
        return response;
    }
    @Step("Login user")
    public Response loginUser(UserData data) {
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
