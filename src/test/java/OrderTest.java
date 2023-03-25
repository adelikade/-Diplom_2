import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojo.Order;
import pojo.User;

import static datatest.OrderDataTest.*;
import static datatest.UserDataTest.getUserAllField;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTest {
    public static UserClient userClient;
    public static User user;
    public static String accessToken;

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа после авторизации с ингредиентами")
    @Description("При создании заказа после авторизации с ингредиентами возвращается код 200 и \"success\":true")
    public void createOrderWithAuthAndIngredients() {
        userClient = new UserClient();
        user = getUserAllField();
        accessToken = userClient.create(user)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");

        Order order = getOrderWithValidIngredients();
        OrderClient orderPageObject = new OrderClient();
        Response response = orderPageObject.createOrderWithAuth(order, accessToken);
        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа после авторизации с некорректным хешем ингредиентов")
    @Description("При создании заказа после авторизации с некорректным хешем ингредиентов возвращается код 500")
    public void createOrderWithIncorrectIngredients() {
        userClient = new UserClient();
        user = getUserAllField();
        Response responseCreate = userClient.create(user);
        accessToken = responseCreate
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");

        Order orderCreateRequest = getOrderWithInvalidIngredients();
        OrderClient orderPageObject = new OrderClient();
        Response response = orderPageObject.createOrderWithAuth(orderCreateRequest, accessToken);
        response.then()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    @Description("При создании заказа без авторизации с ингредиентами возвращается код 200 и \"success\":true")
    public void createOrderWithoutAuth() {
        Order orderCreateRequest = getOrderWithValidIngredients();
        OrderClient orderPageObject = new OrderClient();
        Response response = orderPageObject.createOrderWithoutAuth(orderCreateRequest);
        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    @Description("При создании заказа без авторизации и без ингредиентов возвращается код 400 and сообщение Ingredient ids must be provided")
    public void createOrderWithoutIngredients() {
        userClient = new UserClient();
        user = getUserAllField();
        Response responseCreate = userClient.create(user);
        accessToken = responseCreate
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");

        Order orderCreateRequest = getOrderWithoutIngredients();
        OrderClient orderPageObject = new OrderClient();
        Response response = orderPageObject.createOrderWithAuth(orderCreateRequest, accessToken);
        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }
}