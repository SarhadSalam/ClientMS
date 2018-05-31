package authentication;

import models.Employee;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 31/05/18 Time : 2:31 PM Project Name: ClientMS Class Name:
 * Privileges
 */
public class Privileges
{
	
	private Employee employee;
	
	public Privileges(Employee employee)
	{
		this.employee = employee;
	}
	
	public boolean hasAdminStatus()
	{
		return employee.getRole() == Employee.USER_ROLE.ADMIN;
	}
	
	public boolean hasManagerStatus()
	{
		return employee.getRole() == Employee.USER_ROLE.MANAGER;
	}
	
	public boolean hasUserStatus()
	{
		return employee.getRole() == Employee.USER_ROLE.USER;
	}
	
	public boolean hasMoreThanUserStatus()
	{
		return employee.getRole() != Employee.USER_ROLE.USER;
	}
}
