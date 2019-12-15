package server.database.dto;

import lombok.Data;

@Data
public class ChatDTO extends BaseDTO {
    private UserDTO creator;
    private String name;
}
