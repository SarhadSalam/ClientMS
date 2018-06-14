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
	
	private String name, employee_entered, govid, phone;
	private int age, id;
	private char gender;
	
	public Patient(String name, String employee_entered, int age, String govid, String phone, char gender, int id)
	{
		this.name = name;
		this.employee_entered = employee_entered;
		this.age = age;
		this.govid = govid;
		this.phone = phone;
		this.gender = gender;
		this.id=id;
	}
	
	public Patient()
	{
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
	
	public String getPhone()
	{
		return phone;
	}
	
	public void setPhone(String phone)
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
	
	public String getGovid()
	{
		return govid;
	}
	
	public void setGovid(String govid)
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
		System.out.println("employee Entered: "+employee_entered);
		System.out.println("``````````````````");
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
}
