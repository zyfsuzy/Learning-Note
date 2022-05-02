package com.haodf.communal.cache.redis.locker;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * redis锁
 *
 * @author vincent
 * @version 1.0
 * @since 2019-04-03 16:22:46
 */
@Component
public final class RedisLocker implements Locker {

    private static Logger logger = LoggerFactory.getLogger(RedisLocker.class);

    @Autowired private RedisClientFactory redisClientFactory;

    private RedisClient redisClient;

    private static final ThreadLocal<Map<String, String>> lockKV = new ThreadLocal<>();

    private RedisClient getRedisClient() {
        if (null == redisClient) {
            synchronized (Object.class) {
                if (null == redisClient) {
                    redisClient = redisClientFactory.getRedisClient("default");
                }
            }
        }
        if (lockKV.get() == null) {
            Map<String, String> map = new ConcurrentHashMap<>();
            lockKV.set(map);
        }
        return redisClient;
    }

    @Override
    public boolean lock(String key) {
        return this.lock(key, 5);
    }

    @Override
    public boolean lock(String key, int seconds) {
        // 每次等待时间
        int waitTime = 20;
        int totalWaitTime = 0;
        // 5s
        int time = seconds * 1000;
        long timeMillis = System.currentTimeMillis();
        // value 存放线上名字和时间点
        String name = Thread.currentThread().getName();
        String value = name + timeMillis;
        while (totalWaitTime < time && StringUtils.isEmpty(getRedisClient().set(key, value, seconds))) {
            try {
                Thread.sleep(waitTime);
                totalWaitTime += waitTime;
            } catch (InterruptedException e) {
                //防御性容错
            }
        }
        if (totalWaitTime >= time) {
            throw new RuntimeException("Can not get lock for key \"" + key + "\".");
        }
        lockKV.get().put(key, value);
        return true;
    }

    @Override
    public boolean unlock(String key) {
        if (lockKV.get().containsKey(key)) {
            String value = lockKV.get().get(key);
            String redisValue = getRedisClient().get(key);
            // 检查是不是相等，防止错误释放
            if (value.equals(redisValue)) {
                if (getRedisClient().delete(key) > 0) {
                    lockKV.get().remove(key);
                    return true;
                }
                logger.error("unlock failed for key: {}", key);
                return false;
            }
        }
        return true;
    }

    /**
     * 尝试获取锁
     *
     * @param key         redis key
     * @param waitSeconds 尝试时长
     * @param seconds     获取锁之后锁定时间
     * @return 如果获取成功，则返回true，如果获取失败，则返回false
     */
    public boolean tryLock(String key, int waitSeconds, int seconds) {
        long sleepTime = 50;
        long waitTime = waitSeconds * 1000;
        long totalWaitTime = waitSeconds * 1000;
        String value = Thread.currentThread().getName() + System.currentTimeMillis();
        long startTime = System.currentTimeMillis();
        while (waitTime >= 0 && StringUtils.isEmpty(getRedisClient().set(key, value, seconds))) {
            try {
                Thread.sleep(sleepTime);
                long expendTime = System.currentTimeMillis() - startTime;
                //剩余时间
                long remainingTime = totalWaitTime - expendTime;
                sleepTime = (remainingTime > 20 && remainingTime < sleepTime) ? remainingTime : sleepTime;
                waitTime = remainingTime == 0 ? -1 : remainingTime;
            } catch (InterruptedException e) {
                //防御性容错
            }
        }
        if (waitTime < 0) {
            return false;
        }
        lockKV.get().put(key, value);
        return true;
    }
}
