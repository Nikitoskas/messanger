package server.database.repository;

import server.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String login);
    public boolean existsUserByEmail(String email);
    public boolean existsUserByUsername(String username);
}
