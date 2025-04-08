package hadup.server.server.repository;

import hadup.server.server.entity.Food;
import hadup.server.server.entity.FoodHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodHistoryRepository extends JpaRepository<FoodHistory, String> {
    @Query("SELECT f FROM FoodHistory f WHERE f.createAt >= :start AND f.createAt < :end")
    List<FoodHistory> findFoodHistoriesByCreateAtBetween(@Param("start") Date start, @Param("end") Date end);
}
