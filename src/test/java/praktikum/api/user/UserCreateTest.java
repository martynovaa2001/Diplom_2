package praktikum.api.user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import org.junit.Test;
import praktikum.api.UserRequest;
import praktikum.api.base.BaseApiTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static praktikum.api.base.DataTests.*;
import static org.apache.http.HttpStatus.*;

public class UserCreateTest extends BaseApiTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserSuccessTest() {
        given()
                .contentType("application/json")
                .body(testUser)
                .log().body()
                .log().ifValidationFails()
                .when()
                .post(AUTH_REGISTER_PATH)
                .then()
                .log().status()
                .log().body()
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("success", is(true))
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void createExistingUserFailureTest() {
        // Первая регистрация
        given()
                .contentType("application/json")
                .body(testUser)
                .log().body()
                .log().ifValidationFails()
                .when()
                .post(AUTH_REGISTER_PATH)
                .then()
                .log().status()
                .log().body()
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .body("success", is(true))
                .body("user.email", equalTo(testUser.getEmail()))
                .body("user.name", equalTo(testUser.getName()));

        // Повторная попытка
        given()
                .contentType("application/json")
                .body(testUser)
                .log().body()
                .log().ifValidationFails()
                .when()
                .post(AUTH_REGISTER_PATH)
                .then()
                .log().status()
                .log().body()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", equalTo(USER_EXISTS_MESSAGE));
    }

    @Test
    @DisplayName("Создание пользователя без email")
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
