import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import project.OrdersData;
import project.OrdersRequests;
import project.UserData;
import org.junit.Test;
import project.UserRequests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class TestMakeOrder {
    private OrdersRequests ordersRequests;
    private UserData userData;
    private UserRequests userRequests;
    private String accessToken;
    @Before
    public void generateDataForNewUser() {
        userData = UserData.randomUser();
        userRequests = new UserRequests();
        ordersRequests = new OrdersRequests();
        userRequests.createUser(this.userData);
        accessToken = userRequests.loginUserReturnAccessToken(userData);
    }
    @Test
    @DisplayName("Create order")
    public void createOrder() {
        OrdersData ordersData = new OrdersData();
        Response orderCreate = ordersRequests.createOrder(ordersData.getIngredientsList(), accessToken);
        int actualStatusCode = orderCreate.getStatusCode();
        boolean isResponseSuccessful = orderCreate.jsonPath().getBoolean("success");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
    }

    @Test
    @DisplayName("Create order without authorization")
    public void createOrderWithoutAuthorization() {
        OrdersData ordersData = new OrdersData();
        Response orderCreate = ordersRequests.createOrder(ordersData.getIngredientsList(), "");
        int actualStatusCode = orderCreate.getStatusCode();
        boolean isResponseSuccessful = orderCreate.jsonPath().getBoolean("success");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredient() {
        OrdersData ordersData = new OrdersData(null);
        Response orderCreate = ordersRequests.createOrder(ordersData, accessToken);
        int actualStatusCode = orderCreate.getStatusCode();
        String responseMessage = orderCreate.jsonPath().getString("message");
        assertEquals(400, actualStatusCode);
        assertThat(responseMessage, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with wrong ingredients")
    public void createOrderWithWrongIngredients() {
        OrdersData ordersData = new OrdersData();
        Response orderCreate = ordersRequests.createOrder(ordersData.getInvalidIngredientsList(), accessToken);
        int actualStatusCode = orderCreate.getStatusCode();
        assertEquals(500, actualStatusCode);
    }

    @After
    public void deleteUser() {
        if (!(accessToken == null)) {
            userRequests.deleteUser(accessToken);
        }
    }
}


