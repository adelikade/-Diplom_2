package pageobject;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.Order;

import static configuration.Configuration.getBaseUri;
import static io.restassured.RestAssured.given;

public class OrderPageObject {
    @Step("Создание заказа после авторизации")
    public Response createOrderWithAuth(Order orderCreate, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .body(orderCreate)
                .post("orders");
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order orderCreate) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .body(orderCreate)
                .post("orders");
    }

    @Step("Получение списка заказов после авторизации")
    public Response getOrdersListWithAuth(String accessToken) {
        return given()
                .baseUri(getBaseUri())
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .get("orders");
    }

    @Step("Получение списка заказов без авторизации")
    public Response getOrdersListWithoutAuth() {
        return given()
                .baseUri(getBaseUri())
                .header("Content-type", "application/json")
                .get("orders");
    }
}