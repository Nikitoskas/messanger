package server.database.mapper;


import lombok.Data;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.database.dto.ChatDTO;
import server.database.dto.UserDTO;
import server.database.entity.Chat;
import server.database.entity.User;
import server.database.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Data
public class UserMapper extends ModelMapper{

    @Autowired
    private UserServiceImpl userService;

    public UserDTO userEntityToUserDTO(User user){

        String authUsername = userService.getAuthUsername();
        String username = user.getUsername();

        if (!authUsername.equalsIgnoreCase(username)) {
            setMapperForOtherUser();
        } else {
            setMapperForAuthUser();
        }


       return Objects.isNull(user) ? null : this.map(user, UserDTO.class);
   }

    public UserDTO shortUserEntityToUserDTO(User user){
        String username = user.getUsername();
        setMapperForOtherUser();
        return Objects.isNull(user) ? null : this.map(user, UserDTO.class);
    }

    private void setMapperForOtherUser(){
        this.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapping -> {
                    mapping.skip(UserDTO::setPassword);
                    mapping.skip(UserDTO::setEmail);
                    mapping.skip(UserDTO::setId);
                    mapping.skip(UserDTO::setStatus);
                    mapping.skip(UserDTO::setUpdated);
                    mapping.skip(UserDTO::setChats);
                });
    }

    private void setMapperForAuthUser(){
        TypeMap<User, UserDTO> typeMap = this.getTypeMap(User.class, UserDTO.class);

        if (typeMap == null){
            typeMap = this.createTypeMap(User.class, UserDTO.class);
        }

        typeMap.addMappings(mapping -> mapping.skip(UserDTO::setPassword))
                .setPostConverter(entityToDTOChatsConverter());
    }

   private Converter<User, UserDTO> entityToDTOChatsConverter(){
       return context -> {
         UserDTO destination = context.getDestination();
         Long userId = context.getSource().getId();
         List<Chat> source = userService.findAllChatsByUserId(userId);
         if (destination.getChats() == null){
             destination.setChats(new ArrayList<>());
         }
         for (Chat iterator : source){
            destination.getChats().add(map(iterator, ChatDTO.class));
         }
         return destination;
       };
   }

   public User userDTOToUserEntity(UserDTO userDTO){
       return Objects.isNull(userDTO) ? null : this.map(userDTO, User.class);
   }

}
