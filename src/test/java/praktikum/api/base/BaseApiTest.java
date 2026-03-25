package praktikum.api.base;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import praktikum.api.UserRequest;
import praktikum.api.user.UserSteps;

import static praktikum.api.base.DataTests.BASE_URL;

public class BaseApiTest {
    protected Faker faker;
    protected UserSteps userSteps;
    protected UserRequest testUser;
    protected String accessToken;

    @BeforeClass
    public static void setUpBaseClass() {
        RestAssured.baseURI = BASE_URL;
    }

    @Before
    public void setUp() {
        // Создаем фейкер для генерации данных
        faker = new Faker();
        userSteps = new UserSteps();

        // Генерируем тестового пользователя
        testUser = new UserRequest(
                faker.internet().emailAddress(),
                faker.internet().password(8, 16),
                faker.name().fullName()
        );
    }

    @After
    public void tearDown() {
        try {
            userSteps.deleteUser(accessToken);
        } catch (Exception e) {}
    }
}


