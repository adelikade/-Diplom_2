import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojo.User;

import static datatest.UserDataTest.getUserAllField;
import static datatest.UserDataTest.getUserWithoutOneField;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserTest {
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
    @DisplayName("Создание уникального пользователя")
    @Description("Можно создать уникального пользователя: возвращается код 200 и \"success\":true")
    public void createUser() {
        userClient = new UserClient();
        user = getUserAllField();
        accessToken = userClient.create(user)
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
    @DisplayName("Создание уже существующего пользователя")
    @Description("Если пользователь уже существует, возвращается код 403 и сообщение \"User already exists\"")
    public void createDoubleUser() {
        userClient = new UserClient();
        user = getUserAllField();
        accessToken = userClient.create(user)
                .then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .extract()
                .path("accessToken");

        Response response = userClient.create(user);
        response.then()
                .statusCode(403)
                .and()
                .assertThat().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без одного из обязательных полей")
    @Description("Если нет одного из обязательных полей, вернется код 403 и сообщение \"Email, password and name are required fields\"")
    public void createUserWithoutOneField() {
        userClient = new UserClient();
        user = getUserWithoutOneField();
        Response response = userClient.create(user);
        response.then()
                .statusCode(403)
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }
}