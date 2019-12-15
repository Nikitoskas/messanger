package server.database.mapper;

import org.modelmapper.ModelMapper;
import server.database.dto.ChatDTO;
import server.database.entity.Chat;

import java.util.Objects;

public class ChatMapper {

    ModelMapper modelMapper = new ModelMapper();

    public ChatDTO entityToDTO(Chat chat){
        return Objects.isNull(chat) ? null : modelMapper.map(chat, ChatDTO.class);
    }

    public Chat dtoToEntity(ChatDTO chatDTO){
        return Objects.isNull(chatDTO) ? null : modelMapper.map(chatDTO, Chat.class);
    }

}
