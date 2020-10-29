package zh.shawn.project.framework.boot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.framework.boot.service.session.DefaultSessionProvider;
import zh.shawn.project.framework.boot.service.session.SessionService;
import zh.shawn.project.framework.boot.service.session.SessionServiceManager;
import zh.shawn.project.framework.boot.utils.ServiceUtils;
import zh.shawn.project.framework.commons.exception.InvalidRequestException;
import zh.shawn.project.framework.commons.service.ServiceContainer;
import zh.shawn.project.framework.commons.service.core.*;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Servlet implementation class CoreService
 */
public class CoreService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String SESSION_TOKEN_NAME = "session_token";
	public static String SESSION_PROVIDER_NAME = "session_provider";
	private Logger log = LoggerFactory.getLogger(CoreService.class);

	private ServiceUtils su = new ServiceUtils();

	ServiceContainer sc = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CoreService() {
	}

	@Override
	public void init() throws ServletException {
		sc = (ServiceContainer) ServiceContainer.getContainer();
		super.init();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		serviceRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		serviceRequest(request, response);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		serviceNotSupport(req, resp);
	}

	@Override
	protected void doHead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (ServiceUtils.SUPPORT_CORS) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET,POST,JSON,PUT");
			response.setHeader("Access-Control-Allow-Headers", "x-request-with,content-type,x-forward-for,accept");
		}
		super.doHead(request, response);
	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (ServiceUtils.SUPPORT_CORS) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET,POST,JSON,PUT");
			response.setHeader("Access-Control-Allow-Headers", "x-request-with,content-type,x-forward-for,accept");
		}
		super.doOptions(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		serviceRequest(req, resp);
	}

	@Override
	protected void doTrace(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doTrace(arg0, arg1);
	}

	protected void serviceRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long s1 = System.currentTimeMillis();
		// 预处理
		Map<String, Object> headers = null;
		Map<String, Object> data = null;
		// 设置跨域访问规避
		if (ServiceUtils.SUPPORT_CORS) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET,POST,JSON,PUT");
			response.setHeader("Access-Control-Allow-Headers", "x-request-with,content-type,x-forward-for,accept");
		}
		// 生成请求码
		String random = UUID.randomUUID().toString().replaceAll("[-]", "");
		String requestCode = new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
				+ random.substring(random.length() / 2, random.length() - 1);

		String stoken = "";
		String pname = "";
		log.info("[" + requestCode + "]:请求码已生成.");
		BaseExecutionResult fer = new BaseExecutionResult();
		fer.setRequestCode(requestCode);
		fer.setIdentifyToken(stoken);
		// TODO:接入校验

		// 报文解析
		// 解析报文头
		try {
			headers = su.parseHeaderData(request, new HashMap<String, Object>());
			response.setCharacterEncoding(headers.get(ServiceUtils.CHARSET_NAME).toString());
		} catch (InvalidRequestException e) {
			log.error("[" + requestCode + "]:处理请求时发生错误", e);
			response.setCharacterEncoding(ServiceUtils.DEFAULT_CHARSET_VALUE);
			fer.setStatus(ServiceStatusInfo.FAILED);
			fer.setMsg("[" + requestCode + "]:处理请求时发生错误");
			// returnData.put("", "");
			fer.setData("");
			returnPage("commons/error.ftl", fer, request, response);
			headers = null;
			data = null;
			return;
		}
		log.info("[" + requestCode + "]" + ":报文头:[" + headers + "]");

		// 解析报文内容
		try {
			request.setCharacterEncoding(ServiceUtils.DEFAULT_CHARSET_VALUE);
			data = su.parseRequestData(request, headers, new HashMap<String, Object>());
		} catch (InvalidRequestException e) {
			log.error("[" + requestCode + "]:处理请求时发生错误", e);
			response.setCharacterEncoding(ServiceUtils.DEFAULT_CHARSET_VALUE);
			returnPage("commons/error.ftl",
					new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "处理请求时发生错误")), request,
					response);
			headers = null;
			data = null;
			return;
		}
		data.put("requestCode", requestCode);
		log.info("[" + requestCode + "]" + ":报文体:[" + data + "]");

		long s2 = System.currentTimeMillis();
		// 确定返回类型
		boolean returnPage = false;
		if (!data.containsKey(ServiceUtils.RETURN_MODE_NAME)) {
			returnPage = true;
		} else {
			if (data.get(ServiceUtils.RETURN_MODE_NAME).toString().equals(ServiceUtils.RETURN_MODE_INTERFACE)) {
				returnPage = false;
			}
			if (data.get(ServiceUtils.RETURN_MODE_NAME).toString().equals(ServiceUtils.RETURN_MODE_OutStream)) {
				returnPage = false;
			}
			if (data.get(ServiceUtils.RETURN_MODE_NAME).toString().equals(ServiceUtils.RETURN_MODE_PAGE)) {
				returnPage = true;
			}
		}
		log.debug("[" + requestCode + "]:返回类型为:[" + (returnPage ? "页面" : "接口") + "]");

		if (!data.containsKey(ServiceUtils.ACTION_VALUE)) {
			log.error("[" + requestCode + "]:请求报文非法,未含有行为名称字段");
			// response.getWriter().append("请求报文非法,未指定运行服务");
			if (returnPage) {
				returnPage("commons/error.ftl",
						new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,未指定运行服务")),
						request, response);
			} else {
				response.setStatus(500);
				returnInterface(true,
						new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,未指定运行服务")),
						response);
			}
			headers = null;
			data = null;
			return;
		}
		// 搜索处理器模型
		String action = data.get(ServiceUtils.ACTION_VALUE).toString();
		Service service = null;
		if (!sc.containsService(action)) {
			log.error("[" + requestCode + "]:请求报文非法,非法服务调用");
			if (returnPage) {
				returnPage("commons/error.ftl",
						new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用")),
						request, response);
			} else {
				returnInterface(true,
						new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用")),
						response);
			}
			headers = null;
			data = null;
			return;
		}
		service = sc.get(action);

		// 确认处理器是否可用
		if (service == null || !service.isEnabled()) {
			log.error("[" + requestCode + "]:请求报文非法,非法服务调用");
			if (returnPage) {
				returnPage("commons/error.ftl",
						new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用")),
						request, response);
			} else {
				returnInterface(true,
						new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用")),
						response);
			}
			headers = null;
			data = null;
			return;
		}
		log.info("[" + requestCode + "]:处理器确认完成:[" + service.getLabel() + "]");
		long s3 = System.currentTimeMillis();
		BusinessService businessService = null;
		try {
			businessService = (BusinessService) Class.forName(service.getDoClass()).newInstance();
		} catch (Exception e) {
			log.error("[" + requestCode + "]:获取业务服务处理器时发生错误." + service.getDoClass());
			if (returnPage) {
				returnPage("commons/error.ftl",
						new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用.")),
						request, response);
			} else {
				returnInterface(service.isSupportJson(),
						new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用.")),
						response);
			}
			headers = null;
			data = null;
			return;
		}
		long s4 = System.currentTimeMillis();
		// ***********
		// 会话解析
		// 同步数据确认

		SessionService sservice = null;
		if (service.isGetSession()) {
			log.debug("[" + requestCode + "]:已开启获取会话数据,获取会话处理器：[" + SessionServiceManager.DEFAULT_PROVIDER + "]");
			if (data.containsKey(CoreService.SESSION_PROVIDER_NAME)) {
				pname = data.get(CoreService.SESSION_PROVIDER_NAME).toString();
				log.debug("[" + requestCode + "]:请求中包含会话处理器,会话处理器：[" + pname + "]");
			} else {
				log.debug("[" + requestCode + "]:请求中未包含会话处理器,使用默认级别会话处理器.");
			}
			try {
				if (pname.length() > 0) {
					sservice = SessionServiceManager.getInstance(pname);
				} else {
					sservice = SessionServiceManager.getInstance();
				}
			} catch (Exception e) {
				log.error("[" + requestCode + "]:获取session处理器失败,使用默认处理器.", e);
				sservice = new DefaultSessionProvider();
				try {
					sservice.initial(request);
				} catch (Exception e1) {
					log.error("[" + requestCode + "]:获取容器级会话数据失败.", e);
				}
			}
			try {
				sservice.initial(request);
			} catch (Exception e) {
				log.error("[" + requestCode + "]:容器初始化失败", e);
				if (returnPage) {
					returnPage(businessService.getTargetTemplateId(),
							new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "容器初始化失败")),
							request, response);
				} else {
					returnInterface(service.isSupportJson(),
							new BaseExecutionResult(new ServiceExecutionResult(ServiceStatusInfo.FAILED, "容器初始化失败")),
							response);
				}
				headers = null;
				data = null;
				return;
			}
			if (data.containsKey(CoreService.SESSION_TOKEN_NAME)) {
				log.debug("[" + requestCode + "]:请求中包含会话token,会话token：[" + data.get(CoreService.SESSION_TOKEN_NAME)
						+ "]");
				stoken = data.get(CoreService.SESSION_TOKEN_NAME).toString();
			} else {
				log.debug("[" + requestCode + "]:请求中未包含会话token,是新的会话.");
				stoken = sservice.createSession(60 * 1000 * 1000);
			}
			data.put("identifyToken", stoken);
			fer.setIdentifyToken(stoken);

			log.debug("[" + requestCode + "]:会话数据：" + businessService.getSessionData());

			log.debug("[" + requestCode + "]:会话数据：" + ((CommonBusinessService) businessService).getSession());
			log.debug("[" + requestCode + "]:会话数据实例：" + ((CommonBusinessService) businessService).getSessionInstance());
			businessService.updateSession(
					sservice.getSessionMapData(stoken, ((CommonBusinessService) businessService).getSession()));

		}

		// ***********
		// 排除性检测,排除在外的不校验安全性
		boolean checkSecurity = !su.exceptActionFilter(data.get(ServiceUtils.ACTION_VALUE).toString());

		Map<String, Object> returnMap = new HashMap<String, Object>();
//		for (String key : data.keySet()) {
//			returnMap.put("td_" + key, data.get(key));
//		}
		// 报文校验
		// 报文校验,需要校验报文和逻辑字段中的值的格式,并且产生具体的对象
		ServiceExecutionResult validateResult = null;
		try {
			validateResult = businessService.validateData(data, headers, service);
		} catch (Exception e) {
			log.error("[" + requestCode + "]:数据校验时发生错误." + service.getDoClass(), e);
			validateResult = new ServiceExecutionResult(ServiceStatusInfo.FAILED, "请求报文非法,非法服务调用.");
			Map<String, Object> errMap = new HashMap<String, Object>();
			errMap.put("err_msg", "数据校验时发生错误:" + e.getMessage());
		}

		if (validateResult == null || validateResult.getStatus().value().equals(ServiceStatusInfo.FAILED.value())) {
			Map<String, Object> map = (Map<String, Object>) validateResult.getData();
			map.putAll(returnMap);
			validateResult.setData(map);
			if (returnPage) {
				returnPage(businessService.getTargetTemplateId(), new BaseExecutionResult(validateResult), request,
						response);
			} else {
				returnInterface(service.isSupportJson(), new BaseExecutionResult(validateResult), response);
			}
			returnMap = null;
			validateResult = null;
			headers = null;
			data = null;
			return;
		}
		// ***********
		// 保存报文
		// ***********
		// 执行处理
		// 结果后处理
		log.info("[" + requestCode + "]:处理器预处理完成");
		long s5 = System.currentTimeMillis();
		ServiceExecutionResult businessResult = null;
		try {
			businessResult = businessService.doBusiness(data, headers, service);
		} catch (Exception e) {
			log.error("[" + requestCode + "]:业务处理发生错误." + service.getDoClass(), e);
			businessResult = new ServiceExecutionResult(ServiceStatusInfo.FAILED, "业务处理发生错误.");
		}
		long s6 = System.currentTimeMillis();
		response.addCookie(new Cookie("username", new String(String.valueOf(data.get("username")).getBytes("ISO8859-1"),
				ServiceUtils.DEFAULT_CHARSET_VALUE)));

		Map<String, Object> map = (Map<String, Object>) businessResult.getData();
		// 清理临时数据
		if (service.isClearTdData() && businessService.clearTempData()) {
			map.put("td_abc", returnMap.get("td_abc"));
			map.put("td_def", returnMap.get("td_def"));
			returnMap.clear();
		} else {
			map.putAll(returnMap);
		}
		businessResult.setData(map);
		returnMap = null;
		// businessResult.setData(service.isGetSession() ? sessionValues : "");
		if (businessResult.getStatus().value().equals(ServiceStatusInfo.FAILED.value())) {
			if (returnPage) {
				returnPage("commons/error.ftl", new BaseExecutionResult(businessResult), request, response);
			} else {
				returnInterface(service.isSupportJson(), new BaseExecutionResult(businessResult), response);
			}
			businessResult = null;
			headers = null;
			data = null;
			return;
		}
		// ***********
		// 会话反补
		if (service.isUpdateSession()) {
			log.debug("[" + requestCode + "]:已开启更新会话数据.");
			// SessionService sessionService = initialSessionService(request,
			// data);
			if (sservice == null) {
				if (data.containsKey(CoreService.SESSION_PROVIDER_NAME)) {
					pname = data.get(CoreService.SESSION_PROVIDER_NAME).toString();
					log.debug("[" + requestCode + "]:请求中包含会话处理器,会话处理器：[" + pname + "]");
				} else {
					log.debug("[" + requestCode + "]:请求中未包含会话处理器,使用默认级别会话处理器.");
				}
				try {
					if (pname.length() > 0) {
						sservice = SessionServiceManager.getInstance(pname);
					} else {
						sservice = SessionServiceManager.getInstance();
					}
				} catch (Exception e) {
					log.error("[" + requestCode + "]:获取session处理器失败,使用默认处理器.", e);
					sservice = new DefaultSessionProvider();
					try {
						sservice.initial(request.getSession(true));
					} catch (Exception e1) {
						log.error("[" + requestCode + "]:获取容器级会话数据失败.", e);
					}
				}
				try {
					sservice.initial(request);
				} catch (Exception e) {
					log.error("[" + requestCode + "]:容器初始化失败", e);
					if (returnPage) {
						returnPage(businessService.getTargetTemplateId(),
								new BaseExecutionResult(
										new ServiceExecutionResult(ServiceStatusInfo.FAILED, "容器初始化失败")),
								request, response);
					} else {
						returnInterface(service.isSupportJson(), new BaseExecutionResult(
								new ServiceExecutionResult(ServiceStatusInfo.FAILED, "容器初始化失败")), response);
					}
					headers = null;
					data = null;
					return;
				}
				if (data.containsKey(CoreService.SESSION_TOKEN_NAME)) {
					log.debug("[" + requestCode + "]:请求中包含会话token,会话token：[" + data.get(CoreService.SESSION_TOKEN_NAME)
							+ "]");
					stoken = data.get(CoreService.SESSION_TOKEN_NAME).toString();
				} else {
					log.debug("[" + requestCode + "]:请求中未包含会话token,是新的会话.");
					stoken = sservice.createSession(60 * 1000 * 1000);
				}
			}
			//
			fer.setIdentifyToken(stoken);
			log.debug("[" + requestCode + "]:会话数据：" + businessService.getSessionData());
			log.debug("[" + requestCode + "]:会话数据：" + ((CommonBusinessService) businessService).getSession());
//			log.debug("[" + requestCode + "]:会话数据实例：" + ((CommonBusinessService) businessService).getSessionInstance());
			log.debug("[" + requestCode + "]:将反补session数据:" + businessService.getSessionData());

			if (service.isClearSession()) {
				log.debug("[" + requestCode + "]:已开启清空session数据");
				sservice.clearSessionService();
			}
			log.debug("[" + requestCode + "]:会话数据：" + businessService.getSessionData());
			sservice.updateSessionData(stoken, businessService.getSessionData());
			if (service.isDestroySession()) {
				sservice.destroySession(stoken);
			}
			// session = null;
		}
		long s7 = System.currentTimeMillis();
		// 报文封装
		// 报文安全性封装
		// ***********
		// 结果反馈
		fer.setServiceExecutionResult(businessResult);
		long s8 = System.currentTimeMillis();
		// validateResult.setData(service.isGetSession() ? sessionValues : "");
		if (returnPage) {
			returnPage(businessService.getTargetTemplateId(), fer, request, response);
		} else {
			returnInterface(service.isSupportJson(), fer, response);
		}
		log.debug("************统计信息***********");
		log.debug("请求编码：[" + requestCode + "]");
		log.debug("报文解析耗时：" + (s2 - s1) + "ms");
		log.debug("服务确认耗时：" + (s3 - s2) + "ms");
		log.debug("处理器获取确认耗时：" + (s4 - s3) + "ms");
		log.debug("处理器预处理耗时：" + (s5 - s4) + "ms");
		log.debug("处理器处理耗时：" + (s6 - s5) + "ms");
		log.debug("结果数据后置处理耗时：" + (s7 - s6) + "ms");
		log.debug("包装数据返回耗时：" + (s8 - s7) + "ms");
		log.debug("总耗时：" + (s8 - s1) + "ms");
		log.debug("************统计信息***********");
		log.info("请求处理完成,总耗时：" + (s8 - s1) + "ms");
		fer = null;
		businessResult = null;
		headers = null;
		data = null;
		return;

	}

	protected void returnPage(String targetPage, BaseExecutionResult er, HttpServletRequest request,
                              HttpServletResponse response) throws ServletException, IOException {
		try {
			log.debug("模板数据：" + er.getData());
			PreInitialService.TEMPLATE_CONFIG.getTemplate(targetPage, response.getCharacterEncoding())
					.process(er.getData(), response.getWriter());
		} catch (Exception e) {
			log.debug("模板组合失败.", e);
			request.setAttribute("err_msg", "页面生成失败.");
			request.setAttribute("err_data", er.getMsg());
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

	protected void returnInterface(boolean isJson, BaseExecutionResult er, HttpServletResponse response)
			throws ServletException, IOException {
		String returnStr = null;
		Map<String, Object> erdData = new HashMap<String, Object>();
		if (er.getData() != null) {
			erdData = (Map<String, Object>) er.getData();
		}
		String foo = erdData.containsKey("td_" + ServiceUtils.RETURN_MODE_NAME)
				? String.valueOf(erdData.get("td_" + ServiceUtils.RETURN_MODE_NAME))
				: "";
		log.debug("接口返回数据：" + erdData);
		if (foo.equals(ServiceUtils.RETURN_MODE_OutStream)) {
			returnOutPutStream(er, response);
		} else {
			if (isJson) {
				try {
					returnStr = new ObjectMapper().writeValueAsString(er);
					response.getWriter().print(returnStr);
				} catch (JsonProcessingException e) {
					log.error("数据转换出错");
					er.setStatus(ServiceStatusInfo.FAILED);
					er.setMsg("数据转换出错");
					er.setData("");
					returnStr = parseToQueryString(er);
				}
			}
			returnStr = parseToQueryString(er);

		}

	}

	protected void serviceNotSupport(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().print("{\"msg\":\"method type not support\"}");
	}

	protected String parseToQueryString(BaseExecutionResult er) {
		StringBuffer sb = new StringBuffer();
		sb.append("status=" + er.getStatus().value() + "&msg=" + er.getMsg() + "&data=" + er.getData() + "&requestCode="
				+ er.getRequestCode() + "&idtoken=" + er.getIdentifyToken());
		return sb.toString();
	}

	protected void returnOutPutStream(BaseExecutionResult er, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, Object> erdData = (Map<String, Object>) er.getData();
		String filepath = String.valueOf(erdData.get("filePath"));
		/* 读取文件 */
		File file = new File(filepath);
		/* 如果文件存在 */
		if (file.exists()) {
			String filename = file.getName();
			response.reset();
			response.setContentType("multipart/form-data");
			response.addHeader("Content-Disposition",
					"attachment; filename=" + new String(filename.getBytes("GBK"), "ISO8859_1"));
			int fileLength = (int) file.length();
			response.setContentLength(fileLength);
			/* 如果文件长度大于0 */
			if (fileLength != 0) {
				/* 创建输入流 */
				InputStream inStream = new FileInputStream(file);
				byte[] buf = new byte[4096];
				/* 创建输出流 */
				ServletOutputStream servletOS = response.getOutputStream();
				int readLength;
				while (((readLength = inStream.read(buf)) != -1)) {
					servletOS.write(buf, 0, readLength);
				}
				inStream.close();
				servletOS.flush();
				servletOS.close();
			}
		}
	}

	protected String checkProviderName(Map<String, Object> data) {
		String pname = "";
		if (data.containsKey(CoreService.SESSION_PROVIDER_NAME)) {
			pname = data.get(CoreService.SESSION_PROVIDER_NAME).toString();
			log.debug("请求中包含会话处理器,会话处理器：[" + pname + "]");
		} else {
			log.debug("请求中未包含会话处理器,使用默认级别会话处理器.");
		}
		return pname;
	}

	protected String checkSessionToken(Map<String, Object> data, SessionService sservice) {
		if (data.containsKey(CoreService.SESSION_TOKEN_NAME)) {
			log.debug("请求中包含会话token,会话token：[" + data.get(CoreService.SESSION_TOKEN_NAME) + "]");
			return data.get(CoreService.SESSION_TOKEN_NAME).toString();
		} else {
			log.debug("请求中未包含会话token,是新的会话.");
			return sservice.createSession(60 * 1000 * 1000);
		}
	}

	protected SessionService initialSessionService(HttpServletRequest request, Map<String, Object> data) {
		String pname = checkProviderName(data);
		SessionService sservice = null;
		try {
			if (pname.length() > 0) {
				sservice = SessionServiceManager.getInstance(pname);
			} else {
				sservice = SessionServiceManager.getInstance();
			}
			sservice.initial(request);
		} catch (Exception e) {
			log.error("获取session处理器失败,使用默认处理器.", e);
			sservice = new DefaultSessionProvider();
			try {
				sservice.initial(request);
			} catch (Exception e1) {
				log.error("获取容器级会话数据失败.", e);

			}
		}
		checkSessionToken(data, sservice);
		return sservice;
	}
}
