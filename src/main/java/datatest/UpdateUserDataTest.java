package datatest;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import pojo.UpdateUser;

public class UpdateUserDataTest {
    @Step("Изменение данных пользователя")
    public static UpdateUser getUserUpdate() {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setEmail(RandomStringUtils.randomAlphabetic(6) + "@gmail.com");
        updateUser.setPassword("password");
        updateUser.setName(RandomStringUtils.randomAlphabetic(10));
        return updateUser;
    }
}