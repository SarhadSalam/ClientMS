package global;

import properties.Properties;

import java.io.IOException;
import java.util.Locale;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 29/05/18 Time : 5:09 PM Project Name: ClientMS Class Name:
 * global.Global
 */
public class Global
{
	
	private static Locale locale = new Locale("en", "US");
	
	public static Locale getLocale()
	{
		return locale;
	}
	
	public static void setLocale(Locale locale)
	{
		Global.locale = locale;
	}
	
	public static String getVersion() throws IOException
	{
		Properties p = new Properties();
		return p.getProperty("version", Properties.PROPERTY_TYPE.env);
	}
}
