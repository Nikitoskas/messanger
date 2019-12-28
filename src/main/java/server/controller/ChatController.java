package server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.database.dto.ChatDTO;
import server.database.mapper.ChatMapper;
import server.database.service.impl.ChatServiceImpl;
import server.database.service.impl.MessageServiceImpl;
import server.database.service.impl.UserServiceImpl;


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
    public ResponseEntity getChat(@RequestParam(name = "id") Long id){

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

    private boolean checkAccess(Long chatId){
        Long authUserId = userService.getAuthUserId();
        return chatService.checkAccess(chatId, authUserId);
    }






}
