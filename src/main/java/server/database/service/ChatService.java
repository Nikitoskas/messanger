package server.database.service;

import server.database.entity.Chat;

import java.util.List;

public interface ChatService {
    Chat create(Chat chat);
    List<Chat> getAll();
    List<Chat> getAllByUserId(Long id);
    Chat findById(Long id);
    void delete(Long id);
    Chat findPrivateChat(Long id1, Long id2);


}
