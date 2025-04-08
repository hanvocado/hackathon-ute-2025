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
public class ProfileUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private String id;
    private int height;
    private int weight;
    private int age;
    private boolean isVegetarian;
    @Builder.Default
    private boolean homeCook = false;
    private int sportActivity;
    private String taste;

    @OneToOne
    @JsonIgnore
    private User user;
}
