/*package my.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import my.cache.CacheManager;
import my.service.RequestContext;

*//**
 * 验证码
 * @author smile
 * @date 2013-2-18 下午7:19:27
 *//*
public class ImageUtils {
	private final static String CACHE_REGION = "session";
	private final static String COOKIE_NAME = "_reg_key_";
	private static int WIDTH = 60;
	private static int HEIGHT = 20;
	private static int LENGTH = 4;
	private final static Random random = new Random();
	
	*//**
	 * 生成验证码
	 * @param ctx
	 * @throws IOException
	 *//*
	public static void get(RequestContext ctx) throws IOException {
		ctx.closeCache();
		ctx.response().setContentType("image/png");
		_Render(_GenerateRegKey(ctx), ctx.response().getOutputStream(), WIDTH, HEIGHT);
	}
	
	*//**
	 * 检查验证码是否正确
	 * @param req
	 * @return
	 *//*
	public static boolean validate(HttpServletRequest req) {
		Cookie cke = RequestUtils.getCookie(req, COOKIE_NAME);
		if(cke == null) {
			String ssnId = req.getSession().getId();
			String code1 = CacheManager.get(String.class, CACHE_REGION, ssnId);
			String code2 = req.getParameter("verifycode");
			return StringUtils.equalsIgnoreCase(code1, code2);
		}
		if(cke!=null && StringUtils.isNotBlank(cke.getValue())){
			String key = cke.getValue();
			String code1 = CacheManager.get(String.class, CACHE_REGION, key);
			String code2 = req.getParameter("verifycode");
			return StringUtils.equalsIgnoreCase(code1, code2);
		}
		return false;		
	}
	
	private static String _GenerateRegKey(RequestContext ctx) {
		Cookie cke = ctx.cookie(COOKIE_NAME);
		String REG_VALUE = null;
		if(cke==null || StringUtils.isBlank(cke.getValue())){
			REG_VALUE = RandomStringUtils.randomAlphanumeric(20);
			ctx.cookie(COOKIE_NAME, REG_VALUE, 3600);
		}
		else
			REG_VALUE = cke.getValue();
		String code = RandomStringUtils.randomAlphanumeric(LENGTH).toUpperCase();
		code = StringUtils.replace(code, "0", "W");
		code = StringUtils.replace(code, "o", "R");
		code = StringUtils.replace(code, "I", "E");
		code = StringUtils.replace(code, "1", "T");
		CacheManager.set(CACHE_REGION, REG_VALUE, code);
		if(ctx.session()!=null){
			CacheManager.set(CACHE_REGION, ctx.session().getId(), code);
		}
		return code;
	}
	
	private static void _Render(String text, OutputStream out, int width, int height) throws IOException  {
	    BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);        
	    Graphics2D g = (Graphics2D)bi.getGraphics();
	    
	    g.setColor(Color.WHITE);
	    g.fillRect(0,0,width,height);
	    g.setFont(new Font("Arial", Font.ITALIC, 18));
	    for(int i=0;i<10;i++){
	    	g.setColor(_GetRandColor(150, 250));
	    	g.drawOval(random.nextInt(110), random.nextInt(24), 5+random.nextInt(10), 5+random.nextInt(10));
	    }
	    g.setColor(_GetRandColor(10,240));
	    g.drawString(text, 5, 16);
		ImageIO.write(bi, "png", out);
    }
	
	private static Color _GetRandColor(int fc,int bc){//给定范围获得随机颜色
		if (fc > 255) fc = 255;
		if (bc > 255) bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	*//**
	 * 清除验证码信息
	 * @param req
	 *//*
	public static void clear(HttpServletRequest req) {
		Cookie cke = RequestUtils.getCookie(req, COOKIE_NAME);
		if(cke == null) {
			String ssnId = req.getSession().getId();
			CacheManager.evict(CACHE_REGION, ssnId);
		}
		if(cke!=null && StringUtils.isNotBlank(cke.getValue())){
			String key = cke.getValue();
			CacheManager.evict(CACHE_REGION, key);
		}
	}
	
	
	public static void main(String[] args) throws IOException{
		String code = RandomStringUtils.randomAlphanumeric(LENGTH).toUpperCase();
		code = "0oI1";
		code = StringUtils.replace(code, "0", "W");
		code = StringUtils.replace(code, "o", "R");
		code = StringUtils.replace(code, "I", "E");
		code = StringUtils.replace(code, "1", "T");
    	FileOutputStream out;
		out = new FileOutputStream("d:\\aa.jpg");
		_Render(code,out,60,20);  	
    }
	
}

*/