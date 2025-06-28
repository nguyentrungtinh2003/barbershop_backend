package com.TrungTinhBackend.barbershop_backend.Config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
    public RedisCacheManager manageRedis(RedisConnectionFactory redisConnectionFactory) {
      RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMillis(5)).disableCachingNullValues();

      return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
    }
}
