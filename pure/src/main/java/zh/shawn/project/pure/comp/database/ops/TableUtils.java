package zh.shawn.project.pure.comp.database.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.comp.database.exception.NoDataException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

public class TableUtils {
    private static final Logger log = LoggerFactory.getLogger(TableUtils.class);

    public TableUtils() {
    }

    public static final int findInt(String[] colArray, String key) throws NoDataException {
        for(int i = 0; i < colArray.length; ++i) {
            if (colArray[i].equals(key)) {
                return i;
            }
        }

        throw new NoDataException("没有这个key");
    }

    public static final Object[][] mapValues(ResultSet rs, int colNums) throws SQLException, NoDataException {
        if (rs == null) {
            log.debug("空表不予处理");
            return null;
        } else {
            int rows = 0;
            if (rs.last()) {
                rows = rs.getRow();
            }

            if (rows <= 0) {
                log.debug("空表不予处理");
                return null;
            } else {
                Object[][] tempResult = new Object[rows][colNums];
                log.debug("一阶段赋值已创建储值空间：" + rows + "," + colNums);
                rs.beforeFirst();
                long s1 = System.currentTimeMillis();

                for(int c = 0; rs.next(); ++c) {
                    for(int k = 0; k < colNums; ++k) {
                        new Object();

                        Object v;
                        try {
                            v = rs.getObject(k + 1);
                            if (v == null) {
                                v = "";
                            }
                        } catch (Exception var10) {
                            v = "";
                        }

                        tempResult[c][k] = v;
                    }
                }

                long s2 = System.currentTimeMillis();
                log.debug("数据底层映射耗时：" + (s2 - s1) + "ms");
                return tempResult;
            }
        }
    }

    public static final Object getTempValue(Object[][] tempResult, int rowNum, int colNum) throws Exception {
        return tempResult[rowNum][colNum];
    }

    public static final Object getTempValue(String[] colArray, Object[][] tempResult, int rowNum, String colName) throws Exception {
        int i = 0;

        try {
            i = findInt(colArray, colName);
            return tempResult[rowNum][i];
        } catch (Exception var6) {
            log.error("数据取值映射时发生错误" + rowNum + "，" + colName + "，" + i, var6);
            return null;
        }
    }

    public static final Map<String, Integer> mapColumns(ResultSet rs, Map<String, Integer> colMap) throws SQLException, NoDataException {
        if (!rs.next()) {
            return colMap;
        } else {
            colMap.clear();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();

            for(int i = 0; i < cc; ++i) {
                colMap.put(rsmd.getColumnName(i + 1), i);
            }

            rsmd = null;
            return colMap;
        }
    }
}
