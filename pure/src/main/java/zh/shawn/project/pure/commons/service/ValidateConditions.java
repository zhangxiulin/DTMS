package zh.shawn.project.pure.commons.service;


public class ValidateConditions {

    private boolean checkNull;
    private boolean checkLength;
    private boolean byteLength;
    private boolean checkFormat;
    private int minLength;
    private int maxLength;
    private String msg;
    private String label;
    private String keyName;
    private Object keyValue;
    private boolean fromHeader;

    public ValidateConditions() {
    }

    public ValidateConditions(boolean checkNull, boolean checkLength, boolean byteLength, boolean checkFormat, int minLength, int maxLength, String msg, String label, String keyName, Object keyValue, boolean fromHeader) {
        this.checkNull = checkNull;
        this.checkLength = checkLength;
        this.byteLength = byteLength;
        this.checkFormat = checkFormat;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.msg = msg;
        this.label = label;
        this.keyName = keyName;
        this.keyValue = keyValue;
        this.fromHeader = fromHeader;
    }

    public ValidateConditions(String keyName, Object keyValue, boolean checkFormat, boolean checkNull, boolean checkLength, boolean byteLength) {
        this.keyName = keyName;
        this.keyValue = keyValue;
        this.label = "验证条件";
        this.checkFormat = checkFormat;
        this.checkLength = checkLength;
        this.byteLength = byteLength;
        this.checkNull = checkNull;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Object getKeyValue() {
        return this.keyValue;
    }

    public void setKeyValue(Object keyValue) {
        this.keyValue = keyValue;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMinLength() {
        return this.minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isCheckNull() {
        return this.checkNull;
    }

    public void setCheckNull(boolean checkNull) {
        this.checkNull = checkNull;
    }

    public boolean isCheckLength() {
        return this.checkLength;
    }

    public void setCheckLength(boolean checkLength) {
        this.checkLength = checkLength;
    }

    public boolean isByteLength() {
        return this.byteLength;
    }

    public void setByteLength(boolean byteLength) {
        this.byteLength = byteLength;
    }

    public boolean isCheckFormat() {
        return this.checkFormat;
    }

    public void setCheckFormat(boolean checkFormat) {
        this.checkFormat = checkFormat;
    }

    public boolean isFromHeader() {
        return this.fromHeader;
    }

    public void setFromHeader(boolean fromHeader) {
        this.fromHeader = fromHeader;
    }

}
