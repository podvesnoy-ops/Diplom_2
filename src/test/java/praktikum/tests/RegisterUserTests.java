package praktikum.tests;

import io.qameta.allure.*;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import praktikum.BaseApiTest;
import praktikum.config.TestDataGenerator;
import praktikum.factories.TestUserFactory;
import praktikum.models.TestUser;
import praktikum.steps.AuthSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@Feature("Регистрация пользователя")
public class RegisterUserTests extends BaseApiTest {

    private final AuthSteps authSteps = new AuthSteps();
    private TestUser createdUser;

    @After
    public void cleanUp() {
        TestUserFactory.deleteUser(createdUser);
    }

    @Story("Успешная регистрация")
    @Test
    @Description("Регистрация нового уникального пользователя")
    @Severity(SeverityLevel.CRITICAL)
    public void registerNewUserSuccess() {
        String email = TestDataGenerator.generateUniqueEmail();
        String password = TestDataGenerator.generatePassword();
        String name = TestDataGenerator.generateName();

        ValidatableResponse response = authSteps.registerUser(email, password, name);
        response.statusCode(HttpStatus.SC_OK);

        assertTrue(authSteps.isSuccess(response));
        String accessToken = authSteps.extractAccessToken(response);
        assertNotNull(accessToken);
        createdUser = new TestUser(email, password, name, accessToken, null);
    }

    @Story("Дубликат пользователя")
    @Test
    @Description("Регистрация уже существующего пользователя")
    @Severity(SeverityLevel.NORMAL)
    public void registerExistingUserFailure() {
        createdUser = TestUserFactory.createUniqueUser();

        ValidatableResponse response = authSteps.registerUser(
                createdUser.getEmail(),
                createdUser.getPassword(),
                createdUser.getName()
        );

        response.statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Story("Обязательные поля")
    @Test
    @Description("Регистрация без поля name")
    @Severity(SeverityLevel.NORMAL)
    public void registerMissingNameFieldFailure() {
        String email = TestDataGenerator.generateUniqueEmail();
        String password = TestDataGenerator.generatePassword();

        ValidatableResponse response = authSteps.registerUser(email, password, null);
        response.statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}