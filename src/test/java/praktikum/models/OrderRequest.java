package praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Запрос для создания заказа
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<String> ingredients;
}