package zh.shawn.project.framework.comp.database.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.framework.comp.data.core.LowLevelDataSet;
import zh.shawn.project.framework.comp.database.exception.DataOperationException;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseLowLevelDataOperationV2 {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLowLevelDataOperationV2.class);
    private boolean autoClose = true;

    public DatabaseLowLevelDataOperationV2() {
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public boolean isAutoClose() {
        return this.autoClose;
    }

    public LowLevelDataSet query(Connection conn, String sql) throws Exception {
        long start = System.currentTimeMillis();
        TableServiceV2 ts = new TableServiceV2();
        long start2 = System.currentTimeMillis();
        TableV2 table = ts.query(conn, sql);
        long mid1 = System.currentTimeMillis();
        LowLevelDataSet ds = new LowLevelDataSet(table.getColMap(), table.getTempResult(), table.getTotalRows());
        long end = System.currentTimeMillis();
        log.info("执行语句：" + sql);
        log.debug("连接耗时: " + (start2 - start) + "ms");
        log.debug("查询耗时: " + (mid1 - start2) + "ms");
        log.debug("赋值耗时: " + (end - mid1) + "ms");
        log.info("总耗时: " + (end - start) + "ms");
        table.reset();
        table = null;
        ts = null;
        if (this.autoClose) {
            try {
                conn.close();
            } catch (SQLException var15) {
                ;
            }

            conn = null;
        }

        return ds;
    }

    public boolean update(Connection conn, String... sql) throws DataOperationException {
        TableServiceV2 ts = new TableServiceV2();
        boolean result = ts.update(conn, sql);
        ts = null;
        if (this.autoClose) {
            try {
                conn.close();
            } catch (SQLException var6) {
                ;
            }

            conn = null;
        }

        return result;
    }

}
