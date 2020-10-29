package zh.shawn.project.framework.comp.database.ops;

import java.util.HashMap;
import java.util.Map;


public class TableV2 implements Cloneable {
    private Map<String, Integer> colMap = new HashMap();
    private Object[][] tempResult;
    private int totalRows = 0;

    public TableV2() {
    }

    public final TableV2 addColumns(Map<String, Integer> colMap) {
        this.colMap = colMap;
        return this;
    }

    public final TableV2 addData(Object[][] tempResult) {
        this.tempResult = tempResult;
        if (tempResult != null && tempResult.length >= 1) {
            this.totalRows = tempResult.length;
        } else {
            this.totalRows = 0;
        }

        return this;
    }

    public Object[][] getTempResult() {
        return this.tempResult;
    }

    public void reset() {
        this.colMap = new HashMap();
        this.tempResult = null;
        this.totalRows = 0;
    }

    public Map<String, Integer> getColMap() {
        return this.colMap;
    }

    public int getTotalRows() {
        return this.totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public void setColMap(Map<String, Integer> colMap) {
        this.colMap = colMap;
    }

    public void setTempResult(Object[][] tempResult) {
        this.tempResult = tempResult;
    }
}
