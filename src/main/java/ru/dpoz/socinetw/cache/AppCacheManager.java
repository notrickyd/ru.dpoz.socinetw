package ru.dpoz.socinetw.cache;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface AppCacheManager
{

    /** Получить кеш ID новостей пользователя */
    List<NewsFeedCacheItem> getFeedCache(String cacheName, UUID userId);

    /** Разогрев кеша пользователя */
    List<NewsFeedCacheItem> fillFeedCache(String cacheName, UUID  userId);

    /** Добавить ID новости в кеш */
    List<NewsFeedCacheItem> appendFeedCache(String cacheName, UUID  userId, Long feedId);

    /** Обнулить кеш */
    void evictFriendsFeedCache(UUID  userId);

}
