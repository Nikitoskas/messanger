package server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.dto.UserDTO;
import server.database.entity.User;
import server.database.mapper.UserMapper;
import server.database.service.impl.UserServiceImpl;
import server.security.service.TokenHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@PropertySource("application.properties")
@Controller
@RequestMapping("auth")
public class AuthController {

    private final TokenHandler tokenHandler;

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @Value("${jwt.token.header}")
    private String tokenHeaderName;

    public AuthController(TokenHandler tokenHandler, UserServiceImpl userService, UserMapper userMapper) {
        this.tokenHandler = tokenHandler;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @RequestMapping(value = "id", method = RequestMethod.GET)
    public ResponseEntity getAuthId(){
        return ResponseEntity.ok(userService.getAuthUserId());
    }

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public ResponseEntity sendToken(
            final HttpServletRequest request,
            final HttpServletResponse response,
            @RequestParam(value = "password", required = false) String pass,
            @RequestParam(value = "username", required = false) String login
//            @RequestHeader(value = "X-Auth-Token")
    ) {
        String username;
        String password;

        if (pass != null && login != null) {
            username = login;
            password = pass;
        } else {
            return ResponseEntity.badRequest().body("no login or/and password");
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("user is not found");
        }

        String token = tokenHandler.createToken(username);
        response.setHeader(tokenHeaderName, token);
        return ResponseEntity.ok("token for username: " + username + " was created");

    }

    @RequestMapping(value = "registration", method = RequestMethod.POST)
    public ResponseEntity register(
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

        if (userService.isEmailValid(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email: " + userDTO.getEmail() + " already registered");
        }

        if (userService.isUsernameValid(userDTO.getUsername())){
            return ResponseEntity.badRequest().body("Username: " + userDTO.getUsername() + " already registered");
        }

        User user = userMapper.mapAuthUserDtoToEntity(userDTO);
        User registeredUser = userService.register(user);


        return ResponseEntity.ok(registeredUser);
    }


}
