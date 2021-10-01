package ru.dpoz.socnetw.model;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "hobby")
public class HobbyEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "smallint")
    Short hobbyId;

    @Column(columnDefinition = "varchar(30) not null")
    String name;

}
