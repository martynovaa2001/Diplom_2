package praktikum.api.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.api.UserRequest;

import static io.restassured.RestAssured.given;
import static praktikum.api.base.DataTests.*;

public class UserSteps {

    public String accessToken;

    @Step("Регистрация пользователя {user.email}")
    public Response registerUser(UserRequest user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(AUTH_REGISTER_PATH);
    }

    @Step("Авторизация пользователя {user.email}")
    public Response loginUser(UserRequest user) {
        Response response = given()
                .contentType("application/json")
                .body(user)
                .when()
                .post(AUTH_LOGIN_PATH);
        try {
            accessToken = response
                    .jsonPath()
                    .getString("accessToken");
        }
        catch(NullPointerException e){
        }
        return response;
    }

    @Step("Удаление пользователя по токену {token}")
    public void deleteUser(String token) {
        given()
                .header("Authorization", token)
                .when()
                .delete(AUTH_USER_PATH);
    }
}
