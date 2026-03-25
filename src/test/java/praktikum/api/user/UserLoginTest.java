package praktikum.api.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import praktikum.api.UserRequest;
import praktikum.api.base.BaseApiTest;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.*;
import static praktikum.api.base.DataTests.INVALID_CREDENTIALS_MESSAGE;

public class UserLoginTest extends BaseApiTest {

    @Test
    @DisplayName("Успешный вход существующего пользователя")
    @Description("Успешный логин существующего пользователя, ответ 200")
    public void testUserLoginSuccess() {
        userSteps.registerUser(testUser)
                .then().statusCode(SC_OK);

        Response loginResponse = userSteps.loginUser(testUser);
        loginResponse.then()
                .statusCode(SC_OK)
                .body("success", is(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("Логин с неверным email, ответ 401")
    public void testUserLoginWrongEmail() {
        userSteps.registerUser(testUser)
                .then().statusCode(SC_OK);

        UserRequest wrongUser = new UserRequest(
                "wrong-email@example.com",
                testUser.getPassword(),
                testUser.getName()
        );

        userSteps.loginUser(wrongUser)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo(INVALID_CREDENTIALS_MESSAGE));
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Регистрируем пользователя и логинимся с неверным паролем, ответ 401")
    public void testUserLoginWrongPassword() {
        // Регистрируем пользователя
        userSteps.registerUser(testUser)
                .then().statusCode(SC_OK);

        UserRequest wrongUser = new UserRequest(
                testUser.getEmail(),
                "wrong-password",
                testUser.getName()
        );

        userSteps.loginUser(wrongUser)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", equalTo(INVALID_CREDENTIALS_MESSAGE));
    }
}
