package zh.shawn.project.framework.boot.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Rogue
 * @author zhangxiulin
 *
 */
public class RSAUtils2 {

	private static final int MAX_ENCRYPT_BLOCK = 117;

	private static final int MAX_DECRYPT_BLOCK = 128;
	
	private Key publicKey;
	private Key privateKey;
	private String pubk;
	private String prik;

	private Base64Utils bu;

	public RSAUtils2(){
		this.bu = new Base64Utils();
	}
	
	public void createKeyPair() throws Exception{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
		pubk = bu.encode(publicKey.getEncoded());
		prik = bu.encode(privateKey.getEncoded());
	}
	
	public String encryptWithRSA(String str) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		byte[] data = str.getBytes();
		int inputLen = str.getBytes().length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		
		return bu.encode(encryptedData);
	}
	
	public String encryptWithRSA(String str, PublicKey pubKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		
		byte[] data = str.getBytes();
		int inputLen = str.getBytes().length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		
		return bu.encode(encryptedData);
	}
	
	public String encryptWithRSA(String str, String pubK) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(pubK));
		
		byte[] data = str.getBytes();
		int inputLen = str.getBytes().length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		
		return bu.encode(encryptedData);
	}
	
	public String decryptWithRSA(String scr) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		
		byte[] encryptedData = bu.decode(scr);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		
		return new String(decryptedData);
	}
	
	public String decryptWithRSA(String scr, PrivateKey priKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		
		byte[] encryptedData = bu.decode(scr);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		
		return new String(decryptedData);
	}
	
	public String decryptWithRSA(String scr, String priK) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(priK));
		
		byte[] encryptedData = bu.decode(scr);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		
		return new String(decryptedData);
	}
	
	public PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = bu.decode(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;

	}

	public PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = bu.decode(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}
	
	public String getPubk(){
		return pubk;
	}
	
	public String getPrik(){
		return prik;
	}
	
	public static void main(String[] args) throws Exception {
		String msg = "中国";
		RSAUtils2 RSAUtils2 = new RSAUtils2();
		RSAUtils2.createKeyPair();
		System.out.println("pubk:" + RSAUtils2.pubk);
		System.out.println("prik:" + RSAUtils2.prik);
		System.out.println("加密前：" + msg);
		String encryptWithRSA = RSAUtils2.encryptWithRSA(msg);
		System.out.println("加密后：" + encryptWithRSA);
		String decryptWithRSA = RSAUtils2.decryptWithRSA(encryptWithRSA);
		System.out.println("解密后：" + decryptWithRSA);
		
		//
		RSAUtils2 RSAUtils21 = new RSAUtils2();
		String msg1 = "123苏州园区";
		String pubk1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCle3h7WI8Jpu97WIK97PnHEsWHBjCjxwEuEyfexEC7cM41daJezb4jGO1MjqYIA/gME3gy7uyYlO6LZg1qQKfbpwRA2SC7kp3jiX2gU/1312Kvyh1uXLq1S316eIsc1B//dHX8IQBAtEN+uGnc5GAXJP4+bY7RDeOeV9hjKXLNzQIDAQAB";
		String prik1 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKV7eHtYjwmm73tYgr3s+ccSxYcGMKPHAS4TJ97EQLtwzjV1ol7NviMY7UyOpggD+AwTeDLu7JiU7otmDWpAp9unBEDZILuSneOJfaBT/XfXYq/KHW5curVLfXp4ixzUH/90dfwhAEC0Q364adzkYBck/j5tjtEN455X2GMpcs3NAgMBAAECgYBAbvkUngDj8zogPvB5X0JKf0amMoTR4HTXKeJGXQgPc/b23dzhKR81r0kGnCyNxm3Y1ePhgSJirurLIGtsycwRnHcLZ93/sN1Yhjm4D6JDkyDdnj3h7uQoIxNaJNJcEfZw3VjZPkYZwycReaK1d5NlN1rFtoBFkfxPJzLm5S5VSQJBANPClqlzg1ZHYxJLG2+y/l4aa3A8l/sqCw+LXC6401eINS2v3FoJo9yDNzYH5Wd1MHl4rzECJF/9vrzZ1b92qRcCQQDIDdcPlGy3FKgyeYsyzJREAdqfEJvTaKqkpXDFVP8vexcGQAdZBIlyuBtcYQ2EAv2EA+nfxcG0PU1XhIVEX0a7AkEAkWvaJzgqg3+2q4NkrgqP4HPoQEV8YYF34w7jGTrX+A6T5nIUsshX/UEnEzXM9oVl6qVUOiWscTdCW1KFFV0ZtQJAdbiXNibMNovkUhdtzw3NrZs9r96RI71ytQJZsvVKWQFg0h+5cyuVSjmGeDzwPB+aWSYIaNKxIsP0ECz+UvaR4wJAeWGokmmhUtJUHVPXJX0Yrl/fDmvLYsZMtbs1ZkBLruVdR9VDL0/LRBui84vnHBF509vmwpRKVqE/hLq7QKdSDQ==";
		PublicKey pubKey1 = RSAUtils21.getPublicKey(pubk1);
		PrivateKey priKey1 = RSAUtils21.getPrivateKey(prik1);
		System.out.println("加密前：" + msg1);
		String encryptWithRSA1 = RSAUtils21.encryptWithRSA(msg1, pubKey1);
		System.out.println("加密后：" + encryptWithRSA1);
		String decryptWithRSA1 = RSAUtils21.decryptWithRSA(encryptWithRSA1, priKey1);
		System.out.println("解密后：" + decryptWithRSA1);
		
		
		//
		RSAUtils2 RSAUtils22 = new RSAUtils2();
		String msg2 = "123苏州吴江";
		String pubk2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7I0uJ5zxJt9MKtTGtLxEl6E1Tch49zs7Layh6JxnTT7h5rA2u7_75UBIk02EXwHZsbIauGdO0swlC.g0kbjwmZRCfbW3wkY9kOTSG3ZWPx7Q9g2k9yG0ivarCQqI27vfXIkMMcd99BPGmqef.mTLRWlLKuakqhQxb6yVY.hw6uQIDAQAB";
		String prik2 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALsjS4nnPEm30wq1Ma0vESXoTVNyHj3OzstrKHonGdNPuHmsDa7v_vlQEiTTYRfAdmxshq4Z07SzCUL6DSRuPCZlEJ9tbfCRj2Q5NIbdlY_HtD2DaT3IbSK9qsJCojbu99ciQwxx330E8aap5_6ZMtFaUsq5qSqFDFvrJVj6HDq5AgMBAAECgYEAmpMD0SvMM4QAcUbLeqZ2_p3MSVHowtrxGzsvGv7zKQUfdnj0cHxSWrKV2g8zkr_p0ZMvB93Tbd24l7cdZescCJy6m025QOJOpBnY1GY_1nkvmsaIJl4EYpcAMnTPgUGmxMYx1pgDUdid9CHmxNHU1Kw1GEzLwEz8TZZi.2nd5pECQQDmqgqa0B8ttd8KHrtDYCe_eosRLT6vycAgp4f4DRziFLwEYxQCe9eh3vzqaZjTqN.8rNWjAQLRqQqFzDrOr3A9AkEAz7FaViJ4iYTnx0qUjiwFO1d.yPO8hO.FTxGWebtOyw2lluYg9GZuoQ7d5rGTvYUbuTXk9nrYXeDc.zSl5TOALQJBALPnjevfRWIWhsZS38WV.opqnoqgGlNq7LHLWM5ME.n1OAvH5vRk.PLkg6ilAbCb9x0mRF4lTLiPuohOmNNZnBUCQHA9Xx13aX6f9YYM9vVSMs2b_idpGPMaYgeV8KT.pZk0Wc2PtjV9.cFOQHtAciAUhCP2TTMqti0drS6QiCqyR00CQQCux74G20ywxdwlovX9okIiI9OmWCoK82txQbPTI2FGmfWoEFwj9VjTPKnIZOECw3qIr0T3FvBslBiCDz4UQr5P";
		PublicKey pubKey2 = RSAUtils22.getPublicKey(pubk2);
		PrivateKey priKey2 = RSAUtils22.getPrivateKey(prik2);
		System.out.println("加密前：" + msg2);
		String encryptWithRSA2 = RSAUtils21.encryptWithRSA(msg2, pubKey2);
		System.out.println("加密后：" + encryptWithRSA2);
		String decryptWithRSA2 = RSAUtils21.decryptWithRSA(encryptWithRSA2, priKey2);
		System.out.println("解密后：" + decryptWithRSA2);
	}
}
