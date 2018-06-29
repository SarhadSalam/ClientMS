package crypto;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.config.TinkConfig;
import mail.SendMail;
import properties.Properties;
import toasts.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 04/05/18
 * Time : 6:01 PM
 * Project Name: ClientMS
 * Class Name: Crypto
 */
public class Crypto
{
	
	/*
	 * The class is used to encrypt and decrypt strings. Also provides a method for binary to string conversion
	 * */
	private String key;
	
	//run code on class initalization;
	{
		//set tink config to primitve
		Config.register(TinkConfig.TINK_1_1_0);
		
		//get the key property and set prop to null to save memory and you dont need it anymore
		properties.Properties prop = new properties.Properties();
		key = prop.getProperty("key", properties.Properties.PROPERTY_TYPE.env);
		prop = null;
	}
	
	private ClassLoader cl = ClassLoader.getSystemClassLoader();
	//the keyset handle contains the code to encrypt
	private KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withInputStream(cl.getResourceAsStream("crypto/keyset.json")));
	
	//needed to ensure which code the class throws
	public Crypto() throws GeneralSecurityException, IOException, URISyntaxException
	{
	}
	
	public byte[] encrypt(String str)
	{
		return encrypt(str.getBytes());
	}
	
	public byte[] encrypt(byte[] str)
	{
		try
		{
			Aead aead = AeadFactory.getPrimitive(keysetHandle);
			return aead.encrypt(str, key.getBytes());
		} catch( GeneralSecurityException e )
		{
			SendMail sendMail = new SendMail();
			sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
		}
		return null;
	}
	
	public byte[] decrypt(byte[] encrypted)
	{
		try
		{
			Aead aead = AeadFactory.getPrimitive(keysetHandle);
			return aead.decrypt(encrypted, key.getBytes());
		} catch( GeneralSecurityException e )
		{
			SendMail sendMail = new SendMail();
			sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
		}
		return null;
	}
	
	public String byteArrToString(byte[] b)
	{
		String str = "";
		for( int i = 0; i<b.length; i++ )
		{
			char c = (char) b[i];
			str += c;
		}
		return str;
	}
	
	private void generateKeySet() throws GeneralSecurityException, URISyntaxException, IOException
	{
		KeysetHandle keyH = KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(new File(cl.getResource("crypto/keyset.json").toURI())));
	}
}
