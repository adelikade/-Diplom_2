import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pageobject.UserPageObject;
import pojo.UpdateUser;
import pojo.User;

import static configuration.Configuration.getBaseUri;
import static datatest.UpdateUserDataTest.getUserUpdate;
import static datatest.UserDataTest.getUserAllField;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateUserTest {
    UserPageObject userPageObject;
    User user;
    String accessToken;
    UpdateUser updateUser;

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userPageObject.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Изменение данных пользователя после авторизации")
    @Description("При изменении данных пользователя после авторизации возвращается код 200 и \"success\":true")
    public void updateUserWithAuth() {
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

        updateUser = getUserUpdate();
        Response response = userPageObject.updateWithAuth(updateUser, accessToken);
        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("При изменении данных пользователя без авторизации возвращается код 401 и сообщение You should be authorized")
    public void updateUserWithoutAuth() {
        updateUser = getUserUpdate();
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .baseUri(getBaseUri())
                        .body(updateUser)
                        .patch("auth/user");
        response.then()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
    }
}