package ru.dpoz.socinetw.rmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dpoz.socinetw.SocinetwApplication;
import ru.dpoz.socinetw.cache.AppCacheManager;
import ru.dpoz.socinetw.repository.intf.NewsFeedRepository;
import ru.dpoz.socinetw.repository.intf.UserFriends;
import ru.dpoz.socinetw.rmq.processors.RmqNewFeedProcessor;
import ru.dpoz.socinetw.rmq.processors.RmqPrepareFeedsIdCacheProcessor;
import ru.dpoz.socinetw.rmq.processors.RmqPrepareNewsFeedDataCache;
import ru.dpoz.socinetw.rmq.processors.RmqProcessor;

import java.util.Random;

/**
 * Класс определяет получетеля и метод receiveMessage, который будет вызываться при получении сообщений из очереди.
 */

public class RmqReceiver
{
    @Autowired
    AppCacheManager appCacheManager;
    @Autowired
    NewsFeedRepository newsFeed;
    @Autowired
    UserFriends userFriends;
    @Autowired
    RabbitTemplate rmq;

    public void receiveMessage(RmqEventMessage message)
    {
        Random rnd = new Random();
        Integer workId = rnd.nextInt(99999);
        SocinetwApplication.logger.info(String.format("[%d]Start work on {%s}", workId, message.toString()));
        RmqProcessor processor = null;
        switch (message.getType()){
            case POSTED_NEW_FEED -> processor = new RmqNewFeedProcessor(appCacheManager, userFriends, rmq);
            case PREPARE_FEED_ID_CACHE -> processor = new RmqPrepareFeedsIdCacheProcessor(appCacheManager);
            case PREPARE_FEED_DATA_CACHE -> processor = new RmqPrepareNewsFeedDataCache(appCacheManager, newsFeed);
        }
        assert processor != null;
        processor.processMessage(message);
        SocinetwApplication.logger.info(String.format("[%d]End work", workId));
    }

}
