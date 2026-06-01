package praktikum.config;

//базовый URL, эндпоинты
public class ApiConfig {

    // === Базовые настройки ===
    public static final String BASE_URL = "https://stellarburgers.education-services.ru/api";

    // === Эндпоинты авторизации ===
    public static final String AUTH_REGISTER = "/auth/register";
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_USER = "/auth/user";

    // === Эндпоинты заказов ===
    public static final String ORDERS_CREATE = "/orders";

    // === Эндпоинты ингредиентов ===
    public static final String INGREDIENTS = "/ingredients";

    // === Заголовки ===
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE_JSON = "application/json";


}