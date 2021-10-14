package ru.dpoz.socinetw.rmq.processors;

import ru.dpoz.socinetw.cache.AppCacheManager;
import ru.dpoz.socinetw.cache.CacheNames;
import ru.dpoz.socinetw.cache.NewsFeedCacheItem;
import ru.dpoz.socinetw.repository.intf.NewsFeedRepository;
import ru.dpoz.socinetw.rmq.RmqEventMessage;

import java.util.List;

/** Обновляем кеш с содержимым новостей друзей для пользователя */
public class RmqPrepareNewsFeedDataCache extends RmqProcessor
{
    AppCacheManager appCacheManager;
    NewsFeedRepository newsFeed;

    public RmqPrepareNewsFeedDataCache(AppCacheManager appCacheManager, NewsFeedRepository newsFeed)
    {
        super();
        this.appCacheManager = appCacheManager;
        this.newsFeed = newsFeed;
    }

    @Override
    public void processMessage(RmqEventMessage message)
    {
        List<NewsFeedCacheItem> cachedFeeds = appCacheManager.getFeedCache(CacheNames.FRIEND_FEED_IDS.name(), message.getUserId());
        newsFeed.getNewsFeed(message.getUserId(), cachedFeeds);
    }
}
