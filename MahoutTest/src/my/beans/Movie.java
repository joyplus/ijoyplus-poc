package my.beans;

import my.db.DBbean;


public class Movie extends DBbean {
	
	public static Movie INSTANCE = new Movie();
	
	private String name;
	private String year;
	private String type;

	

	@Override
	protected String TableName() {
		return "movies";
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}