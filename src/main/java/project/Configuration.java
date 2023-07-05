package project;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class Configuration {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    public static RequestSpecification configuration() {
        return  given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json");
    }
}

