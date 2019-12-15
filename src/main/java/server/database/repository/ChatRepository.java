package server.database.repository;

import server.database.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    public List<Chat> findAllByCreatorEquals(Long id);

}
