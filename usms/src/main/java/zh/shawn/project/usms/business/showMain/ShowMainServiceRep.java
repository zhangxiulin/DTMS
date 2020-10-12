package zh.shawn.project.usms.business.showMain;

import zh.shawn.project.pure.commons.service.core.CommonServiceResponseData;

public class ShowMainServiceRep extends CommonServiceResponseData {

    private String fep;

    public String getFep() {
        return fep;
    }

    public void setFep(String fep) {
        this.fep = fep;
    }
}
