package zh.shawn.project.usms.business.scLogin.nocry;

import zh.shawn.project.pure.commons.service.core.CommonServiceResponseData;

import java.util.List;

public class ScLoginServiceRep extends CommonServiceResponseData {

    private String publicKey;//公钥

    private String userName;//用户名

    private String userLastlogin;//上次登录时间

    private String userCode;//档案号

    private String phone;//手机号

    private Object function;

    private String idcode;//身份证

    private String userRegisterorg;//花名册机构

    private String userEmail;//用户邮箱

    private String isAdmin;//是否管理员登陆

    private List roleList;

    private String userRole;

    private Object limits;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLastlogin() {
        return userLastlogin;
    }

    public void setUserLastlogin(String userLastlogin) {
        this.userLastlogin = userLastlogin;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getFunction() {
        return function;
    }

    public void setFunction(Object function) {
        this.function = function;
    }

    public String getIdcode() {
        return idcode;
    }

    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

    public String getUserRegisterorg() {
        return userRegisterorg;
    }

    public void setUserRegisterorg(String userRegisterorg) {
        this.userRegisterorg = userRegisterorg;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List getRoleList() {
        return roleList;
    }

    public void setRoleList(List roleList) {
        this.roleList = roleList;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Object getLimits() {
        return limits;
    }

    public void setLimits(Object limits) {
        this.limits = limits;
    }
}
