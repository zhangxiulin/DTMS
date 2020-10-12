package zh.shawn.project.pure.commons.service.core;

import zh.shawn.project.pure.commons.exception.ServiceBusinessException;

import java.util.Map;

public interface BusinessService {

    ServiceExecutionResult doBusiness(Map<String, Object> var1, Map<String, Object> var2, Service var3) throws ServiceBusinessException;

    ServiceExecutionResult validateData(Map<String, Object> var1, Map<String, Object> var2, Service var3) throws ServiceBusinessException;

    Map<String, Object> getSessionData();

    void updateSession(Map<String, Object> var1);

    String getTargetTemplateId();

    boolean accessEnabled(Map<String, Object> var1, Map<String, Object> var2, Map<String, Object> var3);

    boolean clearTempData();

}
