package zh.shawn.project.pure.boot.dal;

import com.mongodb.client.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/8/17 23:29
 */
public class MongoAccessLayer {

    public static Logger log = LoggerFactory.getLogger(MongoAccessLayer.class);

    public static Map<String, MongoClient> MONGOSOURCE = new HashMap<>(3);

    public static MongoClient getMongoClient(String mongoClientName){
        if (!MongoAccessLayer.MONGOSOURCE.containsKey(mongoClientName)){
            return null;
        }
        return MongoAccessLayer.MONGOSOURCE.get(mongoClientName);
    }
    
    public static void addMongoClient(String mongoClientName, MongoClient mongoClient){
        MongoAccessLayer.MONGOSOURCE.put(mongoClientName, mongoClient);
    }

    public static Set<String> getAllMongoClientName(){
        return MongoAccessLayer.MONGOSOURCE.keySet();
    }
}
