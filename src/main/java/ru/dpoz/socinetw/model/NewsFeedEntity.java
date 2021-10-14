package ru.dpoz.socinetw.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Сущность описывает новостную лентну
 *
 */

@Entity
@Data
@Table(name = "news_feed")
@Getter
@Setter
/** Данная аннотация используется для сереализации/десериализации в промежуточную не персистентную сущность {@link NewsFeedFriends}
 * при имплементации кастомного метода {@link ru.dpoz.socinetw.repository.intf.NewsFeedCustom#getFriendsNewsFeed(UUID)} в
 * {@link ru.dpoz.socinetw.repository.intf.NewsFeedRepository}.
 * Можно было бы обойтись без этого, если бы jpa projections были бы serializable. */
@SqlResultSetMapping(
        name="NewsFeedCustomImpl_FriendsQuery",
        classes={
                @ConstructorResult(
                        targetClass = NewsFeedFriends.class,
                        columns={
                                @ColumnResult(name="message", type = String.class),
                                @ColumnResult(name="timestampx", type = Date.class),
                                @ColumnResult(name="name", type = String.class),
                                @ColumnResult(name="user_id", type = UUID.class),
                                @ColumnResult(name="id", type = Long.class)
                        }
                )
        }
)
public class NewsFeedEntity implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    Long id;

    @Column(length = 16)
    UUID userId;

    @Column()
    String message;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    Timestamp timestampx;

}
