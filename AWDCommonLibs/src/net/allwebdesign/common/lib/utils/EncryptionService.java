package net.allwebdesign.common.lib.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.codec.binary.Base64;
 



/**
 * An encrypted password generator class
 * @author George Moraitakis
 *
 */
public class EncryptionService {
 
	/**
	 * Encrypts a password using the DES encryption
	 * @param plainTextPassword the plain text password
	 * @return an encrypted password prepended with a random number 
	 * @throws Exception if there is a problem in encryption
	 */
	public static String encrypt(String plainTextPassword, String encryptionKey) throws Exception{
		byte[] cleartext = plainTextPassword.getBytes("UTF8");
		DESKeySpec keySpec = new DESKeySpec(encryptionKey.getBytes("UTF8"));  
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		Base64 base64encoder = new Base64(); 

		
		SecretKey key = keyFactory.generateSecret(keySpec); 
		 
		Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe 
		cipher.init(Cipher.ENCRYPT_MODE, key); 

		
		return base64encoder.encodeToString(cipher.doFinal(cleartext)); 
		  
	}
		
	 
	/**
	 * Decrypt a password using the DES algorithm
	 * @param encryptedComplexPwd a DES password that is prepended by a random number
	 * @return the plain text password
	 * @throws Exception if there is a problem in encryption
	 */
	public static String decrypt(String encryptedComplexPwd, String encryptionKey) throws Exception{
		Base64 base64decoder = new Base64();
		
		
		byte[] encrypedPwdBytes = base64decoder.decode(encryptedComplexPwd); 
		DESKeySpec keySpec = new DESKeySpec(encryptionKey.getBytes("UTF8"));  
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(keySpec); 
		Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe 
		cipher.init(Cipher.DECRYPT_MODE, key); 
		byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));
		return new String(plainTextPwdBytes);
	}
	
	/**
	 * Compares a plain text password with an encrypted one
	 * @param encryptedComplexPassword the encrypted password
	 * @param plainTextPassword the plain password
	 * @return true if they match and false if not. If there is problem comparing it also returns false
	 */
	public static boolean compare(String encryptedComplexPassword, String plainTextPassword, String encryptionKey){
		try {
			return decrypt(encryptedComplexPassword,encryptionKey).equals(plainTextPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
}

