package hadup.server.server.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanningResponse {
    int calories;
    double fat;
    double sugar;
    double protein;
    double fiber;
    String reason;
}
