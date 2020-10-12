package zh.shawn.project.pure.commons.service.core;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.commons.exception.ServiceBusinessException;
import zh.shawn.project.pure.commons.service.ValidateConditions;
import zh.shawn.project.pure.commons.service.ValidateServiceContainer;
import zh.shawn.project.pure.commons.utils.ClassMapUtils;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public abstract class CommonBusinessService <REQ extends CommonServiceRequestData, REP extends CommonServiceResponseData, SES extends CommonSessionData> implements BusinessService, CustomBusinessService<REQ, REP, SES> {

    private Logger log = LoggerFactory.getLogger(CommonBusinessService.class);
    public static String UNSUCCESS_MSG = "err_";

    public CommonBusinessService() {
    }

    public boolean clearTempData() {
        return false;
    }

    public ServiceExecutionResult doBusiness(Map<String, Object> requestData, Map<String, Object> headerData, Service service) throws ServiceBusinessException {
        requestData.putAll(headerData);
        CommonServiceResponseData response = null;

        try {
            response = this.service((REQ) (new ClassMapUtils()).parseToObject(requestData, this.getRequestInstance()));
        } catch (Exception var7) {
            this.log.error("业务处理发生错误.", var7);
            ServiceExecutionResult er = new ServiceExecutionResult(ServiceStatusInfo.FAILED, var7.getMessage());
            er.setData(requestData);
            return er;
        }

        ServiceExecutionResult er = new ServiceExecutionResult(ServiceStatusInfo.SUCCESS, "处理成功");
        er.setData((new ClassMapUtils()).parseToMap(CommonServiceResponseData.class, response));
        return er;
    }

    public Map<String, Object> getSessionData() {
        return this.getSession() == null ? null : (new ClassMapUtils()).parseToMap(CommonSessionData.class, this.getSession());
    }

    public abstract void updateSession(SES var1);

    public abstract SES getSession();

    public void updateSession(Map<String, Object> sessionData) {
        this.updateSession((SES)(new ClassMapUtils()).parseToObject(sessionData, this.getSessionInstance()));
    }

    public boolean accessEnabled(Map<String, Object> requestData, Map<String, Object> headerData, Map<String, Object> sessionData) {
        return false;
    }

    public ServiceExecutionResult validateData(Map<String, Object> requestData, Map<String, Object> headerData, Service service) throws ServiceBusinessException {
        ValidateServiceContainer vsc = (ValidateServiceContainer)ValidateServiceContainer.getContainer();
        ServiceExecutionResult validateResult = new ServiceExecutionResult(ServiceStatusInfo.UNDECIDE, "");
        Map<String, Object> msgMap = new HashMap();
        if (vsc.containsService(service.getAction())) {
            Collection<ValidateConditions> conds = vsc.get(service.getAction());
            Iterator var9 = conds.iterator();

            while(true) {
                while(true) {
                    if (!var9.hasNext()) {
                        break ;
                    }

                    ValidateConditions cond = (ValidateConditions)var9.next();
                    if (cond.isFromHeader() && this.checkField(cond, headerData) || !cond.isFromHeader() && this.checkField(cond, requestData)) {
                        if (validateResult.getStatus() == ServiceStatusInfo.UNDECIDE) {
                            validateResult.setStatus(ServiceStatusInfo.SUCCESS);
                        }
                    } else {
                        msgMap.put(UNSUCCESS_MSG + cond.getKeyName(), cond.getMsg());
                        if (validateResult.getStatus() == ServiceStatusInfo.SUCCESS || validateResult.getStatus() == ServiceStatusInfo.UNDECIDE) {
                            validateResult.setStatus(ServiceStatusInfo.FAILED);
                        }
                    }
                }
            }
        }

        this.log.debug("未找到字段验证条件，默认为通过");
        validateResult.setData(msgMap);
        return validateResult;
    }

    protected boolean checkField(ValidateConditions cond, Map<String, Object> data) {
        Object obj = data.get(cond.getKeyName());
        if (!cond.isCheckNull() || obj != null && obj != "") {
            if (obj != null && obj != "" && cond.isCheckLength()) {
                int length;
                if (cond.isByteLength()) {
                    length = obj.toString().getBytes(Charset.forName("UTF-8")).length;
                } else {
                    length = obj.toString().length();
                }

                if (length < cond.getMinLength()) {
                    return false;
                }

                if (length > cond.getMaxLength()) {
                    return false;
                }
            }

            return obj == null || obj == "" || !cond.isCheckFormat() || cond.getKeyValue() == null || obj.toString().matches(cond.getKeyValue().toString());
        } else {
            return false;
        }
    }

    protected REP prepareData(REQ req, REP rep) {
        Field[] fs = req.getClass().getDeclaredFields();
        fs = (Field[]) ArrayUtils.addAll(fs, req.getClass().getSuperclass().getDeclaredFields());
        Field[] fs2 = rep.getClass().getDeclaredFields();
        fs2 = (Field[])ArrayUtils.addAll(fs2, rep.getClass().getSuperclass().getDeclaredFields());
        Field[] var8 = fs;
        int var7 = fs.length;

        for(int var6 = 0; var6 < var7; ++var6) {
            Field f = var8[var6];
            Field[] var12 = fs2;
            int var11 = fs2.length;

            for(int var10 = 0; var10 < var11; ++var10) {
                Field f2 = var12[var10];
                if (f2.getName().equals(f.getName())) {
                    f.setAccessible(true);
                    f2.setAccessible(true);

                    try {
                        Object obj = f.get(req);
                        if (obj != null) {
                            f2.set(rep, obj);
                            break;
                        }
                    } catch (NullPointerException var14) {
                        this.log.debug("对象为空值。" + req.getClass().getName() + ":" + f.getName(), var14);
                    } catch (Exception var15) {
                        this.log.error("对象转换赋值失败。" + req.getClass().getName() + ":" + f.getName(), var15);
                    }
                }
            }
        }

        return rep;
    }

}
