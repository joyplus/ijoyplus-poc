package my.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class RequestUtils {

	/**
	 * 获取COOKIE
	 * 
	 * @param name
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null)	return null;
		for (Cookie ck : cookies) {
			if (StringUtils.equalsIgnoreCase(name,ck.getName())) 
				return ck;			
		}
		return null;
	}

	/**
	 * 获取COOKIE
	 * 
	 * @param name
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null)	return null;
		for (Cookie ck : cookies) {
			if (StringUtils.equalsIgnoreCase(name,ck.getName())) 
				return ck.getValue();			
		}
		return null;
	}

	/**
	 * 设置COOKIE
	 * 
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static void deleteCookie(HttpServletRequest request,
			HttpServletResponse response, String name) {
		setCookie(request,response,name,"",0);
	}	

	/**
	 * 获取浏览器提交的整形参数
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static int getParam(HttpServletRequest req, String param, int defaultValue){
		return NumberUtils.toInt(req.getParameter(param), defaultValue);
	}
	/**
	 * 获取浏览器提交的整形参数
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static long getParam(HttpServletRequest req, String param, long defaultValue){
		return NumberUtils.toLong(req.getParameter(param), defaultValue);
	}
	
	public static long[] getParamValues(HttpServletRequest req, String name){
		String[] values = req.getParameterValues(name);
		if(values==null) return null;
		return (long[])ConvertUtils.convert(values, long.class);
	}
	
	/**
	 * 获取浏览器提交的字符串参数
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static String getParam(HttpServletRequest req, String param, String defaultValue){
		String value = req.getParameter(param);
		return (StringUtils.isEmpty(value))?defaultValue:value;
	}
}
