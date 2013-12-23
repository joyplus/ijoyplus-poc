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

import my.beans.Movie;
import my.db.DBmanager;
import my.utils.StringsUtils;

public class ImportMovies {
	public final static String TABLE_NAME = "movies";
	public final static String ID_COLUMN = "id";
	public final static String NAME_COLUMN = "name";
	public final static String PUBLISHED_YEAR_COLUMN = "publishedYear";
	public final static String TYPE_COLUMN = "type";


	public static void main(String[] args) {
		try {
			LineNumberReader lineReader = new LineNumberReader(new FileReader(
					"/home/ubuntu/mahout/movies.dat"));
			String line = "";
			List<Movie> movieList = new ArrayList<Movie>();
			while ((line = lineReader.readLine()) != null) {
				movieList.add(fillMovie(line));
			}
			persist(movieList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void persist(List<Movie> movies) {
		Connection conn = DBmanager.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into " + TABLE_NAME + " ( " + ID_COLUMN + ", "
				+ NAME_COLUMN + ", " + PUBLISHED_YEAR_COLUMN + ", "
				+ TYPE_COLUMN + ") values (?, ?, ?, ?)";
		try {
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(sql);

			for (Movie movie : movies) {
				ps.setLong(1, movie.getId());
				ps.setString(2, movie.getName());
				ps.setString(3, movie.getYear());
				ps.setString(4, movie.getType());
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

	private static Movie fillMovie(String line) {
		Movie movie = new Movie();
		String[] mo = line.split("::");
		movie.setId(Integer.parseInt(mo[0]));
		movie.setName(mo[1].substring(0, mo[1].lastIndexOf("(") - 1));
		movie.setYear(mo[1].substring(mo[1].lastIndexOf("(") + 1,
				mo[1].lastIndexOf(")")));
		movie.setType(mo[2].replace("|", ","));
		return movie;
	}
}
