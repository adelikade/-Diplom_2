package datatest;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import pojo.Login;
import pojo.User;

public class LoginDataTest {
    @Step("Логин зарегистрированного клиента")
    public static Login from(User user) {
        Login login = new Login();
        login.setEmail(user.getEmail());
        login.setPassword(user.getPassword());
        return login;
    }

    @Step("Логин с неправильным емейлом или паролем")
    public static Login invalidLoginPassword() {
        Login login = new Login();
        login.setEmail(RandomStringUtils.randomAlphanumeric(7));
        login.setPassword(RandomStringUtils.randomAlphanumeric(7));
        return login;
    }
}