package zh.shawn.project.pure.boot.service.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @description: redis数据操作工具类
 * @author: zhangxiulin
 * @time: 2020/8/3 16:48
 */
public class PooledRedisUtil {

    private static Logger log = LoggerFactory.getLogger(PooledRedisUtil.class);

    private JedisPool pool;

    public PooledRedisUtil(JedisPool pool){
        this.pool = pool;
    }

    /**
     *@Description: 设置Key的有效期
     *@params: [key, exTime 单位是s]
     *@return: java.lang.Long
     */
    public Long expire(String key, int exTime){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = pool.getResource();
            result = jedis.expire(key, exTime);
        }catch (Exception e){
            log.error("setex key:{} exTime:{} error", key, exTime, e);
            jedis.close();
            return null;
        }
        jedis.close();
        return result;
    }


    /**
     *@Description:
     *@params: exTime单位是s
     *@return:
     */
    public String setEx(String key, String value, int exTime){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = pool.getResource();
            result = jedis.setex(key, exTime, value);
        }catch (Exception e){
            log.error("setex key:{} value:{} error", key , value, e);
            jedis.close();
            return null;
        }
        jedis.close();
        return result;
    }

    public String set(String key, String value){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = pool.getResource();
            result = jedis.set(key, value);
        }catch (Exception e){
            log.error("set key:{} value:{} error", key, value, e);
            jedis.close();
            return null;
        }
        jedis.close();
        return result;
    }

    public String get(String key){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = pool.getResource();
            result = jedis.get(key);
        }catch (Exception e){
            log.error("set key:{} error", key, e);
            jedis.close();
            return null;
        }
        jedis.close();
        return result;
    }

    public Long del(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = pool.getResource();
            result = jedis.del(key);
        }catch (Exception e){
            log.error("del key:{} error", key, e);
            jedis.close();
            return null;
        }
        jedis.close();
        return result;
    }

    public Long hset(String key, Map<String, String> hash){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = pool.getResource();
            result = jedis.hset(key, hash);
        }catch (Exception e){
            log.error("hset key:{} hash:{} error", key, hash, e);
            jedis.close();
            return null;
        }
        jedis.close();
        return result;
    }

    // hgetAll 有性能问题，不提供

    public String hget(String key, String field){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = pool.getResource();
            result = jedis.hget(key, field);
        }catch (Exception e){
            log.error("hget key:{} field:{} error", key, field, e);
            jedis.close();
            return null;
        }
        jedis.close();
        return result;
    }

    public Boolean hexists(String key, String field){
        Jedis jedis = null;
        Boolean result = null;
        try {
            jedis = pool.getResource();
            result = jedis.hexists(key, field);
        }catch (Exception e){
            log.error("hexists key:{} field:{} error", key, field, e);
            jedis.close();
            return null;
        }
        jedis.close();
        return result;
    }
}
