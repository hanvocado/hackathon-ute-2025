package hadup.server.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "food")
public class Food {
    @Id
    private Long id;
    private String foodName;
    @JsonIgnore
    private String servingSize;
    private int calories;
    private double fat;
    private double sugar;
    private double protein;
    private double fiber;
    private String imageUrl;
}
