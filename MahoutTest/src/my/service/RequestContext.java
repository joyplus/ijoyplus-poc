package my.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import my.beans.User;
//import my.service.ActionException;
import my.utils.RequestUtils;
import my.utils.ResourceUtils;

//import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 请求的上下文(非常重要的一个类)
 * @author smile
 * @date 2013-2-4 下午4:01:55
 */
public class RequestContext {
	private static String UTF_8 = "UTF-8";
	private static int MAX_FILE_SIZE = 100*1024*1024; 
	private static String webroot = null;
	private final static String upload_tmp_path;
	private final static ThreadLocal<RequestContext> contexts = new ThreadLocal<RequestContext>();
	private ServletContext context;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, Cookie> cookies;
	public final static String COOKIE_LOGIN = "zino";
	private final static int MAX_AGE = 86400 * 365;
	private final static String TEMP_UPLOAD_PATH_ATTR_NAME = "TEMP_UPLOAD_PATH";
	
	static {
		webroot = getWebrootPath();
		upload_tmp_path = webroot + "WEB-INF" + File.separator + "tmp" + File.separator;
		try {
			FileUtils.forceMkdir(new File(upload_tmp_path));
		} catch (IOException e) {}
	}
	private final static String getWebrootPath() {
		String root = RequestContext.class.getResource("/").getFile();
		try {
			root = new File(root).getParentFile().getParentFile().getCanonicalPath();
			root += File.separator;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return root;
	}
	public static RequestContext begin(ServletContext context, HttpServletRequest req, HttpServletResponse res) {
		RequestContext rc = new RequestContext();
		rc.context = context;
		rc.request = _AutoUploadRequest(_autoEncodingRequest(req));
		rc.response = res;
		rc.response.setCharacterEncoding(UTF_8);
		rc.cookies = new HashMap<String, Cookie>();
		Cookie[] cookies = req.getCookies();
		if(cookies != null){
			for(Cookie c : cookies) {
				rc.cookies.put(c.getName(), c);
			}
		}
		contexts.set(rc);
		return rc;
	}
	
	private static HttpServletRequest _autoEncodingRequest(HttpServletRequest req) {
		HttpServletRequest auto_encoding_req = req;
		if("POST".equalsIgnoreCase(req.getMethod())) {
			try {
				auto_encoding_req.setCharacterEncoding(UTF_8);
			} catch (UnsupportedEncodingException e) {}
		}else{
			auto_encoding_req = new RequestProxy(req, UTF_8);
		}
		return auto_encoding_req;
	}
	/**
	 * 自动解码
	 * @author liudong
	 */
	private static class RequestProxy extends HttpServletRequestWrapper {
		private String uri_encoding; 
		RequestProxy(HttpServletRequest request, String encoding){
			super(request);
			this.uri_encoding = encoding;
		}
		
		/**
		 * 重载getParameter
		 */
		public String getParameter(String paramName) {
			String value = super.getParameter(paramName);
			return _DecodeParamValue(value);
		}

		/**
		 * 重载getParameterMap
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Map getParameterMap() {
			Map params = super.getParameterMap();
			HashMap<String, Object> new_params = new HashMap<String, Object>();
			Iterator<String> iter = params.keySet().iterator();
			while(iter.hasNext()){
				String key = (String)iter.next();
				Object oValue = params.get(key);
				if(oValue.getClass().isArray()){
					String[] values = (String[])params.get(key);
					String[] new_values = new String[values.length];
					for(int i=0;i<values.length;i++)
						new_values[i] = _DecodeParamValue(values[i]);
					
					new_params.put(key, new_values);
				}
				else{
					String value = (String)params.get(key);
					String new_value = _DecodeParamValue(value);
					if(new_value!=null)
						new_params.put(key,new_value);
				}
			}
			return new_params;
		}

		/**
		 * 重载getParameterValues
		 */
		public String[] getParameterValues(String arg0) {
			String[] values = super.getParameterValues(arg0);
			for(int i=0;values!=null&&i<values.length;i++)
				values[i] = _DecodeParamValue(values[i]);
			return values;
		}

		/**
		 * 参数转码
		 * @param value
		 * @return
		 */
		private String _DecodeParamValue(String value){
			if (StringUtils.isBlank(value) || StringUtils.isBlank(uri_encoding)
					|| StringUtils.isNumeric(value))
				return value;		
			try{
				return new String(value.getBytes("8859_1"), uri_encoding);
			}catch(Exception e){}
			return value;
		}

	}
	public File file(String name) {
		if(request instanceof MultipartRequest) {
			return ((MultipartRequest) request).getFile(name);
		}
		return null;
	}
	public void redirect(String uri) throws IOException {
		response.sendRedirect(uri);
	}
	public static RequestContext get() {
		return contexts.get();
	}
	public String uri() {
		return request.getRequestURI();
	}
	public String contextPath(){
		return request.getContextPath();
	}
	public static String getContextPath() {
		RequestContext ctx = RequestContext.get();
		return (ctx!=null)?ctx.contextPath():"";
	}
	public void error(int code, String...msg) throws IOException {
		if(msg.length>0)
			response.sendError(code, msg[0]);
		else
			response.sendError(code);
	}
/*	public ActionException fromResource(String bundle, String key, Object...args){
		String res = ResourceUtils.getStringForLocale(request.getLocale(), bundle, key, args);
		return new ActionException(res);
	}

	public ActionException error(String key, Object...args){		
		return fromResource("error", key, args);
	}*/
	public void forbidden() throws IOException { 
		error(HttpServletResponse.SC_FORBIDDEN); 
	}

	public void not_found() throws IOException { 
		error(HttpServletResponse.SC_NOT_FOUND); 
	}
	public void forward(String uri) throws ServletException, IOException {
		RequestDispatcher rd = context.getRequestDispatcher(uri);
		rd.forward(request, response);
	}
	
	
	public Enumeration<String> params() {
		return request.getParameterNames();
	}
	
	public String param(String name, String...def_value) {
		String v = request.getParameter(name);
		return (v!=null)?v:((def_value.length>0)?def_value[0]:null);
	}
	
	public long param(String name, long def_value) {
		return NumberUtils.toLong(param(name), def_value);
	}

	public int param(String name, int def_value) {
		return NumberUtils.toInt(param(name), def_value);
	}

	public byte param(String name, byte def_value) {
		return (byte)NumberUtils.toInt(param(name), def_value);
	}
	public String[] params(String name) {
		return request.getParameterValues(name);
	}

	public long[] lparams(String name){
		String[] values = params(name);
		if(values==null) return null;
		List<Long> lvs = new ArrayList<Long>();
		for(String v : values) {
			long lv = NumberUtils.toLong(v, Long.MIN_VALUE);
			if(lv != Long.MIN_VALUE && !lvs.contains(lvs))
				lvs.add(lv);
		}
		long [] llvs = new long[lvs.size()];
		for(int i=0;i<lvs.size();i++)
			llvs[i] = lvs.get(i);
		return llvs;
	}
	/**
	 * 将HTTP请求参数映射到bean对象中
	 * @param beanClass
	 * @return
	 */
/*	public <T> T form(Class<T> beanClass) {
		try{
			T bean = beanClass.newInstance();
			BeanUtils.populate(bean, request.getParameterMap());
			return bean;
		}catch(Exception e) {
			throw new ActionException(e.getMessage());
		}
	}*/
	/**
	 * 获取登陆的用户
	 * @return
	 * @throws IOException 
	 */
//	public User user() {
//		return User.getUser(request);
//	}
//	
//	public User getUserFromCookie() {
//		try{
//			Cookie cookie = cookie(COOKIE_LOGIN);
//			if(cookie!=null && StringUtils.isNotBlank(cookie.getValue())) {
//				return getUserFromUUID(cookie.getValue());
//			}
//		}catch(Exception e) {}
//		return null;
//	}
//	
//	public User getUserFromUUID(String UUID) {
//		final String[] items = StringUtils.split(UUID,"|");
//		if(items==null || items.length!=2) return null;
//		return new User(){
//			public long getId() {
//				return Integer.parseInt(items[0]);
//			}
//			public String getActiveCode() {
//				return items[1];
//			}
//			
//		};
//	}
//	/**
//	 * 将用户存入cookie
//	 * @param user
//	 */
//	public void saveUserInCookie(User user) {
//		String value = userKeygen(user);
//		deleteCookie(COOKIE_LOGIN);
//		cookie(COOKIE_LOGIN, value, MAX_AGE);
//		
//	}
//	
//	public void deleteUserInCookie() {
//		deleteCookie(COOKIE_LOGIN);
//	}
//	private String userKeygen(User user) {
//		if(user.getActiveCode()==null || user.getActiveCode().length()!=User.ACTIVE_CODE_LENGTH)
//			user.createActiveCode();
//		return user.getId()+"|"+user.getActiveCode();
//	}
	/**
	 * 输出信息到浏览器
	 * @param msg
	 * @throws IOException
	 */
	public void print(Object msg) throws IOException {
		if(!UTF_8.equalsIgnoreCase(response.getCharacterEncoding()))
			response.setCharacterEncoding(UTF_8);
		response.getWriter().print(msg);
	}

	public void output_json(String[] key, Object[] value) throws IOException {
		JsonObject jo = new JsonObject();
		for(int i=0;i<key.length;i++){
			if(value[i] instanceof Number)
				jo.addProperty(key[i], (Number)value[i]);
			else if(value[i] instanceof Boolean)
				jo.addProperty(key[i], (Boolean)value[i]);
			else
				jo.addProperty(key[i], (String)value[i]);
		}
		print(new Gson().toJson(jo));
	}
	public void output_json(String key, Object value) throws IOException {
		output_json(new String[]{key}, new Object[]{value});
	}

	public void json_msg(String msgkey, Object...args) throws IOException {
		output_json(
				new String[]{"error","msg"}, 
				new Object[]{0,ResourceUtils.getString("error", msgkey, args)}
		);
	}
	public void end() {
		String tmpPath = (String)request.getAttribute(TEMP_UPLOAD_PATH_ATTR_NAME);
		if(tmpPath != null){
			try {
				FileUtils.deleteDirectory(new File(tmpPath));
			} catch (IOException e) {
			}
		}
		this.context = null;
		this.request = null;
		this.response = null;
		this.cookies = null;
		contexts.remove();
	}
	private static HttpServletRequest _AutoUploadRequest(HttpServletRequest req){
		if(_IsMultipart(req)){
			String path = upload_tmp_path + RandomStringUtils.randomAlphabetic(10);
			File dir = new File(path);
			if(!dir.exists() && !dir.isDirectory()) dir.mkdir();
			try {
				req.setAttribute(TEMP_UPLOAD_PATH_ATTR_NAME,path);
				return new MultipartRequest(req, dir.getCanonicalPath(), MAX_FILE_SIZE, UTF_8);
			}catch(Exception e){
			}
		}
		return req;
	}
	private static boolean _IsMultipart(HttpServletRequest req) {
		return ((req.getContentType() != null) && (req.getContentType()
				.toLowerCase().startsWith("multipart")));
	}
	/**
	 * 关闭缓存
	 */
	public void closeCache(){
        header("Pragma","must-revalidate, no-cache, private");
        header("Cache-Control","no-cache");
        header("Expires", "Sun, 1 Jan 2000 01:00:00 GMT");
	}
	public static String root() { return webroot; }
	public String header(String name) { return request.getHeader(name); }
	public void header(String name, String value) { response.setHeader(name, value); }
	public void header(String name, int value) { response.setIntHeader(name, value); }
	public void header(String name, long value) { response.setDateHeader(name, value); }
	public HttpServletRequest request() { return request; }
	public HttpServletResponse response() { return response; }
	public HttpSession session() { return request.getSession(true); }
	public Cookie cookie(String name) { return cookies.get(name); }
	public void cookie(String name, String value, int max_age) {
		RequestUtils.setCookie(request, response, name, value, max_age);
	}
	public void deleteCookie(String name) { RequestUtils.deleteCookie(request, response, name); }
	public long id() { return param("id", 0);}
}
