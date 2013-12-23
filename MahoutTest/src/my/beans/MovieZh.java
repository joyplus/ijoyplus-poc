package my.beans;

import my.db.DBbean;

public class MovieZh extends DBbean{
	public static final MovieZh INSTANCE = new MovieZh();
	private String name;
	private String type;
	private String year;
	private String director;
	private String area;
	
	
	@Override
	protected String TableName() {
		return "movies_zh";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	
}
