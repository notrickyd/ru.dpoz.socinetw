package ru.dpoz.socinetw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "user_hobbies")
@Data
@AllArgsConstructor
@Getter
public class UserHobbiesEntity implements Serializable
{

    public UserHobbiesEntity(){super();}

    @Id
    @Column(columnDefinition = "binary(16) not null")
    UUID userId;

    @Id
    @Column(nullable = false)
    Short hobbyId;

}
