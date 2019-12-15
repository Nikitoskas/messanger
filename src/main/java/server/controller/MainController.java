package server.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import server.database.entity.User;
import server.database.mapper.UserMapper;
import server.database.repository.MessageRepository;
import server.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.database.service.impl.UserServiceImpl;
import server.security.service.TokenHandler;


@RestController
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TokenHandler tokenHandler;

    @RequestMapping("gettoken")
    public ResponseEntity getToken(@RequestParam(value = "login") String login){

        HashMap<Object, Object> response = new HashMap<>();
        String token = tokenHandler.createToken(login);
        response.put("login", login);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @RequestMapping("getusername")
    public ResponseEntity getUsername(@RequestHeader(value = "X-Auth-Token") String token){

        if (!tokenHandler.validateToken(token)){
            return ResponseEntity.badRequest().body("неверный токен");
        }
        String username = tokenHandler.extractUsername(token);
        HashMap<Object, Object> response = new HashMap<>();
        response.put("username", username);


        return ResponseEntity.ok(response);
    }

    @RequestMapping("getuser")
    public ResponseEntity getUser(@RequestHeader(value = "X-Auth-Token") String token){

        if (!tokenHandler.validateToken(token)){
            return ResponseEntity.badRequest().body("invalid token");
        }

        String username = tokenHandler.extractUsername(token);

        User user = userService.findByUsername(username);
        UserMapper userMapper = new UserMapper();

        HashMap<Object, Object> response = new HashMap<>();
        response.put("user", userMapper.ownerUserEntityToUserDTO(user));


        return ResponseEntity.ok(response);
    }

//    @RequestMapping("messages")
//    public Iterable<Message> messages(@RequestParam(value = "id", defaultValue = "1") Long id){
//        return messageRepository.findAllByAuthor(userRepository.findById(id).get());
//    }

}
