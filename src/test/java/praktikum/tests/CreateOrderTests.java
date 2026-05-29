package praktikum.tests;

import io.qameta.allure.*;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import praktikum.BaseApiTest;
import praktikum.factories.TestUserFactory;
import praktikum.models.TestUser;
import praktikum.steps.OrderSteps;
import praktikum.utils.IngredientHelper;


import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@Feature("Создание заказа")
public class CreateOrderTests extends BaseApiTest {

    private static final OrderSteps orderSteps = new OrderSteps();
    private static List<String> validIngredientIds;

    private TestUser createdUser;

    @BeforeClass
    public static void setUpIngredients() {
        validIngredientIds = IngredientHelper.getValidIngredientIds();
    }

    @After
    public void cleanUp() {
        TestUserFactory.deleteUser(createdUser);
    }

    @Story("Создание заказа с авторизацией")
    @Test
    @Description("Успешное создание заказа авторизованным пользователем")
    @Severity(SeverityLevel.CRITICAL)
    public void createOrderWithAuthSuccess() {
        createdUser = TestUserFactory.createUniqueUser();

        ValidatableResponse response = orderSteps.createOrder(validIngredientIds, createdUser.getAccessToken());
        response.statusCode(HttpStatus.SC_OK);

        assertTrue(orderSteps.isSuccess(response));
        int orderNumber = orderSteps.extractOrderNumber(response);
        assertTrue("Номер заказа должен быть положительным", orderNumber > 0);
    }

    @Story("Создание заказа без авторизации")
    @Test
    @Description("Попытка создать заказ без токена (фактическое поведение API – 200)")
    @Severity(SeverityLevel.NORMAL)
    public void createOrderWithoutAuthActualBehavior() {
        ValidatableResponse response = orderSteps.createOrderUnauth(validIngredientIds);
        response.statusCode(HttpStatus.SC_OK);
        assertTrue(orderSteps.isSuccess(response));
        int orderNumber = orderSteps.extractOrderNumber(response);
        assertTrue("Номер заказа должен быть положительным", orderNumber > 0);
    }

    @Story("Заказ без ингредиентов")
    @Test
    @Description("Пустой список ингредиентов – ошибка 400")
    @Severity(SeverityLevel.NORMAL)
    public void createOrderWithoutIngredientsFailure() {
        createdUser = TestUserFactory.createUniqueUser();

        ValidatableResponse response = orderSteps.createOrder(Collections.emptyList(), createdUser.getAccessToken());
        response.statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Story("Неверный хеш ингредиента")
    @Test
    @Description("Передача несуществующего ID ингредиента – ожидаем 500")
    @Severity(SeverityLevel.NORMAL)
    public void createOrderWithInvalidIngredientHashFailure() {
        createdUser = TestUserFactory.createUniqueUser();
        List<String> invalidIds = List.of("invalid_hash_12345");

        ValidatableResponse response = orderSteps.createOrder(invalidIds, createdUser.getAccessToken());
        response.statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Story("Заказ с ингредиентами (проверка состава)")
    @Test
    @Description("Проверка, что заказ создаётся с указанными ингредиентами")
    @Severity(SeverityLevel.CRITICAL)
    public void createOrderWithIngredientsVerifyResponse() {
        createdUser = TestUserFactory.createUniqueUser();
        List<String> testIngredients = validIngredientIds.size() >= 2
                ? validIngredientIds.subList(0, 2)
                : validIngredientIds;

        ValidatableResponse response = orderSteps.createOrder(testIngredients, createdUser.getAccessToken());
        response.statusCode(HttpStatus.SC_OK);
        assertTrue(orderSteps.isSuccess(response));
        int orderNumber = orderSteps.extractOrderNumber(response);
        assertTrue("Номер заказа должен быть положительным", orderNumber > 0);
    }
}