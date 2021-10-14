package ru.dpoz.socinetw.controller.api;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dpoz.socinetw.cache.AppCacheManager;
import ru.dpoz.socinetw.cache.CacheNames;
import ru.dpoz.socinetw.cache.NewsFeedCacheItem;
import ru.dpoz.socinetw.model.NewsFeedEntity;
import ru.dpoz.socinetw.model.NewsFeedFriends;
import ru.dpoz.socinetw.repository.intf.NewsFeedRepository;
import ru.dpoz.socinetw.response.BasicResponseEntity;
import ru.dpoz.socinetw.response.NeedsAuthResponseEntity;
import ru.dpoz.socinetw.response.OkResponseEntity;
import ru.dpoz.socinetw.response.RedirectResponseEntity;
import ru.dpoz.socinetw.rmq.EventMessageType;
import ru.dpoz.socinetw.rmq.RmqConfig;
import ru.dpoz.socinetw.rmq.RmqEventMessage;
import ru.dpoz.socinetw.security.UserSecretDetails;
import ru.dpoz.socinetw.service.intf.AuthService;

import java.util.List;
import java.util.UUID;

/**
 * Класс описывает API для создания и получения новостной ленты
 * */

@RestController
@RequestMapping("/api/v1/feed")
public class NewsFeedController
{
    @Autowired
    private NewsFeedRepository newsFeed;
    @Autowired
    AuthService authService;
    @Autowired
    AppCacheManager appCacheManager;
    @Autowired
    RabbitTemplate rmq;


    /**
     * Добавление новости пользователем. При этом отправляется задание на обновление кешей подписчиков.
     *
     * @return BasicResponseEntity
     * @param message новость
     */
    @PostMapping("/add")
    public BasicResponseEntity add(@RequestBody NewsFeedEntity message)
    {
        UserSecretDetails user = this.authService.getCurrentUser();
        if (user != null) {
            UUID userId = user.getUserSecretEntity().getUserId();;
            message.setUserId(userId);
            newsFeed.save(message);
            RmqEventMessage event = new RmqEventMessage(EventMessageType.POSTED_NEW_FEED, userId, message.getId());
            rmq.convertAndSend(RmqConfig.getQueueName(), event);

            return new RedirectResponseEntity("/user");
        }
        return new NeedsAuthResponseEntity();
    }


    /**
     * Возвращает собственную ленту новостей (в профиле пользователя). Кешируется.
     *
     * @return BasicResponseEntity
     */
    @GetMapping("/self")
    public BasicResponseEntity self()
    {
        UserSecretDetails user = this.authService.getCurrentUser();
        if (user != null) {
            Iterable<NewsFeedEntity> news = newsFeed.findAllByUserIdOrderByTimestampxDesc(user.getUserSecretEntity().getUserId());
            return new OkResponseEntity("",null);
        }
        return new NeedsAuthResponseEntity();
    }

    /**
     * Возвращает новостную ленту друзей. При первом обращении, если кеш пуст, то автоматически прогревается.
     *
     * @return BasicResponseEntity
     */

    @GetMapping("/friends")
    public BasicResponseEntity friends()
    {
        UserSecretDetails user = this.authService.getCurrentUser();
        if (user != null) {
            UUID userId = user.getUserSecretEntity().getUserId();
            List<NewsFeedCacheItem> friendFeeds = appCacheManager.getFeedCache(CacheNames.FRIEND_FEED_IDS.name(), userId);
             if (friendFeeds.size() == 0)
                friendFeeds = appCacheManager.fillFeedCache(CacheNames.FRIEND_FEED_IDS.name(), userId);
            List<NewsFeedFriends> friendNews = newsFeed.getNewsFeed(userId, friendFeeds);
            return new OkResponseEntity("", friendNews);
        }
        return new NeedsAuthResponseEntity();
    }

}
