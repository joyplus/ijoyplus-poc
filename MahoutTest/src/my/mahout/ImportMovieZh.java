package my.mahout;

import java.util.ArrayList;
import java.util.List;

import my.beans.MovieZh;
import my.beans.MoviesZh;

public class ImportMovieZh {
	public static void main(String[] args) {
		List<MoviesZh> mszh = (List<MoviesZh>) MoviesZh.INSTANCE.List(1, 4000);
		List<MovieZh> mzh = new ArrayList<MovieZh>();
		for(MoviesZh m : mszh) {
			MovieZh z = new MovieZh();
			z.setArea(m.getD_area());
			z.setDirector(m.getD_director());
			z.setName(m.getD_name());
			z.setType(m.getD_type_name());
			z.setYear(m.getD_year());
			z.Save();
		}
		System.out.println("done!");
	}
}
