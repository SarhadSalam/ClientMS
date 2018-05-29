package properties;

import javax.security.auth.login.Configuration;
import java.io.IOException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 9:32 PM Project Name: ClientMS Class Name:
 * Properties
 */
public class Properties
{
	
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
		String res = "user.properties";
		if( type == PROPERTY_TYPE.env )
		{
			res = "env.properties";
		}
		properties.load(cl.getResourceAsStream(res));
		return properties.getProperty(prop);
	}
	
	public void setProperties(String key, String val, PROPERTY_TYPE type) throws Exception
	{
		
		String res = "user.properties";
		if( type == PROPERTY_TYPE.env )
		{
			res = "env.properties";
		}
		properties.load(cl.getResourceAsStream(res));
		properties.setProperty(key, val);
	}
}
