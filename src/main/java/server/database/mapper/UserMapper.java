package server.database.mapper;


import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import server.database.dto.ChatDTO;
import server.database.dto.UserDTO;
import server.database.entity.Chat;
import server.database.entity.User;
import server.database.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Objects;

public class UserMapper extends ModelMapper{

    @Autowired
    private UserServiceImpl userService;


   public UserDTO ownerUserEntityToUserDTO(User user){
       this.createTypeMap(User.class, UserDTO.class)
               .addMappings(mapping -> mapping.skip(UserDTO::setPassword)).setPostConverter(entityToDTOChatsConverter(user.getId()));
       return Objects.isNull(user) ? null : this.map(user, UserDTO.class);
//       UserDTO userDTO = new UserDTO();
//       userDTO.setUsername(user.getUsername());
//       userDTO.setPassword(user.getPassword());
//       userDTO.setEmail(user.getEmail());
//       userDTO.setFirstName(user.getFirstName());
//       userDTO.setLastName(user.getLastName());
//
//       userDTO.setChats(user.getUsername());
//       userDTO.setUsername(user.getUsername());

   }

   private Converter<User, UserDTO> entityToDTOChatsConverter(Long userId){
       return context -> {
         UserDTO destination = context.getDestination();
         List<Chat> source = userService.findAllChatsByUserId(userId);

         for (Chat iterator : source){
            destination.getChats().add(map(iterator, ChatDTO.class));
         }
         map(context.getSource(), destination);
         return context.getDestination();
       };
   }

   public User userDTOToUserEntity(UserDTO userDTO){
       return Objects.isNull(userDTO) ? null : this.map(userDTO, User.class);
   }

   public UserDTO userToOtherUserDTO(User user){
       this.createTypeMap(User.class, UserDTO.class)
               .addMappings(mapping -> {
                   mapping.skip(UserDTO::setPassword);
                   mapping.skip(UserDTO::setEmail);
                   mapping.skip(UserDTO::setId);
                   mapping.skip(UserDTO::setStatus);
                   mapping.skip(UserDTO::setUpdated);
               });
       return Objects.isNull(user) ? null : this.map(user, UserDTO.class);
   }

}
