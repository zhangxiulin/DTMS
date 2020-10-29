package zh.shawn.project.framework.boot.business.test.rpc;

import zh.shawn.project.framework.commons.service.core.CommonServiceRequestData;

public class TestRpcServiceReq extends CommonServiceRequestData {

    private String i;

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }
}
