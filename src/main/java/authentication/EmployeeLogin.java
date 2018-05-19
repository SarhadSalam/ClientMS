package authentication;

import controllers.MenuBarController;
import errors.Error;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Employee;
import properties.Properties;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 03/05/18 Time : 3:30 PM Project Name: inc.sarhad.CMS Class Name:
 * authentication.EmployeeLogin
 */
public class EmployeeLogin extends Application
{
	
	/*
	 * Responsible for validating inputs, checks for username, password and sets the error messages.
	 * */
	
	public boolean validateInput(String username, String password, Error error)
	{
		boolean noInputIsWrong = true;
		if( username.length()<3 )
		{
			error.getErrors().add("Username has to be larger than 3 characters.");
			noInputIsWrong = false;
		}
		
		//if username or password is longer
		if( username.length() >= 10 )
		{
			error.getErrors().add("Username has to be smaller than 10 characters.");
			noInputIsWrong = false;
		}
		
		if( password.length()<6 )
		{
			error.getErrors().add("Password has to be larger than 6 characters.");
			noInputIsWrong = false;
		}
		
		if( password.length() >= 12 )
		{
			error.getErrors().add("Password has to be smaller than 12 characters.");
			noInputIsWrong = false;
		}
		
		if( !username.matches("^[a-zA-Z0-9]*$") )
		{
			error.getErrors().add("Username has to be alphanumeric.");
			noInputIsWrong = false;
		}
		
		if( !password.matches("^[a-zA-Z0-9]*$") )
		{
			error.getErrors().add("Password has to be alphanumeric.");
			noInputIsWrong = false;
		}
		
		return noInputIsWrong;
	}
	
	/*
	 * Starts the primary activity scene, the employee change screen.
	 * */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		//Load Layouts
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		//Set the scenes
		Parent root = FXMLLoader.load(cl.getResource("layout/EmployeeLogin.fxml"));
		
		
		primaryStag
		
		primaryStage.setTitle("Specialized Hijama");
		primaryStage.setScene(new Scene(root, 600, 400));
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		primaryStage.show();
	}
	
	public boolean getEmployee(String username, Connection c, Employee empl) throws SQLException
	{
		String query = "select first_name, last_name,password, username from employee where username='"+username+"'";
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		//employee found
		if( rs.next() )
		{
			empl.setFirst_name(rs.getString("first_name"));
			empl.setLast_name(rs.getString("last_name"));
			empl.setPassword(rs.getBytes("password"));
			empl.setUsername(rs.getString("username"));
			return true;
		}
		
		//employee not found
		return false;
	}
	
	public Employee userExists(String username, String password) throws IOException, SQLException
	{
		Properties prop = new Properties();
		Employee empl = new Employee();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		if( getEmployee(username, c, empl) )
		{
			c.close();
			return empl;
		}
		c.close();
		return null;
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
