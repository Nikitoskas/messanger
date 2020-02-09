package server.database.service;

import server.database.entity.Chat;
import server.database.entity.User;

import java.util.List;

public interface UserService {
    public User register(User user);
    public List<User> getAll();
    public User findByUsername(String login);
    public User findById(Long id);
    public void delete(Long id);
    public boolean isEmailValid(String email);
    public boolean isUsernameValid(String username);
    public List<Chat> findAllChatsByUserId(Long id);
    public Long getIdByUsername(String username);
    public String getAuthUsername();
    public Long getAuthUserId();
}
