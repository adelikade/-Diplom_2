package pageobject;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.Login;
import pojo.UpdateUser;
import pojo.User;

import static configuration.Configuration.getBaseUri;
import static io.restassured.RestAssured.given;

public class UserPageObject {
    @Step("Регистрация пользователя")
    public Response create(User userRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .body(userRequest)
                .post("auth/register");
    }

    @Step("Авторизация пользователя")
    public Response login(Login loginRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .body(loginRequest)
                .post("auth/login");
    }

    @Step("Удаление пользователя")
    public void delete(String accessToken) {
        given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .delete("auth/user");
    }

    @Step("Изменение пользователя")
    public Response updateWithAuth(UpdateUser updateUserRequest, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .body(updateUserRequest)
                .patch("auth/user");
    }
}