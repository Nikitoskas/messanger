package server.controller;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import server.database.entity.Message;
import server.database.entity.User;
import server.database.repository.MessageRepository;
import server.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.security.service.TokenHandler;


@RestController
public class MainController {
    @Autowired
    private UserRepository userRepository;

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
    public String getUsername(@RequestHeader(value = "X-Auth-Token") String token){

        return tokenHandler.extractUsername(token).get();
    }

    @RequestMapping("messages")
    public Iterable<Message> messages(@RequestParam(value = "id", defaultValue = "1") Long id){
        return messageRepository.findAllByAuthor(userRepository.findById(id).get());
    }

}
