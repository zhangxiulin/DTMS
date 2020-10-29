package zh.shawn.project.framework.boot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountJudgeUtil {
	public enum AccountType{
		ACCOUNT_TYPE_CODE,ACCOUNT_TYPE_IDCODE,ACCOUNT_TYPE_PHONE,ACCOUNT_TYPE_EMAIL,ACCOUNT_TYPE_OTHER
	}

	/**
	 * 判断账号类型
	 * @param account
	 * @return 1:档案号；2：身份证号；3：手机号；4：邮箱号
	 */
	public static AccountType judgeAccount(String account){
		if (isCode(account)) {
			return AccountType.ACCOUNT_TYPE_CODE;
		}
		if (isIdCode(account)) {
			return AccountType.ACCOUNT_TYPE_IDCODE;
		}
		if (isPhone(account)) {
			return AccountType.ACCOUNT_TYPE_PHONE;
		}
		if (isEmail(account)) {
			return AccountType.ACCOUNT_TYPE_EMAIL;
		}
		return AccountType.ACCOUNT_TYPE_OTHER;
	}
	
	/**
	 * 验证档案号
	 * @param account
	 * @return
	 */
	public static boolean isCode(String account){
		String REGEX_ID_CARD = "^[a-zA-Z0-9]{4,8}$";
        return Pattern.matches(REGEX_ID_CARD, account);
	}
	
	/**
	 * 验证手机号
	 * @param account
	 * @return
	 */
	public static boolean isPhone(String account){
		boolean flag = false;    
		try{
			String phonePattern = "^1[0-9]{10}$";

		    Pattern regex = Pattern.compile(phonePattern);
		    Matcher m = regex .matcher(account);    
		    flag = m.matches();    
		}catch(Exception e){    
			flag = false;    
		}    
		return flag;
	}
	
	/**
	 * 验证身份证号
	 * @param account
	 * @return
	 */
	public static boolean isIdCode(String account){
		String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
        return Pattern.matches(REGEX_ID_CARD, account);
	}
	
	/**
	 * 验证邮件号
	 * @param account
	 * @return
	 */
	public static boolean isEmail(String account){
	    boolean flag = false;    
		try{    
		    String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
		    Pattern regex = Pattern.compile(check);    
		    Matcher matcher = regex.matcher(account);    
		    flag = matcher.matches();    
		}catch(Exception e){    
		    flag = false;    
		}    
		return flag;
	}
	
	public static void main(String[] args) {
		System.out.println(AccountJudgeUtil.judgeAccount("124"));
		System.out.println(AccountJudgeUtil.judgeAccount("3456"));
		System.out.println(AccountJudgeUtil.judgeAccount("15906137845"));
		System.out.println(AccountJudgeUtil.judgeAccount("320145214563145214"));
		System.out.println(AccountJudgeUtil.judgeAccount("jdasjdi@qq.com"));
	}
}
