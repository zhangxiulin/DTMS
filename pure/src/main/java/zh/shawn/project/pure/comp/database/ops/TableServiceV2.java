package zh.shawn.project.pure.comp.database.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.boot.utils.RandomUtils;
import zh.shawn.project.pure.comp.database.exception.DataUpdateException;

import java.sql.*;
import java.util.HashMap;

public class TableServiceV2 {

    public static final int MAX_FETCH_SIZE = 1;
    private static final Logger log = LoggerFactory.getLogger(TableServiceV2.class);

    public TableServiceV2() {
    }

    public TableV2 query(Connection conn, String sql) throws Exception {
        long s1 = System.currentTimeMillis();
        long s2 = 0L;
        log.debug("数据库查询操作开始执行：" + sql);
        Statement st = this.createStmt(conn, true);
        ResultSet rs = st.executeQuery(sql);
        TableV2 table = new TableV2();

        try {
            if (rs != null) {
                table = table.addColumns(TableUtils.mapColumns(rs, new HashMap()));
                s2 = System.currentTimeMillis();
                Object[][] r = TableUtils.mapValues(rs, table.getColMap().size());
                if (r != null && r.length > 0) {
                    table = table.addData(r);
                }

                r = null;
            }

            rs.close();
            rs = null;
            st.close();
            st = null;
        } catch (SQLException var12) {
            rs.close();
            rs = null;
            st.close();
            st = null;
            log.error("映射数据时发生错误。", var12);
        } catch (Exception var13) {
            rs.close();
            rs = null;
            st.close();
            st = null;
            log.warn("映射数据时发生错误，有可能是因为空表造成的，若是空表引起出错，请忽略。", var13);
        }

        long s3 = System.currentTimeMillis();
        log.debug("映射数据结构耗时：" + (s2 - s1) + "ms");
        log.debug("映射数据耗时：" + (s3 - s2) + "ms");
        log.debug("数据映射总耗时：" + (s3 - s1) + "ms");
        return table;
    }

    public boolean updateTX(Connection conn, String... sqls) throws DataUpdateException {
        Statement stmt = this.createStmt(conn, false);
        log.debug("数据库更新操作批处理开始执行，执行语句数：" + (sqls != null ? sqls.length : "0"));
        Savepoint sp = null;

        try {
            conn.setAutoCommit(false);
            sp = conn.setSavepoint("update_" + RandomUtils.genRandomString("yyyyMMddHHmmss", 0));
            log.debug("已生成回退节点：" + sp.getSavepointName());
            String[] var8 = sqls;
            int var7 = sqls.length;

            for(int var6 = 0; var6 < var7; ++var6) {
                String sql = var8[var6];
                stmt.addBatch(sql);
                log.debug("添加操作语句至批处理：" + sql);
            }

            stmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            stmt.close();
            stmt = null;
            return true;
        } catch (Exception var10) {
            try {
                conn.rollback(sp);
                stmt.close();
                stmt = null;
            } catch (SQLException var9) {
                stmt = null;
                log.error("数据库更新执行操作失败，回滚数据失败", var10);
                return false;
            }

            log.error("数据库更新执行操作失败，已回滚", var10);
            return false;
        }
    }

    public boolean updateNoBatch(Connection conn, String... sqls) throws DataUpdateException {
        Statement stmt = this.createStmt(conn, false);
        log.debug("数据库更新操作批处理开始执行，执行语句数：" + (sqls != null ? sqls.length : "0"));

        try {
            String[] var7 = sqls;
            int var6 = sqls.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                String sql = var7[var5];

                try {
                    stmt.executeUpdate(sql);
                    log.debug("数据库更新执行成功：" + sql);
                } catch (Exception var20) {
                    log.error("数据库更新执行失败。语句：" + sql, var20);
                }
            }

            stmt.close();
            stmt = null;
            return true;
        } catch (Exception var22) {
            Exception ex = var22;

            try {
                log.error("数据库更新失败。", ex);
                stmt.close();
                stmt = null;
            } catch (SQLException var21) {
                stmt = null;
                log.error("数据库基本操作失败。", var22);
                return false;
            }

            log.error("数据库更新执行操作失败,数据回退", var22);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException var19) {
                log.debug("关闭stmt失败", var19);
                stmt = null;
            }

        }

        return false;
    }

    public boolean update(Connection conn, String... sqls) throws DataUpdateException {
        Statement stmt = this.createStmt(conn, false);
        log.debug("数据库更新操作批处理开始执行，执行语句数：" + (sqls != null ? sqls.length : "0"));

        try {
            String[] var7 = sqls;
            int var6 = sqls.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                String sql = var7[var5];
                stmt.addBatch(sql);
                log.debug("添加操作语句至批处理：" + sql);
            }

            stmt.executeBatch();
            stmt.close();
            stmt = null;
            return true;
        } catch (Exception var19) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException var18) {
                stmt = null;
                log.error("数据库更新执行操作失败", var19);
                return false;
            }

            log.error("数据库更新执行操作失败,数据回退", var19);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException var17) {
                log.debug("设置数据库连接自动提交失败", var17);
                stmt = null;
            }

        }

        return false;
    }

    public boolean updateTX(Connection conn, String sql) throws DataUpdateException {
        Statement stmt = this.createStmt(conn, false);
        log.debug("数据库更新操作批处理开始执行，执行语句：" + (sql != null ? sql : ""));
        Savepoint sp = null;

        try {
            conn.setAutoCommit(false);
            sp = conn.setSavepoint("update_" + RandomUtils.genRandomString("yyyyMMddHHmmss", 0));
            log.debug("已生成回退节点：" + sp.getSavepointName());
            stmt.executeUpdate(sql);
            conn.commit();
            conn.setAutoCommit(true);
            stmt.close();
            stmt = null;
            return true;
        } catch (Exception var8) {
            try {
                conn.rollback(sp);
                stmt.close();
                stmt = null;
            } catch (SQLException var7) {
                stmt = null;
                log.error("数据库更新执行操作失败，回滚数据失败", var8);
                return false;
            }

            log.error("数据库更新执行操作失败，已回滚", var8);
            return false;
        }
    }

    public boolean update(Connection conn, String sql) throws DataUpdateException {
        Statement stmt = this.createStmt(conn, false);
        log.debug("数据库更新操作批处理开始执行，执行语句：" + (sql != null ? sql : ""));

        try {
            if (!conn.getAutoCommit()) {
                conn.setAutoCommit(true);
            }

            int i = stmt.executeUpdate(sql);
            if (sql.toLowerCase().contains("insert")) {
                log.debug("更新语句实时结果：" + i + "," + (i == 1 ? "正常" : "非正常"));
            } else if (sql.toLowerCase().contains("update")) {
                log.debug("更新语句实时跟踪：" + i + "," + (i > 0 ? "更新语句已影响结果行。" : "更新语句虽然执行成功，但未影响任何数据。"));
            }

            stmt.close();
            stmt = null;
            return true;
        } catch (Exception var7) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException var6) {
                stmt = null;
                log.error("数据库更新执行操作失败", var7);
                return false;
            }

            log.error("数据库更新执行操作失败", var7);
            return false;
        }
    }

    public Statement createStmt(Connection conn, boolean query) {
        try {
            log.debug("创建STMT: " + (query ? "TYPE_SCROLL_INSENSITIVE,CONCUR_READ_ONLY" : "default"));

            try {
                return conn.createStatement(1004, 1007);
            } catch (Exception var6) {
                log.warn("数据库不支持此类创建的stmt");
                log.debug("创建STMT2: " + (query ? "TYPE_SCROLL_SENSITIVE,CONCUR_UPDATABLE" : "default"));

                try {
                    return conn.createStatement(1005, 1008);
                } catch (Exception var5) {
                    log.warn("数据库不支持此类创建的stmt");
                    log.debug("创建STMT3: " + (query ? "default,default" : "default"));

                    try {
                        return conn.createStatement();
                    } catch (Exception var4) {
                        log.warn("数据库不支持此类创建的stmt");
                        return null;
                    }
                }
            }
        } catch (Exception var7) {
            log.error("数据库创建STMT失败。", var7);
            return null;
        }
    }

}
