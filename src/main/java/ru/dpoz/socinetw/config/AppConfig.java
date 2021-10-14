package ru.dpoz.socinetw.config;

import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class AppConfig
{
    @Bean
    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer()
    {
        return clientConfigurationBuilder -> {
            if (clientConfigurationBuilder.build().isUseSsl()) {
                clientConfigurationBuilder.useSsl().disablePeerVerification();
            }
        };
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration()
    {
        return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader())
                .entryTtl(Duration.ofMinutes(3))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

/*    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration(CacheNames.FRIEND_FEED_DATA.name(),
                    RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(3)))
                .withCacheConfiguration(CacheNames.SELF_FEED_DATA.name(),
                    RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)))
                .withCacheConfiguration(CacheNames.FRIEND_FEED_IDS.name(),
                    RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(3)));
    }*/
}
