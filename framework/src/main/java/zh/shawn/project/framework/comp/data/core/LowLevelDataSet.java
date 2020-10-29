package zh.shawn.project.framework.comp.data.core;

import java.util.Map;

public class LowLevelDataSet {
    private Map<String, Integer> colNames;
    private Object[][] values;
    private int totalRows;

    public static final LowLevelDataSet emptyInstance() {
        return new LowLevelDataSet((Map)null, (Object[][])null, 0);
    }

    public LowLevelDataSet(Map<String, Integer> colNames, Object[][] values, int totalRows) {
        this.colNames = colNames;
        this.values = values;
        this.totalRows = totalRows;
    }

    public Object[][] getValues() {
        return this.values;
    }

    public void setValues(Object[][] values) {
        this.values = values;
    }

    public Map<String, Integer> getColNames() {
        return this.colNames;
    }

    public void setColNames(Map<String, Integer> colNames) {
        this.colNames = colNames;
    }

    public int getTotalRows() {
        return this.totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public Object getValue(String name, int row) {
        if (!this.colNames.containsKey(name)) {
            return null;
        } else {
            return row > this.values.length ? null : this.values[row][(Integer)this.colNames.get(name)];
        }
    }

    public void clear() {
        if (this.colNames != null && this.colNames.size() > 0) {
            this.colNames.clear();
            this.colNames = null;
        }

        if (this.values != null && this.values.length > 0) {
            this.values = null;
        }

    }
}
