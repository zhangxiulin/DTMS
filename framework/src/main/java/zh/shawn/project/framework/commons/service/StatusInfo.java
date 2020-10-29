package zh.shawn.project.framework.commons.service;

public enum StatusInfo {

    SUCCESS("111111"),
    FAILED("999999"),
    DATA_FAILED("900101"),
    GEN_DATA_FAILED("900102"),
    STATUS_UPDATE_FAILED("900105"),
    BUSINESS_FAILED("900104"),
    SAVE_DATA_FAILED("900103");

    private String code;

    private StatusInfo(String code) {
        this.code = code;
    }

    public String value() {
        return this.code;
    }

    public int intValue() {
        return Integer.valueOf(this.code);
    }

}
