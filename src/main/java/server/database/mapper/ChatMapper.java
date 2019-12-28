package server.database.mapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import server.database.dto.ChatDTO;
import server.database.dto.MessageDTO;
import server.database.entity.Chat;
import server.database.service.impl.MessageServiceImpl;
import server.database.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@NoArgsConstructor
@Configuration
public class ChatMapper{

    private MessageMapper messageMapper;
    private MessageServiceImpl messageService;
    private UserServiceImpl userService;

    private ModelMapper standardMapper;

    @Autowired
    public ChatMapper(MessageMapper messageMapper, MessageServiceImpl messageService, UserServiceImpl userService) {
        this.messageMapper = messageMapper;
        this.messageService = messageService;
        this.userService = userService;

        this.standardMapper = initStandardMapper();

    }

    public List<ChatDTO> standardListEntityToDTO(List<Chat> chats){
        List<ChatDTO> dtoChats = new ArrayList<>();
        for (Chat chat : chats) {
            dtoChats.add(standardChatMapperEntityToDto(chat));
        }
        return dtoChats;
    }


    public ChatDTO standardChatMapperEntityToDto(Chat chat){
        return Objects.isNull(chat) ? null : standardMapper.map(chat, ChatDTO.class);
    }

    private ModelMapper initStandardMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(Chat.class, ChatDTO.class)
                .setPostConverter(standardConverter());

        mapper.createTypeMap(ChatDTO.class, Chat.class);

        return mapper;
    }

    private Converter<Chat, ChatDTO> standardConverter(){
        return context -> {
          ChatDTO destination = context.getDestination();
          Long chatId = context.getSource().getId();
          MessageDTO lastMessage = messageMapper.standardEntityToDTO(messageService.getLastMessage(chatId));
          destination.setLastMessage(lastMessage);
          return destination;
        };
    }

    public Chat standardDtoToEntity(ChatDTO chatDTO){
        return Objects.isNull(chatDTO) ? null : standardMapper.map(chatDTO, Chat.class);
    }

}
