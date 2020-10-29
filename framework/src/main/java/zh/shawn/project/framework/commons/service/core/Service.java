package zh.shawn.project.framework.commons.service.core;


public class Service {
    private String action;
    private boolean enabled;
    private boolean servicePage;
    private boolean serviceInterface;
    private boolean updateSession;
    private boolean getSession;
    private boolean supportJquery;
    private boolean needReturnData;
    private boolean supportJson;
    private boolean supportXML;
    private String label;
    private boolean checkUserKey;
    private String doClass;
    private boolean clearSession;
    private boolean clearTdData;
    private boolean destroySession;
    private String opType;

    public Service() {
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isServicePage() {
        return this.servicePage;
    }

    public void setServicePage(boolean servicePage) {
        this.servicePage = servicePage;
    }

    public boolean isServiceInterface() {
        return this.serviceInterface;
    }

    public void setServiceInterface(boolean serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public boolean isUpdateSession() {
        return this.updateSession;
    }

    public void setUpdateSession(boolean updateSession) {
        this.updateSession = updateSession;
    }

    public boolean isGetSession() {
        return this.getSession;
    }

    public void setGetSession(boolean getSession) {
        this.getSession = getSession;
    }

    public boolean isSupportJquery() {
        return this.supportJquery;
    }

    public void setSupportJquery(boolean supportJquery) {
        this.supportJquery = supportJquery;
    }

    public boolean isNeedReturnData() {
        return this.needReturnData;
    }

    public void setNeedReturnData(boolean needReturnData) {
        this.needReturnData = needReturnData;
    }

    public boolean isSupportJson() {
        return this.supportJson;
    }

    public void setSupportJson(boolean supportJson) {
        this.supportJson = supportJson;
    }

    public boolean isSupportXML() {
        return this.supportXML;
    }

    public void setSupportXML(boolean supportXML) {
        this.supportXML = supportXML;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCheckUserKey() {
        return this.checkUserKey;
    }

    public void setCheckUserKey(boolean checkUserKey) {
        this.checkUserKey = checkUserKey;
    }

    public String getDoClass() {
        return this.doClass;
    }

    public void setDoClass(String doClass) {
        this.doClass = doClass;
    }

    public boolean isClearSession() {
        return this.clearSession;
    }

    public void setClearSession(boolean clearSession) {
        this.clearSession = clearSession;
    }

    public boolean isClearTdData() {
        return this.clearTdData;
    }

    public void setClearTdData(boolean clearTdData) {
        this.clearTdData = clearTdData;
    }

    public boolean isDestroySession() {
        return this.destroySession;
    }

    public void setDestroySession(boolean destroySession) {
        this.destroySession = destroySession;
    }

    public String getOpType() {
        return this.opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }
}
