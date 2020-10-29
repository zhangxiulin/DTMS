package zh.shawn.project.framework.boot.service;


import zh.shawn.project.framework.commons.service.core.ServiceExecutionResult;
import zh.shawn.project.framework.commons.service.core.ServiceStatusInfo;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/9/26 1:26
 */
public class BaseExecutionResult {

    private String msg;
    private ServiceStatusInfo status;
    private Object data;
    private String requestCode;
    private String identifyToken;

    public BaseExecutionResult(){}

    public BaseExecutionResult(ServiceExecutionResult ser){
        if (ser.getMsg() != null) {
            this.msg = ser.getMsg();
        }
        if (ser.getData() != null) {
            this.data = ser.getData();
        }
        if (ser.getStatus() != null) {
            this.status = ser.getStatus();
        }
    }

    public void setServiceExecutionResult(ServiceExecutionResult ser) {
        if (ser.getMsg() != null) {
            this.msg = ser.getMsg();
        }
        if (ser.getData() != null) {
            this.data = ser.getData();
        }
        if (ser.getStatus() != null) {
            this.status = ser.getStatus();
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ServiceStatusInfo getStatus() {
        return status;
    }

    public void setStatus(ServiceStatusInfo status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getIdentifyToken() {
        return identifyToken;
    }

    public void setIdentifyToken(String identifyToken) {
        this.identifyToken = identifyToken;
    }
}
