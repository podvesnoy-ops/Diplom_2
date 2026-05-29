package praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Пользователь
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestUser {
    private String email;
    private String password;
    private String name;
    private String accessToken;
    private String refreshToken;
}