package praktikum.config;

//Генератор тестовых данных.
public class TestDataGenerator {

    private static final String EMAIL_DOMAIN = "@stellar-test.ru";
    private static final String PASSWORD_PREFIX = "Qwerty_";
    private static final String NAME_PREFIX = "TestUser_";

    //email
    public static String generateUniqueEmail() {
        return "user_" + System.currentTimeMillis() + EMAIL_DOMAIN;
    }

    //пароль
    public static String generatePassword() {
        return PASSWORD_PREFIX + System.currentTimeMillis();
    }

    //имя
    public static String generateName() {
        return NAME_PREFIX + System.currentTimeMillis();
    }

}