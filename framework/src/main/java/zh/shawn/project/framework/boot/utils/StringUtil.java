package zh.shawn.project.framework.boot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class StringUtil
{
	/**
	 * CHARSET_NAME .
	 */
	private static final String CHARSET_NAME = "UTF-8";
	
	
	private static Logger log = LoggerFactory.getLogger(StringUtil.class);


	
	/**
	 * 
	 * @param str .
	 * @return  boolean .
	 */
	public static boolean isEmpty(String str)
	{
		return ( str == null || str.trim().length() == 0 || "null".equals(str.trim())||str =="" );
	}
	
	/**
	 * 
	 * @param text .
	 * @param boxString .
	 * @param with .
	 * @return String .
	 */
	public static String betweenReplace(String text, String boxString, String with)
	{
		if (text == null || boxString == null || with == null || boxString.length() == 0)
		{
			return text;
		}

		StringBuffer buf = new StringBuffer(text.length());
		int start = 0, end = 0;
		while (( end = text.indexOf(boxString, start) ) != -1)
		{
			buf.append(text.substring(start, end));
			buf.append(with);
			start = end + boxString.length();
			end = text.indexOf(boxString, start);
			if (end == -1)
			{
				return buf.toString();
			}
			start = end + boxString.length();
		}
		buf.append(text.substring(start));
		return buf.toString();
	}
	
	/**
	 * 
	 * @param str .
	 * @return String .
	 */
	public static final String toUpperCaseTop(String str)
	{
		if (str == null)
		{
			return null;
		}
		if (str.equals(""))
		{
			return "";
		}
		StringBuffer newName = new StringBuffer("");
		newName.append(str.toUpperCase().charAt(0));
		newName.append(str.substring(1));
		return newName.toString();
	}
	/**
	 * 
	 * @param str .
	 * @return String .
	 */
	public static final String toLowerCaseTop(String str)
	{
		if (str == null)
		{
			return null;
		}
		if (str.equals(""))
		{
			return "";
		}
		StringBuffer newName = new StringBuffer("");
		newName.append(str.toLowerCase().charAt(0));
		newName.append(str.substring(1));
		return newName.toString();
	}
	
	
	/**
	 * 
	 * @param value .
	 * @return int .
	 */
	public static int getByteLength(String value)
	{
		if (value == null)
		{
			return 0;
		}
		try
		{
			return value.getBytes(CHARSET_NAME).length;
		}
		catch (UnsupportedEncodingException e)
		{
			return 0;
		}
	}


	/**
	 * 
	 * @param value .
	 * @return String .
	 */
	public static String rtrim(String value)
	{
		if (value == null)
		{
			return null;
		}
		return value.replaceAll("\\s+$", "");
	}
	/**
	 * 
	 * @param value .
	 * @return String .
	 */
	public static String ltrim(String value)
	{
		if (value == null)
		{
			return null;
		}
		return value.replaceAll("^\\s+", "");
	}

	/**
	 * 将一个字符串按着“utf-8”格式转换成字节进行拼装 .
	 * @param str .
	 * @return String .
	 */
	public static String stringToBytes(String str)
	{
		byte [] b = null;
		try
		{
			b = str.getBytes("utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++)
		{
			sb.append(b[i]).append("/");
		}
		return sb.toString();
	}

	/**
	 * 重复字符串 .
	 * @param type r右补位l左补位 .
	 * @param val 字符串 .
	 * @param len 长度 .
	 * @return .
	 */
	public static String repeat(char type, String val, int len)
	{
		StringBuffer tBuffer = new StringBuffer();
		while (tBuffer.length() < len)
		{
			tBuffer.append(val);
		}

		if ('r' == type)
		{
			return tBuffer.substring(0, len);
		}
		else
		{
			return tBuffer.substring(tBuffer.length() - len);
		}

	}

	/**
	 * 字符串补位.
	 * @param val 字符串 .
	 * @param type r右补位l左补位 .
	 * @param size 补足长度 .
	 * @param delim 补位用字符串 .
	 * @return String .
	 */
	public static String pad2(String val, String type, int size, String delim)
	{
		if (val.contains(".") && val.split("\\.").length > 1)
		{
			String left = val.split("\\.")[0];
			String right = val.split("\\.")[1];
			if (right.length() == 1)
			{
				right = right + "0";
			}
			if (right.length() > 2)
			{
				//TODO 是否要四舍五入
				right = right.substring(0, 2);
			}
			val = left + right;
		}
		else
		{
			if (val.contains(".") && val.length() > 1)
			{
				val = val.split("\\.")[0];
			}else {
				val=val.replace(".","");
			}
			val = val + "00";
		}

		if ("r".equals(type))
		{
			if (val.length() >= size)
			{
				return val.substring(0, size);
			}
			else
			{
				return val + repeat('r', delim, size - val.length());
			}
		}
		else
		{
			if (val.length() >= size)
			{
				return val.substring(val.length() - size);
			}
			else
			{
				return repeat('l', delim, size - val.length()) + val;
			}
		}
	}
	/**
	 * 字符串补位.
	 * @param val 字符串 .
	 * @param type r右补位l左补位 .
	 * @param size 补足长度 .
	 * @param delim 补位用字符串 .
	 * @return String .
	 */
	public static String pad(String val, String type, int size, String delim)
	{
		if ("r".equals(type))
		{
			if (val.length() >= size)
			{
				return val.substring(0, size);
			}
			else
			{
				return val + repeat('r', delim, size - val.length());
			}
		}
		else
		{
			if (val.length() >= size)
			{
				return val.substring(val.length() - size);
			}
			else
			{
				return repeat('l', delim, size - val.length()) + val;
			}
		}
	}

	/**
	 * hexDigits .
	 */
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 
	 * @param message .
	 * @return String
	 */
	public static String getMD5Digest(String message)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte [] b = md.digest(message.getBytes());
			// 用字节表示就是 16个字节,每个字节用 16进制表示的话，使用两个字符，
			char str[] = new char [32];
			// 表示转换结果中对应的字符位置
			int k = 0;
			for (int i = 0; i < 16; i++)
			{
				// 取字节中高 4 位的数字转换,>>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[b[i] >>> 4 & 0xf];
				// 取字节中低 4 位的数字转换
				str[k++] = hexDigits[b[i] & 0xf];
			}
			return new String(str);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 
	 * @param dataBytes .
	 * @param algName .
	 * @return byte[] .
	 * @throws Exception .
	 */
	private static byte [] doDigest(byte [] dataBytes, String algName) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance(algName);
		md.update(dataBytes);
		return md.digest();
	}
	/**
	 * 
	 * @param map .
	 * @param isSort .
	 * @return String .
	 */
	public static String getURLParam(Map<String, Object> map, boolean isSort)
	{
		StringBuffer param = new StringBuffer();
		List<String> msgList = new ArrayList<String>();
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();)
		{
			String key = (String) it.next();
			String value = (String) map.get(key);
			msgList.add(key + "=" + value);
		}

		if (isSort)
		{
			// 排序
			Collections.sort(msgList);
		}

		for (int i = 0; i < msgList.size(); i++)
		{
			String msg = (String) msgList.get(i);
			if (i > 0)
			{
				param.append("&");
			}
			param.append(msg);
		}

		return param.toString();
	}

	
	/**
	 * 判断2个纯数字字符串的大小 .
	 * @param str1 .
	 * @param str2 .
	 * @return boolean .
	 */
	public static boolean judgeBig(String str1, String str2)
	{
		Long long1 = Long.parseLong(str1);
		Long long2 = Long.parseLong(str2);
		boolean flag = false;
		if (long1 >= long2)
		{
			flag = true;
		}
		if (long1 < long2)
		{
			flag = false;
		}
		return flag;
	}

	
	/**
	 * 用于转化null为需要的值.
	 * @param object1 .
	 * @param <T> .
	 * @param need .
	 * @throws Exception .
	 */
	public static<T> void deleteNull(T object1,String need) throws Exception{
		try {
			Field[] fs = object1.getClass().getDeclaredFields();
			for (Field f : fs) {
				try{
				    f.setAccessible(true);
					Object object = f.get(object1);
					if(f.getGenericType().equals(String.class)&&(object==null||"".equals(object)||"null".equals(object))){
							f.set(object1,need);
					}
					
				}catch(IllegalArgumentException e){
					log.error("格式转化出错",e);
						f.set(object1,null);
				}
			}
		}catch (Exception ex) {
			log.error("把实体类中的值修改null为need失败",ex);
			throw new Exception(ex);
		}
	}
	/**
	 * 
	 * @param url .
	 * @return String .
	 */
	public static String getprotrol(String url){
		if(url.indexOf("https:/")>-1){
			return "https";
		}
		return "http";
	}
	
	/**
	 * 
	 * @param map .
	 * @param object1 .
	 * @param fieldName .
	 */
	public static void  beanToMap(Map<String, Object> map,Object object1,
			String fieldName) {
		try {
			Field[] fs = object1.getClass().getDeclaredFields();
			log.debug("设置属性个数:" + fs.length);
			log.debug("设置字段：[" + fieldName + "]");
			for (Field f : fs) {
				// 设置字段访问权限 .
				if ((f.getName()).equalsIgnoreCase(fieldName.trim())) {
					f.setAccessible(true);
					Object object = f.get(object1);
					if(object==null||"".equals(object)){
						break;
					}
					map.put(fieldName, object.toString());
					log.debug("设置["+fieldName+"]属性值["+object.toString()+"]成功");
				}
			}
		} catch (Exception ex) {
			log.error("数据添加失败.[" + fieldName + "]", ex);
		}
	}
	
	/**
	 * 密码加密
	 * @param message
	 * @param method
	 * @return
	 */
	public static String checkIdSecurity(String message, String method) {
		try {
			MessageDigest md = MessageDigest.getInstance(method);
			byte[] b = md.digest(message.getBytes());
			// 用字节表示就是 16个字节,每个字节用 16进制表示的话，使用两个字符，
			char str[] = new char[32];
			// 表示转换结果中对应的字符位置
			int k = 0;
			for (int i = 0; i < 16; i++) {
				// 取字节中高 4 位的数字转换,>>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[b[i] >>> 4 & 0xf];
				// 取字节中低 4 位的数字转换
				str[k++] = hexDigits[b[i] & 0xf];
			}
			// System.out.println(String.valueOf(str));
			return new String(str);
		} catch (NoSuchAlgorithmException e) {
			log.error("没有该算法", e);
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	/**
	 * 
	 * @param f
	 * @return String
	 */
	public static String  formatM2(Double d){
		 DecimalFormat df = new DecimalFormat("#.00");
		 String f="";
		 if(d==0d){
			 f="0.00";
		 }else{
			 f=df.format(d); 
		 }
		
		return f;
		
	}
	
	
	/**
	 * 时间格式化为:yyyy年MM月dd日HH时mm分ss秒
	 * @param datetime
	 * @return
	 */
	public static  String format14Date(String datetime){
	try {
		if(datetime.length()==14){
			SimpleDateFormat sourcesdf=new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat sf=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
				Date date=sourcesdf.parse(datetime);
				datetime=sf.format(date);
			} 
		}catch (ParseException e) {
			
			e.printStackTrace();
		} 
		return datetime;
		
	}
	
	/**
	 * 值为null处理为""
	 * @param datetime
	 * @return
	 */
	public static  String formatNull(String value){
		 if(value==null) {
			 value="";
		 }
		 if(value.equals("null")) {
			 value="";
		 }
		  return value;
		
	}

	/**
	 *@Description: 将一个字符串的前n位换到字符串的末尾
	 *@params: [s 字符串, c 前n位]
	 *@return: java.lang.String
	 */
	public static String reversePart(String s, int c){
		char[] chars = s.toCharArray();
		int len = chars.length;
		char[] newChar = new char[len];
		System.arraycopy(chars, c, newChar, 0, len-c);
		System.arraycopy(chars, 0, newChar, len-c, c);
		return new String(newChar);
	}
	
}
