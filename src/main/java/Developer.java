import authentication.DatabaseConnection;
import authentication.EmployeeLogin;
import crypto.Crypto;
import com.github.javafaker.Faker;
import models.Employee;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 04/05/18
 * Time : 10:09 PM
 * Project Name: ClientMS
 * Class Name: Developer
 */
public class Developer
{
	
	public void createEmployee(String first_name, String last_name, String username, String password, Connection c) throws SQLException, GeneralSecurityException, IOException, URISyntaxException
	{
		Crypto crypto = new Crypto();
		String statement = "INSERT INTO employee (first_name, last_name, username, password) values (?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(statement);
		ps.setString(1, first_name);
		ps.setString(2, last_name);
		ps.setString(3, username);
		ps.setBytes(4, crypto.encrypt(password));
		ps.execute();
	}
	
	public static void createClusterOfEmployees(String[] args) throws IOException, SQLException, GeneralSecurityException, URISyntaxException
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection("sarhad", "4791");
		Developer dev = new Developer();
		for( int i = 0; i<10; i++ )
		{
			Faker faker = new Faker();
			dev.createEmployee(faker.name().firstName(), faker.name().lastName(), faker.name().username(), faker.artist().name(), c);
		}
		c.close();
	}
	
	public static void main(String[] args) throws IOException, SQLException, GeneralSecurityException, URISyntaxException
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection("sarhad", "4791");
		Crypto crypto = new Crypto();
		
		Developer dev = new Developer();
		dev.createEmployee("bigboss", "okboss", "boss", "stillahoe", c);

		Employee empl = new Employee();
		EmployeeLogin employeeLogin = new EmployeeLogin();
		
		if(employeeLogin.getEmployee("boss", c, empl))
		{
			empl.print();
		} else System.out.println("Error, Employee not found.");
	}
	
}
