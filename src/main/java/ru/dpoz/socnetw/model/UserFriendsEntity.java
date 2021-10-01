package ru.dpoz.socnetw.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Data @Entity
@Getter @Setter
@Table(name = "user_friends")
public class UserFriendsEntity implements Serializable
{
    @Id
    @Column(columnDefinition = "varchar(36) not null")
    UUID userId;
    @Id
    @Column(columnDefinition = "varchar(36) not null")
    UUID friendId;
}
