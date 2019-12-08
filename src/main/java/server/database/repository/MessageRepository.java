package server.database.repository;

import server.database.entity.Message;
import server.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public Iterable<Message> findAllByAuthor(User author);
}
