package server.database.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import server.database.entity.Chat;
import server.database.entity.Status;
import server.database.entity.User;

import server.database.entity.UserChats;
import server.database.repository.ChatRepository;
import server.database.repository.UserChatsRepository;
import server.database.repository.UserRepository;
import server.database.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.security.SecurityUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserChatsRepository userChatsRepository;

    @Override
    public User register(User user) {
        user.setStatus(Status.ACTIVE);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        User registeredUser = userRepository.saveAndFlush(user);
        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        log.info("IN findByLogin - user {} found by login {}", result, username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null){
            log.warn("IN findById - user not found by id: {}", id);
            return null;
        }
        log.info("IN findById - user {} found by id", result);
        return result;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean checkUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }

    @Override
    public List<Chat> findAllChatsByUserId(Long id) {
        List<UserChats> userChats = userChatsRepository.findAllByUserId(id);

        List<Chat> chats = new ArrayList<>();
        Chat chat;

        for (UserChats iterator : userChats) {
            chat = chatRepository.findById(iterator.getChatId()).get();
            if (Objects.isNull(chat)){
                System.out.println("Не все чаты загрузились");
                continue;
            }
            chats.add(chat);
        }

        return chats;
    }

    @Override
    public Long getIdByUsername(String username) {
        User user = findByUsername(username);
        return user.getId();
    }

    @Override
    public String getAuthUsername(){
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return securityUser.getUsername();
    }

    @Override
    public Long getAuthUserId(){
        return getIdByUsername(getAuthUsername());
    }
}
