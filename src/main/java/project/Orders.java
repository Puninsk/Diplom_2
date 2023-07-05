package project;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.List;

public class Orders {
    private final String ORDERS_LIST = "/api/orders";
    private List<String> ingredients;
    public Orders() {
    }
    public Orders(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static Orders getIngredientsList() {
        List<String> list = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa79");
        return new Orders(list);
    }

    public static Orders getInvalidIngredientsList() {
        List<String> list = List.of("invalidIngredientHash", "233253532nkjo34434o");
        return new Orders(list);
    }

    @Step("Get the order list")
    public Response getTheOrderList(String accessToken) {
        Response response = given()
                .spec(Configuration.configuration())
                .header("authorization", accessToken)
                .when()
                .get(ORDERS_LIST);
        return response;
    }
    @Step("Create order")
    public Response createOrder(Orders order, String accessToken) {
        Response response = given()
                .spec(Configuration.configuration())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_LIST);
        return response;
    }
}