package hadup.server.server.repository;

import hadup.server.server.entity.Food;
import hadup.server.server.entity.FoodHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodHistoryRepository extends JpaRepository<FoodHistory, String> {
    List<FoodHistory> findFoodHistoriesByCreateAt(LocalDate createAt);
}
