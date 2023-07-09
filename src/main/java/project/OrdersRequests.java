package project;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrdersRequests {

    private final String ORDERS_LIST = "/api/orders";

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
    public Response createOrder(OrdersData ordersData, String accessToken) {
        Response response = given()
                .spec(Configuration.configuration())
                .header("Authorization", accessToken)
                .body(ordersData)
                .when()
                .post(ORDERS_LIST);
        return response;
    }
}
