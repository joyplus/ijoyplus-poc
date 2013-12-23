package my.beans;

import my.db.DBbean;

public class Rating extends DBbean{
	public static final Rating INSTANCE = new Rating();
	private int userID;
	private int movieID;
	private int preference;
	private int timestamp;

	
	@Override
	protected String TableName() {
		return "movie_preferences";
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getMovieID() {
		return movieID;
	}

	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}

	public int getPreference() {
		return preference;
	}

	public void setPreference(int preference) {
		this.preference = preference;
	}

	public int getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
}