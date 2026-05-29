package praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//*Тело запроса
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
    private String name;
}