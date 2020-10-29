package zh.shawn.project.framework.boot.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {
	private static Logger log = Logger.getLogger(HttpClientUtil.class);
	
	public static String HTTPCLIENT_PROXY_HOST = "";
	
	public static String HTTPCLIENT_PROXY_PORT = "";

	public static String XML_REGEX = "[<]([a-zA-Z0-9: =\\\\\\\\\\\"/.]+)[>]";
	public static String XML_SEPERATE = ";";

	public static Map<String, Object> httpPost(String url, Map<String, String> map) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
//		 HttpClient httpclient = new DefaultHttpClient();
//		 HttpHost proxy = new HttpHost(HttpClientUtil.HTTPCLIENT_PROXY_HOST,Integer.valueOf(HttpClientUtil.HTTPCLIENT_PROXY_PORT).intValue(), null);
//		 httpclient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
		log.debug("报文内容：" + map);
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		HttpHost proxy = new HttpHost(HttpClientUtil.HTTPCLIENT_PROXY_HOST, Integer.valueOf(HttpClientUtil.HTTPCLIENT_PROXY_PORT).intValue(), "http");
		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		httppost.setConfig(config);
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if (map != null && map.size() > 0) {
			for (String key : map.keySet()) {
				formparams.add(new BasicNameValuePair(key, map.get(key)));
			}
		}
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			log.debug("发送地址： " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			// HttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String returnMsg = EntityUtils.toString(entity, "UTF-8");
					Map<String, Object> dMap = null;
					if(!(returnMsg.endsWith("}") && returnMsg.startsWith("{")))
					{
						dMap = new HashMap<String, Object>();
						dMap.put("returnMsg", returnMsg);
					}else {
						dMap = new ObjectMapper().readValue(returnMsg, Map.class);
					}

					// Map<String, Object> data = new HashMap<String, Object>();
					// data=(Map<String, Object>)dMap.get("data");
					// data.put("rep_status", dMap.get("status"));
					// data.put("rep_msg", dMap.get("msg"));
					// log.debug("--------------------------------------");
					// log.debug("返回报文："+data);
					// log.debug("--------------------------------------");

					return dMap;
				}
			} catch (Exception e) {
				log.error("发送请求出错", e);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			log.error("ClientProtocolException", e);
		} catch (UnsupportedEncodingException e1) {
			log.error("UnsupportedEncodingException", e1);
		} catch (IOException e) {
			log.error("IOException", e);
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error("finally IOException", e);
			}
		}
		return null;

	}

	/**
	 * 
	 * 发送HTTP请求
	 * 
	 * 
	 * 
	 * @param urlString
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */

	public static String sendPost(String urlString, String method,
			Map<String, String> parameters, Map<String, String> propertys)

	{

		HttpURLConnection urlConnection = null;
		URL url;
		String response = "";
		try {
			url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod(method);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			if (propertys != null)

				for (String key : propertys.keySet()) {

					urlConnection.addRequestProperty(key, propertys.get(key));

				}

			if (method.equalsIgnoreCase("POST") && parameters != null) {
				StringBuffer param = new StringBuffer();
				for (String key : parameters.keySet()) {
					param.append("&");
					param.append(key).append("=").append(parameters.get(key));

				}
				urlConnection.getOutputStream().write(param.toString().getBytes());
				urlConnection.getOutputStream().flush();

			}
			// 读取响应
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String lines;

			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				response += lines;
			}
			reader.close();
		} catch (MalformedURLException e) {
			log.error("MalformedURLException", e);
			e.printStackTrace();
		} catch (ProtocolException e) {
			log.error("ProtocolException", e);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException", e);
			e.printStackTrace();
		}finally {
			// 断开连接
			urlConnection.disconnect();
		}
		return response;

	}
	/**
	 * @param url 发送地址
	 * @param map 参数报文
	 */
	public static Map<String, Object> httpPostJson(String url, Map<String, Object> map) {
		HttpPost post = new HttpPost(url);
		post.setHeader("charset", "UTF-8");
		post.setHeader("Content-Type", " application/json");
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		Map<String, Object> dMap = new HashMap<String, Object>();
		StringEntity stringEntity;
		
		try {
			JSONObject json = new JSONObject(map);
			stringEntity = new StringEntity(json.toString(), "UTF-8");
			post.setEntity(stringEntity);
			log.info("发送地址： " + post.getURI());
			response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String returnMsg = EntityUtils.toString(entity, "UTF-8");
				if(!"[]".equals(returnMsg)&&!returnMsg.equals("")) {
					 dMap = new ObjectMapper().readValue(returnMsg, Map.class);
				}
				log.info("返回报文：" + dMap);
				return dMap;
			}
		} catch (Exception e) {
			log.error("数据查询异常", e);
		} finally {
			try {
				httpClient.close();
				response.close();
			} catch (IOException e) {
				log.error("关闭连接,释放资源失败", e);
			}
		}
		return null;
	}
	/**
	 * @param url 发送地址
	 * @param map 参数报文
	 */
	public static List<Map<String, Object>> httpPostJsonRetList(String url, Map<String, Object> map) {
		HttpPost post = new HttpPost(url);
		post.setHeader("charset", "UTF-8");
		post.setHeader("Content-Type", " application/json");
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		List<Map<String, Object>> dMap = new ArrayList<Map<String, Object>>();
		StringEntity stringEntity;
		
		try {
			JSONObject json = new JSONObject(map);
			stringEntity = new StringEntity(json.toString(), "UTF-8");
			post.setEntity(stringEntity);
			log.info("发送地址： " + post.getURI());
			response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String returnMsg = EntityUtils.toString(entity, "UTF-8");
				if(!"[]".equals(returnMsg)&&!returnMsg.equals("")) {
					 dMap = new ObjectMapper().readValue(returnMsg, List.class);
				}
				log.info("返回报文：" + dMap);
				return dMap;
			}
		} catch (Exception e) {
			log.error("数据查询异常", e);
		} finally {
			try {
				httpClient.close();
				response.close();
			} catch (IOException e) {
				log.error("关闭连接,释放资源失败", e);
			}
		}
		return null;
	}
	// public static void main(String[] args) {
	// Map<String, String> map=new HashMap<String,String>();
	// map.put("f", "updateProjectStat");
	// map.put("foo", "a");
	// map.put("projectNo", "4");
	// map.put("status", "0002");
	//
	// Map<String, Object> map2=
	// HttpClientUtil.httpPost("http://d.ntclouds.cn:80/SHISIM_Service/a", map);
	// System.out.println(map2.get("status"));
	// }

}
