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
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class MyItemBasedRecommender {
	public final static Log log = LogFactory.getLog(MyItemBasedRecommender.class);
	public List<RecommendedItem> myItemBasedRecommender(long userID,int size){
		List<RecommendedItem> recommendations = null;
		try {
			log.info("从数据库获取model");
			DataModel model = new FileDataModel(new File(Mahout.source));//构造数据模型，File-based
			//DataModel model = new MySQLJDBCDataModel(DBmanager.getDataSource(),"movie_preferences", "userID", "movieID","preference", "timestamp");//构造数据模型
			log.info("model个数:"+model.getNumItems());
			log.info("开始计算相似度");
			ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);//计算内容相似度
			log.info("开始构造推荐引擎");
			Recommender recommender = new GenericItemBasedRecommender(model, similarity);//构造推荐引擎
			log.info("缓存结果");
			Recommender cachingRecommender = new CachingRecommender(recommender); //缓存结果
			log.debug("获取推荐结果");
			recommendations = cachingRecommender.recommend(userID, size);//得到推荐结果
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recommendations;
	}
}
