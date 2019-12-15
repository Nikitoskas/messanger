package server.database.mapper;

import org.modelmapper.ModelMapper;
import server.database.dto.MessageDTO;
import server.database.dto.UserDTO;
import server.database.entity.Message;
import server.database.entity.User;

import java.util.Objects;

public class MessageMapper {

    ModelMapper modelMapper = new ModelMapper();


    public MessageDTO entityToDTO(Message message){

        return Objects.isNull(message) ? null : modelMapper.map(message, MessageDTO.class);
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

    public Message dtoToEntity(MessageDTO messageDTO){
        return Objects.isNull(messageDTO) ? null : modelMapper.map(messageDTO, Message.class);
    }

}
