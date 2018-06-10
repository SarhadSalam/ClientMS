package authentication;

import controllers.EmployeeLoginController;
import controllers.MenuBarController;
import crypto.Crypto;
import errors.Error;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Employee;
import properties.Properties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 03/05/18 Time : 3:30 PM Project Name: inc.sarhad.CMS Class Name:
 * authentication.EmployeeLogin
 */
public class EmployeeLogin extends Application
{
	
	/*
	 * Responsible for validating inputs, checks for username, password and sets the error messages.
	 * */
	
	public static boolean validateInput(String username, String password, Error error)
	{
		ResourceBundle rs = new i18n.i18n().getResourceBundle("en", "US");
		boolean noInputIsWrong = true;
		if( username.length()<3 )
		{
			error.getErrors().add(rs.getString("username_min_char"));
			noInputIsWrong = false;
		}
		
		//if username or password is longer
		if( username.length() >= 10 )
		{
			error.getErrors().add(rs.getString("username_max_char"));
			noInputIsWrong = false;
		}
		
		if( password.length()<6 )
		{
			error.getErrors().add(rs.getString("password_min_char"));
			noInputIsWrong = false;
		}
		
		if( password.length() >= 12 )
		{
			error.getErrors().add(rs.getString("password_max_char"));
			noInputIsWrong = false;
		}
		
		if( !username.matches("^[a-zA-Z0-9]*$") )
		{
			error.getErrors().add(rs.getString("username_alphanum"));
			noInputIsWrong = false;
		}
		
		if( !password.matches("^[a-zA-Z0-9]*$") )
		{
			error.getErrors().add(rs.getString("password_alphanum"));
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
		
		EmployeeLoginController employeeLoginController = new EmployeeLoginController();
		MenuBarController menuBarController = new MenuBarController();
		
		//Load Layouts
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		//load the language
		ResourceBundle rs = new i18n.i18n().getResourceBundle("en", "US");
		
		//Set the scenes
		FXMLLoader login = new FXMLLoader(cl.getResource("layout/EmployeeLogin.fxml"));
		FXMLLoader menu = new FXMLLoader(cl.getResource("layout/Menubar.fxml"));
		
		login.setController(employeeLoginController);
		menu.setController(menuBarController);
		
		login.setResources(rs);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menu.load());
		borderPane.setCenter(login.load());
		
		primaryStage.setTitle(rs.getString("title"));
		primaryStage.setScene(new Scene(borderPane, 600, 400));
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		primaryStage.setOnCloseRequest(event -> {
			PreventClose preventClose = new PreventClose();
			event.consume();
			preventClose.createAlert(primaryStage);
		});
		employeeLoginController.setButtonAction(primaryStage);
		menuBarController.setUpMenuBar();
		menuBarController.setMenuItemOptions(primaryStage);
		
		primaryStage.show();
	}
	
	private static boolean getEmployee(String username, Connection c, Employee empl) throws SQLException
	{
		String query = "select first_name, last_name,password, username, role from employee where username='"+username+"'";
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		//employee foundP
		if( rs.next() )
		{
			empl.setFirst_name(rs.getString("first_name"));
			empl.setLast_name(rs.getString("last_name"));
			empl.setPassword(rs.getBytes("password"));
			empl.setUsername(rs.getString("username"));
			empl.setRole(Employee.USER_ROLE.valueOf(rs.getString("role")));
			
			return true;
		}
		
		//employee not found
		return false;
	}
	
	public static Employee userExists(String username, String password) throws IOException, SQLException, GeneralSecurityException, URISyntaxException
	{
		Properties prop = new Properties();
		Employee empl = new Employee();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		if( getEmployee(username, c, empl) )
		{
			c.close();
			Crypto crypto = new Crypto();
			if( password.equals(new String(crypto.decrypt(empl.getPassword()), "UTF-8")) ) return empl;
			else return null;
		}
		c.close();
		return null;
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
