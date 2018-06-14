package employee;

import authentication.DatabaseConnection;
import authentication.EmployeeLogin;
import crypto.Crypto;
import errors.Error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Employee;
import properties.Properties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 12/06/18 Time : 10:01 PM Project Name: ClientMS Class Name:
 * EmployeeManagement
 */
public class EmployeeManagement
{
	
	public boolean validate(String name, String username, String password, String confirmPassword, Error error)
	{
		boolean cont = true;
		
		if( !EmployeeLogin.validateInput(username, password, error) )
		{
			cont = false;
		}
		
		if( name == null || name.equals("") )
		{
			cont = false;
			error.getErrors().add("Name cannot be empty.");
		}
		if( name != null && !name.matches("^[a-zA-Z]*$") )
		{
			cont = false;
			error.getErrors().add("Name has to be only alpha.");
		}
		
		if( !password.equals(confirmPassword) )
		{
			cont = false;
			error.getErrors().add("Password has to be the same.");
		}
		
		return cont;
	}
	
	public void createASingleEmployee(String firstName, String lastName, String userName, String password, String role) throws IOException, SQLException, GeneralSecurityException, URISyntaxException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		createEmployee(firstName, lastName, userName, password, c, Employee.USER_ROLE.valueOf(role));
		c.close();
	}
	
	private void createEmployee(String first_name, String last_name, String username, String password, Connection c, Employee.USER_ROLE user_role) throws SQLException, GeneralSecurityException, IOException, URISyntaxException
	{
		Crypto crypto = new Crypto();
		String statement = "INSERT INTO employee (first_name, last_name, username, password, role) values (?,?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(statement);
		ps.setString(1, first_name);
		ps.setString(2, last_name);
		ps.setString(3, username);
		ps.setBytes(4, crypto.encrypt(password));
		ps.setString(5, user_role.toString());
		ps.execute();
	}
	
	public boolean validate(String username, String password, String confirmPassword, Error error)
	{
		boolean cont = true;
		
		if( !EmployeeLogin.validateInput(username, password, error) )
		{
			cont = false;
		}
		
		if( !password.equals(confirmPassword) )
		{
			cont = false;
			error.getErrors().add("Password has to be the same.");
		}
		
		return cont;
	}
	
	public void updatePassword(Employee employee, String password) throws GeneralSecurityException, IOException, URISyntaxException, SQLException
	{
		Crypto crypto = new Crypto();
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		String statement = "UPDATE employee SET password = ? WHERE username = ?";
		
		PreparedStatement ps = c.prepareStatement(statement);
		ps.setBytes(1, crypto.encrypt(password));
		ps.setString(2, employee.getUsername());
		ps.executeUpdate();
		
		c.close();
	}
	
	public void disable(Employee employee) throws IOException, SQLException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		String statement = "UPDATE employee SET disabled = ? WHERE username = ?";
		
		PreparedStatement ps = c.prepareStatement(statement);
		ps.setByte(1, (byte) ( employee.isDisabled() ? 0 : 1 ));
		ps.setString(2, employee.getUsername());
		ps.executeUpdate();
		
		c.close();
	}
	
	public ObservableList<Employee> getAllEmployees() throws IOException, SQLException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		ArrayList<Employee> employees = new ArrayList<>();
		
		String query = "SELECT first_name, last_name, username, role, disabled FROM employee ORDER BY id ASC";
		
		ResultSet rs = c.createStatement().executeQuery(query);
		
		while( rs.next() )
		{
			Employee employee = new Employee();
			employee.setFirst_name(rs.getString("first_name"));
			employee.setLast_name(rs.getString("last_name"));
			employee.setUsername(rs.getString("username"));
			employee.setRole(Employee.USER_ROLE.valueOf(rs.getString("role")));
			employee.setDisabled(rs.getString("disabled"));
			employees.add(employee);
		}
		
		return FXCollections.observableArrayList(employees);
	}
	
	public ObservableList getRoles()
	{
		return FXCollections.observableList(Arrays.stream(Employee.USER_ROLE.values()).map(Enum::toString).collect(Collectors.toList()));
	}
}
