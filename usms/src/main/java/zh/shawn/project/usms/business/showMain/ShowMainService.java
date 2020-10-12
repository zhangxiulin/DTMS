package zh.shawn.project.usms.business.showMain;

import org.apache.log4j.Logger;
import zh.shawn.project.pure.boot.utils.ServiceUtils;
import zh.shawn.project.pure.commons.exception.ServiceBusinessException;
import zh.shawn.project.pure.commons.service.core.CommonBusinessService;


public class ShowMainService extends CommonBusinessService<ShowMainServiceReq, ShowMainServiceRep, ShowMainServiceSessionData> {

    private Logger LOGGER = Logger.getLogger(ShowMainService.class);
    private ShowMainServiceSessionData session;

    @Override
    public String getTargetTemplateId() {
        return "admin/main.ftl";
    }

    @Override
    public ShowMainServiceReq getRequestInstance() {
        return new ShowMainServiceReq();
    }

    @Override
    public ShowMainServiceRep getResponseInstance() {
        return new ShowMainServiceRep();
    }

    @Override
    public ShowMainServiceRep service(ShowMainServiceReq req) throws ServiceBusinessException {
        ShowMainServiceRep rep = new ShowMainServiceRep();

        rep.setFep(ServiceUtils.FRONT_END_PREFIX);

        return rep;
    }

    @Override
    public ShowMainServiceSessionData getSessionInstance() {
        return new ShowMainServiceSessionData();
    }

    @Override
    public ShowMainServiceSessionData getSession() {
        return this.session;
    }

    @Override
    public void updateSession(ShowMainServiceSessionData session) {
        this.session = session;
    }
}
