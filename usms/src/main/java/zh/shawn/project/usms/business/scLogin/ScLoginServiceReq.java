package zh.shawn.project.usms.business.scLogin;

import zh.shawn.project.pure.commons.service.core.CommonServiceRequestData;

public class ScLoginServiceReq extends CommonServiceRequestData {

    private String enc;

    private String enc_code;

    private String appID;

    public String getEnc() {
        return enc;
    }

    public void setEnc(String enc) {
        this.enc = enc;
    }

    public String getEnc_code() {
        return enc_code;
    }

    public void setEnc_code(String enc_code) {
        this.enc_code = enc_code;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }
}
