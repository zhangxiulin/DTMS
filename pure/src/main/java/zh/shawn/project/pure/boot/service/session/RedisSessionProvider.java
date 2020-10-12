package zh.shawn.project.pure.boot.service.session;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.boot.service.RedisService;
import zh.shawn.project.pure.boot.service.redis.PooledRedisUtil;
import zh.shawn.project.pure.boot.utils.StringUtil;
import zh.shawn.project.pure.commons.service.core.CommonSessionData;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: redis支持的session存储
 * kv数据库一般表设计：
 * key    表名:主键值:列名
 * value  列值
 * 为了减少空间浪费，改成:
 * key    表名:主键值
 * value  {'列名':'列值','列名':'列值'}   map结构
 * @author: zhangxiulin
 * @time: 2020/8/3 10:56
 */
public class RedisSessionProvider implements SessionService {

    private Logger log = LoggerFactory.getLogger(RedisSessionProvider.class);
    public static boolean STRICT_VALID = false;

    int timeout = 3600 * 1000 * 24;
    String sessionToken = "";

    private boolean checkSessionValid(Object requestObject){
        if(RedisSessionProvider.STRICT_VALID){
            if(requestObject instanceof HttpServletRequest){
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public String serviceName() {
        return "redis_based";
    }

    @Override
    public void initial(Object obj) throws Exception {
        if(!checkSessionValid(obj)){
            if(RedisSessionProvider.STRICT_VALID){
                log.error("输入的对象并不是一个合法的会话对象。当前：[" + obj.getClass().getName() + "]，期望：["
                    + HttpServletRequest.class.getName() + "]");
                throw new Exception("输入的对象并不是一个合法的会话对象。当前：[" + obj.getClass().getName() + "]，期望：["
                        + HttpServletRequest.class.getName() + "]");
            }else{
                log.warn("输入的对象并不是一个合法的会话对象。当前：[" + obj.getClass().getName() + "]，期望：["
                        + HttpServletRequest.class.getName() + "]");
            }
        }else{

        }
    }

    @Override
    public String createSession(int timeout) {
        String random = UUID.randomUUID().toString().replaceAll("[-]", "");
        this.sessionToken = new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
                + random.substring((random.length() / 2), (random.length() - 2));
        random = null;
        return this.sessionToken;
    }

    @Override
    public String getSessionToken() {
        return this.sessionToken;
    }

    @Override
    public <SD extends CommonSessionData> SD getSessionData(String sessionToken, SD sessionData) {
        return null;
    }

    @Override
    public <SD extends CommonSessionData> void updateSessionData(String sessionToken, SD sessionData) {

    }

    @Override
    public <SD extends CommonSessionData> Map<String, Object> getSessionMapData(String sessionToken, SD sessionData) {
        log.debug("[" + sessionToken + "]: 即将获取数据");
        Map<String, Object> data = new HashMap<>(100, 0.9f);
        if(StringUtil.isEmpty(sessionToken)){
            return data;
        }
        Set<String> keySet = RedisService.CACHES.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();){
            String cacheName = i.next();
            log.debug("[" + sessionToken + "]:查询缓存容器:[" + cacheName + "]");
            PooledRedisUtil pooledRedisUtil = RedisService.CACHES.get(cacheName);
            String _key = cacheName + ":" + sessionToken;
            for (Object o : RedisService.CACHE_KEYS.get(cacheName)){
                String f = String.valueOf(o);
                if (pooledRedisUtil.hexists(_key, f)){
                    String v = pooledRedisUtil.hget(_key, f);
                    data.put(f, v);
                    log.debug("[" + sessionToken + "]:已获取标识 key:[" + _key + "] field:[" + f + "] value:[" + v + "]");
                }else{
                    log.debug("[" + sessionToken + "]:不存在标识 key:[" + _key + "] field:[" + f + "]");
                }
            }
        }
        return data;
    }

    @Override
    public void updateSessionData(String sessionToken, Map<String, Object> sessionData) {
        log.debug("[" + sessionToken + "]:即将写入数据:[" + sessionData + "]");
        if (RedisService.CACHE_KEYS.size() > 0){
            for (Map.Entry<String, List> o : RedisService.CACHE_KEYS.entrySet()){
                Map<String, String> oData = new HashMap<>();
                for (Object oo : o.getValue()) {
                    if (sessionData.containsKey(oo.toString())) {
                        oData.put(oo.toString(), String.valueOf(sessionData.get(oo.toString())) );
                    }
                }
                if (oData.size() > 0) {
                    try {
                        String key = o.getKey() + ":" + sessionToken;
                        log.debug("[" + o.getKey() + "]:准备写入:[" + key + "]:[" + oData + "]");
                        Long hset = RedisService.CACHES.get(o.getKey()).hset(key, oData);
                        //RedisService.CACHES.get(o.getKey()).expire();
                    } catch (Exception e) {
                        log.error("缓存写入失败", e);
                    }
                    oData = null;
                }
            }
        }
    }

    @Override
    public void destroySession(String sessionToken) {

    }

    @Override
    public void clearSessionService() {

    }

    @Override
    public Map<String, Object> getAllSessionData(String sessionToken) {
        return null;
    }
}
