package zh.shawn.project.usms.business.showLogin;

import org.apache.log4j.Logger;
import zh.shawn.project.framework.boot.utils.ServiceUtils;
import zh.shawn.project.framework.commons.exception.ServiceBusinessException;
import zh.shawn.project.framework.commons.service.core.CommonBusinessService;


public class ShowLoginService extends CommonBusinessService<ShowLoginServiceReq, ShowLoginServiceRep, ShowLoginServiceSessionData> {

    private Logger LOGGER = Logger.getLogger(ShowLoginService.class);
    private ShowLoginServiceSessionData session;

    @Override
    public String getTargetTemplateId() {
        return "login.ftl";
    }

    @Override
    public ShowLoginServiceReq getRequestInstance() {
        return new ShowLoginServiceReq();
    }

    @Override
    public ShowLoginServiceRep getResponseInstance() {
        return new ShowLoginServiceRep();
    }

    @Override
    public ShowLoginServiceRep service(ShowLoginServiceReq req) throws ServiceBusinessException {
        ShowLoginServiceRep rep = new ShowLoginServiceRep();

        rep.setFep(ServiceUtils.FRONT_END_PREFIX);

        return rep;
    }

    @Override
    public ShowLoginServiceSessionData getSessionInstance() {
        return new ShowLoginServiceSessionData();
    }

    @Override
    public ShowLoginServiceSessionData getSession() {
        return this.session;
    }

    @Override
    public void updateSession(ShowLoginServiceSessionData session) {
        this.session = session;
    }
}
