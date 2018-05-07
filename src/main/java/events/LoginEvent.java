package events;

import models.Employee;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 05/05/18
 * Time : 1:11 AM
 * Project Name: ClientMS
 * Class Name: LoginEvent
 */
public class LoginEvent
{
	public final Employee employee;
	public final boolean authStatus;
	public final Date loginTime;
	
	public LoginEvent(Employee employee, boolean authStatus, Date loginTime)
	{
		this.employee = employee;
		this.authStatus = authStatus;
		this.loginTime = loginTime;
	}
	
	@Subscribe
	public void onLoginEvent(LoginEvent event) throws GeneralSecurityException, IOException, URISyntaxException
	{
		//what does event contain?
		event.employee.print();
	}
}
