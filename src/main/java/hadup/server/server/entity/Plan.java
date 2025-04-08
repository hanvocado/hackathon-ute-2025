package hadup.server.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;
    int calories;
    double fat;
    double sugar;
    double protein;
    double fiber;
    @Column(length = 1000)
    @JsonIgnore
    String reason;

    @OneToOne
    @JsonIgnore
    private User user;
}
