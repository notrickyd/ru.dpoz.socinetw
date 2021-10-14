package ru.dpoz.socinetw.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Класс для сериализации/десериализации данных из/в кеш по ID новостей newsFeedId.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsFeedCacheItem implements Serializable
{
    Timestamp newsFeedDt;
    Long newsFeedId;
}
