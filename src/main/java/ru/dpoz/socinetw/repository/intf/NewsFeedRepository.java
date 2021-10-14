package ru.dpoz.socinetw.repository.intf;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.CrudRepository;
import ru.dpoz.socinetw.cache.NewsFeedCacheItem;
import ru.dpoz.socinetw.model.NewsFeedEntity;
import ru.dpoz.socinetw.model.NewsFeedFriends;

import java.util.List;
import java.util.UUID;

public interface NewsFeedRepository extends CrudRepository<NewsFeedEntity, Long>, NewsFeedCustom
{
    /** Кешируем собственную ленту новостей */
    @Cacheable(value = "getSelfNewsData", key = "#userId")
    List<NewsFeedEntity> findAllByUserIdOrderByTimestampxDesc(UUID userId);

    /** Получаем ленту новостей друзей пользователя */
    @Override
    List<NewsFeedFriends> getFriendsNewsFeed(UUID userId);

    /** Сбрасываем кеш своей ленты по юзер-ид */
    @Caching(evict = {
            @CacheEvict(value = "getSelfNewsData", key = "#s.userId"),
    })
    @Override
    <S extends NewsFeedEntity> S save(S s);

    /** Получаем ленту новостей друзей по ID-кешу новостей и авто-кешируем данные новостей */
    //@Cacheable(value = "getFriendsNewsFeedData", key = "#userId")
    @Override
    List<NewsFeedFriends> getNewsFeed(UUID userId, List<NewsFeedCacheItem> feedIdList);

}
