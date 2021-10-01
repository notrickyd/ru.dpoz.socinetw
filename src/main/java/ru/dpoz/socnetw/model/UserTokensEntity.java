package ru.dpoz.socnetw.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Getter @Setter
@Entity @Data
@Table(name = "user_tokens")
public class UserTokensEntity implements Serializable
{
    @Id
    @GeneratedValue
    @Column(columnDefinition = "varchar(36) not null")
    UUID token;
    @Column(columnDefinition = "varchar(36) not null")
    UUID userId;
    @Column
    Timestamp expires;
}
