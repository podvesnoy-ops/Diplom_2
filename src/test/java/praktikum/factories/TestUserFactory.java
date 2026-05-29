package praktikum.factories;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import praktikum.config.TestDataGenerator;
import praktikum.models.TestUser;
import praktikum.steps.AuthSteps;

public class TestUserFactory {

    private static final AuthSteps authSteps = new AuthSteps();

    public static TestUser createUniqueUser() {
        String email = TestDataGenerator.generateUniqueEmail();
        String password = TestDataGenerator.generatePassword();
        String name = TestDataGenerator.generateName();

        ValidatableResponse response = authSteps.registerUser(email, password, name);
        response.statusCode(HttpStatus.SC_OK);

        String accessToken = authSteps.extractAccessToken(response);
        String refreshToken = authSteps.extractRefreshToken(response);

        return new TestUser(email, password, name, accessToken, refreshToken);
    }


    public static void deleteUser(TestUser user) {
        if (user != null && user.getAccessToken() != null) {
            try {
                authSteps.deleteUser(user.getAccessToken()).statusCode(HttpStatus.SC_ACCEPTED);
            } catch (Exception e) {
                System.out.println("Не удалось удалить пользователя: " + e.getMessage());
            }
        }
    }
}