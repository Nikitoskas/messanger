package server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.database.dto.ChatDTO;
import server.database.entity.Chat;
import server.database.entity.UserChats;
import server.database.mapper.ChatMapper;
import server.database.service.impl.ChatServiceImpl;
import server.database.service.impl.MessageServiceImpl;
import server.database.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


@Controller
@RestController
@RequestMapping(path = "chat")
public class ChatController {

    private final ChatMapper chatMapper;

    private final ChatServiceImpl chatService;

    private final MessageServiceImpl messageService;

    private final UserServiceImpl userService;

    public ChatController(ChatMapper chatMapper, ChatServiceImpl chatService, MessageServiceImpl messageService, UserServiceImpl userService) {
        this.chatMapper = chatMapper;
        this.chatService = chatService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @RequestMapping("")
    public ResponseEntity getChat(@RequestParam(name = "chat_id") Long id){

        if (!checkAccess(id)){
            return ResponseEntity.badRequest().body("chat not found or haven't access");
        }
        ChatDTO chat = chatMapper.standardChatMapperEntityToDto(chatService.findById(id));
        return ResponseEntity.ok(chat);
    }

    @RequestMapping("messages")
    public ResponseEntity getAllMessages(@RequestParam(name = "chat_id") Long id){
        if (!checkAccess(id)){
            return ResponseEntity.badRequest().body("chat not found or haven't access");
        }
        return ResponseEntity.ok(messageService.getAllByChatId(id));
    }

    @RequestMapping("create")
    public ResponseEntity createChat(@RequestBody Map<String, Object> body){
        List users;
        Set<Long> usersId = new TreeSet<>();
        String chatName;
        try{
            users = (List) body.get("users");
            chatName = (String) body.get("chat_name");

            for (Object entry: users) {
                String username = (String) entry;
                if (!userService.checkUsername(username)){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user with username: " + username + " not found");
                }
                usersId.add(userService.getIdByUsername(username));
            }
        } catch (Exception e){
            return ResponseEntity.badRequest().body("bad JSON");
        }

        boolean group = (usersId.size() > 1);
        Long authUserId = userService.getAuthUserId();

        Chat chat = new Chat();
        chat.setGroup(group);
        chat.setCreator(authUserId);
        chat.setName(chatName);
        chat = chatService.create(chat);

        Long chatId = chat.getId();

        chatService.addUserToChat(authUserId, chatId);
        for (Long id: usersId) {
            chatService.addUserToChat(id, chatId);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private boolean checkAccess(Long chatId){
        Long authUserId = userService.getAuthUserId();
        return chatService.checkAccess(chatId, authUserId);
    }






}
