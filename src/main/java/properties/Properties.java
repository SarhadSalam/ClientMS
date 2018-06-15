package properties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

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
		return properties.getProperty(prop);
	}
}
