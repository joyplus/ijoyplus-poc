package my.toolbox;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.view.context.ViewContext;

/**
 * 国际化资源velocity工具类
 * @author smile
 * @date 2013-2-18 下午2:07:23
 */
public class ResourceTool {
	private HttpServletRequest request;
	private VelocityContext velocity;
	
	/*
	 * Initialize toolbox
	 * @see org.apache.velocity.tools.view.tools.ViewTool#init(java.lang.Object)
	 */
	public void init(Object arg) {
		if(arg instanceof ViewContext){
			ViewContext viewContext = (ViewContext) arg;
			request = viewContext.getRequest();
			velocity = (VelocityContext)viewContext.getVelocityContext();
		}
	}
	
	public String this_vm(){
		return velocity.getCurrentTemplateName();
	}
}
