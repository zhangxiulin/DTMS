package zh.shawn.project.usms.business.scLogin;

import zh.shawn.project.framework.commons.service.core.CommonSessionData;

public class ScLoginServiceSessionData extends CommonSessionData {

    //公钥、私钥
    private String publicKey;

    private String privateKey;

    // 对称密钥
    private String AES;

    //用户id
    private String userInfoUserId;

    //用户档案号
    private String userInfoUserCode;

    //身份证号
    private String userInfoIdCode;

    //邮箱
    private String userInfoUserEmail;


    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAES() {
        return AES;
    }

    public void setAES(String AES) {
        this.AES = AES;
    }

    public String getUserInfoIdCode() {
        return userInfoIdCode;
    }

    public void setUserInfoIdCode(String userInfoIdCode) {
        this.userInfoIdCode = userInfoIdCode;
    }

    public String getUserInfoUserEmail() {
        return userInfoUserEmail;
    }

    public void setUserInfoUserEmail(String userInfoUserEmail) {
        this.userInfoUserEmail = userInfoUserEmail;
    }

    public String getUserInfoUserId() {
        return userInfoUserId;
    }

    public void setUserInfoUserId(String userInfoUserId) {
        this.userInfoUserId = userInfoUserId;
    }

    public String getUserInfoUserCode() {
        return userInfoUserCode;
    }

    public void setUserInfoUserCode(String userInfoUserCode) {
        this.userInfoUserCode = userInfoUserCode;
    }
}
