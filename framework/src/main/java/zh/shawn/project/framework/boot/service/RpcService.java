package zh.shawn.project.framework.boot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.framework.boot.service.contract.IRpcService;
import zh.shawn.project.framework.boot.service.session.DefaultSessionProvider;
import zh.shawn.project.framework.boot.service.session.SessionService;
import zh.shawn.project.framework.boot.service.session.SessionServiceManager;
import zh.shawn.project.framework.boot.utils.ServiceUtils;
import zh.shawn.project.framework.commons.service.ServiceContainer;
import zh.shawn.project.framework.commons.service.core.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description: RPC接口的实现类
 * 负责接收RPC请求并调用具体的业务处理器
 * @author: zhangxiulin
 * @time: 2020/7/28 19:56
 */
public class RpcService implements IRpcService {

    private Logger log = LoggerFactory.getLogger(RpcService.class);

    private ServiceUtils su = new ServiceUtils();
    ServiceContainer sc = null;

    public RpcService(){
        super();
        init();
    }

    public void init(){
        sc = (ServiceContainer) ServiceContainer.getContainer();
    }

    @Override
    public Object service(Object rpcRequest) {

        Map<String, Object> rpcResponse = new HashMap<>();

        long s1 = System.currentTimeMillis();
        Map<String, Object> headers = null;
        Map<String, Object> data = null;


        // 生成请求码
        String random = UUID.randomUUID().toString().replaceAll("[-]", "");
        String requestCode = new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
                + random.substring(random.length() / 2, random.length() - 1);

        String stoken = "";
        String pname = "";
        log.info("[" + requestCode + "]:请求码已生成.");
        BaseExecutionResult fer = new BaseExecutionResult();
        fer.setRequestCode(requestCode);
        fer.setIdentifyToken(stoken);

        // 报文解析
        // 解析报文头
        headers = new HashMap<>();
        // 解析报文内容
        try {
            data = (JSONObject) JSON.toJSON(rpcRequest);
        } catch (JSONException jsone){
            log.error("[" + requestCode + "]:请求报文转JSON异常", jsone);

            returnInterface(true,
                    new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,不是正确的JSON格式")),
                    rpcResponse);

            headers = null;
            data = null;
            return rpcResponse;
        }

        data.put("requestCode", requestCode);
        log.info("[" + requestCode + "]" + ":报文体:[" + data + "]");
        long s2 = System.currentTimeMillis();

        // 确定返回类型
        boolean returnPage = false;
        if (!data.containsKey(ServiceUtils.RETURN_MODE_NAME)) {
            returnPage = true;
        } else {
            if (data.get(ServiceUtils.RETURN_MODE_NAME).toString().equals(ServiceUtils.RETURN_MODE_INTERFACE)) {
                returnPage = false;
            }
            if (data.get(ServiceUtils.RETURN_MODE_NAME).toString().equals(ServiceUtils.RETURN_MODE_OutStream)) {
                returnPage = false;
            }
            if (data.get(ServiceUtils.RETURN_MODE_NAME).toString().equals(ServiceUtils.RETURN_MODE_PAGE)) {
                returnPage = true;
            }
        }
        log.debug("[" + requestCode + "]:返回类型为:[" + (returnPage ? "页面" : "接口") + "]");

        if (!data.containsKey(ServiceUtils.ACTION_VALUE)) {
            log.error("[" + requestCode + "]:请求报文非法,未含有行为名称字段");
            if (returnPage) {

            }else{
                returnInterface(true,
                        new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,未指定运行服务")),
                        rpcResponse);
            }
            headers = null;
            data = null;
            return rpcResponse;
        }
        // 搜索处理器模型
        String action = data.get(ServiceUtils.ACTION_VALUE).toString();
        Service service = null;
        if (!sc.containsService(action)) {
            log.error("[" + requestCode + "]:请求报文非法,非法服务调用");
            if (returnPage) {

            } else {
                returnInterface(true,
                        new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用")),
                        rpcResponse);
            }
            headers = null;
            data = null;
            return rpcResponse;
        }
        service = sc.get(action);

        // 确认处理器是否可用
        if (service == null || !service.isEnabled()) {
            log.error("[" + requestCode + "]:请求报文非法,非法服务调用");
            if (returnPage) {

            } else {
                returnInterface(true,
                        new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用")),
                        rpcResponse);
            }
            headers = null;
            data = null;
            return rpcResponse;
        }
        log.info("[" + requestCode + "]:处理器确认完成:[" + service.getLabel() + "]");
        long s3 = System.currentTimeMillis();

        BusinessService businessService = null;
        try {
            businessService = (BusinessService) Class.forName(service.getDoClass()).newInstance();
        } catch (Exception e) {
            log.error("[" + requestCode + "]:获取业务服务处理器时发生错误." + service.getDoClass());
            if (returnPage) {

            } else {
                returnInterface(service.isSupportJson(),
                        new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用.")),
                        rpcResponse);
            }
            headers = null;
            data = null;
            return rpcResponse;
        }
        long s4 = System.currentTimeMillis();

        // ***********
        // 会话解析
        // 同步数据确认

        SessionService sservice = null;
        if (service.isGetSession()) {
            log.debug("[" + requestCode + "]:已开启获取会话数据,获取会话处理器：[" + SessionServiceManager.REDIS_PROVIDER + "]");
            if (data.containsKey(CoreService.SESSION_PROVIDER_NAME)) {
                pname = data.get(CoreService.SESSION_PROVIDER_NAME).toString();
                log.debug("[" + requestCode + "]:请求中包含会话处理器,会话处理器：[" + pname + "]");
            } else {
                log.debug("[" + requestCode + "]:请求中未包含会话处理器,使用默认级别会话处理器.");
            }
            try {
                if (pname.length() > 0) {
                    sservice = SessionServiceManager.getInstance(pname);
                } else {
                    sservice = SessionServiceManager.getInstance(SessionServiceManager.REDIS_PROVIDER);
                }
            } catch (Exception e) {
                log.error("[" + requestCode + "]:获取session处理器失败,使用默认处理器.", e);
                sservice = new DefaultSessionProvider();
                try {
                    sservice.initial(rpcRequest);
                } catch (Exception e1) {
                    log.error("[" + requestCode + "]:获取容器级会话数据失败.", e);
                }
            }

            if (data.containsKey(CoreService.SESSION_TOKEN_NAME)) {
                log.debug("[" + requestCode + "]:请求中包含会话token,会话token：[" + data.get(CoreService.SESSION_TOKEN_NAME)
                        + "]");
                stoken = data.get(CoreService.SESSION_TOKEN_NAME).toString();
            } else {
                log.debug("[" + requestCode + "]:请求中未包含会话token,是新的会话.");
                stoken = sservice.createSession(60 * 1000 * 1000);
            }
            data.put("identifyToken", stoken);
            fer.setIdentifyToken(stoken);

            log.debug("[" + requestCode + "]:会话数据：" + businessService.getSessionData());

            log.debug("[" + requestCode + "]:会话数据：" + ((CommonBusinessService) businessService).getSession());
            log.debug("[" + requestCode + "]:会话数据实例：" + ((CommonBusinessService) businessService).getSessionInstance());
            businessService.updateSession(
                    sservice.getSessionMapData(stoken, ((CommonBusinessService) businessService).getSession()));
        }

        // ***********
        // 排除性检测,排除在外的不校验安全性
        boolean checkSecurity = !su.exceptActionFilter(data.get(ServiceUtils.ACTION_VALUE).toString());

        Map<String, Object> returnMap = new HashMap<String, Object>();
//		for (String key : data.keySet()) {
//			returnMap.put("td_" + key, data.get(key));
//		}
        // 报文校验
        // 报文校验,需要校验报文和逻辑字段中的值的格式,并且产生具体的对象
        ServiceExecutionResult validateResult = null;
        try {
            validateResult = businessService.validateData(data, headers, service);
        } catch (Exception e) {
            log.error("[" + requestCode + "]:数据校验时发生错误." + service.getDoClass(), e);
            validateResult = new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用.");
            Map<String, Object> errMap = new HashMap<String, Object>();
            errMap.put("err_msg", "数据校验时发生错误:" + e.getMessage());
        }

        if (validateResult == null || validateResult.getStatus().value().equals(ServiceStatusInfo.FAILED.value())) {
            Map<String, Object> map = (Map<String, Object>) validateResult.getData();
            map.putAll(returnMap);
            validateResult.setData(map);
            if (returnPage) {

            } else {
                returnInterface(service.isSupportJson(), new BaseExecutionResult(validateResult), rpcResponse);
            }
            returnMap = null;
            validateResult = null;
            headers = null;
            data = null;
            return rpcResponse;
        }

        // ***********
        // 保存报文
        // ***********
        // 执行处理
        // 结果后处理
        log.info("[" + requestCode + "]:处理器预处理完成");
        long s5 = System.currentTimeMillis();
        ServiceExecutionResult businessResult = null;
        try {
            businessResult = businessService.doBusiness(data, headers, service);
        } catch (Exception e) {
            log.error("[" + requestCode + "]:业务处理发生错误." + service.getDoClass(), e);
            businessResult = new ServiceExecutionResult(ServiceStatusInfo.FAILED, "业务处理发生错误.");
        }
        long s6 = System.currentTimeMillis();


        Map<String, Object> map = (Map<String, Object>) businessResult.getData();
        // 清理临时数据
        if (service.isClearTdData() && businessService.clearTempData()) {
            map.put("td_abc", returnMap.get("td_abc"));
            map.put("td_def", returnMap.get("td_def"));
            returnMap.clear();
        } else {
            map.putAll(returnMap);
        }
        businessResult.setData(map);
        returnMap = null;
        // businessResult.setData(service.isGetSession() ? sessionValues : "");
        if (businessResult.getStatus().value().equals(ServiceStatusInfo.FAILED.value())) {
            if (returnPage) {

            } else {
                returnInterface(service.isSupportJson(), new BaseExecutionResult(businessResult), rpcResponse);
            }
            businessResult = null;
            headers = null;
            data = null;
            return rpcResponse;
        }

        // ***********
        // 会话反补
        if (service.isUpdateSession()) {
            log.debug("[" + requestCode + "]:已开启更新会话数据.");
            // SessionService sessionService = initialSessionService(request,
            // data);
            if (sservice == null) {
                if (data.containsKey(CoreService.SESSION_PROVIDER_NAME)) {
                    pname = data.get(CoreService.SESSION_PROVIDER_NAME).toString();
                    log.debug("[" + requestCode + "]:请求中包含会话处理器,会话处理器：[" + pname + "]");
                } else {
                    log.debug("[" + requestCode + "]:请求中未包含会话处理器,使用默认级别会话处理器.");
                }
                try {
                    if (pname.length() > 0) {
                        sservice = SessionServiceManager.getInstance(pname);
                    } else {
                        sservice = SessionServiceManager.getInstance();
                    }
                } catch (Exception e) {
                    log.error("[" + requestCode + "]:获取session处理器失败,使用默认处理器.", e);
                    sservice = new DefaultSessionProvider();
                    try {
                        sservice.initial(rpcRequest);
                    } catch (Exception e1) {
                        log.error("[" + requestCode + "]:获取容器级会话数据失败.", e);
                    }
                }

                if (data.containsKey(CoreService.SESSION_TOKEN_NAME)) {
                    log.debug("[" + requestCode + "]:请求中包含会话token,会话token：[" + data.get(CoreService.SESSION_TOKEN_NAME)
                            + "]");
                    stoken = data.get(CoreService.SESSION_TOKEN_NAME).toString();
                } else {
                    log.debug("[" + requestCode + "]:请求中未包含会话token,是新的会话.");
                    stoken = sservice.createSession(60 * 1000 * 1000);
                }
            }
            //
            fer.setIdentifyToken(stoken);
            log.debug("[" + requestCode + "]:会话数据：" + businessService.getSessionData());
            log.debug("[" + requestCode + "]:会话数据：" + ((CommonBusinessService) businessService).getSession());
//			log.debug("[" + requestCode + "]:会话数据实例：" + ((CommonBusinessService) businessService).getSessionInstance());
            log.debug("[" + requestCode + "]:将反补session数据:" + businessService.getSessionData());

            if (service.isClearSession()) {
                log.debug("[" + requestCode + "]:已开启清空session数据");
                sservice.clearSessionService();
            }
            log.debug("[" + requestCode + "]:会话数据：" + businessService.getSessionData());
            sservice.updateSessionData(stoken, businessService.getSessionData());
            if (service.isDestroySession()) {
                sservice.destroySession(stoken);
            }
            // session = null;
        }
        long s7 = System.currentTimeMillis();
        // 报文封装
        // 报文安全性封装
        // ***********
        // 结果反馈
        fer.setServiceExecutionResult(businessResult);
        long s8 = System.currentTimeMillis();
        // validateResult.setData(service.isGetSession() ? sessionValues : "");
        if (returnPage) {

        } else {
            returnInterface(service.isSupportJson(), fer, rpcResponse);
        }
        log.debug("************统计信息***********");
        log.debug("请求编码：[" + requestCode + "]");
        log.debug("报文解析耗时：" + (s2 - s1) + "ms");
        log.debug("服务确认耗时：" + (s3 - s2) + "ms");
        log.debug("处理器获取确认耗时：" + (s4 - s3) + "ms");
        log.debug("处理器预处理耗时：" + (s5 - s4) + "ms");
        log.debug("处理器处理耗时：" + (s6 - s5) + "ms");
        log.debug("结果数据后置处理耗时：" + (s7 - s6) + "ms");
        log.debug("包装数据返回耗时：" + (s8 - s7) + "ms");
        log.debug("总耗时：" + (s8 - s1) + "ms");
        log.debug("************统计信息***********");
        log.info("请求处理完成,总耗时：" + (s8 - s1) + "ms");
        fer = null;
        businessResult = null;
        headers = null;
        data = null;
        return rpcResponse;
    }

    protected void returnInterface(boolean isJson, BaseExecutionResult er, Map rpcResponse){
        Map<String, Object> erdData = new HashMap<String, Object>();
        if (er.getData() != null) {
            erdData = (Map<String, Object>) er.getData();
        }
        String foo = erdData.containsKey("td_" + ServiceUtils.RETURN_MODE_NAME)
                ? String.valueOf(erdData.get("td_" + ServiceUtils.RETURN_MODE_NAME))
                : "";
        log.debug("接口返回数据：" + erdData);
        if (foo.equals(ServiceUtils.RETURN_MODE_OutStream)) {

        }else{
            if (isJson) {
                try {
                    rpcResponse.putAll(JSONObject.parseObject(JSONObject.toJSONString(er),HashMap.class));
                }catch (Exception e) {
                    rpcResponse.put("msg", er.getMsg());
                    rpcResponse.put("status", er.getStatus().value());
                    rpcResponse.put("data", er.getData());
                    rpcResponse.put("requestCode", er.getRequestCode());
                    rpcResponse.put("identifyToken", er.getIdentifyToken());
                }
            }
        }
    }
}
