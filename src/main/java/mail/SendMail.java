package mail;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import toasts.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 19/06/18 Time : 9:24 PM Project Name: ClientMS Class Name:
 * SendMail
 */
public class SendMail
{
	
	private Configuration getConfiguration()
	{
		return new Configuration().domain("test.sarhad.me").apiKey("key-a1a12134cbb1223de504eab02ffea0a2").from("Client MS", "postmaster@test.sarhad.me");
	}
	
	public void sendErrorMail(String e)
	{
		if( netIsAvailable() )
		{
			String message = e+"\n"+new Date().toString();
			Mail.using(getConfiguration())
					.to("sarhadmaisoon18@gmail.com")
					.subject("Application Runtime Error")
					.text(message)
					.build().send();
		} else {
			Toast.makeText(null, "No Internet!", 3000, 500, 500);
		}
		
		System.out.println("Bug Report Filed");
	}
	
	private static boolean netIsAvailable()
	{
		try
		{
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			conn.getInputStream().close();
			return true;
		} catch( MalformedURLException e )
		{
			throw new RuntimeException(e);
		} catch( IOException e )
		{
			return false;
		}
	}
}
