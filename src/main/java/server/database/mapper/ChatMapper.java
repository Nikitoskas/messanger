package server.database.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import server.database.dto.ChatDTO;
import server.database.dto.MessageDTO;
import server.database.entity.Chat;
import server.database.service.impl.MessageServiceImpl;
import server.database.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ChatMapper extends ModelMapper{

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private UserServiceImpl userService;

    public List<ChatDTO> ListEntityToDTO(List<Chat> chats){
        List<ChatDTO> dtoChats = new ArrayList<>();
        for (Chat chat : chats) {
            dtoChats.add(standardChatEntityToDTOMapper(chat));
        }
        return dtoChats;
    }


    @Bean
    public ChatDTO standardChatEntityToDTOMapper(Chat chat){
        TypeMap<Chat, ChatDTO> typeMap = this.getTypeMap(Chat.class, ChatDTO.class);

        if (typeMap == null){
            typeMap = this.createTypeMap(Chat.class, ChatDTO.class);
        }

        typeMap.setPostConverter(standardConverter());

        return Objects.isNull(chat) ? null : this.map(chat, ChatDTO.class);
    }

    private Converter<Chat, ChatDTO> standardConverter(){
        return context -> {
          ChatDTO destination = context.getDestination();
          Long chatId = context.getSource().getId();
          MessageDTO lastMessage = messageMapper.entityToDTO(messageService.getLastMessage(chatId));
          destination.setLastMessage(lastMessage);
          return destination;
        };
    }

    public Chat dtoToEntity(ChatDTO chatDTO){
        return Objects.isNull(chatDTO) ? null : this.map(chatDTO, Chat.class);
    }

}
