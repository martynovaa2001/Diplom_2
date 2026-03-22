package praktikum.api.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import praktikum.api.OrderRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static praktikum.api.base.DataTests.ORDERS_PATH;

public class OrderSteps {

    @Step("Создание заказа с токеном авторизации {accessToken}")
    public Response createOrder(String accessToken, List<String> ingredients) {
        RequestSpecification spec = given()
                .log().all()
                .contentType("application/json")
                .body(new OrderRequest(ingredients));

        if (accessToken != null) {
            spec = spec.header(AUTHORIZATION, accessToken);
        }

        return spec.when()
                .post(ORDERS_PATH)
                .then()
                .extract()
                .response();
    }
}

