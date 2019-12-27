package server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.dto.ChatDTO;
import server.database.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.mapper.ChatMapper;
import server.database.mapper.UserMapper;
import server.database.service.impl.ChatServiceImpl;
import server.database.service.impl.MessageServiceImpl;
import server.database.service.impl.UserServiceImpl;

import java.util.List;

@Controller
@RestController
@RequestMapping(path = "user")
public class UserController {

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private ChatServiceImpl chatService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("")
    public ResponseEntity userPage(@RequestParam(name = "username") String username){

        UserDTO userDTO = userMapper.userEntityToUserDTO(userService.findByUsername(username));

        return ResponseEntity.ok(userDTO);
    }

    @RequestMapping("test")
    public ResponseEntity test(){

//        UserDTO userDTO = new UserMapper(userService).ownerUserEntityToUserDTO(userService.findById(6L));



        return ResponseEntity.ok(chatMapper.standardChatEntityToDTOMapper(chatService.findPrivateChat(6L, 2L)));
    }

    @RequestMapping("chats")
    public ResponseEntity getChats(){
        Long id = userService.getAuthUserId();
        List<ChatDTO> chats = chatMapper.ListEntityToDTO(userService.findAllChatsByUserId(id));
        return ResponseEntity.ok(chats);
    }



}
