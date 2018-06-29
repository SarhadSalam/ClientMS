package events;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 13/06/18 Time : 4:01 PM Project Name: ClientMS Class Name:
 * LogoutEvent
 */
public class LogoutEvent
{
	public final boolean logout;
	
	public LogoutEvent(boolean logout)
	{
		this.logout = logout;
	}
	
	@Subscribe
	public void onLogoutEvent(LogoutEvent event)
	{
	}
	
}
