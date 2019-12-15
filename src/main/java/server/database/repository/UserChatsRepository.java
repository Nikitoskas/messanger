package server.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.database.entity.UserChats;

import java.util.List;

public interface UserChatsRepository extends JpaRepository<UserChats, Long> {
    public List<UserChats> findAllByChatId(Long id);
    public List<UserChats> findAllByUserId(Long id);
}
