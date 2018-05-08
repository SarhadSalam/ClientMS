import authentication.DatabaseConnection;
import authentication.EmployeeLogin;
import crypto.Crypto;
import com.github.javafaker.Faker;
import models.Employee;
import properties.Properties;

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
	private Properties p=new Properties();
	
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
	
	public void createClusterOfEmployees(String[] args) throws IOException, SQLException, GeneralSecurityException, URISyntaxException
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(p.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), p.getProperty("dbPassword", Properties.PROPERTY_TYPE.env));
		Developer dev = new Developer();
		for( int i = 0; i<10; i++ )
		{
			Faker faker = new Faker();
			dev.createEmployee(faker.name().firstName(), faker.name().lastName(), faker.name().username(), faker.artist().name(), c);
		}
		c.close();
	}
	
	public void createASingleEmployee(String firstName, String lastName, String userName, String password) throws IOException, SQLException, GeneralSecurityException, URISyntaxException
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(p.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), p.getProperty("dbPassword", Properties.PROPERTY_TYPE.env));
		Developer dev = new Developer();
		dev.createEmployee(firstName, lastName, userName, password, c);
		c.close();
	}
	
}
