package praktikum;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import praktikum.config.ApiConfig;

//Базовый класс

public class BaseApiTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
        // Логирование запросов/ответов только при падении проверок
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    //Возвращает спецификацию запроса с Allure-фильтром.
    protected RequestSpecification getSpec() {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .log().ifValidationFails(LogDetail.ALL);
    }
}