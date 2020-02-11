package com.example.demo.rateLimiter;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;

@Component
public class RedisRateLimiterFactory {
	@Autowired
	private JedisPool jedisPool;
	private final WeakHashMap<String, RedisRateLimiter> limiterMap = new WeakHashMap<String, RedisRateLimiter>();
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public RedisRateLimiter get(String keyPrefix, TimeUnit timeUnit, int permits) {
		RedisRateLimiter redisRateLimiter = null;
		try {
			lock.readLock().lock();
			if (limiterMap.containsKey(keyPrefix)) {
				redisRateLimiter = limiterMap.get(keyPrefix);
			}
		} finally {
			lock.readLock().unlock();
		}

		if (redisRateLimiter == null) {
			try {
				lock.writeLock().lock();
				if (limiterMap.containsKey(keyPrefix)) {
					redisRateLimiter = limiterMap.get(keyPrefix);
				}
				if (redisRateLimiter == null) {
					redisRateLimiter = new RedisRateLimiter(jedisPool, timeUnit, permits);
					limiterMap.put(keyPrefix, redisRateLimiter);
				}
			} finally {
				lock.writeLock().unlock();
			}
		}
		return redisRateLimiter;
	}
}