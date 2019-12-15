package server.database.service;

import server.database.entity.Chat;

import java.util.List;

public interface ChatService {
    public Chat create(Chat chat);
    public List<Chat> getAll();
    public List<Chat> getAllByUserId(Long id);
    public Chat findById(Long id);
    public void delete(Long id);
}
