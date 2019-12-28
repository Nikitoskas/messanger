package server.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.database.entity.UserChats;

import java.util.List;

public interface UserChatsRepository extends JpaRepository<UserChats, Long> {
    List<UserChats> findAllByChatId(Long id);
    List<UserChats> findAllByUserId(Long id);
    boolean existsUserChatsByChatIdAndUserId(Long chatId, Long userId);
}
