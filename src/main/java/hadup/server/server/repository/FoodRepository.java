package hadup.server.server.repository;

import hadup.server.server.entity.Food;
import hadup.server.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findFoodById(Long id);

}
