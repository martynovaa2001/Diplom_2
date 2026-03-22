package praktikum.api.order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.base.BaseApiTest;
import praktikum.api.user.UserSteps;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
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
    public void testCreateOrderWithoutAuth() {
        List<String> ingredients = new ArrayList<>(VALID_INGREDIENTS);

        given()
                .contentType("application/json")
                .body(ingredients)
                .log().body()  // Логируем отправляемый запрос
                .when()
                .post(ORDERS_PATH)
                .then()
                .log().body()  // Логируем ответ
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false));
    }

    @Test
    @DisplayName("Ошибка создания заказа с неверным хешем ингредиентом")
    public void testCreateOrderWithInvalidIngredient() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(INVALID_INGREDIENT_ID);

        orderSteps.createOrder(accessToken, ingredients)
                .then().log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Ошибка создания заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        List<String> emptyIngredients = new ArrayList<>();

        orderSteps.createOrder(accessToken, emptyIngredients)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo(NO_INGREDIENTS_MESSAGE));
    }
}



