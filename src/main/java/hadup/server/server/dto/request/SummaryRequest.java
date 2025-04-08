package hadup.server.server.dto.request;

import hadup.server.server.entity.Plan;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SummaryRequest {
    Plan plan;
    Plan real;
}
