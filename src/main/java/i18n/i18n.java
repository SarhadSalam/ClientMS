package i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 29/05/18 Time : 11:31 AM Project Name: ClientMS Class Name:
 * i18n.i18n
 */
public class i18n
{
	
	public ResourceBundle getResourceBundle(String lang, String country)
	{
		Locale locale = new Locale(lang, country);
		return ResourceBundle.getBundle("i18n/MessageBundle", locale, new UTF8Control());
	}
	
	public ResourceBundle getResourceBundle(Locale locale)
	{
		return ResourceBundle.getBundle("i18n/MessageBundle", locale, new UTF8Control());
	}
	
	//loads the resource bundle as utf 8 instead of ascii
	public class UTF8Control extends ResourceBundle.Control
	{
		
		public ResourceBundle newBundle
				(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException
		{
			// The below is a copy of the default implementation.
			String bundleName = toBundleName(baseName, locale);
			String resourceName = toResourceName(bundleName, "properties");
			ResourceBundle bundle = null;
			InputStream stream = null;
			if( reload )
			{
				URL url = loader.getResource(resourceName);
				if( url != null )
				{
					URLConnection connection = url.openConnection();
					if( connection != null )
					{
						connection.setUseCaches(false);
						stream = connection.getInputStream();
					}
				}
			} else
			{
				stream = loader.getResourceAsStream(resourceName);
			}
			if( stream != null )
			{
				try
				{
					// Only this line is changed to make it to read properties files as UTF-8.
					bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
				} finally
				{
					stream.close();
				}
			}
			return bundle;
		}
	}
}
