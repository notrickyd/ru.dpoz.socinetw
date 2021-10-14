package ru.dpoz.socinetw.cache;

public enum CacheNames
{
    /* Кеш ID новостей друзей пользователя без данных */
    FRIEND_FEED_IDS ("friendsNewsFeed"),
    /* Кеш ленты новостей друзей пользователя с данными */
    FRIEND_FEED_DATA("getFriendsNewsFeedData"),
    /* Кеш собственной ленты новостей с данными */
    SELF_FEED_DATA("getSelfNewsData");

    private CacheNames(String name){}
}
