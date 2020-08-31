package com.epam.songservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.SocketUtils;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
@Profile("test")
public class RedisTestConfiguration {

    private int port;
    private RedisServer redisServer;

    public RedisTestConfiguration() throws IOException {
        port = SocketUtils.findAvailableTcpPort();
        redisServer = RedisServer.builder()
                .port(port)
                .setting("maxheap 128M")
                .build();
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate() {
//        final RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        template.setDefaultSerializer(redisSerializer);
//        return template;
//    }

}
