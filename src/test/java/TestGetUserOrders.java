import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import project.Orders;
import project.User;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;

public class TestGetUserOrders {
    private Orders orders;
    private User user;
    private String accessToken;

    @Before
    public void generateDataForNewUser() {
        user = User.randomUser();
        orders = new Orders();
        user.createUser(this.user);
        accessToken = user.loginUserReturnAccessToken(user);
    }
    @Test
    @DisplayName("Get the order list")
    public void getTheOrderList() {
        Orders orders = new Orders();
        orders.createOrder(orders.getIngredientsList(), accessToken);
        Response response = orders.getTheOrderList(accessToken);
        List<Object> orders2 = response.jsonPath().getList("orders");
        boolean isResponseSuccessful = response.jsonPath().getBoolean("success");
        assertFalse(orders2.isEmpty());
        assertTrue(isResponseSuccessful);

    }

    @Test
    @DisplayName("Get the order list without authorization")
    public void getTheOrderListWithoutAuthorization() {
        Response response = orders.getTheOrderList("");
        int actualStatusCode = response.getStatusCode();
        String responseMessage = response.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertThat(responseMessage, equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() {
        if (!(accessToken == null)) {
            user.deleteUser(accessToken);
        }
    }

}