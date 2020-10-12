package zh.shawn.project.pure.boot.service;

import com.alibaba.dubbo.config.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.boot.dal.DatabaseAccessLayer;
import zh.shawn.project.pure.boot.dal.MongoAccessLayer;
import zh.shawn.project.pure.boot.rpc.RpcAccessLayer;
import zh.shawn.project.pure.boot.utils.StringUtil;
import zh.shawn.project.pure.commons.service.ServiceContainer;
import zh.shawn.project.pure.commons.service.ValidateConditions;
import zh.shawn.project.pure.commons.service.ValidateServiceContainer;
import zh.shawn.project.pure.commons.service.core.Service;
import zh.shawn.project.pure.commons.utils.ClassMapUtils;
import zh.shawn.project.pure.commons.utils.ClassUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PreInitialService {

	private Logger log = LoggerFactory.getLogger(PreInitialService.class);

	public static List<String> EXCEPT_ACTIONS = new ArrayList<String>(60);

	public static String EXCEPT_ACTION_NAMES = new String("home*;login_p;reg_p");

	public static String DEFAULT_CONFIG_FILE_NAME = "config/ucdf.default.properties";
	public static String DEFAULT_CONFIG_FILE_PATH = "/";

	public static String ACTION_CONFIG_FILE = "config/ucdf.action.dat";
	public static String DEFAULT_DATABASE_CONFIG_FILE_NAME = "config/database.dat";

	public static String REDIS_CACHE_FILE_NAME = "config/redis/cache.dat";
	public static String REDIS_CACHEKEY_FILE_NAME = "config/redis/cache_attr.dat";
	public static boolean REDIS_ENABLED = true;

	public static List<Map> actionList;

	public static String VALIDATION_CONFIG_FILE = "config/ucdf.validation.dat";
	public static boolean DEFAULT_CONFIG_READER_FILE = true;
	public static boolean AUTO_SAVE_CONFIG = false;

	public static String DEFAULT_JAR_STORAGE_PATH = "JARS";

	public final static String DEFAULT_SCAN_PACKAGE_NAME = "zh.shawn.project";
	/**
	 * 默认页面模板文件存储路径地址。
	 */
	public static String DEFAULT_CONTEXT_PAGE_PATH = "WEB-INF/p";
	public static String DEFAULT_FILE_PAGE_PATH = "";

	public static String DEFAULT_SERVLET_API_JAR_NAME_KEY = "DEFAULT_SERVLET_API_JAR_NAME";
	public static String DEFAULT_JSP_API_JAR_NAME_KEY = "DEFAULT_JSP_API_JAR_NAME";
	public static String DEFAULT_SERVLET_API_JAR_NAME = "servlet-api.jar";
	public static String DEFAULT_JSP_API_JAR_NAME = "jsp-api.jar";

	public static List<TemplateLoader> TEMPLATE_LOADER = new ArrayList<TemplateLoader>(20);
	public static Configuration TEMPLATE_CONFIG = new Configuration();

	public static ExecutorService sendService;
	public static int SEND_SERVICE_WORKER = 100;


	public static boolean CHECK_DATABASE = true;
	public static String DETAUL_DATABASE_NAME = "CT";


	public static String DUBBO_PROVIDER_CONFIG_FILE_NAME = "config/dubbo/dubbo-provider.dat";
	public static String DUBBO_CONSUMER_CONFIG_FILE_NAME = "config/dubbo/dubbo-consumer.dat";
	public static String DEFAULT_DUBBO_APPLICATION_QOSPORT = "22222";

	public static boolean DUBBO_PROVIDER_ENABLED = true;	// DUBBO服务提供者开关
	public static boolean DUBBO_CONSUMER_ENABLED = true; // DUBBO服务消费者开关

    public static String MONGODB_CONFIG_FILE_NAME = "config/mongodb/mongodb.dat";
    public static boolean MONGODB_ENABLED = true;


	public static String DUBBO_APPLICATION_NAME = "cams-provider";
	public static String DUBBO_MONITOR_PROTOCOL = "registry";
	public static String DUBBO_REGISTRY_ADDRESS = "zookeeper://localhost:2181";
	public static String DUBBO_PROTOCOL_NAME = "dubbo";
	public static int DUBBO_PROTOCOL_PORT = 20881;
	public static int DUBBO_PROTOCOL_THREADS = 200;
	public static String DUBBO_SERVICE_VERSION = "1.0.0";


	public void destroy() {
		// PreInitialService.dataSource = null;

	}

	/**
	 * 系统初始化启动
	 */
	public void initial() {

		// 获取配置文件内容，初始化文件数据
		Properties configProp = new Properties();
		if (PreInitialService.DEFAULT_CONFIG_READER_FILE) {
			// 从配置文件中获取内容
			try {
				configProp.load(new FileReader(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
						? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
								+ PreInitialService.DEFAULT_CONFIG_FILE_NAME
						: PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
								+ PreInitialService.DEFAULT_CONFIG_FILE_NAME));
			} catch (Exception e) {
				log.debug("配置文件读取失败。", e);
			}
		} else {
			// 从数据库中获取内容
		}

		// 获取本包和jar包下的所有类
		ClassUtils cu = new ClassUtils();
		File defaultStorageFile = new File(DEFAULT_JAR_STORAGE_PATH);
		log.debug("JAR包库地址：" + defaultStorageFile.getAbsolutePath());
		List<File> jars = listFiles(defaultStorageFile, new ArrayList<File>());
		List<Class<?>> clazzes = new ArrayList<Class<?>>();
		for (File file : jars) {
			try {
				clazzes.addAll(cu.findJarClasses(file.getAbsolutePath()));
			} catch (Exception e) {
				log.error("扫描jar包下的类时出错，jar包地址：" + file.getAbsolutePath(), e);
			}
		}
		log.debug("已扫描JAR包地址下的类库class数：" + clazzes.size());
		try {
			clazzes.addAll(cu.findClasses(DEFAULT_SCAN_PACKAGE_NAME));
		} catch (Exception e) {
			log.error("扫描包下的类时出错，包地址：" + DEFAULT_SCAN_PACKAGE_NAME, e);
		}
		String selfJarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		log.debug("开始扫描本类所在JAR包下的类，本类所在JAR包地址：" + selfJarPath);
		String selfJarFolderPath = selfJarPath.substring(0, selfJarPath.lastIndexOf("/"));
		log.debug("开始扫描本类所在JAR包的目录下的其他JAR包，JAR包目录：" + selfJarFolderPath);
		List<String> neighbourJarPaths = listFilePaths(new File(selfJarFolderPath), new ArrayList<>(), new File(selfJarPath).getAbsolutePath());
		List<String> servletApiJarPaths = new ArrayList<>();
		if (!StringUtil.isEmpty(System.getProperty("catalina.home"))){  // 判断应用服务为tomcat
			String catalinaHome = System.getProperty("catalina.home");
			log.debug("开始扫描本类所在JAR包依赖的TOMCAT相关的JAR包，JAR包目录：" + catalinaHome + File.separator + "lib");
			String servletApiJarPath = catalinaHome + File.separator + "lib" + File.separator + configProp.getProperty(DEFAULT_SERVLET_API_JAR_NAME_KEY, DEFAULT_SERVLET_API_JAR_NAME);
			String jspApiJarPath = catalinaHome + File.separator + "lib" + File.separator + configProp.getProperty(DEFAULT_JSP_API_JAR_NAME_KEY, DEFAULT_JSP_API_JAR_NAME);
			servletApiJarPaths.add(servletApiJarPath);
			servletApiJarPaths.add(jspApiJarPath);
		}
		List<String> dependentJarPaths = new ArrayList<>();
		dependentJarPaths.addAll(neighbourJarPaths);
		dependentJarPaths.addAll(servletApiJarPaths);
		try{
			clazzes.addAll(cu.findJarClasses(dependentJarPaths.toArray(new String[neighbourJarPaths.size()]), selfJarPath));
		} catch (Exception e) {
			log.error("扫描本类所在JAR包下的类时出错，包地址：" + DEFAULT_SCAN_PACKAGE_NAME, e);
		}
		// 根据属性文件的配置项，对所有的类进行配置
		// 筛选配置数据
		Enumeration<Object> propNames = configProp.keys();
		{
			while (propNames.hasMoreElements()) {
				String key = propNames.nextElement().toString();
				log.debug("待配置key: " + key);
				if (key.contains("#")) {
					{
						for (Class<?> clazz : clazzes) {
							if (clazz.getName().equals(key.split("[#]")[0])) {
								try {
									Field f = clazz.getDeclaredField(key.split("[#]")[1]);
									if (f.getType().isPrimitive()
											|| f.getType().getName().equals(String.class.getName())) {
										f.setAccessible(true);
										if (configProp.get(key) != null) {
											f.set(null, cu.parseObject(configProp.get(key), f.getType()));
											log.debug("已配置:" + key + "," + configProp.get(key));
										}
									}
									f = null;
								} catch (Exception e) {
									log.debug("配置的字段名称未找到。" + key, e);
								}
							}
						}
					}
				}
				key = null;
			}
		}
		// 加载配置文件中的已配置行为
		File file = new File(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
				? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
						+ PreInitialService.ACTION_CONFIG_FILE
				: PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator + PreInitialService.ACTION_CONFIG_FILE);
		if (!file.exists()) {
			file = new File(PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath() + File.separator
					+ PreInitialService.ACTION_CONFIG_FILE);
		}
		ObjectMapper om = new ObjectMapper();
		List<Map> values = null;
		try {
			values = om.readValue(file, List.class);
			actionList = values;
		} catch (Exception e) {
			log.error("转换对象发生错误", e);
		}
		ServiceContainer sc = (ServiceContainer) ServiceContainer.getContainer();
		ClassMapUtils cmu = new ClassMapUtils();
		{
			if (values != null) {
				for (Map m : values) {
					sc.update((Service) cmu.parseToObject(m, new Service()));
				}
			}
		}

		// 加载Redis
		if (PreInitialService.REDIS_ENABLED){
			loadRedis();
		}

		// 加载验证配置
		File valFile = new File(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
				? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
						+ PreInitialService.VALIDATION_CONFIG_FILE
				: PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
						+ PreInitialService.VALIDATION_CONFIG_FILE);
		if (!valFile.exists()) {
			valFile = new File(PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath() + File.separator
					+ PreInitialService.VALIDATION_CONFIG_FILE);
		}
		if (valFile.length() > 1) {
			Map valValues = null;
			try {
				valValues = om.readValue(valFile, Map.class);
			} catch (Exception e) {
				log.error("转换对象发生错误", e);
			}
			ValidateServiceContainer vsc = (ValidateServiceContainer) ValidateServiceContainer.getContainer();
			{
				if (valValues != null) {
					for (Object key : valValues.keySet()) {
						// sc.update(cmu.parseToObject(m, new DACIAService()));

						for (Object tvc : (Collection) valValues.get(key.toString())) {
							vsc.updateServiceCondition(key.toString(),
									(ValidateConditions) cmu.parseToObject((Map) tvc, new ValidateConditions()));
						}
					}
				}
			}
			log.debug("已配置验证条件集数为：" + vsc.countServices());
			vsc = null;
		} else {
			log.debug("验证容器配置文件为空。");
		}
		sendService = new ThreadPoolExecutor(10, SEND_SERVICE_WORKER, 1000L, TimeUnit.MILLISECONDS,
				new LinkedBlockingDeque<>(SEND_SERVICE_WORKER));

		// 数据库校验
		Properties dbProp = new Properties();
		List dbList = new ArrayList<>();
		// 从配置文件中获取内容
		if (PreInitialService.CHECK_DATABASE) {
			log.debug("即将加载数据库配置文件："+(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
					? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
							+ PreInitialService.DEFAULT_DATABASE_CONFIG_FILE_NAME
					: PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
							+ PreInitialService.DEFAULT_DATABASE_CONFIG_FILE_NAME));
			try {
				dbList = om.readValue(new FileReader(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
						? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
								+ PreInitialService.DEFAULT_DATABASE_CONFIG_FILE_NAME
						: PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
								+ PreInitialService.DEFAULT_DATABASE_CONFIG_FILE_NAME),
						List.class);
			} catch (Exception e) {
				log.debug("数据库配置文件读取失败。", e);
			}
		}

		// MidHighLevelDataOperation highdo;
		if (PreInitialService.CHECK_DATABASE) {
			log.info("数据库配置开始，即将配置：" + dbList.size());
			if (dbList != null && dbList.size() > 0) {
				for (Object ld : dbList) {
					Map dm = new HashMap<>();
					if (ld instanceof Map) {
						dm = (Map) ld;
					}
					HikariConfig config = new HikariConfig();
					config.setDriverClassName(dm.get("driverClass").toString());
					config.setJdbcUrl(dm.get("url").toString());
					config.setUsername(dm.get("username").toString());
					config.setPassword(dm.get("password").toString());
					config.setAutoCommit(
							Boolean.valueOf(dm.get("autoCommit") == null ? "true" : dm.get("autoCommit").toString()));
//					config.setConnectionInitSql(
//							dm.get("conn.init.sql") == null ? "" : dm.get("conn.init.sql").toString());
					config.setIdleTimeout(
							Long.valueOf(dm.get("idleTime") == null ? "30000" : dm.get("idleTime").toString()));
					config.setPoolName(dm.get("poolName") == null ? "default" : dm.get("poolName").toString());
					config.setRegisterMbeans(Boolean
							.valueOf(dm.get("registerMbean") == null ? "true" : dm.get("registerMbean").toString()));
					config.setCatalog(dm.get("catalog") == null ? "" : dm.get("catalog").toString());
					config.setMaximumPoolSize(Integer.valueOf(dm.get("maxPoolSize") == null ? "2" : dm.get("maxPoolSize").toString()));
					config.setMinimumIdle(Integer.valueOf(dm.get("minIdle") == null ? "2" : dm.get("minIdle").toString()));
					DatabaseAccessLayer.addDatasource(dm.get("datasourceName").toString(), new HikariDataSource(config));
					config = null;
					dm = null;
				}
			}
			log.info("数据库配置完成：" + DatabaseAccessLayer.getAllDatasourceName().size());
		}

        // 加载MongoDB
        if (PreInitialService.MONGODB_ENABLED){
			loadMongoDB();
        }

		// 模板文件预加载
		FileTemplateLoader filel = null;
		if (PreInitialService.DEFAULT_FILE_PAGE_PATH != null && PreInitialService.DEFAULT_FILE_PAGE_PATH.length() > 0) {
			try {
				filel = new FileTemplateLoader(new File(PreInitialService.DEFAULT_FILE_PAGE_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (filel != null) {
			PreInitialService.TEMPLATE_LOADER.add(filel);
		}


		// RPC服务提供者暴露服务
		if(PreInitialService.DUBBO_PROVIDER_ENABLED){
			publishDubboService();
		}


		// RPC服务消费者订阅服务
		if(PreInitialService.DUBBO_CONSUMER_ENABLED){
			subscribeDubboService();
		}

	}

	private void loadRedis(){
		if (PreInitialService.DEFAULT_CONFIG_READER_FILE){
			// 从配置文件中获取内容
            try{
				List caches = new ObjectMapper()
						.readValue(new File(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
										? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
										+ PreInitialService.REDIS_CACHE_FILE_NAME
										: PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
										+ PreInitialService.REDIS_CACHE_FILE_NAME),
								List.class);
				log.debug("[REDIS]待配置缓存数共：" + (caches != null ? caches.size() : 0));
				List cacheKeys = new ObjectMapper()
						.readValue(new File(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
										? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
										+ PreInitialService.REDIS_CACHEKEY_FILE_NAME
										: PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
										+ PreInitialService.REDIS_CACHEKEY_FILE_NAME),
								List.class);
				log.debug("[REDIS]待配置缓存字段配置数共：" + (cacheKeys != null ? cacheKeys.size() : 0));
				if (caches != null && caches.size() > 0 && cacheKeys != null && cacheKeys.size() > 0) {
					new RedisService().init(caches, cacheKeys);
				} else {
					log.error("[REDIS]缓存配置或缓存字段配置数据有误。");
				}
			} catch (Exception e) {
				log.debug("[REDIS]配置文件读取失败。", e);
			}
		}
	}


	private void publishDubboService(){
		log.info("发布DUBBO服务开始");

		ObjectMapper om = new ObjectMapper();
		// rpc配置文件读取
		List dubboList = new ArrayList<>();
		// 从配置文件中获取内容
		log.debug("DUBBO[provider]读取配置文件开始");
		try {
			dubboList = om.readValue(new FileReader(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
							? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
							+ PreInitialService.DUBBO_PROVIDER_CONFIG_FILE_NAME
							: PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
							+ PreInitialService.DUBBO_PROVIDER_CONFIG_FILE_NAME),
					List.class);
		} catch (Exception e) {
			log.error("DUBBO[provider]读取配置文件异常", e);
		}
		log.debug("DUBBO[provider]读取配置文件完成");
		if(dubboList != null && dubboList.size() > 0) {
			log.debug("DUBBO[provider]解析配置文件开始，即将配置：" + dubboList.size());
			List<String> providerLayer = new ArrayList<>();
			for (Object dubbo : dubboList){
				if (dubbo instanceof Map){
					Map dm = (Map) dubbo;
					String rpcName = dm.get("rpcName").toString();
					boolean enabled = Boolean.valueOf(dm.get("enabled") == null ? "true" : dm.get("enabled").toString());
					log.info("DUBBO[provider]rpcName[" + rpcName + "]开关[" + (enabled ? "开启" : "关闭" ) + "]" );
					if(enabled){
						log.debug("DUBBO[provider]配置当前应用[" + dm.get("dubbo.application.name").toString() + "]开始");
						// 当前应用配置
						ApplicationConfig application = new ApplicationConfig();
						application.setName(dm.get("dubbo.application.name").toString());
						application.setQosEnable(Boolean.valueOf(dm.get("dubbo.application.qosEnable") == null ? "true"
								: dm.get("dubbo.application.qosEnable").toString()));
						application.setQosAcceptForeignIp(Boolean.valueOf(dm.get("dubbo.application.qosAcceptForeignIp") == null ? "true"
								: dm.get("dubbo.application.qosAcceptForeignIp").toString()));
						application.setQosPort(Integer.valueOf(dm.get("dubbo.application.qosPort") == null ? PreInitialService.DEFAULT_DUBBO_APPLICATION_QOSPORT
								: dm.get("dubbo.application.qosPort").toString()));
						log.debug("DUBBO[provider]配置当前应用[" + dm.get("dubbo.application.name").toString() + "]结束");
						// 连接注册中心配置
						log.debug("DUBBO[provider]配置连接注册中心开始");
						RegistryConfig registry = new RegistryConfig();
						registry.setAddress(dm.get("dubbo.registry.address").toString());
						log.debug("DUBBO[provider]配置连接注册中心结束");
						// 服务提供者协议配置
						log.debug("DUBBO[provider]配置服务提供者协议开始");
						ProtocolConfig protocol = new ProtocolConfig();
						protocol.setPort(Integer.valueOf(dm.get("dubbo.protocol.port").toString()));
						protocol.setThreads(Integer.valueOf(dm.get("dubbo.protocol.threads").toString()));
						protocol.setName(dm.get("dubbo.protocol.name").toString());
						log.debug("DUBBO[provider]配置服务提供者协议结束");
						// 服务提供者暴露服务配置
						log.debug("DUBBO[provider]暴露服务配置开始");
						Map<String, Object> BEAN_MAP = new HashMap<>();
						if(dm.get("bean") instanceof List){
							List beans = (List) dm.get("bean");
							if(beans != null && beans.size() > 0){
								for (Object bean : beans){
									Map beanm = (Map) bean;
									String beanClassName = beanm.get("class") == null ? "" : beanm.get("class").toString();
									if(StringUtil.isEmpty(beanClassName)){
										continue;
									}
									String beanId = beanm.get("id") == null ? beanClassName : beanm.get("id").toString();	// 如果不指定beanId，则使用全类名作为id
									log.debug("DUBBO[provider]实例化对象[id:" + beanId + ", class:" + beanClassName + "]开始");
									try {
										BEAN_MAP.put(beanId, Class.forName(beanClassName).newInstance());
									} catch (InstantiationException e) {
										log.error("DUBBO[provider]实例化对象[" + beanClassName + "]异常", e);
									} catch (IllegalAccessException e) {
										log.error("DUBBO[provider]实例化对象[" + beanClassName + "]异常", e);
									} catch (ClassNotFoundException e) {
										log.error("DUBBO[provider]实例化对象[" + beanClassName + "]异常", e);
									}
									log.debug("DUBBO[provider]实例化对象[id:" + beanId + ", class:" + beanClassName + "]结束");
								}
							}
						}
						if(dm.get("dubbo.service") instanceof List){
							List services = (List) dm.get("dubbo.service");
							if(services != null && services.size() > 0){
								for (Object service : services){
									Map sm = (Map) service;
									ServiceConfig serviceConfig = new ServiceConfig();
									serviceConfig.setApplication(application);
									serviceConfig.setRegistry(registry); // 多个注册中心可以用setRegistries()
									serviceConfig.setProtocol(protocol); // 多个协议可以用setProtocols()
									try {
										serviceConfig.setInterface(Class.forName(sm.get("interface").toString()));
									} catch (ClassNotFoundException e) {
										log.error("DUBBO[provider]暴露服务配置异常", e);
									}
									serviceConfig.setRef(BEAN_MAP.get(sm.get("ref").toString()));
									serviceConfig.setVersion(PreInitialService.DUBBO_SERVICE_VERSION);

									// 暴露及注册服务
									serviceConfig.export();
								}
							}
						}
						log.debug("DUBBO[provider]暴露服务配置结束");
						providerLayer.add(rpcName);
						log.info("DUBBO[provider] RPCName[" + rpcName + "]装载完成");
					}

				}
			}
			log.debug("DUBBO[provider]配置文件解析完成，已配置：" + providerLayer.size() + " " + providerLayer);
		}

		log.info("服务提供者发布DUBBO服务完成");
	}

	private void subscribeDubboService(){
		log.debug("订阅DUBBO服务开始");
		ObjectMapper om = new ObjectMapper();
		// rpc配置文件读取
		List dubboList = new ArrayList<>();
		// 从配置文件中获取内容
		log.debug("DUBBO[consumer]读取配置文件开始");
		try {
			dubboList = om.readValue(new FileReader(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
							? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
							+ PreInitialService.DUBBO_CONSUMER_CONFIG_FILE_NAME
							: PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
							+ PreInitialService.DUBBO_CONSUMER_CONFIG_FILE_NAME),
					List.class);
		} catch (Exception e) {
			log.error("DUBBO[consumer]配置文件读取失败。", e);
		}
		log.debug("DUBBO[consumer]读取配置文件完成");
		if(dubboList != null && dubboList.size() > 0){
			log.debug("DUBBO[consumer]解析配置文件开始，待配置：" + dubboList.size());
			for (Object dubbo : dubboList){
				if (dubbo instanceof Map){
					Map dm = (Map) dubbo;
					String rpcName = dm.get("rpcName").toString();
					boolean enabled = Boolean.valueOf(dm.get("enabled") == null ? "true" : dm.get("enabled").toString());
					log.info("DUBBO[consumer] RPCName[" + rpcName + "] 开关[" + (enabled ? "开启" : "关闭" ) + "]" );
					if(enabled){
						Map<String, Object> RPC_SERVICES = new HashMap<>();	// 服务代理集合
						RpcAccessLayer.addRpc(rpcName, RPC_SERVICES);
						log.debug("DUBBO[consumer]配置当前应用[" + dm.get("dubbo.application.name").toString() + "]开始");
						// 当前应用配置
						ApplicationConfig application = new ApplicationConfig();
						application.setName(dm.get("dubbo.application.name").toString());
						application.setQosEnable(Boolean.valueOf(dm.get("dubbo.application.qosEnable") == null ? "true"
								: dm.get("dubbo.application.qosEnable").toString()));
						application.setQosAcceptForeignIp(Boolean.valueOf(dm.get("dubbo.application.qosAcceptForeignIp") == null ? "true"
								: dm.get("dubbo.application.qosAcceptForeignIp").toString()));
						application.setQosPort(Integer.valueOf(dm.get("dubbo.application.qosPort") == null ? "22222"
								: dm.get("dubbo.application.qosPort").toString()));
						log.debug("DUBBO[consumer]配置当前应用[" + dm.get("dubbo.application.name").toString() + "]结束");
						// 连接注册中心配置
						log.debug("DUBBO[consumer]配置连接注册中心开始");
						RegistryConfig registry = new RegistryConfig();
						registry.setAddress(dm.get("dubbo.registry.address").toString());
						log.debug("DUBBO[consumer]配置连接注册中心结束");
						// 引用远程服务
						log.debug("DUBBO[consumer]引用远程服务配置开始");
						if(dm.get("dubbo.reference") instanceof List){
							List references = (List) dm.get("dubbo.reference");
							if (references != null && references.size() > 0){
								for(Object reference : references){
									Map rm = (Map) reference;
									ReferenceConfig referenceConfig = new ReferenceConfig();
									referenceConfig.setApplication(application);
									referenceConfig.setRegistry(registry); // 多个注册中心可以用setRegistries()
									try {
										referenceConfig.setInterface(Class.forName(rm.get("interface").toString()));
									} catch (ClassNotFoundException e) {
										log.error("DUBBO[consumer]引用远程服务配置异常", e);
									}
									referenceConfig.setVersion(dm.get("dubbo.service.version").toString());
									Object rpcService = referenceConfig.get();
									RPC_SERVICES.put(rm.get("id").toString(), rpcService);
								}
							}
						}else {

						}
						log.debug("DUBBO[consumer]引用远程服务配置结束");
						log.info("DUBBO[consumer] RPCName[" + rpcName + "]装载完成");
					}

				}
			}
			log.debug("DUBBO[consumer]配置文件解析完成，已配置：" + RpcAccessLayer.getAllRpcName().size() + " " + RpcAccessLayer.getAllRpcName());
		}

		log.info("服务消费者订阅DUBBO服务完成");
	}


	private void loadMongoDB(){
		List dbList = new ArrayList<>();
        log.debug("即将加载MongoDB配置文件："+(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
                ? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
                + PreInitialService.MONGODB_CONFIG_FILE_NAME
                : PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
                + PreInitialService.MONGODB_CONFIG_FILE_NAME));
        try {
            dbList = new ObjectMapper().readValue(new FileReader(PreInitialService.DEFAULT_CONFIG_FILE_PATH.equals("/")
                            ? PreInitialService.class.getResource(DEFAULT_CONFIG_FILE_PATH).getPath()
                            + PreInitialService.MONGODB_CONFIG_FILE_NAME
                            : PreInitialService.DEFAULT_CONFIG_FILE_PATH + File.separator
                            + PreInitialService.MONGODB_CONFIG_FILE_NAME),
                    List.class);
        } catch (Exception e) {
            log.debug("MongoDB配置文件读取失败。", e);
        }

		log.info("MongoDB配置开始，即将配置：" + dbList.size());
		if (dbList != null && dbList.size() > 0) {
			for (Object ld : dbList) {
				Map dm = new HashMap<>();
				if (ld instanceof Map) {
					dm = (Map) ld;
				}

				ConnectionString connectionString = new ConnectionString(dm.get("url").toString());
				MongoClientSettings settings = MongoClientSettings.builder()
						.applyConnectionString(connectionString)
						.retryWrites(true)
						.build();
				MongoClient mongoClient = MongoClients.create(settings);
				MongoAccessLayer.addMongoClient(dm.get("datasourceName").toString(), mongoClient);

				settings = null;
				connectionString = null;
				dm = null;
			}
		}
		log.info("MongoDB配置完成：" + MongoAccessLayer.getAllMongoClientName().size());
    }


	protected List<File> listFiles(File file, List<File> files) {
		if (file.exists() && file.isFile()) {
			if (file.getName().endsWith(".jar")) {
				files.add(file);
			}
		} else if (file.exists() && file.isDirectory()) {
			for (File f : file.listFiles()) {
				files.addAll(listFiles(f, files));
			}
		}
		return files;
	}

	protected List<String> listFilePaths(File file, List<String> filePaths, String excludeFilePath){
		if (file.exists() && file.isFile()){
			if (file.getName().endsWith(".jar") && !file.getAbsolutePath().equals(excludeFilePath)){
				filePaths.add(file.getAbsolutePath());
			}
		}else if (file.exists() && file.isDirectory()){
			for (File f : file.listFiles()){
				listFilePaths(f, filePaths, excludeFilePath);
			}
		}
		return filePaths;
	}
}
