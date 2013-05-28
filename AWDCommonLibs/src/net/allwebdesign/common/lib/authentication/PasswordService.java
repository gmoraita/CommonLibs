package net.allwebdesign.common.lib.authentication;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * An encrypted password generator class
 * @author George Moraitakis
 */
public class PasswordService {
 
	/**
	 * Encrypts a password using the DES encryption
	 * @param plainTextPassword the plain text password
	 * @return an encrypted password prepended with a random number 
	 * @throws Exception if there is a problem in encryption
	 */
	public static String encrypt(String plainTextPassword) throws Exception{
		byte[] cleartext = plainTextPassword.getBytes("UTF8");
		DESKeySpec keySpec = new DESKeySpec("YourSecr".getBytes("UTF8"));  
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		BASE64Encoder base64encoder = new BASE64Encoder(); 

		
		SecretKey key = keyFactory.generateSecret(keySpec); 
		 
		Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe 
		cipher.init(Cipher.ENCRYPT_MODE, key); 
		
		
		Double rnd = Math.random()*1000;
		String srnd = Double.toString(rnd).substring(0,2); 
		
		return srnd+base64encoder.encode(cipher.doFinal(cleartext)); 
		  
	}
		
	 
	/**
	 * Decrypt a password using the DES algorithm
	 * @param encryptedComplexPwd a DES password that is prepended by a random number
	 * @return the plain text password
	 * @throws Exception if there is a problem in encryption
	 */
	public static String decrypt(String encryptedComplexPwd) throws Exception{
		BASE64Decoder base64decoder = new BASE64Decoder();
		String encryptedPwd = encryptedComplexPwd.substring(2);
		
		byte[] encrypedPwdBytes = base64decoder.decodeBuffer(encryptedPwd); 
		DESKeySpec keySpec = new DESKeySpec("YourSecr".getBytes("UTF8"));  
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
	public static boolean compare(String encryptedComplexPassword, String plainTextPassword){
		try {
			return decrypt(encryptedComplexPassword).equals(plainTextPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
}
