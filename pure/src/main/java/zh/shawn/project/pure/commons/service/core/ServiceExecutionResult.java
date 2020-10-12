package zh.shawn.project.pure.commons.service.core;

public class ServiceExecutionResult {
    private ServiceStatusInfo status;
    private String msg;
    private Object data;

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ServiceExecutionResult(ServiceStatusInfo status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ServiceStatusInfo getStatus() {
        return this.status;
    }

    public void setStatus(ServiceStatusInfo status) {
        this.status = status;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
