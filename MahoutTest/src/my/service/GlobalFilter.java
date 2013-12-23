package my.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.db.DBmanager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 全局过滤器，url映射
 * @author smile
 * @date 2013-2-4 下午10:16:54
 */
public class GlobalFilter implements Filter {
	
	private static Log log = LogFactory.getLog(GlobalFilter.class);
	
	public final static String REQUEST_URI = "request_uri";
	private ServletContext context;
	private String PATH_PREFIX;
	private final static String VM_EXT = ".vm";
	private final static String VM_INDEX = "/index" + VM_EXT;
	private List<String> ignoresURIs = new ArrayList<String>();
	private List<String> ignoresEXTs = new ArrayList<String>();

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		this.context = cfg.getServletContext();
		
		PATH_PREFIX = cfg.getInitParameter("template-path");
		if(PATH_PREFIX == null)
			PATH_PREFIX = "/WEB-INF";
		else if(PATH_PREFIX.endsWith("/"))
			PATH_PREFIX = PATH_PREFIX.substring(0, PATH_PREFIX.length()-1);
		String ignores = cfg.getInitParameter("ignore");
		if(ignores != null)
			for(String ig : StringUtils.split(ignores, ",")) {
				ignoresURIs.add(ig.trim());
			}
		ignores = cfg.getInitParameter("ignoreExts");
		if(ignores != null) {
			for(String ig : StringUtils.split(ignores, ",")) {
				ignoresEXTs.add('.'+ig.trim());
			}
		}
	}

	/**
	 * url映射
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain china) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		RequestContext rc = RequestContext.begin(this.context, request, response);
		String req_uri = rc.uri();
		if("HEAD".equalsIgnoreCase(request.getMethod()))
			return ;
		
		try {
			for(String ignoreURI : ignoresURIs) {
				if(req_uri.startsWith(ignoreURI)) {
					china.doFilter(rc.request(), rc.response());
					return;
				}
			}
			for(String ignoreEXT : ignoresEXTs) {
				if(req_uri.endsWith(ignoreEXT)) {
					china.doFilter(rc.request(), rc.response());
					return;
				}
			}
			
			rc.request().setAttribute(REQUEST_URI, req_uri);
			String[] paths = StringUtils.split(req_uri,"/");
			String vm = _GetTemplate(rc.request(), paths, paths.length);
			beforeFilter(rc);
			log.info("[URI] >>> "+vm);
			rc.forward(vm);
			afterFilter(rc);
			
		} catch(IllegalAccessException e) {
			rc.forbidden();
		} finally {
			try{
				DBmanager.closeConnection();
			}catch(Exception e){}
			if(rc!=null) rc.end();
		}
		
		
	}
	protected void beforeFilter(RequestContext ctx) throws IllegalAccessException {
		
	}
	protected void afterFilter(RequestContext ctx) {}
	
	/**
	 * 将路径映射成参数
	 * @param paths
	 * @param idx_base
	 * @return
	 */
	private String _MakeQueryString(String[] paths, int idx_base) {
		StringBuilder params = new StringBuilder();
		int idx = 1;
		for(int i=idx_base;i<paths.length;i++){
			if(params.length()==0)
				params.append('?');
			if(i>idx_base)
				params.append('&');
			params.append("p");
			params.append(idx++);
			params.append('=');
			params.append(paths[i]);
		}
		return params.toString();
	}
	
	/**
	 * url映射，寻找模板文件
	 * @param req
	 * @param paths
	 * @param idx_base
	 * @return
	 */
	private String _GetTemplate(HttpServletRequest req, String[] paths, int idx_base) {		
		StringBuilder vm = new StringBuilder(PATH_PREFIX);
		
		if(idx_base == 0)
			return vm.toString() + VM_INDEX + _MakeQueryString(paths, idx_base);
		
		for(int i=0;i<idx_base;i++){
			vm.append('/');
			vm.append(paths[i]);
		}
		String vms = vm.toString();
		String the_path = vms;
		if(_IsVmExist(the_path + VM_EXT))
			return the_path + VM_EXT + _MakeQueryString(paths, idx_base);
		
		the_path += VM_INDEX;

		if(_IsVmExist(the_path))
			return the_path + _MakeQueryString(paths, idx_base);
		
		vms += VM_EXT;
		if(_IsVmExist(vms))
			return vms + _MakeQueryString(paths, idx_base);
		
		return _GetTemplate(req, paths, idx_base-1);
	}
	private final static List<String> vm_cache = new Vector<String>();
	
	/**
	 * 判断模板文件是否存在 ,并缓存
	 * @param path
	 * @return
	 */
	private boolean _IsVmExist(String path){
		if(vm_cache.contains(path))
			return true;
		File testFile = new File(context.getRealPath(path));
		boolean isVM = testFile.exists() && testFile.isFile();
		if(isVM)
			vm_cache.add(path);
		return isVM;
	}
	@Override
	public void destroy() {
	}

}
