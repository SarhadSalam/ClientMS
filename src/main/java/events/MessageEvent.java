package events;

import models.Employee;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 12/06/18 Time : 6:03 PM Project Name: ClientMS Class Name:
 * MessageEvent
 */
public class MessageEvent
{
	public final String message;
	
	public MessageEvent(String message)
	{
		this.message= message;
	}
	
	@Subscribe
	public void onMessageEvent(MessageEvent event) throws GeneralSecurityException, IOException, URISyntaxException
	{
		//what does event contain?
		System.out.println(event.message);
	}
}
