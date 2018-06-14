package models;

import crypto.Crypto;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 04/05/18
 * Time : 10:52 PM
 * Project Name: ClientMS
 * Class Name: employee
 */
public class Employee
{
	
	public boolean isDisabled()
	{
		return disabled;
	}
	
	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}
	
	public void setDisabled(byte disabled)
	{
		this.disabled = disabled != 0;
		
	}
	
	public void setDisabled(String disabled)
	{
		this.disabled = Integer.valueOf(disabled) != 0;
	}
	
	public enum USER_ROLE{
		USER,
		MANAGER,
		ADMIN
	}
	
	private String first_name;
	private String last_name;
	private String username;
	private byte[] password;
	private USER_ROLE role;
	private boolean disabled=false;
	
	public String getFirst_name()
	{
		return first_name;
	}
	
	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}
	
	public String getLast_name()
	{
		return last_name;
	}
	
	public void setLast_name(String last_name)
	{
		this.last_name = last_name;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public byte[] getPassword()
	{
		return password;
	}
	
	public void setPassword(byte[] password)
	{
		this.password = password;
	}
	
	public void print() throws GeneralSecurityException, IOException, URISyntaxException
	{
		if( password == null )
		{
			System.out.println("NULL - EMPLOYEE");
			return;
		}
		Crypto c = new Crypto();
		String decrypt = c.byteArrToString(c.decrypt(password));
		System.out.println("employee First Name: "+first_name);
		System.out.println("employee Last Name: "+last_name);
		System.out.println("employee Username: "+username);
		System.out.println("employee Password: "+decrypt);
		System.out.println("employee Role: "+role.name());
	}
	
	public USER_ROLE getRole()
	{
		return role;
	}
	
	public void setRole(USER_ROLE role)
	{
		this.role = role;
	}
}
