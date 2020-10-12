package zh.shawn.project.pure.boot.service;

import freemarker.cache.WebappTemplateLoader;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.boot.utils.ServiceUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;

/**
 * Servlet implementation class InitService
 */
public class InitService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = LoggerFactory.getLogger(InitService.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitService() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		// 加载初始化日志
		if (this.getServletConfig().getInitParameter("logConfig") != null) {
			String logPath = InitService.class.getResource("/").getPath()
					+ this.getServletConfig().getInitParameter("logConfig");
			System.out.println(logPath);
			System.out.println("开始加载初始化日志.[" + this.getServletConfig().getInitParameter("logConfig") + "]");
			PropertyConfigurator.configure(logPath);
			System.out.println("初始化日志完成." + logPath);
		}
		// 加载初始化配置文件
		if (this.getServletConfig().getInitParameter("configFile") != null) {
			File configFile = new File(InitService.class.getResource("/").getPath()
					+ this.getServletConfig().getInitParameter("configFile"));
			log.debug("欲加载的配置文件:" + configFile.getAbsolutePath());
			if (!configFile.exists()) {
				configFile = new File(
						InitService.class.getResource("/").getPath() + PreInitialService.DEFAULT_CONFIG_FILE_NAME);
				log.debug("欲加载的配置文件不存在，使用默认配置。" + configFile.getAbsolutePath());
			}
			configFile = null;
		}
		// 运行启动器
		new PreInitialService().initial();
		// 设置上下文的模板加载器
		if (this.getServletConfig().getInitParameter("templateConfig") != null) {
			log.debug("开始加载上下文模板加载器");
			PreInitialService.TEMPLATE_LOADER.add(new WebappTemplateLoader(getServletContext(),
					this.getServletConfig().getInitParameter("templateConfig")));
			log.debug("上下文模板加载器完成");
		}
		// 启动加载已配置的模板加载器
		ServiceUtils.reloadTemplate();
	}

}
