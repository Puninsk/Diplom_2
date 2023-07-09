import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import project.OrdersData;
import project.OrdersRequests;
import project.UserData;
import org.junit.Test;
import project.UserRequests;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;

public class TestGetUserOrders {
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
    @DisplayName("Get the order list")
    public void getTheOrderList() {
        OrdersData ordersData = new OrdersData();
        ordersRequests.createOrder(ordersData.getIngredientsList(), accessToken);
        Response response = ordersRequests.getTheOrderList(accessToken);
        List<Object> orders2 = response.jsonPath().getList("orders");
        boolean isResponseSuccessful = response.jsonPath().getBoolean("success");
        assertFalse(orders2.isEmpty());
        assertTrue(isResponseSuccessful);

    }

    @Test
    @DisplayName("Get the order list without authorization")
    public void getTheOrderListWithoutAuthorization() {
        ordersRequests= new OrdersRequests();
        Response response = ordersRequests.getTheOrderList("");
        int actualStatusCode = response.getStatusCode();
        String responseMessage = response.jsonPath().getString("message");
        assertEquals(401, actualStatusCode);
        assertThat(responseMessage, equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() {
        if (!(accessToken == null)) {
            userRequests.deleteUser(accessToken);
        }
    }

}

