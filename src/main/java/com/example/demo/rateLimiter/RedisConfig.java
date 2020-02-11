package com.example.demo.rateLimiter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 
@Configuration
public class RedisConfig {
 
	@Value("${spring.redis.host}")
	private String host;
 
	@Value("${spring.redis.port}")
	private int port;
 
	@Value("${spring.redis.password}")
	private String password;
 
	@Value("${spring.redis.timeout}")
	private int timeout;
 
	@Value("${spring.redis.pool.maxIdle}")
	private int maxIdle;
 
	@Value("${spring.redis.pool.minIdle}")
	private int minIdle;
 
	@Value("${spring.redis.pool.max-wait}")
	private long maxWaitMillis;
 
	@Value("${spring.redis.pool.max-active}")
	private int maxActive;
 
	@Bean
	public JedisPool redisPoolFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setMaxTotal(maxActive);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, host,port,timeout,password);
		return jedisPool;
	}
}