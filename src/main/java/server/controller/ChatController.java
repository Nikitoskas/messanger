package server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.dto.ChatDTO;
import server.database.mapper.ChatMapper;
import server.database.service.impl.ChatServiceImpl;

@Controller
@RestController
@RequestMapping(path = "chat")
public class ChatController {

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ChatServiceImpl chatService;

    @RequestMapping
    public ResponseEntity getChat(Long id){

        ChatDTO chat = chatMapper.standardChatEntityToDTOMapper(chatService.findById(id));

        return ResponseEntity.ok(chat);
    }

}
