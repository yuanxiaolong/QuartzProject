package com.yxl.demo.quartzProject.dao.redis;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * Created by huanglichao on 15/6/8.
 */
public class RedisDAO {
    @Resource
    private ShardedJedisPool shardedJedisPool;

    public String get(String key)  {
        ShardedJedis jedis =  shardedJedisPool.getResource();
        return jedis.get(key);
    }
}
