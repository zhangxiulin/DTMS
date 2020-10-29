package zh.shawn.project.framework.boot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import zh.shawn.project.framework.boot.service.redis.PooledRedisUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 基于redis的缓存服务
 * @author: zhangxiulin
 * @time: 2020/8/3 13:59
 */
public class RedisService {

    private Logger log = LoggerFactory.getLogger(RedisService.class);

    public static Map<String, PooledRedisUtil> CACHES = new HashMap<>(100, 0.9f);
    public static Map<String, List> CACHE_KEYS = new HashMap<>(100, 0.9f);

//    public static Integer maxTotal = 20;
//    public static Integer maxIdle = 10;
//    public static Integer minIdle = 2;
//    public static Integer maxWaitMillis = 2;
//    public static Boolean testOnBorrow = true;
//    public static Boolean testOnReturn = true;
//    public static Integer timeBetweenEvictionRunsMillis = 2;
//    public static Boolean testWhileIdle = true;
//    public static Integer numTestsPerEvictionRun = 2;
//    public static String redisIp = "";
//    public static Integer redisPort = 6379;

    public static Integer DEFAULT_MAX_TOTAL = 20;
    public static Integer DEFAULT_MAX_IDLE = 10;
    public static Integer DEFAULT_MIN_IDLE = 2;
    public static Integer DEFAULT_MAX_WAIT_MILLIS = 2;
    public static Boolean DEFAULT_TEST_ON_BORROW = true;
    public static Boolean DEFAULT_TEST_ON_RETURN = true;
    public static Integer DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 2;
    public static Boolean DEFAULT_TEST_WHILE_IDLE = true;
    public static Integer DEFAULT_NUM_TESTS_PER_EVICTION_RUN = 2;
    public static String DEFAULT_REDIS_IP = "";
    public static Integer DEFAULT_REDIS_PORT = 6379;

    public void init(List caches, List cacheKeys) throws Exception{
        for (Object configData : caches){
            Map cacheConfig = (Map) configData;

            //////
            String cacheName = cacheConfig.get("cache_name").toString();
            JedisPoolConfig config = new JedisPoolConfig();

            config.setMaxTotal(cacheConfig.get("pool.maxTotal") != null ? Integer.valueOf(cacheConfig.get("pool.maxTotal").toString()) : DEFAULT_MAX_TOTAL);
            config.setMaxIdle(cacheConfig.get("pool.maxIdle") != null ? Integer.valueOf(cacheConfig.get("pool.maxIdle").toString()) : DEFAULT_MAX_IDLE);
            config.setMinIdle(cacheConfig.get("pool.minIdle") != null ? Integer.valueOf(cacheConfig.get("pool.minIdle").toString()) : DEFAULT_MIN_IDLE);
            config.setMaxWaitMillis(cacheConfig.get("pool.maxWaitMillis") != null ? Integer.valueOf(cacheConfig.get("pool.maxWaitMillis").toString()) : DEFAULT_MAX_WAIT_MILLIS);
            config.setTestOnBorrow(cacheConfig.get("pool.testOnBorrow") != null ? Boolean.valueOf(cacheConfig.get("pool.testOnBorrow").toString()) : DEFAULT_TEST_ON_BORROW);
            config.setTestOnReturn(cacheConfig.get("pool.testOnReturn") != null ? Boolean.valueOf(cacheConfig.get("pool.testOnReturn").toString()) : DEFAULT_TEST_ON_RETURN);
            config.setTimeBetweenEvictionRunsMillis(cacheConfig.get("pool.timeBetweenEvictionRunsMillis") != null ? Integer.valueOf(cacheConfig.get("pool.timeBetweenEvictionRunsMillis").toString()) : DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
            config.setTestWhileIdle(cacheConfig.get("pool.testWhileIdle") != null ? Boolean.valueOf(cacheConfig.get("pool.testWhileIdle").toString()) : DEFAULT_TEST_WHILE_IDLE);
            config.setNumTestsPerEvictionRun(cacheConfig.get("pool.numTestsPerEvictionRun") != null ? Integer.valueOf(cacheConfig.get("pool.numTestsPerEvictionRun").toString()) : DEFAULT_NUM_TESTS_PER_EVICTION_RUN);

            //连接池耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时，会抛出超时异常，默认为true
            config.setBlockWhenExhausted(true);

            //这里超时时间是2s
            JedisPool pool = new JedisPool(config,
                    cacheConfig.get("ip").toString(),
                    cacheConfig.get("port") != null ? Integer.valueOf(cacheConfig.get("port").toString()) : DEFAULT_REDIS_PORT,
                    1000*2);

            PooledRedisUtil pooledRedisUtil = new PooledRedisUtil(pool);
            RedisService.CACHES.put(cacheName, pooledRedisUtil);
            log.debug("已配置缓存名称[" + cacheName + "]");
        }

        if (cacheKeys != null){
            for (Object configData : cacheKeys){
                log.debug("缓存字段配置，共计：{}，当前：{}", cacheKeys.size(), configData.toString());
                Map cacheConfig = (Map) configData;
                if (cacheConfig.containsKey("cache_keys")){
                    if (cacheConfig.get("cache_keys") != null){
                        log.debug("已获取字段配置详情：" + cacheConfig.get("cache_keys").toString());
                        List keys = (List) cacheConfig.get("cache_keys");
                        List kv = new ArrayList<>();
                        for (Object key : keys){
                            kv.add(key.toString());
                        }
                        RedisService.CACHE_KEYS.put(cacheConfig.get("cache_name").toString(), kv);
                        keys = null;
                        kv = null;
                    }
                }
            }
        }
        log.debug("已配置缓存配置共计：" + RedisService.CACHES.size());
        log.debug("已配置缓存字段配置共计：" + RedisService.CACHE_KEYS.size());
    }


    public static void main(String[] args) {
        //连接本地的 Redis 服务
        //Jedis jedis = new Jedis("localhost");
        Jedis jedis = new Jedis("http://39.99.174.15:6379");
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("user_info:1:name", "t.s1.z");
        // 设置过期时间
        jedis.expire("user_info:1:name", 3);
        jedis.set("user_info:2:name", "t.s2.z");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("user_info:1:name"));
        System.out.println("redis 存储的字符串为: "+ jedis.get("user_info:2:name"));
    }

}
