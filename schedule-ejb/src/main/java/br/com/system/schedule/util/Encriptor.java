package br.com.system.schedule.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

public final class Encriptor {
	
	private final String NOME_CLASS = "Encriptor";

	@Inject
	private Logger logger;
	
	public static final String ALGORITHM_MD5 = "MD5";
	
	private static final Locale LOCALE_PTBR = new Locale("pt", "BR");
	
	public String criptografar(final String password) {
	       
        MessageDigest messageDigest = null;
        String retorno = null;
		try {
			messageDigest = MessageDigest.getInstance(ALGORITHM_MD5);
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.SEVERE, NOME_CLASS +".criptografar()", e);
		}
			
		retorno = hexCodes(digest(password, messageDigest));
		
		retorno = retorno.toUpperCase(LOCALE_PTBR);
		
		return retorno;
	}


	private byte[] digest(final String password, final MessageDigest messageDigest) {
		return messageDigest.digest(password.getBytes());
	}
	
	
	private String hexCodes(final byte[] text){
		char[] hexOutput = new char[text.length * 2];
		String hexString;
		String strToHexString;
		int intTextLen;
		byte bText;
		String strHexString;
		intTextLen = text.length;
		int lenHexString;
		
		for (int i = 0; i < intTextLen; i++) {
			bText = text[i];
			strToHexString = Integer.toHexString(bText);
			hexString = "00" + strToHexString;
		    lenHexString = hexStringLength(hexString);
		    strHexString = hexStringToLowerCase(hexString);
		    strHexStringGetChars(hexOutput, strHexString, lenHexString, i);
		}	
		
		String ret = new String(hexOutput);
		
		return ret;
	}


	private void strHexStringGetChars(final char[] hexOutput, final String strHexString, final int lenHexString, final int index) {
		strHexString.getChars(lenHexString - 2, lenHexString, hexOutput, index * 2);
	}


	private String hexStringToLowerCase(final String hexString) {
		return hexString.toLowerCase(LOCALE_PTBR);
	}


	private int hexStringLength(final String hexString) {
		return hexString.length();
	}
	
	
}
