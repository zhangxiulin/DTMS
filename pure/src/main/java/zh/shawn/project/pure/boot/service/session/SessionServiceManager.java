package zh.shawn.project.pure.boot.service.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceLoader;

public class SessionServiceManager {
	private static Logger log = LoggerFactory.getLogger(SessionServiceManager.class);
	public static String DEFAULT_PROVIDER="container_based";
	public static String REDIS_PROVIDER = "redis_based";
	
	public static SessionService getInstance(String serviceProvider) throws Exception{
		AccessController.doPrivileged(new PrivilegedAction<Void>() {
			public Void run() {

				ServiceLoader<SessionService> loadedServerManagements = ServiceLoader.load(SessionService.class);
				Iterator<SessionService> serverManagementIterator = loadedServerManagements.iterator();
				try {
					while (serverManagementIterator.hasNext()) {
						SessionService server = serverManagementIterator.next();
						log.debug("已搜索到服务器管理器：" + server.serviceName());
					}
				} catch (Throwable t) {
				}
				return null;
			}
		});

		for (SessionService s : ServiceLoader.load(SessionService.class)) {
			if (s.serviceName().equals(serviceProvider)) {
				log.debug("已成功获取服务器管理器。" + serviceProvider);
				return s;
			}
		}
		log.warn("找不到此类服务器管理器-[" + serviceProvider + "]");
		throw new Exception("找不到此类服务器管理器-[" + serviceProvider + "]");
	}
	
	public static SessionService getInstance() throws Exception{
		return SessionServiceManager.getInstance(SessionServiceManager.DEFAULT_PROVIDER);
	}
}
