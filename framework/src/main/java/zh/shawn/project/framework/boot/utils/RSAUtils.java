package zh.shawn.project.framework.boot.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

	public static final String KEY_ALGORITHM = "RSA";
//	public static final String KEY_ALGORITHM = "PKCS8NoPadding";

	public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

	public static final String PUBLIC_KEY = "RSAPublicKey";

	public static final String PRIVATE_KEY = "RSAPrivateKey";

	private static final int MAX_ENCRYPT_BLOCK = 117;

	private static final int MAX_DECRYPT_BLOCK = 128;
	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	private Base64Utils bu;

	public RSAUtils(Base64Utils bu) {
		this.bu = bu;
	}

	public RSAUtils() {
		this.bu = new Base64Utils();
	}

	public Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(2048);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	public String sign(byte[] data, PrivateKey privateKey) throws Exception {
		byte[] keyBytes = privateKey.getEncoded();
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return bu.encode(signature.sign());
	}

	public boolean verify(byte[] data, PublicKey publicKey, String sign) throws Exception {
		byte[] keyBytes = publicKey.getEncoded();
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(bu.decode(sign));
	}

	public byte[] decryptByPrivateKey(byte[] encryptedData, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding", "BC");
//		Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
//		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
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
		// byte[] decryptedData = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	public byte[] decryptByPrivateKey(String cipherInstane, byte[] encryptedData, PrivateKey privateKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance(cipherInstane, "BC");
//		Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
//		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
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
		// byte[] decryptedData = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 * 
	 * @param encryptedData 已加密数据
	 * @param publicKey     公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public byte[] decryptByPublicKey(byte[] encryptedData, PublicKey publicKey) throws Exception {
		// Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
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
		// byte[] decryptedData = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 * 
	 * @param data      源数据
	 * @param publicKey 公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
		// 对数据加密
		// Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
//		System.out.println(publicKey.getAlgorithm());
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
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
		// byte[] encryptedData = cipher.doFinal(data);
		return encryptedData;
	}

	/** */
	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 * 
	 * @param data       源数据
	 * @param privateKey 私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
		// Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
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
		// byte[] encryptedData = cipher.doFinal(data);
		return encryptedData;
	}

	/** */
	/**
	 * <p>
	 * 获取私钥
	 * </p>
	 * 
	 * @param keyMap 密钥对
	 * @return
	 * @throws Exception
	 */
	public String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return bu.encode(key.getEncoded());
	}

	public PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = this.bu.decode(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;

	}

	public PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = this.bu.decode(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/** */
	/**
	 * <p>
	 * 获取公钥
	 * </p>
	 * 
	 * @param keyMap 密钥对
	 * @return
	 * @throws Exception
	 */
	public String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return bu.encode(key.getEncoded());
	}

	public static void main(String[] args) throws Exception {

		RSAUtils rau = new RSAUtils();
		Map<String, Object> kdata = rau.genKeyPair();
//		Map<String, Object> kdata = new HashMap<>(2);
		String priks = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKB3TYF1iSl5sIwQhnmyxJeKyYN9pRJQph3h/+ld9Ss5yUAlg4CPs3EE//svVd9jQ8PY1uRhvffxHLnvO7tVD0SjSh1BvKs3gXT392Np96pUhm5QlTsVS6lTB3tNle6qU7RcMCirQY31oCwbMmkRNjDvbrh5sNCWm4ITevcclnCxAgMBAAECgYAtEvYBfGuH6/fMkEPF4ZJZYxHRzrRUnbTbxgTln4/jkXrXevIiZbso7xecjiJX+oEpfTp3soDzKZZBIFMt/umnkzzelmSxB5m1pYNcEauEuIapiZLrJujfi5blPgTkx8cAWz8eu9/2cjOG8cjSZWMDywDBTNnK6FDP+WeYiKFXcQJBAMtnwlc5cCEi4IrcVwUf44st4d3pU48g81VnBLPwnGyDj/NGmKmsJFNg0glNiKrjCU1xXn/IIdT6FDBLT5PvF50CQQDJ9TblLsInlskO55njtxr12rzr+7PEd2lRtjN51x9OtlEEgK6Sehw3J+dlCu94nqY4li1F60zlildCTAKWtvMlAkBRNrmCyB08LcfHydg45JWewZAYYYwKbtaZd8uJ6P0b2p/EO6DCxkeuvs6+BiYWTRYuxKA9lvPjLIKZEcjRQTqFAkB3fQzgpjybXbxRhDfpofFimuuCF6bxy9DVVp64LM5KLNZceqJO6c59168xejnbPMqN2lSo9KHHgYVFJWG2uEG9AkBFxaUsTX+z9orPMNnXZIzyYh8EKYIIY5SGzScSC0JFnEl/LbMWT/3s7RyHMmwYrVcNQhQpBRRiXp+9tJj4AHo4";
		String pubks = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgd02BdYkpebCMEIZ5ssSXismDfaUSUKYd4f/pXfUrOclAJYOAj7NxBP/7L1XfY0PD2NbkYb338Ry57zu7VQ9Eo0odQbyrN4F09/djafeqVIZuUJU7FUupUwd7TZXuqlO0XDAoq0GN9aAsGzJpETYw7264ebDQlpuCE3r3HJZwsQIDAQAB";
		System.out.println("pubk: " + rau.getPublicKey(kdata));
		System.out.println("prik: " + rau.getPrivateKey(kdata));
		String plaintext = "{\n" + "	\"service\": {\n" + "		\"Head\": {\n"
				+ "			\"SvcCd\": \"50013000009\",\n" + "			\"SvcScn\": \"01\",\n"
				+ "			\"CnlTp\": \"02060000\",\n" + "			\"CnsmrSysId\": \"101100\",\n"
				+ "			\"CnsmrSeqNo\": \"S10110018101710164702600000076\",\n"
				+ "			\"BsnSeqNo\": \"G10110018101710164702600000076\",\n"
				+ "			\"TxnDt\": \"20181017\",\n" + "			\"TxnTm\": \"101647\",\n"
				+ "			\"FileFlg\": \"0\"\n" + "		},\n" + "		\"Body\": {\n"
				+ "			\"UsrId\": \"0235\",\n" + "			\"BsnInd\": \"1\"\n" + "		}\n" + "	}\n" + "}";
//		System.out.println("pt: " + plaintext.getBytes().length + "," + plaintext);
//		byte[] encryptedData = rau.encryptByPublicKey(plaintext.getBytes(), rau.getPublicKey(rau.getPublicKey(kdata)));
		byte[] encryptedData = rau.encryptByPublicKey(plaintext.getBytes(), rau.getPublicKey(pubks));
		String emp = new Base64Utils().encode(encryptedData);
		System.out.println("encrypted: " + emp);
//		System.out.println("decrypted: "
//				+ new String(rau.decryptByPrivateKey(encryptedData, rau.getPrivateKey(rau.getPrivateKey(kdata)))));
//		System.out.println(new Base64Utils().decode("h7gxzM34ohyF96Q+u7zkReQY0ail8RSSjtqdvAKQcyjH8flYf4gDv62iQ1D4F8zOgFmfz8iBIXTifmqdHK+msrxVRWgOlODufTB2IxqM7UNmmK7lVABDDoa0PFOKkbzt/0lq+jK2MN2cIXKPcJQn+11S2bxYXguo6ELGQl28G4F3x0+9ogxidZxgCgN1Y4lGgIAKEfZL5uriBWAOTlFnVFlXRROkNLUuOZwgfeUYWdwPNK4QL/xIDVIPnX8/fW8NOX0qveHl1qqNrP+2C4g7NHah6f4Q1piiMeSKFVwzvxR8hySKrje4KcTXC+fqg7nAeSOeZC11JkNu/cPAmq+aLYMYq8nv78Z8oybxV7uaDtwf8blCuNGu9QaLUL2+PQvivln5kGfbiQYFwXtKUYO1iJ0/9kEbxHqG7Lf34fnKJaw89UMTe25Djkzcyo/5SHJue6HJdoyso3OmETgI2MdFHffIYBixSfCLqwyRiTo6wUjy9mMq8MHd7CTRueBFGsHMd3i4y0sYnGFF+im7PpmCWi6u9AcB4xpN8Ry40jvBbNwNfTlGJnWVKcf42r3B5qHgknwlr31SJ7805ZURNnx1CZOM1r6YHyOcpmY/T9pevnaQvqu7/wUUBcBIj+4P+XSvyPJUdm573Zd6QdvIrbWAw0O5nHKHTfvSme74fORb1zQVBy7g33RXwJIl632ttUWLd2z0QywG8pn+9NdVRUO6BteWeDV2POiydrYl4j9Vc0AVu+HGbyS6Gcm+7HlvTN2C4R0GCbpi+8j0haMDWQB2Tpl0MMig3rNYbc2YjHbgwgdcfD+wXwtd7gQn8nqjCEM8H0TG8ggZm/Wd8GlNWwrl8Q=="));
//		System.out.println(new Base64Utils().encode(new String("123456").getBytes("GBK")));
		System.out.println(rau.getPrivateKey(priks).getFormat());
		String testdec = "M/tet95JzJNbyeUQs3rpAppqr/HfZ60X4mmKfxDlbklkp2bI1wvFRq/ifIpay4PL9N1YbJEqRl57rIcNVtHNcsCuFU/B3QY6BcQlH8aAlhrRMAkysaREvu2co+70SH0CZRMUqeZLJTmdCX7HlBqBHIV6Zzv1mzS31qW8Gtp9fWIqHnVN37Cnj8Y1FhPqI0JG/ttntnhs4TU2pZD1zsz9dKuekiLl+DmRyeJB7YgpWCvDeVcOX6yWT/zJi5bguDtZhs0QEDLbJEX5xxGPqLlJuuWLCMnpeRujSk1G8XPTe85UNaHIoz5Mba9JbK9KXHcjsALF5rlhE+80lbwsplDUkYstuRGLQNiLaUDrvJpszT2/lGxIGihNCEM6LUJohioj8uKe1ylCekJ44et0ROUnsJiGknzFD1Yr6r0bBYR8oQSoT1zGLlE82M4D+1r33Np7+uvkihOwNDH7V10e3bQDDdCU+iNFZl/cg03xexlOSl3D0aojC5b9og5YpfFTIfyxfogNP0ntmxX4mKisx49xtumUvgEGPIH320tvUubpoiMr67wnJEH0QgfArA+Q3wK5uEb0iXTN+6ukNGOFPjwUAtNwqWG7Hk3Wa3IidCDKmhFTRSJkWLVHpByGWc91hXf6lApDqPyPKOgmZMHAnjQ+VBRV+ykzgdiu27ezfJCBhNtAjuC+/A1bnfY3BS7x2AFIK+APhITwX84fefKSbhaM4+c+BEiYjrg2ip8AHx7eXqmb/01OHT0VAXfQ5KmBQkNYLT6GrCndcnGPUsFpLo71vTTks+uLMSMQZeHCm8lUR6UN8viCPdVeC9eEmFKkU2rSQ2OPQD3DzPoWDhcnTVkJEQ==";
		System.out.println("decrypted 1: " + testdec);
//		System.out.println("decrypted 2: " + new Base64Utils().decode(testdec));
//		System.out.println(
//				"decrypted 3: " + rau.decryptByPrivateKey(testdec.getBytes("UTF-8"), rau.getPrivateKey(priks)));
		System.out.println("decrypted: " + new String(
				rau.decryptByPrivateKey("RSA", new Base64Utils().decode(emp), rau.getPrivateKey(priks)), "UTF-8"));
		PublicKey pk = rau.getPublicKey(rau.getPublicKey(kdata));
		System.out.println("PUBK format: " + rau.getPublicKey(rau.getPublicKey(kdata)).getFormat());
		System.out.println("PRIK format: " + rau.getPrivateKey(rau.getPrivateKey(kdata)).getFormat());
//		EncodedKeySpec eks = new PKCS8EncodedKeySpec(rau.getPublicKey(rau.getPublicKey(kdata)).getEncoded());
//		System.out.println(eks.getFormat());
//		System.out.println(new Base64Utils().encode(eks.getEncoded()));

		String testencdata = "eqbl/APlOIFVh7zVHOq7DyOAWENi10o28ROsclLA6GrcS/hfpXb5dl9NhJessupxoB03apzhuRgtQ6lEKzVBiE2gEaZzAvR3w3+N4GD/MXGF5XppND5fTQJKpZ5ImIMbLVufh7JGIM3Lu01RACRwJ5Pjzk9V9fboqhoLIaAGlw3bMU/iRBVBglFIqGDkJLw7fyof4H6VQB8oRfpNiqXhszjpVhwUBGS4HkxtYHMtAK6tp9OEjp6gN/umI80xOdcy8+MmkYYvnI37tMgjU8ClfGJH1hL2WtZCPM9RDdcEztAQpHXNNopHbtH27ItmHiaReo7UOJd5QBBfbKFAveML5g==";
//		byte[] testplaintext = rau.decryptByPrivateKey(testencdata.getBytes(),
//				rau.getPrivateKey(rau.getPrivateKey(kdata)));
		byte[] testplaintext = rau.decryptByPrivateKey(testencdata.getBytes(),
				rau.getPrivateKey(rau.getPrivateKey(kdata)));
//		byte[] testplaintext = rau.decryptByPrivateKey(new Base64Utils().decode(testencdata),
//				rau.getPrivateKey(rau.getPrivateKey(kdata)));
//		byte[] testplaintext = rau.decryptByPrivateKey(new Base64Utils().decode(testencdata),
//				rau.getPrivateKey(new String(eks.getEncoded())));
//		byte[] testplaintext = rau.decryptByPublicKey(new Base64Utils().decode(testencdata),
//				rau.getPublicKey(rau.getPublicKey(kdata)));
		System.out.println(new String(testplaintext));
	}
}