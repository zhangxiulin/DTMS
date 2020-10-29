package zh.shawn.project.usms.business.showLogin;

import zh.shawn.project.framework.commons.service.core.CommonServiceResponseData;

public class ShowLoginServiceRep extends CommonServiceResponseData {

    private String fep;  // front-end web prefix path

    public String getFep() {
        return fep;
    }

    public void setFep(String fep) {
        this.fep = fep;
    }
}
