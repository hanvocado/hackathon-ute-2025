package hadup.server.server.repository;

import hadup.server.server.entity.ProfileUser;
import hadup.server.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileUserRepository extends JpaRepository<ProfileUser, String> {
    ProfileUser findProfileUserByUser_Email(String email);
}
