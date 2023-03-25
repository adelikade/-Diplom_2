package datatest;

import io.qameta.allure.Step;
import pojo.Order;

public class OrderDataTest {
    @Step("Создание заказа с корректными ингредиентами")
    public static Order getOrderWithValidIngredients() {
        Order order = new Order();
        order.setIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa71"});
        return order;
    }

    @Step("Создание заказа без ингедиентов")
    public static Order getOrderWithoutIngredients() {
        Order order = new Order();
        order.setIngredients(new String[]{});
        return order;
    }

    @Step("Создание заказа с некорректными ингредиентами")
    public static Order getOrderWithInvalidIngredients() {
        Order order = new Order();
        order.setIngredients(new String[]{"1", "2", "3"});
        return order;
    }
}