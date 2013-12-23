package my.toolbox;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class FormatTool {
	/**
	 * 格式化日期显示
	 * @param format
	 * @param date
	 * @return
	 */
	public static String date(String format, Date date) {
		if(date==null)
			return "";
		SimpleDateFormat  sf = new SimpleDateFormat(format);
		return sf.format(date);
	}
	
	/**
	 * 格式化html
	 * @param content
	 * @return
	 */
	public static String html(String content) {
        if(content==null) return "";        
		String html = content;
        html = StringUtils.replace(html, "'", "&apos;");
		html = StringUtils.replace(html, "\"", "&quot;");
		html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
		html = StringUtils.replace(html, "<", "&lt;");
		html = StringUtils.replace(html, ">", "&gt;");
		return html;
	}
	
	/**
	 * 截取字符串长度
	 * @param text
	 * @param length
	 * @return
	 */
	public static String prefix(String text, int length) {
		int count = 0;
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			int j = text.charAt(i);
			if ((j & 0xff00 >>> 8) != 0)
				count++;
			if (count <= length)
				res.append((char) j);
			else
				break;
		}
		return res.toString();
	}
	/**
	 * URL编码
	 * 
	 * @param url
	 * @return
	 */
	public static String encode_url(String url) {
		return encode_url(url, "utf-8");
	}

	/**
	 * URL编码
	 * 
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String encode_url(String url, String charset) {
		if (StringUtils.isEmpty(url))
			return "";
		try {
			return URLEncoder.encode(url, charset);
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}
	/**
	 * 分页数
	 * @param recordCount
	 * @param perPage
	 * @return
	 */
	public static int pageCount(long recordCount, int perPage) {
		int pc = (int)Math.ceil(recordCount / (double)perPage);
		return (pc==0)?1:pc;
	}
	public static int pageCount(long recordCount, long perPage) {
		int pc = (int)Math.ceil(recordCount / (double)perPage);
		return (pc==0)?1:pc;
	}
	public static int pageCount(int recordCount, long perPage) {
		int pc = (int)Math.ceil(recordCount / (double)perPage);
		return (pc==0)?1:pc;
	}
	public static int pageCount(int recordCount, int perPage) {
		int pc = (int)Math.ceil(recordCount / (double)perPage);
		return (pc==0)?1:pc;
	}
}
