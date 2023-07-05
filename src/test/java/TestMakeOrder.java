import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import project.Orders;
import project.User;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class TestMakeOrder {

    private static User user;
    private static Orders orders;
    private String accessToken;
    @Before
    public void generateDataForNewUser() {
        user = User.randomUser();
        orders = new Orders();
        user.createUser(user);
        accessToken = user.loginUserReturnAccessToken(user);
    }
    @Test
    @DisplayName("Create order")
    public void createOrder() {
        Orders orders = new Orders();
        Response orderCreate = orders.createOrder(orders.getIngredientsList(), accessToken);
        int actualStatusCode = orderCreate.getStatusCode();
        boolean isResponseSuccessful = orderCreate.jsonPath().getBoolean("success");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
    }

    @Test
    @DisplayName("Create order without authorization")
    public void createOrderWithoutAuthorization() {
        Orders orders = new Orders();
        Response orderCreate = orders.createOrder(orders.getIngredientsList(), "");
        int actualStatusCode = orderCreate.getStatusCode();
        boolean isResponseSuccessful = orderCreate.jsonPath().getBoolean("success");
        assertEquals(200, actualStatusCode);
        assertTrue(isResponseSuccessful);
    }

    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredient() {
        Orders order = new Orders(null);
        Response orderCreate = orders.createOrder(order, accessToken);
        int actualStatusCode = orderCreate.getStatusCode();
        String responseMessage = orderCreate.jsonPath().getString("message");
        assertEquals(400, actualStatusCode);
        assertThat(responseMessage, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with wrong ingredients")
    public void createOrderWithWrongIngredients() {
        Orders orders = new Orders();
        Response orderCreate = orders.createOrder(orders.getInvalidIngredientsList(), accessToken);
        int actualStatusCode = orderCreate.getStatusCode();
        assertEquals(500, actualStatusCode);
    }

    @After
    public void deleteUser() {
        if (!(accessToken == null)) {
            user.deleteUser(accessToken);
        }
    }
}
