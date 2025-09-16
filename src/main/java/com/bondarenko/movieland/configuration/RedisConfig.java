package com.bondarenko.movieland.configuration;

import com.bondarenko.movieland.service.cache.security.TokenBlacklist;
import com.bondarenko.movieland.service.security.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(); // auto get host/port з application.yml
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public TokenBlacklist tokenBlacklist(StringRedisTemplate redisTemplate, TokenService tokenService) {
        return new TokenBlacklist(tokenService, redisTemplate);
    }
}
