package zh.shawn.project.pure.boot.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密
 */
public class AESUtils {

    // 算法
    private static final String DEFAULT_CIPHER_ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * AES解密
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, String key) throws Exception{
        // 实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
        byte[] bytes = cipher.doFinal(new Base64Utils().decode(content));
        return new String(bytes, "utf-8");
    }

    public static String encrypt(String content, String key) throws Exception{
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHMSTR);
        byte[] bytes = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE,getSecretKey(key));
        byte[] result = cipher.doFinal(bytes);
        return new Base64Utils().encode(result);
    }

    /**
     * 生成加密密钥
     * @param key
     * @return
     * @throws Exception
     */
    private static SecretKeySpec getSecretKey(final String key) throws Exception{
        // 返回生成指定算法密钥生成器
        KeyGenerator kg = null;
        kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        return new SecretKeySpec(key.getBytes(), "AES");
        //kg.init(128, new SecureRandom(key.getBytes("utf-8")));
        // 生成一个密钥
        //SecretKey secretKey = kg.generateKey();
        //return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }


    public static void main(String args[]) throws Exception{
        String msg = "{\"username\":\"zhang\",\"password\":\"123\"}";
        String key = "NDMwMTM4OTg1MQ==";
        System.out.println("加密前：" + msg);
        String eStr = encrypt(msg, key);
        System.out.println("加密后：" + eStr);
        String dStr = decrypt(eStr, key);
        System.out.println("解密后：" + dStr);
    }
}
