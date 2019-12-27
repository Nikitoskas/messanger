package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.dto.UserDTO;
import server.database.entity.User;
import server.database.mapper.UserMapper;
import server.database.service.impl.UserServiceImpl;
import server.security.service.TokenHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@PropertySource("application.properties")
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserMapper userMapper;

    @Value("${jwt.token.header}")
    private String tokenHeaderName;


    @RequestMapping(value = "gettoken")
    public ResponseEntity sendToken(
            final HttpServletRequest request,
            final HttpServletResponse response,
            @RequestBody(required = false) UserDTO userDTO,
            @RequestParam(value = "password", required = false) String pass,
            @RequestParam(value = "username", required = false) String login
//            @RequestHeader(value = "X-Auth-Token")
            ){
        String username;
        String password;
        if (userDTO != null && userDTO.getUsername() != null && userDTO.getPassword() != null){
            username = userDTO.getUsername();
            password = userDTO.getPassword();
        } else {
            if (pass != null && login != null){
                username = login;
                password = pass;
            } else {
                return ResponseEntity.badRequest().body("no login and password");
            }
        }

        User user = userService.findByUsername(username);
        if (user == null){
            return ResponseEntity.badRequest().body("user is not found");
        }

        String token = tokenHandler.createToken(username);
        response.setHeader(tokenHeaderName, token);
        return ResponseEntity.ok("Cool");

    }

    @RequestMapping(value = "reg")
    public ResponseEntity registration(
            @RequestBody(required = false) UserDTO userDTO
    ){
        String username;
        String password;
        if (userDTO != null && userDTO.getUsername() != null && userDTO.getPassword() != null){
            username = userDTO.getUsername();
            password = userDTO.getPassword();
        } else {
            return ResponseEntity.badRequest().body("no login and password or bad json");
        }

        if (userService.checkEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email: " + userDTO.getEmail() + " already registered");
        }

        if (userService.checkUsername(userDTO.getUsername())){
            return ResponseEntity.badRequest().body("Username: " + userDTO.getUsername() + " already registered");
        }

        User user = userMapper.userDTOToUserEntity(userDTO);
        User registeredUser = userService.register(user);


        return ResponseEntity.ok(registeredUser);
    }


}
