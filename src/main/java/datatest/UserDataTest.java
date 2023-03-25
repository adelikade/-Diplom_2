package datatest;

import io.qameta.allure.Step;
import pojo.User;

public class UserDataTest {
    @Step("Создание пользователя со всеми заполненными полями")
    public static User getUserAllField() {
        User user = new User();
        user.setEmail("olstogova@gmail.com");
        user.setPassword("qwerty765");
        user.setName("Ольга");
        return user;
    }

    @Step("Создание пользователя, когда одно из полей не заполнено")
    public static User getUserWithoutOneField() {
        User user = new User();
        user.setEmail("olstogova@gmail.com");
        user.setName("Ольга");
        return user;
    }
}