package my.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import my.service.RequestContext;

/**
 * 文件存储
 * @author smile
 *
 */
public class StorageUtils {
	public final static String FILE_APTH_FORMAT = "yyyyMM/ddHHmmss_";
	public static StorageUtils INSTANCE = new StorageUtils();
	public static String rootPath = RequestContext.root();
	private String prePath;
	
	public StorageUtils(String preDir) {
		this.prePath = "uploads"+File.separator+preDir+File.separator;
	}
	
	public StorageUtils() {
		this.prePath = "uploads"+File.separator;
	}
	
	/**
	 * 获取文件路径
	 * @param filename
	 * @return
	 */
	public String path(String filename) {
		return this.prePath + DateFormatUtils.format(new Date(), FILE_APTH_FORMAT)
				+RandomStringUtils.randomAlphanumeric(4)+"."
				+FilenameUtils.getExtension(filename).toLowerCase();
	}
	
	/**
	 * 保存文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String save(File file) throws IOException {
		String path = path(file.getName());
		save(file, rootPath + path);
		return path;
	}
	
	/**
	 * 读取文件
	 * @param path
	 * @return
	 */
	public File read(String path) {
		return new File(rootPath + path);
	}
	
	/**
	 * 删除文件
	 * @param path
	 * @throws IOException
	 */
	public void delete(String path) throws IOException {
		FileUtils.forceDelete(new File(rootPath+path));
	}
	
	/**
	 * 文件是否存在
	 * @param path
	 * @return
	 */
	public boolean exist(String path) {
		return new File(rootPath + path).exists();
	}
	/**
	 * 保存文件到指定路径
	 * @param file
	 * @param path
	 * @throws IOException
	 */
	private void save(File file, String path) throws IOException {
		File dest = new File(path);
		if(!dest.getParentFile().exists())
			dest.getParentFile().mkdirs();
		FileUtils.copyFile(file, dest);
	}
	
	/**
	 * 文件是否合法
	 * @param filename
	 * @return
	 */
	public static boolean isLegalFile(String filename) {
		String ext = FilenameUtils.getExtension(filename).toLowerCase();
		return mime_types.containsKey(ext);
	}
	
	/**
	 * 判断图片是否合法
	 * @param filename
	 * @return
	 */
	public static boolean isLegalImg(String filename) {
		String ext = FilenameUtils.getExtension(filename).toLowerCase();
		return imageTypes.contains(ext);
	}
	
	/**
	 * 图片类型
	 */
	public final static List<String> imageTypes = new ArrayList<String>(){{
		add("gif");
		add("jpg");
		add("jpge");
		add("bmp");
		add("png");
	}};
	/**
	 * 文档类型
	 */
	public final static HashMap<String, String> mime_types = new HashMap<String, String>()
			{{
				put("jar","application/java-archive");
				put("jad","text/vnd.sun.j2me.app-descriptor");
				put("sis","application/vnd.symbian.install");
				put("sisx","x-epoc/x-sisx-app");
				put("thm","application/vnd.eri.thm");
				put("nth","application/vnd.nok-s40theme");
				put("zip","application/zip");
				put("rar","application/octet-stream");
				put("cab","application/octet-stream");
				put("gz","application/x-gzip");
				put("bz2","application/bzip2");
				put("tar","application/x-tar");
				
				put("gif","image/gif");
				put("jpg","image/jpeg");
				put("jpeg","image/jpeg");
				put("png","image/png");
				put("bmp","image/bmp");

				put("avi","video/x-msvideo");
				put("rm","application/vnd.rn-realmedia"); 
				put("3gp","video/3gpp");
				put("wmv","video/x-ms-wmv");
				put("mpg","video/mpg");
				put("asf","video/x-ms-asf");
				put("flv","video/x-flv");
				put("mp4","video/mp4");

				put("wma","audio/x-ms-wma"); 
				put("mp3","audio/mp3");
				put("arm","audio/amr");
				put("mid","audio/x-midi");
				put("aac","audio/aac");
				put("imy","audio/imelody");

				put("swf", "application/x-shockwave-flash");

				put("txt","text/plain");
				put("htm","text/html");
				put("html","text/html");
				put("pdf","application/pdf");
				put("doc","application/msword");
				put("rtf","application/msword");
				put("docx","application/msword");
				put("xls","application/vnd.ms-excel");
				put("ppt","application/vnd.ms-powerpoint");
				put("pps","application/vnd.ms-pps");
				put("xlsx","application/vnd.ms-excel");
				put("pptx","application/vnd.ms-powerpoint");
				put("chm","application/octet-stream");
				put("crx","application/octet-stream");
			}};
}
