package my.beans;

import my.db.DBbean;

public class MoviesZh extends DBbean{
	public final static MoviesZh INSTANCE = new MoviesZh();
	private String d_name;
	private String d_type_name;
	private String d_year;
	private String d_director;
	private String d_area;
	
	
	@Override
	protected String TableName() {
		return "mac_vod";
	}
	public String getD_name() {
		return d_name;
	}
	public void setD_name(String d_name) {
		this.d_name = d_name;
	}
	public String getD_type_name() {
		return d_type_name;
	}
	public void setD_type_name(String d_type_name) {
		this.d_type_name = d_type_name;
	}
	public String getD_year() {
		return d_year;
	}
	public void setD_year(String d_year) {
		this.d_year = d_year;
	}
	public String getD_director() {
		return d_director;
	}
	public void setD_director(String d_director) {
		this.d_director = d_director;
	}
	public String getD_area() {
		return d_area;
	}
	public void setD_area(String d_area) {
		this.d_area = d_area;
	}
	
}
