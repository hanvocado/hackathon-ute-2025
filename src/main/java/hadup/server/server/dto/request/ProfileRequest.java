package hadup.server.server.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileRequest {
    String email;
    int height;
    int weight;
    int age;
    boolean isVegetarian;
    boolean homeCook;
    int sportActivity;
    String taste;
}
