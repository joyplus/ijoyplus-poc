package my.mahout.recommender;

import java.io.File;
import java.util.List;

import my.db.DBmanager;
import my.mahout.Mahout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


public class MyUserBasedRecommender {
	public final static Log log = LogFactory.getLog(MyUserBasedRecommender.class);
	public List<RecommendedItem> userBasedRecommender(long userID,int size) {
		// step:1 构建模型 2 计算相似度 3 查找k紧邻 4 构造推荐引擎
		List<RecommendedItem> recommendations = null;
		try {
			log.info("获取model");
			//DataModel model = new MySQLJDBCDataModel(DBmanager.getDataSource(),"movie_preferences", "userID", "movieID","preference", "timestamp");//构造数据模型
			DataModel model = new FileDataModel(new File(Mahout.source));//构造数据模型
			log.info("获取相似度");
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);//用PearsonCorrelation 算法计算用户相似度
			log.info("计算用户邻居");
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, model);//计算用户的“邻居”，这里将与该用户最近距离为 3 的用户设置为该用户的“邻居”。
			log.info("构造推荐引擎并缓存");
			Recommender recommender = new CachingRecommender(new GenericUserBasedRecommender(model, neighborhood, similarity));//采用 CachingRecommender 为 RecommendationItem 进行缓存
			log.info("获得推荐结果");
			recommendations = recommender.recommend(userID, size);//得到推荐的结果，size是推荐接过的数目
			log.info("结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recommendations;
	}
}