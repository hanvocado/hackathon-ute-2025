package hadup.server.server.repository;

import hadup.server.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);

    Optional<User> findByUsername(String username);
}
