package server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.dto.UserDTO;
import server.database.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.service.impl.UserServiceImpl;
import server.security.SecurityUser;

@Controller
@RestController
@RequestMapping(path = "user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("")
    public ResponseEntity userPage(@RequestParam(name = "username") String username){

        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String authUsername = securityUser.getUsername();

        UserDTO userDTO;
        if (!authUsername.equalsIgnoreCase(username)) {
            userDTO = new UserMapper().userToOtherUserDTO(userService.findByUsername(username));
        } else {
            userDTO = new UserMapper().ownerUserEntityToUserDTO(userService.findByUsername(username));
        }
        return ResponseEntity.ok(userDTO);
    }

    @RequestMapping("test")
    public ResponseEntity test(){

        UserDTO userDTO = new UserMapper().ownerUserEntityToUserDTO(userService.findById(6L));

        return ResponseEntity.ok(userDTO);
    }

}
