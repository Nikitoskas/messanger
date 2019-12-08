package server.database.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "chats")
public class Chat extends BaseEntity{

//    @Column(name = "creator_id")
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "creator_id")
//    private User creator;

    @Column(name = "name")
    private String name;

}
