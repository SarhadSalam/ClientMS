package models;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 07/05/18
 * Time : 9:05 PM
 * Project Name: ClientMS
 * Class Name: Patient
 */
public class Patient
{
	
	private String name, employee_entered;
	private int age, govid, phone;
	private char gender;
	
	public Patient(String name, String employee_entered, int age, int govid, int phone, char gender)
	{
		this.name = name;
		this.employee_entered = employee_entered;
		this.age = age;
		this.govid = govid;
		this.phone = phone;
		this.gender = gender;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getEmployee_entered()
	{
		return employee_entered;
	}
	
	public void setEmployee_entered(String employee_entered)
	{
		this.employee_entered = employee_entered;
	}
	
	public int getPhone()
	{
		return phone;
	}
	
	public void setPhone(int phone)
	{
		this.phone = phone;
	}
	
	public char getGender()
	{
		return gender;
	}
	
	public void setGender(char gender)
	{
		this.gender = gender;
	}
	
	public int getGovid()
	{
		return govid;
	}
	
	public void setGovid(int govid)
	{
		this.govid = govid;
	}
	
	public int getAge()
	{
		return age;
	}
	
	public void setAge(int age)
	{
		this.age = age;
	}
	
	public void print()
	{
		System.out.println("Patient information:");
		if( name == null )
		{
			System.out.println("NULL");
			return;
		}
		System.out.println("Name: "+name);
		System.out.println("Age: "+age);
		System.out.println("Sex: "+gender);
		System.out.println("GovId: "+govid);
		System.out.println("Phone: "+phone);
		System.out.println("Employee Entered: "+employee_entered);
		System.out.println("``````````````````");
	}
}
