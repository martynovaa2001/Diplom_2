package praktikum.api.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import praktikum.api.UserRequest;
import praktikum.api.base.BaseApiTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static praktikum.api.base.DataTests.*;
import static org.apache.http.HttpStatus.*;

public class UserCreateTest extends BaseApiTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Регистрируем уникального пользователя, ответ 200")
    public void createUniqueUserSuccessTest() {
        Response response = userSteps.registerUser(testUser);

        response.then()
                .log().all()
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("success", is(true))
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    @Description("Регистрируем пользователя и повторно его регистрируем, ответ 403")
    public void createExistingUserFailureTest() {
        assertNotNull(testUser);
        // Первая регистрация
        Response firstResponse = userSteps.registerUser(testUser);
        firstResponse.then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", is(true));

        // Повторная попытка
        Response secondResponse = userSteps.registerUser(testUser);
        secondResponse.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(USER_EXISTS_MESSAGE));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Регистрируем пользователя без email, ответ 403")
    public void createUserWithoutEmailFailureTest() {
        UserRequest userWithoutEmail = new UserRequest(
                null,
                testUser.getPassword(),
                testUser.getName()
        );

        given()
                .body(userWithoutEmail)
                .post(AUTH_REGISTER_PATH)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(REQUIRED_FIELDS_MESSAGE));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Регистрируем пользователя без пароля, ответ 403")
    public void createUserWithoutPasswordFailureTest() {
        UserRequest userWithoutPassword = new UserRequest(
                testUser.getEmail(),
                null,
                testUser.getName()
        );

        given()
                .body(userWithoutPassword)
                .post(AUTH_REGISTER_PATH)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(REQUIRED_FIELDS_MESSAGE));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Регистрируем пользователя без имени, ответ 403")
    public void createUserWithoutNameFailureTest() {
        UserRequest userWithoutName = new UserRequest(
                testUser.getEmail(),
                testUser.getPassword(),
                null
        );

        given()
                .body(userWithoutName)
                .post(AUTH_REGISTER_PATH)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(REQUIRED_FIELDS_MESSAGE));
    }

    @Test
    @DisplayName("Создание пользователя с некорректным email")
    @Description("Регистрируем пользователя с некорректным email, ответ 403")
    public void createUserWithInvalidEmailFailureTest() {
        UserRequest userWithInvalidEmail = new UserRequest(
                "invalid-email",
                testUser.getPassword(),
                testUser.getName()
        );

        given()
                .body(userWithInvalidEmail)
                .post(AUTH_REGISTER_PATH)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", containsString(REQUIRED_FIELDS_MESSAGE));
    }
}
