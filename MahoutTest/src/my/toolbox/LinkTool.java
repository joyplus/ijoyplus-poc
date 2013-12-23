package my.toolbox;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.velocity.tools.struts.StrutsLinkTool;
import my.service.RequestContext;
import my.utils.RequestUtils;

/**
 * 
 * @author smile
 * @date 2013-2-4 下午10:17:11
 */
public class LinkTool extends StrutsLinkTool{
	public static String root () {
		return "/";
	}
	public static String action(String action_uri){
		return _link("/action", action_uri);
	}
	
	public static String js(String js_uri) {
		return _link("/js", js_uri);
	}
	
	public static String css(String css_uri) {
		return _link("/css", css_uri);
	}
	
	public static String img(String img_uri) {
		return _link("/img", img_uri);
	}

	public static String bootstrap(String url) {
		return _link("/css/bootstrap", url);
	}
	
	public static String link(String uri) {
		return _link(null, uri);
	}
	
	private static String _link(String base, String uri) {
		StringBuilder url = new StringBuilder(RequestContext.getContextPath());
		if(base != null)
			url.append(base);
		if(uri.length()>0 && uri.charAt(0) != '/')
			url.append('/');
		url.append(uri);
		return url.toString();
	}

	public void login() throws IOException {
		response.sendRedirect("/login");
	}

	/**
	 * 跳转
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String redirect(String url) throws IOException {
		response.sendRedirect(url);
		return "";
	}
	
	/**
	 * 获取浏览器提交的整形参数
	 * 
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public int param(String param, int defaultValue) {
		return RequestUtils.getParam(request, param, defaultValue);
	}

	/**
	 * 获取浏览器提交的长整形参数
	 * 
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public long lparam(String param, long defaultValue) {
		return RequestUtils.getParam(request, param, defaultValue);
	}

	public long[] lparams(String param) {
		return RequestUtils.getParamValues(request, param);
	}

	/**
	 * 获取浏览器提交的字符串参数
	 * 
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public String param(String param, String defaultValue) {
		return RequestUtils.getParam(request, param, defaultValue);
	}
	
	public String param(String param){
		return request.getParameter(param);
	}
	
	public static String encode_url(String url) {
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}
}
