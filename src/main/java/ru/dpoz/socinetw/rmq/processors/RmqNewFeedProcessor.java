package ru.dpoz.socinetw.rmq.processors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dpoz.socinetw.cache.AppCacheManager;
import ru.dpoz.socinetw.cache.CacheNames;
import ru.dpoz.socinetw.repository.intf.UserFriends;
import ru.dpoz.socinetw.rmq.EventMessageType;
import ru.dpoz.socinetw.rmq.RmqConfig;
import ru.dpoz.socinetw.rmq.RmqEventMessage;

import java.util.List;
import java.util.UUID;

public class RmqNewFeedProcessor extends RmqProcessor
{
    AppCacheManager appCacheManager;
    UserFriends userFriends;
    RabbitTemplate rmq;

    public RmqNewFeedProcessor(AppCacheManager appCacheManager, UserFriends userFriends, RabbitTemplate rmq)
    {
        super();
        this.appCacheManager = appCacheManager;
        this.userFriends = userFriends;
        this.rmq = rmq;
    }

    @Override
    public void processMessage(RmqEventMessage message)
    {
        List<UUID> friends = userFriends.get(message.getUserId());
        friends.forEach(
                fid -> {
                    appCacheManager.appendFeedCache(CacheNames.FRIEND_FEED_IDS.name(), fid, (Long) message.getObject());
                    appCacheManager.evictFriendsFeedCache(fid);
                    RmqEventMessage event = new RmqEventMessage(EventMessageType.PREPARE_FEED_DATA_CACHE, fid, null);
                    rmq.convertAndSend(RmqConfig.queueName, event);
                }
        );
    }
}
