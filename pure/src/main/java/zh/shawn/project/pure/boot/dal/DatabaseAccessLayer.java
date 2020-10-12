package zh.shawn.project.pure.boot.dal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.comp.data.core.LowLevelDataSet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface DatabaseAccessLayer {
	public static Logger log = LoggerFactory.getLogger(DatabaseAccessLayer.class);

	public static Map<String, DataSource> DATASOURCE = new HashMap<String, DataSource>(3);
	public static Map<String, String> NAMEDQUERY_PRESTMT = new HashMap<String, String>(100, 0.75f);
	public static Map<String, String> NAMEDQUERY_DACIAQUERY = new HashMap<String, String>(100, 0.75f);

	public static Set<String> getAllDatasourceName() {
		return DatabaseAccessLayer.DATASOURCE.keySet();
	}

	public Map<String, String> getAllNamedQuery();

	public static Connection getConnection(String datasourceName) {
		try {
			if (!DatabaseAccessLayer.DATASOURCE.containsKey(datasourceName)) {
				return null;
			}
			return DatabaseAccessLayer.DATASOURCE.get(datasourceName).getConnection();
		} catch (SQLException e) {
			log.error("数据库连接获取失败", e);
			return null;
		}
	} 
 
	public LowLevelDataSet query(String sql, Connection... connections);

	public LowLevelDataSet queryNamedSql(String queryName, Object... args);

	public static void addDatasource(String datasourceName, DataSource dataSource) {
		DatabaseAccessLayer.DATASOURCE.put(datasourceName, dataSource);
	}

	public static void clearAllDatasource() {
		DatabaseAccessLayer.DATASOURCE.clear();
	}

	public static void clearAllNamedQuery() {
		DatabaseAccessLayer.NAMEDQUERY_DACIAQUERY.clear();
		DatabaseAccessLayer.NAMEDQUERY_PRESTMT.clear();
	}
}
