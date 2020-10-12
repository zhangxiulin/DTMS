package zh.shawn.project.usms.business.scLogin;

import zh.shawn.project.pure.commons.service.core.CommonServiceResponseData;

public class ScLoginServiceRep extends CommonServiceResponseData {

    private String enc;

    public String getEnc() {
        return enc;
    }

    public void setEnc(String enc) {
        this.enc = enc;
    }
}
