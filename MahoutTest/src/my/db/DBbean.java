package my.db;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.DbUtils;

/**
 * 数据库对象的基类
 * @author smile
 * @date 2013-2-4 下午10:15:28
 */
public class DBbean implements Serializable{
	private static final long serialVersionUID = 1L;
	private long __key_id;
	private String __table_name;
	public long getId() { return __key_id; }
	public void setId(long id) { this.__key_id = id; }
	
	/**
	 * 子类须覆盖此方法
	 * @return
	 */
	protected String TableName() {
		return __table_name;
	}
	/**
	 * 返回对象对应的缓存区域名
	 * 
	 * @return
	 */
	public String CacheRegion() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * 根据对象主键更新字段
	 * @param field
	 * @param value
	 * @return
	 */
	public boolean UpdateField(String field, Object value) {
		String sql = "UPDATE " + TableName() + " SET " + field + " = ? WHERE id = ?";
		return QueryHelper.update(sql, value, getId()) > 0;
	}
	
	/**
	 * 根据filter更新字段
	 * @param field
	 * @param value
	 * @param filter
	 * @return
	 */
	public boolean UpdateField(String field, Object value, String filter) {
		String sql = "UPDATE " + TableName() + " SET " + field + " = ? WHERE " + field;
		return QueryHelper.update(sql, value) > 0;
	}
	/**
	 * 更新对象
	 * @param escape
	 *		不需更新的字段
	 * @return
	 */
	public long Update(List<String> escape) {
		Map<String, Object> pojo_bean = this.ListInsertableFields();
		String[] fields = pojo_bean.keySet().toArray(
				new String[pojo_bean.size()]);
		List<String> newitems = new ArrayList<String>();
		for (String str : fields) {
			if (!escape.contains(str))
				newitems.add(str);
		}
		fields = newitems.toArray(new String[newitems.size()]);
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(this.TableName());
		sql.append(" SET ");
		for (int i = 0; i < fields.length; i++) {
			sql.append("`");
			sql.append(fields[i]);
			sql.append("` = ?");
			if (i < fields.length - 1)
				sql.append(" , ");
		}
		sql.append(" WHERE id = ?");
		List<Object> param = new ArrayList<Object>();
		for (int i = 0; i < fields.length; i++) {
			param.add(pojo_bean.get(fields[i]));
		}
		param.add(getId());
		Object[] params = param.toArray();
		int rt = QueryHelper.update(sql.toString(), params);
		return rt == 1 ? getId() : 0;
	}
	/**
	 * 根据主键删除对象
	 * @return
	 */
	public boolean Delete() {
		String sql = "DELETE FROM " + TableName() + " WHERE id = ?";
		return QueryHelper.update(sql, getId()) > 0;
	}
	
	/**
	 * 根据filter删除对象
	 * @param filter
	 * @return
	 */
	public boolean Delete(String filter) {
		String sql = "DELETE FROM " + TableName() + " WHERE " + filter;
		return QueryHelper.update(sql) > 0;
	}
	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public <T extends DBbean> T Get(long id) {
		if(id <= 0)
			return null;
		String sql = "SELECT * FROM " + TableName() + " WHERE id = ?";
		return (T) QueryHelper.read(getClass(), sql, id);
	}
	
	/**
	 * 根据filter获取对象
	 * @param filter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends DBbean> T Get(String filter) {
		String sql = "SELECT * FROM " + TableName() + " WHERE " + filter;
		return (T) QueryHelper.read(getClass(), sql);
	}
	
	/**
	 * 列出所有对象
	 * @return
	 */
	public List<? extends DBbean> List() {
		String sql = "SELECT * FROM " + TableName();
		return QueryHelper.query(getClass(), sql);
	}
	/**
	 * 根据filter列出所有对象
	 * @param filter
	 * @return
	 */
	public List<? extends DBbean> List(String filter) {
		String sql = "SELECT * FROM " + TableName() + " WHERE " + filter;
		return QueryHelper.query(getClass(), sql);
	}
	/**
	 * 分页列出所有对象
	 * @param page
	 * @param size
	 * @return
	 */
	public List<? extends DBbean> List(int page, int size) {
		String sql = "SELECT * FROM " + TableName();
		return QueryHelper.query_slice(getClass(), sql, page, size);
	}
	
	/**
	 * 根据filter分页列出所有对象
	 * @param page
	 * @param size
	 * @param filter
	 * @return
	 */
	public List<? extends DBbean> List(int page, int size, String filter) {
		String sql = "SELECT * FROM " + TableName() + " WHERE " + filter;
		return QueryHelper.query_slice(getClass(), sql, page, size);
	}
	/**
	 * 获取此对象的记录数
	 * @param filter
	 * @return
	 */
	public long Count() {
		String sql = "SELECT COUNT(*) FROM " +TableName();
		return QueryHelper.stat(sql);
	}
	/**
	 * 获取此对象的记录数
	 * @param filter
	 * @return
	 */
	public long Count(String filter) {
		String sql = "SELECT COUNT(*) FROM " +TableName() + " WHERE " + filter ;
		return QueryHelper.stat(sql);
	}
	/**
	 * 插入对象到数据库
	 * @return
	 */
	public long Save() {
		if (getId() > 0)
			_InsertObject(this);
		else
			setId(_InsertObject(this));
		return getId();
	}

	/**
	 * 插入对象
	 * @param obj
	 * @return 插入到数据库的主键
	 */
	private long _InsertObject(DBbean obj) {
		Map<String, Object> pojo_bean = obj.ListInsertableFields();
		String[] fields = pojo_bean.keySet().toArray(
				new String[pojo_bean.size()]);
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(obj.TableName());
		sql.append("(`");
		for (int i = 0; i < fields.length; i++) {
			if (i > 0)
				sql.append("`,`");
			sql.append(fields[i]);
		}
		sql.append("`) VALUES(");
		for (int i = 0; i < fields.length; i++) {
			if (i > 0)
				sql.append(',');
			sql.append('?');
		}
		sql.append(')');
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DBmanager.getConnection().prepareStatement(sql.toString(),
					PreparedStatement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < fields.length; i++) {
				ps.setObject(i + 1, pojo_bean.get(fields[i]));
			}
			ps.executeUpdate();
			if (getId() > 0)
				return getId();

			rs = ps.getGeneratedKeys();
			return rs.next() ? rs.getLong(1) : -1;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			sql = null;
			fields = null;
			pojo_bean = null;
		}
	}
	/**
	 * 列出要插入到数据库的域集合，子类可以覆盖此方法
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> ListInsertableFields() {
		try {
			Map<String, Object> props = BeanUtils.describe(this);
			if (getId() <= 0)
				props.remove("id");
			props.remove("class");
			return props;
		} catch (Exception e) {
			throw new RuntimeException("Exception when Fetching fields of " + this);
		}
	}
}
