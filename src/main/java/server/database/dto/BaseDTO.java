package server.database.dto;

import lombok.Data;
import server.database.entity.Status;

import java.util.Date;

@Data
public class BaseDTO {
    private Long id;
    private Date created;
    private Date updated;
    private Status status;
}
