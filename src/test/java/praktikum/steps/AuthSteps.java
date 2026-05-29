package praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.BaseApiTest;
import praktikum.config.ApiConfig;
import praktikum.models.AuthRequest;

import static io.restassured.RestAssured.given;

//Шаги авторизации и лог ина.
public class AuthSteps extends BaseApiTest{

    @Step("Регистрация пользователя: {email}")
    public ValidatableResponse registerUser(String email, String password, String name) {
        AuthRequest request = new AuthRequest(email, password, name);
        return given(getSpec())
                .contentType(ApiConfig.CONTENT_TYPE_JSON)
                .body(request)
                .post(ApiConfig.AUTH_REGISTER)
                .then();
    }

    @Step("Авторизация пользователя: {email}")
    public ValidatableResponse loginUser(String email, String password) {
        AuthRequest request = new AuthRequest(email, password, null);
        return given(getSpec())
                .contentType(ApiConfig.CONTENT_TYPE_JSON)
                .body(request)
                .post(ApiConfig.AUTH_LOGIN)
                .then();
    }


    //Удаление пользователя
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header(ApiConfig.HEADER_AUTHORIZATION, accessToken)
                .delete(ApiConfig.AUTH_USER)
                .then();
    }

    //Извлечение данных из ответа

    //Извлечение accessToken
    public String extractAccessToken(ValidatableResponse response) {
        return response.extract().jsonPath().getString("accessToken");
    }

    //Извлечение refreshToken
    public String extractRefreshToken(ValidatableResponse response) {
        return response.extract().jsonPath().getString("refreshToken");
    }

    //Проверка поля success в ответе
    public boolean isSuccess(ValidatableResponse response) {
        return response.extract().jsonPath().getBoolean("success");
    }
}