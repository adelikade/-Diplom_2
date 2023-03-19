import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pageobject.OrderPageObject;
import pageobject.UserPageObject;
import pojo.User;

import static datatest.UserDataTest.getUserAllField;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ListOrderTest {
    UserPageObject userPageObject;
    User user;
    String accessToken;

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userPageObject.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Получение списка заказов после авторизации")
    @Description("При получении списка заказов после авторизации возвращается корректный список заказов и код 200")
    public void getListOrderWithAuth() {
        userPageObject = new UserPageObject();
        user = getUserAllField();
        accessToken = userPageObject.create(user)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");

        OrderPageObject orderPageObject = new OrderPageObject();
        Response response = orderPageObject.getOrdersListWithAuth(accessToken);
        response.then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("При получении списка заказов без авторизации возвращается код 401 и сообщение You should be authorized")
    public void getListOrderWithoutAuth() {
        OrderPageObject orderPageObject = new OrderPageObject();
        Response response = orderPageObject.getOrdersListWithoutAuth();
        response.then().statusCode(401)
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
    }
}