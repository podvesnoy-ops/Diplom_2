package praktikum.tests;

import io.qameta.allure.*;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import praktikum.BaseApiTest;
import praktikum.factories.TestUserFactory;
import praktikum.models.TestUser;
import praktikum.steps.AuthSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@Feature("Авторизация пользователя")
public class LoginUserTests extends BaseApiTest {

    private final AuthSteps authSteps = new AuthSteps();
    private TestUser createdUser;

    @After
    public void cleanUp() {
        TestUserFactory.deleteUser(createdUser);
    }

    @Story("Успешный вход")
    @Test
    @Description("Вход под существующим пользователем")
    @Severity(SeverityLevel.CRITICAL)
    public void loginExistingUserSuccess() {
        createdUser = TestUserFactory.createUniqueUser();

        ValidatableResponse response = authSteps.loginUser(createdUser.getEmail(), createdUser.getPassword());
        response.statusCode(HttpStatus.SC_OK);

        assertTrue(authSteps.isSuccess(response));
        assertNotNull(authSteps.extractAccessToken(response));
        assertNotNull(authSteps.extractRefreshToken(response));
    }

    @Story("Неверные данные")
    @Test
    @Description("Вход с неверным логином и паролем")
    @Severity(SeverityLevel.NORMAL)
    public void loginWrongCredentialsFailure() {
        ValidatableResponse response = authSteps.loginUser("invalid@email.ru", "wrongpass");
        response.statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}