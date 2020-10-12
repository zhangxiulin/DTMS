package zh.shawn.project.pure.boot.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description: 存放远程服务的代理对象集合至指定名称的归集
 * 双层Map，第一层存放多套RPC服务框架，第二层用来存放远程服务代理对象集合
 * @author: zhangxiulin
 * @time: 2020/7/31 15:56
 */
public interface RpcAccessLayer {

    public static Logger log = LoggerFactory.getLogger(RpcAccessLayer.class);

    public static Map<String, Map> RPC = new HashMap<>();

    public static Map getRpc(String rpcName){
        if(!RpcAccessLayer.RPC.containsKey(rpcName)){
            return null;
        }
        return RpcAccessLayer.RPC.get(rpcName);
    }

    public static void addRpc(String rpcName, Map rpc){
        RpcAccessLayer.RPC.put(rpcName, rpc);
    }

    public static Set<String> getAllRpcName(){
        return RpcAccessLayer.RPC.keySet();
    }

}
