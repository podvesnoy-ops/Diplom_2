package praktikum.utils;

import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import praktikum.config.ApiConfig;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientHelper {

    private static List<String> validIngredientIds;

    public static List<String> getValidIngredientIds() {
        if (validIngredientIds == null) {
            ValidatableResponse response = given()
                    .get(ApiConfig.INGREDIENTS)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
            List<String> allIds = response.extract().jsonPath().getList("data._id", String.class);
            validIngredientIds = allIds.subList(0, Math.min(2, allIds.size()));
        }
        return validIngredientIds;
    }
}