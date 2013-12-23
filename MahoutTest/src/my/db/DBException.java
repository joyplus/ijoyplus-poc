package my.db;

import java.io.PrintStream;
import java.io.PrintWriter;
/**
 * 
 * @author smile
 * @date 2013-2-4 下午10:15:37
 */
public class DBException extends RuntimeException{
	public DBException(Exception cause) {
		super(cause);
	}

	@Override
	public String getLocalizedMessage() {
		return getCause().getLocalizedMessage();
	}

	@Override
	public String getMessage() {
		return getCause().getMessage();
	}

	@Override
	public void printStackTrace() {
		getCause().printStackTrace();
	}

	@Override
	public void printStackTrace(PrintStream s) {
		getCause().printStackTrace(s);
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		getCause().printStackTrace(s);
	}
	
}
