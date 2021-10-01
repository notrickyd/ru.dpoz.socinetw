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

@Getter @Setter
@Entity @Data
@Table (name = "user_secret")
public class UserSecretEntity implements Serializable
{
    @Id
    @Column(nullable = false)
    String username;

    @Id
    @Column(nullable = false)
    String password;

    @Column(columnDefinition = "varchar(36) not null")
    UUID userId;
}
