package my.mahout;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import my.beans.Rating;
import my.db.DBmanager;

public class ImportRatings {

	public final static String TABLE_NAME = "movie_preferences";
	public final static String USER_ID_COLUMN = "userID";
	public final static String MOVIE_ID_COLUMN = "movieID";
	public final static String RATING = "preference";
	public final static String TIMESTAMP = "timestamp";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LineNumberReader lineReader = new LineNumberReader(new FileReader(
					"/home/ubuntu/mahout/ratings.dat"));
			String line = "";
			List<Rating> ratingList = new ArrayList<Rating>();
			while ((line = lineReader.readLine()) != null) {
				ratingList.add(fillRating(line));
			}
			System.out.println("before insert\n");
			insertRatings(ratingList);
			System.out.println("after insert\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Rating fillRating(String line) {

		String[] ra = line.split("::");
		Rating rating = new Rating();
		rating.setUserID(Integer.parseInt(ra[0]));
		rating.setMovieID(Integer.parseInt(ra[1]));
		rating.setPreference(Integer.parseInt(ra[2]));
		rating.setTimestamp(Integer.parseInt(ra[3]));
		return rating;
	}


	public static void insertRatings(List<Rating> ratings) {
		Connection conn = DBmanager.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into " + TABLE_NAME + " ( " + USER_ID_COLUMN
				+ ", " + MOVIE_ID_COLUMN + ", " + RATING + ", " + TIMESTAMP
				+ ") values (?, ?, ?, ?)";
		try {
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(sql);

			for (Rating rating : ratings) {
				ps.setInt(1, rating.getUserID());
				ps.setInt(2, rating.getMovieID());
				ps.setInt(3, rating.getPreference());
				ps.setInt(4, rating.getTimestamp());
				ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
