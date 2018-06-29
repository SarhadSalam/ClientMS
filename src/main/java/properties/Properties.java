package properties;

import crypto.Crypto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 9:32 PM Project Name: ClientMS Class Name:
 * Properties
 */
public class Properties
{
	
	private final boolean production = true;
	/*
	 * Written to prevent rewriting of code. gets the property from the properties file when user requests
	 * */
	private ClassLoader cl = Thread.currentThread().getContextClassLoader();
	private java.util.Properties properties = new java.util.Properties();
	
	public enum PROPERTY_TYPE
	{
		user,
		env
	}
	
	public String getProperty(String prop, PROPERTY_TYPE type) throws IOException
	{
		String res;
		if( production )
		{
			res = "user.properties";
			if( type == PROPERTY_TYPE.env )
			{
				res = "env.properties";
			}
		} else {
			res = "user.properties";
			if( type == PROPERTY_TYPE.env )
			{
				res = "dev.env.properties";
			}
		}
		properties.load(cl.getResourceAsStream(res));
		
		if(prop.equals("dbPassword")) {
			try
			{
				byte b[] = {1, 33, 86, -33, -7, 97, -76, 83, 13, 30, 44, -71, 82, 105, -15, -19, -57, 111, 28, -7, -88, -27, 1, 83, 65, -74, -1, 123, 72, 23, 127, 59, 20, -106, 107, -106, -3, 118, -104, -19, -51, -22, -19};
				Crypto c = new Crypto();
				return c.byteArrToString(c.decrypt(b));
			} catch( GeneralSecurityException | URISyntaxException e )
			{
				return null;
			}
		}
		
		return properties.getProperty(prop);
	}
}
