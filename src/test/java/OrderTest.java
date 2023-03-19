import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pageobject.OrderPageObject;
import pageobject.UserPageObject;
import pojo.Order;
import pojo.User;

import static datatest.OrderDataTest.*;
import static datatest.UserDataTest.getUserAllField;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTest {
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
    @DisplayName("Создание заказа после авторизации с ингредиентами")
    @Description("При создании заказа после авторизации с ингредиентами возвращается код 200 и \"success\":true")
    public void createOrderWithAuthAndIngredients() {
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

        Order order = getOrderWithValidIngredients();
        OrderPageObject orderPageObject = new OrderPageObject();
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
        userPageObject = new UserPageObject();
        user = getUserAllField();
        Response responseCreate = userPageObject.create(user);
        accessToken = responseCreate
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");

        Order orderCreateRequest = getOrderWithInvalidIngredients();
        OrderPageObject orderPageObject = new OrderPageObject();
        Response response = orderPageObject.createOrderWithAuth(orderCreateRequest, accessToken);
        response.then()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    @Description("При создании заказа без авторизации с ингредиентами возвращается код 200 и \"success\":true")
    public void createOrderWithoutAuth() {
        Order orderCreateRequest = getOrderWithValidIngredients();
        OrderPageObject orderPageObject = new OrderPageObject();
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
        userPageObject = new UserPageObject();
        user = getUserAllField();
        Response responseCreate = userPageObject.create(user);
        accessToken = responseCreate
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");

        Order orderCreateRequest = getOrderWithoutIngredients();
        OrderPageObject orderPageObject = new OrderPageObject();
        Response response = orderPageObject.createOrderWithAuth(orderCreateRequest, accessToken);
        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }
}