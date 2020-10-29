package zh.shawn.project.framework.boot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.framework.boot.service.PreInitialService;
import zh.shawn.project.framework.commons.exception.InvalidRequestException;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class ServiceUtils {

	private static Logger log = LoggerFactory.getLogger(ServiceUtils.class);
	/**
	 * 是否开启解析请求头信息。0关闭，1开启。
	 */
	public static int ENABLE_PARSE_HEADER = 0;

	/**
	 * 是否开启请求验证。
	 */
	public static int ENABLE_REQUEST_AUTH = 0;

	/**
	 * 是否开启校验权限，若关闭则所有请求不校验权限和角色。
	 */
	public static int ENABLE_CHECK_FUNCTIONS = 0;

	/**
	 * 执行方法获取的名称。
	 */
	public static String ACTION_VALUE = "f";
	public static String RETURN_MODE_NAME = "foo";
	public static String RETURN_MODE_PAGE = "p";
	public static String RETURN_MODE_INTERFACE = "a";
	public static String RETURN_MODE_OutStream = "d";
	public static String PAGE_TYPE="pageType";

	public static String ACTION_NAME = "ucdf_actname";
	public static String EXCEPT_ACTION_NAME = "";

	/**
	 * 以下信息为头部信息解析所需参数的变量名称
	 */

	/**
	 * 默认编码。
	 */
	public static String DEFAULT_CHARSET_VALUE = "UTF-8";
	public static String CHARSET_NAME = "ucdf_CHARSET";
	public static String REMOTE_HOST_NAME = "ucdf_REMOTE_HOST";
	public static String REMOTE_ADDR_NAME = "ucdf_REMOTE_ADDR";
	public static String REQUEST_METHOD = "ucdf_METHOD";
	public static String REMOTE_PORT_NAME = "ucdf_REMOTE_PORT";
	public static String LOCAL_PORT_NAME = "ucdf_LOCAL_PORT";
	public static String LOCAL_HOST_NAME = "ucdf_LOCAL_HOST";
	public static String LOCAL_PATH_NAME = "ucdf_LOCAL_PATH";
	public static String REQUEST_PATH = "ucdf_REQUEST_PATH";
	public static String REQUEST_REFERER_NAME = "ucdf_referer";
	public static String USER_INFO_NAME = "USER_ROLE";
	public static String REQUEST_DATA = "ucdf_req_dat";

	public static String FILE_ORGNAIZE_NAME = "filename";
	public static String FILE_STORAGE_NAME = "fpath";
	public static String FILE_STORAGE_PATH = "/Users/zhangxiulin/Desktop";
	public static String FILE_UPLOAD_PATH = "";
	public static String IMPORTUSERINFO_TEMPLE_FILE_PATH= "";

	public static boolean SUPPORT_CORS = true;
	public static boolean RETURN_DATA_CONTAINS_REQUEST = false;
	public static String TOKEN_SIGN="token";
	public static String SESSION_SERVICE_PROVIDER="";

	public static String FRONT_END_PREFIX = "/fe/";

	public static void reloadTemplate() {
		PreInitialService.TEMPLATE_CONFIG.setTemplateLoader(new MultiTemplateLoader(PreInitialService.TEMPLATE_LOADER
				.toArray(new TemplateLoader[PreInitialService.TEMPLATE_LOADER.size()])));
	}

	public Map<String, Object> parseRequestData(HttpServletRequest request, Map<String, Object> headerData,
			Map<String, Object> requestData) throws InvalidRequestException {
		// 获取所有字段信息
		Enumeration<String> pnames = request.getParameterNames();
		while (pnames.hasMoreElements()) {
			String pname = pnames.nextElement();
			String p = request.getParameter(pname);
			if (p != null && p.length() > 0) {
				requestData.put(pname, p);
			} else {
				requestData.put(pname, "");
			}
		}
		log.debug("获取到的所有参数：" + requestData);
		log.debug("获取到的所有头：" + headerData);
		String data = null;
		// 获取流信息
		try {
			if (request.getInputStream() != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int i = -1;
				while ((i = request.getInputStream().read()) != -1) {
					baos.write(i);
				}
				data = baos.toString(headerData.get(ServiceUtils.CHARSET_NAME).toString());
			}
		} catch (IOException e) {
			log.error("请求输入流操作错误", e);
		}
		log.debug("流数据包含：" + data);
		if (data != null && data.length() > 0) {
			// 判断是否是json数据
			if (data.startsWith("{") && data.endsWith("}")) {
				try {
					requestData.putAll(new ObjectMapper().readValue(data, Map.class));
				} catch (Exception e) {
					log.error("json解析数据时发生错误。", e);
				}
			} else {
//				// 默认认为其是一个文件
//				log.debug("流数据含有具体的数据，但未被json格式识别，默认为文件流，保存为文件。");
//				String fileName = headerData.get(ServiceUtils.FILE_ORGNAIZE_NAME).toString();
//				if (fileName.contains(".")) {
//					String[] names = fileName.split("[.]");
//					fileName = RandomUtils.genRandomString("yyyyMMddHHmmss", 20);
//					for (int i = 1; i < names.length; i++) {
//						fileName = fileName + "." + names[i];
//					}
//				}
//				File file = new File(ServiceUtils.FILE_STORAGE_PATH + File.separator + fileName);
//				try {
//					FileWriter fw = new FileWriter(file);
//					fw.write(data);
//					fw.close();
//					requestData.put(ServiceUtils.FILE_STORAGE_NAME,
//							ServiceUtils.FILE_STORAGE_PATH + File.separator + fileName);
//					log.debug("文件保存完成：" + ServiceUtils.FILE_STORAGE_PATH + File.separator + fileName);
//				} catch (Exception e) {
//					log.error("保存文件失败。", e);
//				}
			}
		}
		return requestData;
	}

	public Map<String, Object> parseHeaderData(final HttpServletRequest request, Map<String, Object> headerData)
			throws InvalidRequestException {
		try {
			// 获取标准header
			headerData.put(CHARSET_NAME,
					request.getCharacterEncoding() == null ? DEFAULT_CHARSET_VALUE : request.getCharacterEncoding());
			headerData.put(REMOTE_HOST_NAME, request.getRemoteHost());
			headerData.put(REMOTE_ADDR_NAME, request.getRemoteAddr());
			headerData.put(REMOTE_PORT_NAME, request.getRemotePort());
			headerData.put(LOCAL_PORT_NAME, request.getRemoteAddr());
			headerData.put(LOCAL_HOST_NAME, request.getRemoteAddr());
			headerData.put(LOCAL_PATH_NAME, request.getRemoteAddr());
			headerData.put(REQUEST_PATH, request.getRequestURI());
			headerData.put(REQUEST_METHOD, request.getMethod().toUpperCase());
			// 获取额外的或自定义的header
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String header = headerNames.nextElement();
				headerData.put(header, request.getHeader(header) == null ? "" : request.getHeader(header));
			}
		} catch (Exception e) {
			log.error("请求头部处理失败。", e);
			throw new InvalidRequestException("请求解析失败");
		}
		return headerData;
	}

	/**
	 * 排除性检测，检测到排除性，将不校验安全性。
	 * 
	 * @param action
	 * @return
	 */
	public boolean exceptActionFilter(final String action) {
		if (PreInitialService.EXCEPT_ACTION_NAMES.equals("*")) {
			return true;
		}
		if (PreInitialService.EXCEPT_ACTIONS.contains(action)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 校验用户访问页面权限。
	 */
	public boolean checkAuthority(String userId,String url,String pageType,Map canUseMenuBeans) {
		try {
			if(canUseMenuBeans.containsKey(url) && ((List)canUseMenuBeans.get(url)).contains(pageType)){
				return true;
			}else {
				return false ;
			}
			
		} catch (Exception e) {
			log.error("校验用户页面属性失败："+e);
			return false;
		}
	}

}
