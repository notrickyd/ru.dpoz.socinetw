package ru.dpoz.socinetw.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import ru.dpoz.socinetw.model.NewsFeedFriends;
import ru.dpoz.socinetw.repository.intf.NewsFeedRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.min;

@Component
public class AppCacheManagerImpl implements AppCacheManager
{
    @Autowired
    CacheManager cacheManager;
    @Autowired
    NewsFeedRepository newsFeed;

    @Override
    public List<NewsFeedCacheItem> getFeedCache(String cacheName, UUID userId)
    {
        Cache cache = cacheManager.getCache(cacheName);
        List<NewsFeedCacheItem> cachedFeeds = new ArrayList<>();
        cache.putIfAbsent(userId.toString(), cachedFeeds);
        cachedFeeds = (ArrayList<NewsFeedCacheItem>) cache.get(userId.toString()).get();
        return cachedFeeds;
    }

    @Override
    public List<NewsFeedCacheItem> fillFeedCache(String cacheName, UUID  userId)
    {
        Cache cache = cacheManager.getCache(cacheName);
        List<NewsFeedCacheItem> cachedFeeds = new ArrayList<>();
        List<NewsFeedFriends> feeds = newsFeed.getFriendsNewsFeed(userId);
        feeds.subList(0,  min(feeds.size(), 1000)).forEach(
                feed -> {
                    cachedFeeds.add(new NewsFeedCacheItem(Timestamp.from(Instant.now()), feed.getId()));
                }
        );
        cache.put(userId.toString(), cachedFeeds);
        return cachedFeeds;
    }

    @Override
    public List<NewsFeedCacheItem> appendFeedCache(String cacheName, UUID  userId, Long feedId)
    {
        Cache cache = cacheManager.getCache(cacheName);
        List<NewsFeedCacheItem> cachedFeeds = new ArrayList<>();
        cache.putIfAbsent(userId.toString(), cachedFeeds);
        cachedFeeds = (ArrayList<NewsFeedCacheItem>) cache.get(userId.toString()).get();
        cachedFeeds.add(0, new NewsFeedCacheItem(Timestamp.from(Instant.now()), feedId));
        cachedFeeds = new ArrayList<>(cachedFeeds.subList(0,  min(cachedFeeds.size(), 1000)));
        cache.put(userId.toString(), cachedFeeds);
        return cachedFeeds;
    }

    @Override
    public void evictFriendsFeedCache(UUID  userId)
    {
        Cache cache = cacheManager.getCache(CacheNames.FRIEND_FEED_DATA.name());
        cache.evictIfPresent(userId.toString());
    }

}
