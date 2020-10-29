package zh.shawn.project.framework.boot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ApplicationListener
 *
 */
@WebListener
public class ApplicationListener implements ServletContextListener {

	private static Logger log = LoggerFactory.getLogger(ApplicationListener.class);
	public static int APP_DEBUG = 1;

	/**
	 * Default constructor.
	 */
	public ApplicationListener() {

	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		new PreInitialService().destroy();
		log.info("应用停止");
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("应用启动");
		if (ApplicationListener.APP_DEBUG == 1) {
			log.info("应用为[调试/测试状态]。设置应用数据等级为[调试/测试]。");
		} else {
			log.info("应用为[生产状态]。设置应用数据等级为[生产]。");
		}
	}

}
