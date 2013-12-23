package my.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.tools.view.servlet.VelocityLayoutServlet;

/**
 * 
 * @author smile
 * @date 2013-2-4 下午10:17:01
 */
public class VelocityServlet extends VelocityLayoutServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		super.doRequest(req, res);
	}
	
}
