package praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.BaseApiTest;
import praktikum.config.ApiConfig;
import praktikum.models.OrderRequest;

import java.util.List;

import static io.restassured.RestAssured.given;

//Шаги для работы с заказом.

public class OrderSteps extends BaseApiTest {

    @Step("Создание заказа: {ingredientIds}")
    public ValidatableResponse createOrder(List<String> ingredientIds, String accessToken) {
        OrderRequest request = new OrderRequest(ingredientIds);
        return given(getSpec())
                .header(ApiConfig.HEADER_AUTHORIZATION, accessToken)
                .contentType(ApiConfig.CONTENT_TYPE_JSON)
                .body(request)
                .post(ApiConfig.ORDERS_CREATE)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderUnauth(List<String> ingredientIds) {
        OrderRequest request = new OrderRequest(ingredientIds);
        return given(getSpec())
                .contentType(ApiConfig.CONTENT_TYPE_JSON)
                .body(request)
                .post(ApiConfig.ORDERS_CREATE)
                .then();
    }

    //Методы для извлечения данных из ответа

    //Извлечение номера заказа
    public int extractOrderNumber(ValidatableResponse response) {
        return response.extract().jsonPath().getInt("order.number");
    }

    //Проверка поля success в ответе
    public boolean isSuccess(ValidatableResponse response) {
        return response.extract().jsonPath().getBoolean("success");
    }
}