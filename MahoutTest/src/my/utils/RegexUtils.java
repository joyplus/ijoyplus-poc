package my.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 正则工具类
 * @author zino
 *
 */
public class RegexUtils {
	
	/**
	 * 是否是邮箱
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String pattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		return check(pattern, email);
	}
	
	/**
	 * 匹配
	 * @param regex
	 * @param data
	 * @return
	 */
	private static boolean check(String regex, String data) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(data);
		return m.matches();
	}
}
