package ru.dpoz.socinetw.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Класс описывает промежуточный не персистеный датасет, содержащий данные новостной ленты друзей
 * Не является сущностью БД.
 */

@NoArgsConstructor
@Getter
@Setter
@Data
public class NewsFeedFriends implements Serializable
{
    String message;
    Date timestampx;
    String name;
    UUID userId;
    Long id;

    public NewsFeedFriends(String message, Date timestampx, String name, UUID userId, Long id)
    {
        super();
        setMessage(message);
        setName(name);
        setTimestampx(timestampx);
        setUserId(userId);
        setId(id);
    }
}
