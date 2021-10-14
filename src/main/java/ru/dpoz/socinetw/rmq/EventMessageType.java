package ru.dpoz.socinetw.rmq;

/** Типы событий/заданий в очереди */
public enum EventMessageType
{
    /* Событие: добавлена новость */
    POSTED_NEW_FEED,
    /* Задание: прогреть ID-кеш новостей без данных */
    PREPARE_FEED_ID_CACHE,
    /* Задание: прогреть кеш новостей с данными */
    PREPARE_FEED_DATA_CACHE
}
