package zh.shawn.project.framework.boot.service.session;

import zh.shawn.project.framework.commons.service.core.CommonSessionData;

import java.util.Map;

/**
 * SD 会话数据对象：是一个集成commonsession的对象，D 数据集
 * 
 * @author hanyi
 *
 */
public interface SessionService {

	public String serviceName();

	/**
	 * 设置具体的session操作对象.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void initial(Object obj) throws Exception;

	/**
	 * 返回session标识token
	 * 
	 * @param timeout
	 * @return
	 */
	public String createSession(int timeout);

	public String getSessionToken();

	public <SD extends CommonSessionData> SD getSessionData(
            String sessionToken, SD sessionData);

	public <SD extends CommonSessionData> void updateSessionData(
            String sessionToken, SD sessionData);

	public <SD extends CommonSessionData> Map<String, Object> getSessionMapData(
            String sessionToken, SD sessionData);

	public void updateSessionData(String sessionToken,
                                  Map<String, Object> sessionData);

	public void destroySession(String sessionToken);

	public void clearSessionService();

	public Map<String, Object> getAllSessionData(String sessionToken);

}
