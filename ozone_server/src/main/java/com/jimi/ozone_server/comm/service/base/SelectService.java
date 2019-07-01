package com.jimi.ozone_server.comm.service.base;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jimi.ozone_server.comm.model.MysqlMappingKit;

import cc.darhao.dautils.api.StringUtil;

/**
 * 通用查询业务层<br>
 * 本工具依赖DaUtils
 * <br>
 * <b>2019年4月4日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class SelectService {

	/**
	 * 单表查询，支持筛选、限制和排序
	 */
	public List<Record> select(String table, Integer offset, Integer limit, String ascBy, String descBy, Set<Expression> expressions){
		return select(null, new String[] {table}, null, offset, limit, ascBy, descBy, expressions);
	}
	
	
	/**
	 * 单表查询，支持筛选、限制和排序，可选数据源
	 */
	public List<Record> select(String datasource, String table, Integer offset, Integer limit, String ascBy, String descBy, Set<Expression> expressions){
		return select(datasource, new String[] {table}, null, offset, limit, ascBy, descBy, expressions);
	}


	/**
	 * 连表查询，支持筛选、限制和排序
	 */
	public List<Record> select(String[] tables, String[] refers, Integer offset, Integer limit, String ascBy, String descBy, Set<Expression> expressions){
		return select(null, tables, null, offset, limit, ascBy, descBy, expressions);
	}
	
	
	/**
	 * 连表查询，支持筛选、限制和排序，可选数据源
	 */
	public List<Record> select(String datasource, String[] tables, String[] refers, Integer offset, Integer limit, String ascBy, String descBy, Set<Expression> expressions){
		StringBuffer sql = new StringBuffer();
		createResultSet(tables, sql);
		createFrom(tables, refers, sql);
		createWhere(expressions, sql);
		createOrderBy(ascBy, descBy, sql);
		createLimit(offset, limit, sql);
		return exeSQL(datasource, fillWhere(expressions,sql));
	}
	
	
	/**
	 * 防止SQL注入攻击
	 */
	private SqlPara fillWhere(Set<Expression> expressions, StringBuffer sql) {
		SqlPara sqlPara = new SqlPara();
		sqlPara.setSql(sql.toString());
		if(expressions != null && !expressions.isEmpty()) {
			for (Expression expression : expressions) {
				sqlPara.addPara(expression.value);
			}
		}
		return sqlPara;
	}


	private List<Record> exeSQL(String datasource, SqlPara sql) {
		if(datasource == null) {
			return Db.find(sql);
		}else {
			return Db.use(datasource).find(sql);
		}
	}


	private void createLimit(Integer offset, Integer limit, StringBuffer sql) {
		if(offset == null && limit == null) {
			return;
		}else if(offset != null && limit == null) {
			throw new RuntimeException("offset必须和limit搭配使用");
		}else if(offset == null && limit != null) {
			sql.append(" LIMIT " + limit);
		}else if(offset != null && limit != null) {
			sql.append(" LIMIT " + offset + " , " + limit);
		}
	}


	private void createFrom(String[] tables, String[] refers, StringBuffer sql) {
		//表名非空判断
		if(tables == null) {
			throw new RuntimeException("表名必须提供");
		}
		//创建FROM
		sql.append(" FROM ");
		for (String table : tables) {
			sql.append(table + " JOIN ");
		}
		sql.delete(sql.lastIndexOf("JOIN"), sql.length());
		//创建ON
		if(refers != null) {
			sql.append(" ON ");
			for (String refer : refers) {
				sql.append(refer);
				sql.append(" AND ");
			}
			sql.delete(sql.lastIndexOf("AND"), sql.length());
		}
	}

	
	private void createWhere(Set<Expression> expressions, StringBuffer sql) {
		//判断filter存在与否
		if(expressions != null && !expressions.isEmpty()) {
			sql.append(" WHERE ");
			for (Expression expression: expressions) {
				if (expression.operation.equals("like")) {	// 判断是否进行模糊查询
					expression.value = "%" + expression.value + "%";
				}
				sql.append(expression.colum + " " + expression.operation + " ? AND ");
			}
			sql.delete(sql.lastIndexOf("AND"), sql.length());
		}
	}


	private void createOrderBy(String ascBy, String descBy, StringBuffer sql) {
		if(ascBy != null && descBy != null) {
			throw new RuntimeException("升序排序和倒序排序不能同时指定");
		}else if(ascBy != null) {
			sql.append(" ORDER BY " + ascBy + " ASC ");
		}else if(descBy != null){
			sql.append(" ORDER BY " + descBy + " DESC ");
		}
	}


	private void createResultSet(String[] tables, StringBuffer sql) {
		if(tables.length == 1) {
			sql.append("SELECT *");
			return;
		}
		sql.append(" SELECT ");
		PropKit.use("properties.ini");
		String baseModelPackage = PropKit.get("baseModelPackage");
		for (String table : tables) {
			try {
				String beanClassName = StrKit.firstCharToUpperCase(StringUtil.trimUnderLineAndToUpCase(table));
				Class<?> beanClass = Class.forName(baseModelPackage + ".Base" + beanClassName);
				Method[] methods = beanClass.getMethods();
				for (Method method : methods) {
					String methodName = method.getName();
					if (methodName.startsWith("set") == false || methodName.length() <= 3) {
						continue;
					}
					Class<?>[] types = method.getParameterTypes();
					if (types.length != 1) {	
						continue;
					}
					String attrName = methodName.substring(3);
					String colName = StringUtil.toLowCaseAndInsertUnderLine(StrKit.firstCharToLowerCase(attrName));
					sql.append(table + "." + colName + " AS " + beanClassName + "_" + attrName);
					sql.append(",");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		sql.delete(sql.lastIndexOf(","), sql.length());
	}
	
	
	public static class Expression {
		public static final String LIKE = "like";
		public static final String EQUAL = "=";
		public static final String GREATER = ">";
		public static final String LESS = "<";
		public static final String GREATER_EQUAL = ">=";
		public static final String LESS_EQUAL = "<=";
		private String colum;
		private String operation;
		private Object value;
		public Expression(String colum, String operation, Object value) {
			this.colum = colum;
			this.operation = operation;
			this.value = value;
		}
	}


	public static void main(String[] args) {
		PropKit.use("properties.ini");
		DruidPlugin dp = new DruidPlugin(PropKit.get("d_url"), PropKit.get("d_user"), PropKit.get("d_password"));
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
	    arp.setDialect(new MysqlDialect());	// 用什么数据库，就设置什么Dialect
	    arp.setShowSql(true);
	    MysqlMappingKit.mapping(arp);
		dp.start();
		arp.start();
//		SelectService selectService = new SelectService();
//		String result; 
////		result = selectService.select("adct", 1, null, null, null, null).toString();//预期报错
////		System.out.println(result);
//		result = selectService.select("adct", 1, 1, null, null, null).toString();//预期1条
//		System.out.println(result);
//		result = selectService.select("adct", null, null, null, "name", null).toString();//预期名字倒序
//		System.out.println(result);
////		result = selectService.select("adct", null, null, "id", "name", null).toString();//预期报错
////		System.out.println(result);
//		result = selectService.select("adct", null, null, "address", null, null).toString();//预期地址升序
//		System.out.println(result);
//		
//		Set<Expression> expressions = new HashSet<>();
//		expressions.add(new Expression("name", Expression.LIKE, "BoBo"));
//		expressions.add(new Expression("id", Expression.EQUAL, 1));
//		result = selectService.select("adct", null, null, null, null, expressions).toString();//预期1条
//		System.out.println(result);
//		result = selectService.select(new String[] {"adct_log","adct_log_type"}, new String[] {"adct_log.type=adct_log_type.id"}, null, null, null, null, null).toString();
//		System.out.println(result);
	}
	
}
