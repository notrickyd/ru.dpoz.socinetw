package ru.dpoz.socinetw.repository.intf;

import ru.dpoz.socinetw.cache.NewsFeedCacheItem;
import ru.dpoz.socinetw.model.NewsFeedFriends;

import java.util.List;
import java.util.UUID;

public interface NewsFeedCustom
{
    List<NewsFeedFriends> getFriendsNewsFeed(UUID userId);

    List<NewsFeedFriends> getNewsFeed(UUID userId, List<NewsFeedCacheItem> feedIdList);

}
