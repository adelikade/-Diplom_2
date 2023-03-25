import client.UserClient;
import datatest.LoginDataTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojo.Login;
import pojo.User;

import static datatest.UserDataTest.getUserAllField;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTest {
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
    @DisplayName("Логин под существующим пользователем")
    @Description("При авторизации под существующим пользователем возвращается код 200 и \"success\":true")
    public void loginUnderExistingUser() {
        userClient = new UserClient();
        user = getUserAllField();
        userClient.create(user);
        Login loginRequest = LoginDataTest.from(user);
        accessToken = userClient.login(loginRequest)
                .then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    @Description("При авторизации с неправильным логином и паролем возвращается код 401 и сообщение \"email or password are incorrect\"")
    public void loginWithInvalidUsernameAndPassword() {
        Login loginRequest = LoginDataTest.invalidLoginPassword();
        userClient = new UserClient();
        Response response = userClient.login(loginRequest);
        response.then()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo("email or password are incorrect"));
    }
}