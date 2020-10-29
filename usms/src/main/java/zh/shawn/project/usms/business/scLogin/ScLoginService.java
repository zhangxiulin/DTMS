package zh.shawn.project.usms.business.scLogin;

import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import zh.shawn.project.framework.boot.utils.AESUtils;
import zh.shawn.project.framework.boot.utils.AccountJudgeUtil;
import zh.shawn.project.framework.boot.utils.RSAUtils;
import zh.shawn.project.framework.boot.utils.RSAUtils2;
import zh.shawn.project.framework.commons.exception.ServiceBusinessException;
import zh.shawn.project.framework.commons.service.core.CommonBusinessService;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;


public class ScLoginService extends CommonBusinessService<ScLoginServiceReq, ScLoginServiceRep, ScLoginServiceSessionData> {

    private Logger LOGGER = Logger.getLogger(ScLoginService.class);
    private ScLoginServiceSessionData session;

    private String USER_ID = "user_id";
    private String USER_CODE = "user_code";

    @Override
    public String getTargetTemplateId() {
        return "";
    }

    @Override
    public ScLoginServiceReq getRequestInstance() {
        return new ScLoginServiceReq();
    }

    @Override
    public ScLoginServiceRep getResponseInstance() {
        return new ScLoginServiceRep();
    }

    @Override
    public ScLoginServiceRep service(ScLoginServiceReq req) throws ServiceBusinessException {
        ScLoginServiceRep rep = new ScLoginServiceRep();

        // 公钥、密钥
        String publicKey = session.getPublicKey();
        String privateKey = session.getPrivateKey();
        // 加密参数、被公钥加密后的对称密钥
        String enc = req.getEnc();
        String enc_code = req.getEnc_code();
        String appID = req.getAppID();

        // 使用RSA私钥解密对称密钥密文得到对称密钥AES
        String AES = null;
        try{
            RSAUtils2 rsaUtils = new RSAUtils2();
            AES = rsaUtils.decryptWithRSA(enc_code, privateKey);
        }catch(Exception e){
            LOGGER.error("系统错误！"+e.getMessage(), e);
            throw new ServiceBusinessException("登录失败，系统错误！");
        }

        String paramStr = null;
        // 使用AES解密参数密文
        try{
            paramStr = AESUtils.decrypt(enc, AES);
        }catch (Exception e){
            LOGGER.error("系统错误！"+e.getMessage(), e);
            throw new ServiceBusinessException("登录失败，系统错误！");
        }

        LOGGER.info("paramStr:" + paramStr);

        if(!StringUtil.isNullOrEmpty(paramStr)){
            JSONObject paramJsonObj = JSONObject.parseObject(paramStr);
            String username = paramJsonObj.getString("username");
            String password = paramJsonObj.getString("password");
            String deviceNo = "";
            Map<String, String> userDataMap = new HashMap<>();
            //是否是管理员
            String isAdmin = "false";
            if (username.startsWith("@")) {
                username = username.substring(1, username.length());
                isAdmin = "true";
                userDataMap.put("isAdmin", isAdmin);
            }
            // 通过 身份证、手机、档案号、邮箱 做用户名
            checkUsernamePass(username, password, deviceNo, userDataMap);

            String userId = userDataMap.get(USER_ID);
            if (StringUtils.isBlank(userId)) {
                throw new ServiceBusinessException("登录失败，用户名密码不匹配");
            }

            String userName = StringUtils.isBlank(userDataMap.get("USER_REALNAME")) ? "" : userDataMap.get("USER_REALNAME");

            // 保存对称加密AES密钥
            session.setAES(AES);
            // 用户标识、档案号
            session.setUserInfoUserId(userId);
            //将身份证号和邮箱放入到session中去




            JSONObject rtDataJsonObj = new JSONObject();
            rtDataJsonObj.put("publicKey", publicKey);
            rtDataJsonObj.put("userName", userName);

            // 使用AES解密参数密文
            String rtDataStr = rtDataJsonObj.toJSONString();
            String rtEnc = "";
            try{
                LOGGER.info("AES加密前：" + rtDataStr);
                rtEnc = AESUtils.encrypt(rtDataStr, AES);
                LOGGER.info("AES加密后：" + rtEnc);
                String rtDec = AESUtils.decrypt(rtEnc, AES);
                LOGGER.info("AES解密后：" + rtDec);
            }catch (Exception e){
                LOGGER.error("系统错误！"+e.getMessage(), e);
                throw new ServiceBusinessException("登录失败，系统错误！");
            }

            rep.setEnc(rtEnc);
        }else{
            LOGGER.error("请求参数为空");
            throw new ServiceBusinessException("登录失败，请求参数为空！");
        }

        return rep;
    }

    /**
     * 验证用户名密码
     *
     * @param userName
     * @param password
     * @return
     */
    public String checkUsernamePass(String userName, String password, String deviceNo, Map<String, String> userDataMap)
            throws ServiceBusinessException {

        //用户名类型
        AccountJudgeUtil.AccountType accountType = AccountJudgeUtil.judgeAccount(userName);
        if (accountType == null) {
            return null;
        }



        return "111111";

    }



    @Override
    public ScLoginServiceSessionData getSessionInstance() {
        return new ScLoginServiceSessionData();
    }

    @Override
    public ScLoginServiceSessionData getSession() {
        return this.session;
    }

    @Override
    public void updateSession(ScLoginServiceSessionData session) {
        this.session = session;
    }

    public static void main(String args[]) throws Exception{
        String msg = "苏州";
        //String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCBJ2HY3Dz6RV6cW7v/u8r71ou+dun3EjSEfvWOJ2omELqmD8xMbLl+XSB13b5V02PT7Tr6TigrAlWD8KMdrd7zdnYHS6fFTl4uJhTpAqrQrNgxxGKjBtPl/QKLWv8Id9dVbkcatLSV/bcGNG0hmB4eB0LF9aqG2FTsSJdFhBhepMkTQJ+riy/9V6DG51miWMCvOwUH+qlM2hvMc6t3wl4n2w70RhwcCxIozv1j9meRiFaE4rsp4RyY02GS2YZRncwH0pUx5JfIUhMYK5PudbyBuW7jnlcUC+hAiice1fw4UzQ0hitwf3JudmiTcZ0e0sYA1eQOOya80ExjJ8XLGCi9AgMBAAECggEAPu4uihk+CQBWCj/UidY6IJ24d7S/aajQOCMYiQyBd0y9dX7LIA32EDFWSLgkLvOSd8fm7eG5RsObYW+WOzbmJKD6/7EIbWYzE69wjtzC2Gzv5sf2+9Ku2yh93eMX00t5e+IA+W1FdLQsQvS2nCDfw+frzENYtN2h+4OfWbr0mOBlOh1tNf/ibRNcVlFUKLzlcXqKJCLwfh6vemyoufBQ/KIIuNZv087M8BXrATn8lIPDoH+AjhSuQEF87BRctAkyD8Ex/LQxBc+aFX3b+ez1OdFd+sulnv+SNtlbSiK9janWxDZQ366TNCC9cIRw9ZD0+8YUoVFjSIEEycEQygLqTQKBgQDRQBPje9H8MQ9WjygUQROOz23tFBBNu2Rs7yEB0nrfPstyS3VH46l+wawhE4G9n5n9LMpbUl/+LdkJ835TLBzehU/CrQBnau6UjBqp0UIEuuT8rpsEbOdjJKtfhr+/a7ma1Bw8yC/U2nfxLuFzRuNdDTGxK87+Jbo1HRz0m85hDwKBgQCeAj57TZv4P+b0bsbsWFK3ujwQoMffWP79YUtLRnbkPjL2j4mcTRuG9CcPiEjgshczjxTQg1efDDas2CkxXROogHDNfEF/nFbmmrBrtQZA47nChqSTwIWyneXdikQEDZzsMpbVZYcPf7562JMyr5p5ygSKj5OvO6LrYCFKteaBcwKBgGxPsbvHhNr4fgr9EO7i/H+oiSr1frbtr77fTIOH8ogKFf70ZiGvCzYMg8+fLEH+EhRFkf0RN5T3IGgRPKtKoyrfcHjBWfsuE46astaB9Z1DLHJK4vy4blEYUwigVDJ+TVWCQ5eGu5N+/iS+cn5cDCZHRRiDJ6LP3oHNrwZCe+IrAoGACYOFE5OGspnWZB1PaOegg2N5Pl8P6cjIAaWcDPo/Klr8YpAd7wVNhQCqYWsJSE4wy2/wBuYBnpfzqtCRGmHKTzj8kcLLhs15BgWGXdXWfI25n5bPzf2njyumrYInuj6/lhEtt8eXLD260Fa99i1QlciuIVGnyMNEefudxYvSRzUCgYEAmsXwafyU6v77ZJLo3r/XgD10er4de7zW54pvtjpQEKirkg37WblSTS8CmkC4KzO3/9+P+X65j55tVnxCHWxWBfgg7hwiY4lEWwPn10RloM4tU/EQR7TC8ll2AgYyOm+nnBKbt0Kwtmv5Fszl+lebgsZ8lg9qDJa5s8AnsMPHh4I=";
        //String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgSdh2Nw8+kVenFu7/7vK+9aLvnbp9xI0hH71jidqJhC6pg/MTGy5fl0gdd2+VdNj0+06+k4oKwJVg/CjHa3e83Z2B0unxU5eLiYU6QKq0KzYMcRiowbT5f0Ci1r/CHfXVW5HGrS0lf23BjRtIZgeHgdCxfWqhthU7EiXRYQYXqTJE0Cfq4sv/VegxudZoljArzsFB/qpTNobzHOrd8JeJ9sO9EYcHAsSKM79Y/ZnkYhWhOK7KeEcmNNhktmGUZ3MB9KVMeSXyFITGCuT7nW8gblu455XFAvoQIonHtX8OFM0NIYrcH9ybnZok3GdHtLGANXkDjsmvNBMYyfFyxgovQIDAQAB";

        String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCle3h7WI8Jpu97WIK97PnHEsWHBjCjxwEuEyfexEC7cM41daJezb4jGO1MjqYIA/gME3gy7uyYlO6LZg1qQKfbpwRA2SC7kp3jiX2gU/1312Kvyh1uXLq1S316eIsc1B//dHX8IQBAtEN+uGnc5GAXJP4+bY7RDeOeV9hjKXLNzQIDAQAB";
        String privateKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKV7eHtYjwmm73tYgr3s+ccSxYcGMKPHAS4TJ97EQLtwzjV1ol7NviMY7UyOpggD+AwTeDLu7JiU7otmDWpAp9unBEDZILuSneOJfaBT/XfXYq/KHW5curVLfXp4ixzUH/90dfwhAEC0Q364adzkYBck/j5tjtEN455X2GMpcs3NAgMBAAECgYBAbvkUngDj8zogPvB5X0JKf0amMoTR4HTXKeJGXQgPc/b23dzhKR81r0kGnCyNxm3Y1ePhgSJirurLIGtsycwRnHcLZ93/sN1Yhjm4D6JDkyDdnj3h7uQoIxNaJNJcEfZw3VjZPkYZwycReaK1d5NlN1rFtoBFkfxPJzLm5S5VSQJBANPClqlzg1ZHYxJLG2+y/l4aa3A8l/sqCw+LXC6401eINS2v3FoJo9yDNzYH5Wd1MHl4rzECJF/9vrzZ1b92qRcCQQDIDdcPlGy3FKgyeYsyzJREAdqfEJvTaKqkpXDFVP8vexcGQAdZBIlyuBtcYQ2EAv2EA+nfxcG0PU1XhIVEX0a7AkEAkWvaJzgqg3+2q4NkrgqP4HPoQEV8YYF34w7jGTrX+A6T5nIUsshX/UEnEzXM9oVl6qVUOiWscTdCW1KFFV0ZtQJAdbiXNibMNovkUhdtzw3NrZs9r96RI71ytQJZsvVKWQFg0h+5cyuVSjmGeDzwPB+aWSYIaNKxIsP0ECz+UvaR4wJAeWGokmmhUtJUHVPXJX0Yrl/fDmvLYsZMtbs1ZkBLruVdR9VDL0/LRBui84vnHBF509vmwpRKVqE/hLq7QKdSDQ==";



        RSAUtils2 rsaUtils2 = new RSAUtils2();
        PublicKey publicKey2 = rsaUtils2.getPublicKey(publicKeyStr);
        PrivateKey privateKey2 = rsaUtils2.getPrivateKey(privateKeyStr);
        System.out.println("加密前：" + msg);
        String s = rsaUtils2.encryptWithRSA(msg, publicKey2);
        String s1 = rsaUtils2.decryptWithRSA(s, privateKey2);
        System.out.println("解密后：" + s1);

        RSAUtils rSAUtils = new RSAUtils();
        PublicKey publicKey = rSAUtils.getPublicKey(publicKeyStr);
        PrivateKey privateKey = rSAUtils.getPrivateKey(privateKeyStr);
        Map<String, Object> kdata = rSAUtils.genKeyPair();
        System.out.println("pubk: " + rSAUtils.getPublicKey(kdata));
        System.out.println("prik: " + rSAUtils.getPrivateKey(kdata));
        System.out.println("h b:" + msg);
        byte[] bytesE = rSAUtils.encryptByPublicKey(msg.getBytes(), publicKey);
        byte[] bytesD = rSAUtils.decryptByPrivateKey(bytesE, privateKey);
        System.out.println("h a---" );
        System.out.println("h a:" + new String(bytesD, "utf-8"));
    }
}
