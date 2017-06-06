package com.ihsinformatics.cad4tb.mobile;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256Endec {

	private static final byte[] keyValue =
			new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't',
					'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
	
	private static AES256Endec instance;
	private Cipher cipher;
	private AES256Endec() {
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	public static AES256Endec getInstance() {
		if(instance == null) {
			instance = new AES256Endec();
		}
		
		return instance;
	}
	
	public SecretKey generateSecretKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		SecretKey secretKey = keyGenerator.generateKey();
		

		String plainText = "AES Symmetric Encryption Decryption";
		System.out.println("Plain Text Before Encryption: " + plainText);

		String encryptedText = encrypt(plainText, secretKey);
		System.out.println("Encrypted Text After Encryption: " + encryptedText);

		String decryptedText = decrypt(encryptedText, secretKey);
		System.out.println("Decrypted Text After Decryption: " + decryptedText);
		
		return secretKey;
	}
	
	public SecretKey generateSecretKey(char[] password, byte[] salt) {
		try{
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			return secret;
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public SecretKey generateSecretKey(String password, String salt) {
		try{
			byte[] _salt = salt.getBytes();
			char[] _password = password.toCharArray();
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(_password, _salt, 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			return secret;
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String encrypt(String plainText, SecretKey secretKey) throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		Key key = generateKey();
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		String encryptedText = org.apache.xerces.impl.dv.util.Base64.encode(encryptedByte);
		
		return encryptedText;
	}

	public String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
		byte[] encryptedTextByte = org.apache.xerces.impl.dv.util.Base64.decode(encryptedText);
		SecretKey key = generateKey();
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		
		return decryptedText;
	}
	
	public SecretKey generateKey() throws Exception {
		
		SecretKey key = new SecretKeySpec(ParamNames.KEY_VALUE, "AES");
		return key;
	}
}
