package com.mfun.javaconfigdemo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author: JMing
 * @date: 2019/11/19 16:35
 * @description: 缓存配置
 */

@Configuration
// @EnableCaching默认模式为AdviceMode.PROXY，，是基于Spring AOP，
// 会造成类内部调用同类方法时，@Cacheable等缓存注解不生效，
// 解决方法：你可以使用@EnableCaching(mode=AdviceMode.ASPECTJ),
// 启用AdviceMode.ASPECTJ模式，同时需要在ClassPath中添加 spring-aspects.jar，
// 注意此模式的注解只能作用于类上，接口上将不生效。
// 具体可看https://www.cnblogs.com/cyhbyw/p/8615816.html
@EnableCaching // 启用缓存注解支持，如@Cacheable
public class CachingConfig {
    @Value("${redis.hostname:}")
    private String host;

    @Value("${redis.port:6379}")
    private int port;

    @Value("${redis.database:0}")
    private int database;

    @Value("${redis.timeout:2000}")
    private int timeout;

    @Value("${redis.pool.maxActive:8}")
    private int poolMaxActive;

    @Value("${redis.pool.maxIdle:8}")
    private int poolMaxIdle;

    @Value("${redis.pool.maxWait:-1}")
    private long poolMaxWait;

    @Value("${redis.pool.testOnBorrow:false}")
    private boolean poolTestOnBorrow;

    @Value("${redis.defaultExpiration:0}")
    private long defaultExpiration;

    /**
     * Redis缓存管理器，主要用于注解式缓存，如@Cacheable
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setDefaultExpiration(defaultExpiration);
        return cacheManager;
    }

    /**
     * Redis模板
     */
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 使用Jackson2JsonRedisSerializer替换默认的序列化规则
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 调用后初始化方法，没有它将抛出异常
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * Redis连接工厂，此处配置的为单机模式，
     * 如需要高可用，可使用哨兵模式Sentinel或集群模式Cluster
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisPoolConfig());
        connectionFactory.setDatabase(database);
        connectionFactory.setHostName(host);
        connectionFactory.setPort(port);
        connectionFactory.setTimeout(timeout);

        // 调用后初始化方法，没有它将抛出异常
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    /**
     * Redis连接池设置
     */
    private JedisPoolConfig redisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // redis最大实例数
        poolConfig.setMaxTotal(poolMaxActive);
        // 连接池中最多可空闲maxIdle个连接，
        // 表示即使没有数据库连接时依然可以保持maxIdle个空闲的连接，
        // 而不被清除，随时处于待命状态。
        poolConfig.setMaxIdle(poolMaxIdle);
        // 最大等待时间:当没有可用连接时,
        // 连接池等待连接被归还的最大时间(以毫秒计数),
        // 超过时间则抛出异常
        poolConfig.setMaxWaitMillis(poolMaxWait);
        // 是否在获取连接的时候检查有效性,如果检验失败,则从池中去除连接并尝试取出另一个
        poolConfig.setTestOnBorrow(poolTestOnBorrow);

        return poolConfig;
    }
}
