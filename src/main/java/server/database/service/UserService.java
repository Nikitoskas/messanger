package server.database.service;

import server.database.entity.User;

import java.util.List;

public interface UserService {
    public User register(User user);
    public List<User> getAll();
    public User findByUsername(String login);
    public User findById(Long id);
    public void delete(Long id);
}
