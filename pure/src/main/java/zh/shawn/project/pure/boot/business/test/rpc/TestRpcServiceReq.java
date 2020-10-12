package zh.shawn.project.pure.boot.business.test.rpc;

import zh.shawn.project.pure.commons.service.core.CommonServiceRequestData;

public class TestRpcServiceReq extends CommonServiceRequestData {

    private String i;

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }
}
