package server.database.mapper;

import lombok.Data;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.database.dto.MessageDTO;
import server.database.dto.UserDTO;
import server.database.entity.Message;
import server.database.entity.User;
import server.database.service.UserService;
import server.database.service.impl.UserServiceImpl;

import java.util.Objects;
@Component
@Data
public class MessageMapper extends ModelMapper {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserMapper userMapper;

    public MessageDTO entityToDTO(Message message){
        TypeMap<Message, MessageDTO> typeMap = this.getTypeMap(Message.class, MessageDTO.class);

        if (typeMap == null){
            typeMap = this.createTypeMap(Message.class, MessageDTO.class);
        }

        typeMap.setPostConverter(addAuthor());
        return Objects.isNull(message) ? null : this.map(message, MessageDTO.class);
    }

    private Converter<Message, MessageDTO> addAuthor(){
        return context -> {
            MessageDTO destination = context.getDestination();
            User author = userService.findById(context.getSource().getAuthor());
            destination.setAuthor(userMapper.shortUserEntityToUserDTO(author));
            return destination;
        };
    }

    public Message dtoToEntity(MessageDTO messageDTO){
        return Objects.isNull(messageDTO) ? null : this.map(messageDTO, Message.class);
    }

}
