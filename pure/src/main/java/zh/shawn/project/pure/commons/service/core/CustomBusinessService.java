package zh.shawn.project.pure.commons.service.core;

import zh.shawn.project.pure.commons.exception.ServiceBusinessException;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/9/28 15:30
 */
public interface CustomBusinessService <REQ extends CommonServiceRequestData, REP extends CommonServiceResponseData, SES extends CommonSessionData> {

    REP service(REQ var1) throws ServiceBusinessException;

    REQ getRequestInstance();

    REP getResponseInstance();

    SES getSessionInstance();

}
