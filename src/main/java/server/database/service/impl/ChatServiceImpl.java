package server.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import server.database.entity.Chat;
import server.database.entity.Status;
import server.database.repository.ChatRepository;
import server.database.service.ChatService;

import java.util.List;

public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Chat create(Chat chat) {

        chat.setStatus(Status.ACTIVE);

        Chat createdChat = chatRepository.saveAndFlush(chat);
        return createdChat;
    }

    @Override
    public List<Chat> getAll() {
        List<Chat> chats = chatRepository.findAll();
        return chats;
    }

    @Override
    public List<Chat> getAllByUserId(Long id) {
        List<Chat> chats = chatRepository.findAllByCreatorEquals(id);

        return chats;
    }

    @Override
    public Chat findById(Long id) {
        Chat foundedChat = chatRepository.findById(id).orElse(null);
        return foundedChat;
    }

    @Override
    public void delete(Long id) {
        chatRepository.deleteById(id);

    }
}
