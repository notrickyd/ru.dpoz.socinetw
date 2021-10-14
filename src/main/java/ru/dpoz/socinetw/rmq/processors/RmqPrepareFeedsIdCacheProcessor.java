package ru.dpoz.socinetw.rmq.processors;

import ru.dpoz.socinetw.cache.AppCacheManager;
import ru.dpoz.socinetw.cache.CacheNames;
import ru.dpoz.socinetw.rmq.RmqEventMessage;

/** Обновляем кеш связей пользователя с id новостей */
public class RmqPrepareFeedsIdCacheProcessor extends RmqProcessor
{
    AppCacheManager appCacheManager;

    public RmqPrepareFeedsIdCacheProcessor(AppCacheManager appCacheManager)
    {
        super();
        this.appCacheManager = appCacheManager;
    }

    @Override
    public void processMessage(RmqEventMessage message)
    {
        appCacheManager.fillFeedCache(CacheNames.FRIEND_FEED_IDS.name(), message.getUserId());
    }
}
