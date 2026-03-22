package praktikum.api.base;

import java.util.Arrays;
import java.util.List;

public class DataTests {
    // Базовые URL
    public static final String BASE_URL = "https://stellarburgers.education-services.ru";
    public static final String AUTH_REGISTER_PATH = "/api/auth/register";
    public static final String AUTH_LOGIN_PATH = "/api/auth/login";
    public static final String AUTH_USER_PATH = "/api/auth/user";
    public static final String INGREDIENTS_PATH = "/api/ingredients";
    public static final String ORDERS_PATH = "/api/orders";

    // Валидные ID ингредиентов
    public static final List<String> VALID_INGREDIENTS = Arrays.asList(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa72",
            "61c0c5a71d1f82001bdaaa70"
    );

    // Только одна булка
    public static final List<String> SINGLE_INGREDIENT = Arrays.asList(
            "61c0c5a71d1f82001bdaaa72"
    );

    // Ожидаемые сообщения об ошибках
    public static final String USER_EXISTS_MESSAGE = "User already exists";
    public static final String REQUIRED_FIELDS_MESSAGE = "Email, password and name are required fields";
    public static final String INVALID_CREDENTIALS_MESSAGE = "email or password are incorrect";
    public static final String NO_INGREDIENTS_MESSAGE = "Ingredient ids must be provided";
    public static final String NOT_AUTHORIZED_MESSAGE = "You should be authorised";

    // Примеры тестовых данных для пользователей
    public static final String TEST_EMAIL = "test-user@example.com";
    public static final String TEST_PASSWORD = "testpassword123";
    public static final String TEST_NAME = "Test User";

    // Невалидный ID ингредиента для тестов
    public static final String INVALID_INGREDIENT_ID = "invalid-ingredient-id";
}

