package my.db;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import my.cache.CacheManager;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * @author smile
 * @date 2013-2-4 下午10:16:25
 */
public class QueryHelper {
	private final static QueryRunner _g_runner = new QueryRunner();
	
	private final static ColumnListHandler _g_columnListHandler = new ColumnListHandler() {
		@Override
		protected Object handleRow(ResultSet rs) throws SQLException {
			Object obj = super.handleRow(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}

	};
	private final static ScalarHandler _g_scaleHandler = new ScalarHandler() {

		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if(obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}
		
	};
	
	@SuppressWarnings("serial")
	private final static List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>() {
		{
			add(Long.class);
			add(Integer.class);
			add(String.class);
			add(java.util.Date.class);
			add(java.sql.Date.class);
			add(java.sql.Timestamp.class);
		}
	};
	private final static boolean _IsPrimitive(Class<?> cls) {
		return cls.isPrimitive() || PrimitiveClasses.contains(cls);
	}
	
	/**
	 * 执行update,delete,insert语句
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int update(String sql, Object... params) {
		try {
			return _g_runner.update(DBmanager.getConnection(), sql, params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * 统计查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public static long stat(String sql, Object...params) {
		try {
			Number num = (Number) _g_runner.query(DBmanager.getConnection(), sql, _g_scaleHandler, params);
			return (num != null)?num.longValue() : -1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	/**
	 * 读取某个对象
	 * @param beanClass
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T read(Class<T> beanClass, String sql, Object...params) {
		try {
			return (T) _g_runner.query(DBmanager.getConnection(), sql,
					_IsPrimitive(beanClass) ? _g_scaleHandler : new BeanHandler(beanClass), params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
//	public static <T> T read_cache(Class<T> beanClass, String cache, Serializable key, String sql, Object... params) {
//		@SuppressWarnings("unchecked")
//		T obj = (T) CacheManager.get(cache, key);
//		if (obj == null) {
//			obj = read(beanClass, sql, params);
//			CacheManager.set(cache, key, (Serializable) obj);
//		}
//		return obj;
//	}
	/**
	 * 对象查询
	 * @param beanClass
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> query(Class<T> beanClass, String sql, Object...params) {
		try {
			return (List<T>) _g_runner.query(DBmanager.getConnection(), sql, 
					_IsPrimitive(beanClass) ? _g_columnListHandler	: new BeanListHandler(beanClass), params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	/**
	 * 支持缓存的对象查询
	 * @param beanClass
	 * @param cache_region
	 * @param key
	 * @param sql
	 * @param params
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public static <T> List<T> query_cache(Class<T> beanClass,
//			String cache_region, Serializable key, String sql, Object... params) {
//		List<T> objs = (List<T>) CacheManager.get(cache_region, key);
//		if (objs == null) {
//			objs = query(beanClass, sql, params);
//			CacheManager.set(cache_region, key, (Serializable) objs);
//		}
//		return objs;
//	}
	/**
	 * 分页查询
	 * @param beanClass
	 * @param sql
	 * @param page
	 * @param count
	 * @param params
	 * @return
	 */
	public static <T> List<T> query_slice(Class<T> beanClass, String sql, int page, int count, Object... params) {
		if (page < 0 || count < 0)
			throw new IllegalArgumentException("Illegal parameter of 'page' or 'count', Must be positive.");
		int from = (page - 1) * count;
		count = (count > 0) ? count : Integer.MAX_VALUE;
		return query(beanClass, sql + " LIMIT ?,?",	ArrayUtils.addAll(params, new Integer[] { from, count }));
	}
	/**
	 * 批量插入
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int[] batch(String sql, Object[][] params) {
		try{
			Connection conn = DBmanager.getConnection();
			boolean automit = conn.getAutoCommit();
			try {
				conn.setAutoCommit(false);
				int[] m = _g_runner.batch(conn, sql, params);
				conn.commit();
				return m;
			} catch (SQLException e) {
                conn.rollback();
				throw new DBException(e);
			} finally {
				conn.setAutoCommit(automit);
			}
		}catch(SQLException e){
            throw new DBException(e);
		}
	}
}
