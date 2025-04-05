package hadup.server.server.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hadup.server.server.entity.FoodHistory;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodHistoryResponse {
    LocalDate createAt;
    String imageUrl;
    String foodName;
    int calories;
    double fat;
    double sugar;
    double protein;
    double fiber;
}
