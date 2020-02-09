package server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.dto.ChatDTO;
import server.database.dto.UserDTO;
import server.database.mapper.ChatMapper;
import server.database.mapper.UserMapper;
import server.database.service.impl.ChatServiceImpl;
import server.database.service.impl.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping(path = "user")
public class UserController {

    private final ChatMapper chatMapper;

    private final UserServiceImpl userService;

    private final ChatServiceImpl chatService;

    private final UserMapper userMapper;

    public UserController(ChatMapper chatMapper, UserServiceImpl userService, ChatServiceImpl chatService, UserMapper userMapper) {
        this.chatMapper = chatMapper;
        this.userService = userService;
        this.chatService = chatService;
        this.userMapper = userMapper;
    }

    @GetMapping("")
    public ResponseEntity getUser(@RequestParam(name = "username", required = false) String username){

        String authUsername = userService.getAuthUsername();
        UserDTO userDTO;
        if (username == null || username.isEmpty() || authUsername.equals(username)){
            userDTO = userMapper.mapAuthUserEntityToDTO(userService.findByUsername(authUsername));
        } else {
            userDTO = userMapper.mapNoAuthUserEntityToDTO(userService.findByUsername(username));
        }

        return ResponseEntity.ok(userDTO);
    }

    @RequestMapping("test")
    public ResponseEntity test(){
        return ResponseEntity.ok(chatMapper.mapStandardChatMapperEntityToDto(chatService.findPrivateChat(6L, 2L)));
    }

    @RequestMapping("chats")
    public ResponseEntity getChats(){
        Long id = userService.getAuthUserId();
        List<ChatDTO> chats = chatMapper.mapStandardListEntityToDTO(userService.findAllChatsByUserId(id));
        return ResponseEntity.ok(chats);
    }

    @RequestMapping("users")
    public ResponseEntity getAllUsers(){
        return ResponseEntity.ok(userMapper.mapNoAuthUserListEntityToDTO(userService.getAll()));
    }



}
