package hadup.server.server.repository;

import hadup.server.server.entity.Plan;
import hadup.server.server.entity.ProfileUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {
    Optional<Plan> findPlanByUser_Email(String userEmail);
}
