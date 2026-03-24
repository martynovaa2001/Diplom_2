package praktikum.api.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.base.BaseApiTest;
import praktikum.api.user.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static praktikum.api.base.DataTests.*;

public class OrderCreateTest extends BaseApiTest {
    private OrderSteps orderSteps;

    @Before
    public void initSteps() {
        orderSteps = new OrderSteps();
        userSteps = new UserSteps();
        userSteps.registerUser(testUser);
        userSteps.loginUser(testUser);
        accessToken = userSteps.accessToken;
    }

    @Test
    @DisplayName("Успешное создание заказа с авторизацией и одним ингредиентом")
    @Description("Создаем заказ с авторизацией и одним ингредиентом, ответ 200")
    public void testCreateOrderWithSingleIngredient() {
        Response orderResponse = orderSteps.createOrder(accessToken, SINGLE_INGREDIENT);
        orderResponse.then().log().all()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue())
                .body("order.ingredients", notNullValue());
    }

    @Test
    @DisplayName("Успешное создание заказа с авторизацией и несколькими ингредиентами")
    @Description("Создаем заказ с авторизацией и несколькими ингредиентами, ответ 200")
    public void testCreateOrderWithAuthAndMultipleIngredients() {
        Response orderResponse = orderSteps.createOrder(accessToken, VALID_INGREDIENTS);
        orderResponse.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue())
                .body("order.owner", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Создаем заказ без авторизации с валидным ингредиентом, ответ 401")
    public void testCreateOrderWithoutAuth() {
        List<String> ingredients = new ArrayList<>(VALID_INGREDIENTS);
        Response response = orderSteps.createOrderWithoutAuth(ingredients);

        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false));
    }

    @Test
    @DisplayName("Ошибка создания заказа с неверным хешем ингредиента")
    @Description("Создаем заказ с авторизацией и неверным хешем ингредиента, ответ 500")
    public void testCreateOrderWithInvalidIngredient() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(INVALID_INGREDIENT_ID);

        orderSteps.createOrder(accessToken, ingredients)
                .then().log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Ошибка создания заказа без ингредиентов")
    @Description("Создаем заказ с авторизацией и без ингредиентов, ответ 400")
    public void testCreateOrderWithoutIngredients() {
        List<String> emptyIngredients = new ArrayList<>();

        orderSteps.createOrder(accessToken, emptyIngredients)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo(NO_INGREDIENTS_MESSAGE));
    }
}



