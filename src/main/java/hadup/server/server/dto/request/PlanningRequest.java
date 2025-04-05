package hadup.server.server.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanningRequest {
    int height;
    int weight;
    int age;
    boolean isVegetarian;
    int sportActivity;
    String taste;
}
