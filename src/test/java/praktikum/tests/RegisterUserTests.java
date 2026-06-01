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

        if (createdUser != null) {
            TestUserFactory.deleteUser(createdUser);
        }
    }

    @Story("Успешная регистрация")
    @Test
    @Description("Регистрация нового уникального пользователя")
    @Severity(SeverityLevel.CRITICAL)
    public void registerNewUserSuccess() {
        createdUser = TestUserFactory.createUniqueUser();
        assertNotNull("Access token не должен быть null", createdUser.getAccessToken());
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
    @Description("Регистрация без поля email")
    @Severity(SeverityLevel.NORMAL)
    public void registerMissingEmailFailure() {
        String password = TestDataGenerator.generatePassword();
        String name = TestDataGenerator.generateName();

        ValidatableResponse response = authSteps.registerUser(null, password, name);
        response.statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Story("Обязательные поля")
    @Test
    @Description("Регистрация без поля password")
    @Severity(SeverityLevel.NORMAL)
    public void registerMissingPasswordFailure() {
        String email = TestDataGenerator.generateUniqueEmail();
        String name = TestDataGenerator.generateName();

        ValidatableResponse response = authSteps.registerUser(email, null, name);
        response.statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Story("Обязательные поля")
    @Test
    @Description("Регистрация без поля name")
    @Severity(SeverityLevel.NORMAL)
    public void registerMissingNameFailure() {
        String email = TestDataGenerator.generateUniqueEmail();
        String password = TestDataGenerator.generatePassword();

        ValidatableResponse response = authSteps.registerUser(email, password, null);
        response.statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}