package zh.shawn.project.pure.boot.service.session;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.commons.service.core.CommonSessionData;
import zh.shawn.project.pure.commons.utils.ClassMapUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DefaultSessionProvider implements SessionService {
	private Logger log = LoggerFactory.getLogger(DefaultSessionProvider.class);
	public static boolean STRICT_VALID = true;
	HttpSession session;
	String sessionToken = "";

	@Override
	public String serviceName() {
		return "container_based";
	}

	private boolean checkSessionValid(Object sessionObject) {
		if (sessionObject instanceof HttpServletRequest) {
			return true;
		}
		return false;
	}

	@Override
	public void initial(Object obj) throws Exception {
		if (!checkSessionValid(obj)) {
			if (DefaultSessionProvider.STRICT_VALID) {
				log.error("输入的对象并不是一个合法的会话对象。当前：[" + obj.getClass().getName()
						+ "],期望：[" + HttpServletRequest.class.getName() + "]");
				throw new Exception("输入的对象并不是一个合法的会话对象。当前：["
						+ obj.getClass().getName() + "],期望：["
						+ HttpServletRequest.class.getName() + "]");
			} else {
				log.warn("输入的对象并不是一个合法的会话对象。当前：[" + obj.getClass().getName()
						+ "],期望：[" + HttpServletRequest.class.getName() + "]");
			}
		} else {
			this.session = ((HttpServletRequest) obj).getSession(true);
		}
	}

	@Override
	public String createSession(int timeout) {
		this.sessionToken = this.session.getId();
		return this.sessionToken;
	}

	@Override
	public void destroySession(String sessionToken) {
		this.session.invalidate();
	}

	@Override
	public void clearSessionService() {
		this.session = null;
	}

	@Override
	public <SD extends CommonSessionData> SD getSessionData(
			String sessionToken, SD sessionData) {
		Map<String, Object> data = new HashMap<String, Object>(10, 0.9F);
		if (!this.session.isNew()) {
			Enumeration<String> attrNames = this.session.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String key = attrNames.nextElement();
				data.put(key, this.session.getAttribute(key));
				key = null;
			}
			attrNames = null;
		}
		if (data == null || data.size() == 0) {
			log.warn("未包含任何会话数据，返回原始数据。");
			return sessionData;
		}
		return new ClassMapUtils().parseToObject(data, sessionData);
	}

	@Override
	public <SD extends CommonSessionData> void updateSessionData(
			String sessionToken, SD sessionData) {
		Map<String, Object> sdata = new HashMap<String, Object>();
		if (sessionData != null) {
			sdata = new ClassMapUtils().parseToMap(CommonSessionData.class,
					sessionData);
		}
		for (String key : sdata.keySet()) {
			if (sdata.get(key) != null) {
				this.session.setAttribute(key, sdata.get(key));
			}
		}
	}

	@Override
	public Map<String, Object> getAllSessionData(String sessionToken) {
		Map<String, Object> data = new HashMap<String, Object>(10, 0.9F);
		if (!this.session.isNew()) {
			Enumeration<String> attrNames = session.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String key = attrNames.nextElement();
				data.put(key, this.session.getAttribute(key));
				key = null;
			}
			attrNames = null;
		}
		return data;
	}

	@Override
	public <SD extends CommonSessionData> Map<String, Object> getSessionMapData(
			String sessionToken, SD sessionData) {
		Map<String, Object> data = new HashMap<String, Object>(10, 0.9F);
		if (!this.session.isNew()) {
			Enumeration<String> attrNames = session.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String key = attrNames.nextElement();
				data.put(key, this.session.getAttribute(key));
				key = null;
			}
			attrNames = null;
		}
		if (data == null || data.size() == 0) {
			log.warn("未包含任何会话数据。");
		}
		return data;
	}

	@Override
	public void updateSessionData(String sessionToken,
			Map<String, Object> sessionData) {
		for (String key : sessionData.keySet()) {
			if (sessionData.get(key) != null) {
				this.session.setAttribute(key, sessionData.get(key));
			}
		}
	}

	@Override
	public String getSessionToken() {
		return this.sessionToken;
	}
}
