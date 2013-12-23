package my.mahout;

import java.util.ArrayList;
import java.util.List;

import my.beans.MovieZh;
import my.beans.Rating;
import my.mahout.recommender.MyItemBasedRecommender;
import my.mahout.recommender.MySlopeOneRecommender;
import my.mahout.recommender.MyUserBasedRecommender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class Mahout {
	public final static String source = "/home/ubuntu/mahout/rating.txt";
	public final static Log log = LogFactory.getLog(Mahout.class);
	public List<MovieZh> recommend(long uid, int size, String type) {
		if(uid==0 || size==0 || type==null)
			return null;
		List<RecommendedItem> RecommendedItems = new ArrayList<RecommendedItem>();
		List<MovieZh> movies = new ArrayList<MovieZh>();
		if(type.equalsIgnoreCase("user")) {
			RecommendedItems = userRecommend(uid, size);
		}else if(type.equalsIgnoreCase("item")) {
			RecommendedItems = itemRecommend(uid, size);
		}else if(type.equalsIgnoreCase("user-item")) {
			RecommendedItems = userItemRecommend(uid, size);
		}
		for(RecommendedItem i : RecommendedItems) {
			MovieZh m = MovieZh.INSTANCE.Get(i.getItemID());
			movies.add(m);
		}
		
		return movies;
	}
	
	public List<MovieZh> userMovies(long id) {
		List<MovieZh> movies = new ArrayList<MovieZh>();
		List<Rating> ratings = (List<Rating>) Rating.INSTANCE.List("userID="+String.valueOf(id));
		for(Rating r : ratings) {
			MovieZh m = MovieZh.INSTANCE.Get(r.getMovieID());
			movies.add(m);
		}
		return movies;
	}
	public List<RecommendedItem> userRecommend(long uid, int size) {
		return new MyUserBasedRecommender().userBasedRecommender(uid, size);
	}
	
	public List<RecommendedItem> itemRecommend(long uid, int size) {
		return new MyItemBasedRecommender().myItemBasedRecommender(uid, size);
	}
	
	public List<RecommendedItem> userItemRecommend(long uid, int size) {
		return new MySlopeOneRecommender().mySlopeOneRecommender(uid, size);
	}
}
