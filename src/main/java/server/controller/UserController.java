package server.controller;

import org.springframework.stereotype.Controller;
import server.database.entity.User;
import server.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping(path = "api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("{username}")
    public User userPage(@PathVariable(name = "username") String username){
        User user = userRepository.findByUsername((username == null) ? "test" : username);
        return user;
    }

}
