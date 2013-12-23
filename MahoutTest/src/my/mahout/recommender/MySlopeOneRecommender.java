package my.mahout.recommender;

import java.io.File;
import java.util.List;

import my.db.DBmanager;
import my.mahout.Mahout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class MySlopeOneRecommender {
	public static Log log = LogFactory.getLog(MySlopeOneRecommender.class);
	public List<RecommendedItem> mySlopeOneRecommender(long userID,int size){
		List<RecommendedItem> recommendations = null;
		try {
			log.info("获取数据模型");
			DataModel model = new FileDataModel(new File(Mahout.source));//构造数据模型
			
			//DataModel model = new MySQLJDBCDataModel(DBmanager.getDataSource(),"movie_preferences", "userID", "movieID","preference", "timestamp");//构造数据模型
			log.info("构造推荐引擎");
			SlopeOneRecommender sr = new SlopeOneRecommender(model);
			log.info("缓存结果");
			Recommender recommender = new CachingRecommender(sr);
			log.info("获得推荐");
			recommendations = recommender.recommend(userID, size);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return recommendations;
	}

}
