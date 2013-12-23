package my.mahout;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import my.beans.Rating;

public class ExportMovies {
	public static void main(String[] args) throws IOException {
		List<Rating> ratings = (List<Rating>) Rating.INSTANCE.List();
		FileWriter fw = new FileWriter("/home/ubuntu/mahout/rating.txt");
		for(Rating m : ratings) {
			fw.write(String.valueOf(m.getUserID()) +","+ String.valueOf(m.getMovieID())+","+m.getPreference()+","+m.getTimestamp()+"\n");
		}
		fw.close();
		System.out.println("done\n");
	}
}
