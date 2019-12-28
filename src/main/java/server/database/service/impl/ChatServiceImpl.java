package server.database.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.entity.Chat;
import server.database.entity.Status;
import server.database.entity.UserChats;
import server.database.repository.ChatRepository;
import server.database.repository.UserChatsRepository;
import server.database.service.ChatService;

import java.util.List;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    private final UserChatsRepository userChatsRepository;

    public ChatServiceImpl(ChatRepository chatRepository, UserChatsRepository userChatsRepository) {
        this.chatRepository = chatRepository;
        this.userChatsRepository = userChatsRepository;
    }

    @Override
    public Chat create(Chat chat) {

        chat.setStatus(Status.ACTIVE);

        return chatRepository.saveAndFlush(chat);
    }

    @Override
    public List<Chat> getAll() {
        return chatRepository.findAll();
    }

    @Override
    public List<Chat> getAllByUserId(Long id) {

        return chatRepository.findAllByCreatorEquals(id);
    }

    @Override
    public Chat findById(Long id) {
        return chatRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        chatRepository.deleteById(id);
    }

    @Override
    public Chat findPrivateChat(Long id1, Long id2) {
        Long chatId = chatRepository.getPrivateChatIdByUsers(id1, id2);
        return chatRepository.findById(chatId).orElse(null);
    }

    @Override
    public boolean chatExists(Long id) {
        return chatRepository.existsChatById(id);
    }

    @Override
    public boolean checkAccess(Long chatId, Long userId) {
        return userChatsRepository.existsUserChatsByChatIdAndUserId(chatId, userId);
    }

    @Override
    public void addUserToChat(Long userId, Long chatId) {
        UserChats userChats = new UserChats();
        userChats.setChatId(chatId);
        userChats.setUserId(userId);
        userChats.setStatus(Status.ACTIVE);
        userChatsRepository.saveAndFlush(userChats);
    }
}
