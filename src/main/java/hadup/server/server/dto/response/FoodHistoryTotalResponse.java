package hadup.server.server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodHistoryTotalResponse {
    List<FoodHistoryResponse> foodHistoryList;
    int totalCalories;
    double totalFat;
    double totalSugar;
    double totalProtein;
    double totalFiber;
}
