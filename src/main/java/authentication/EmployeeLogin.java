package authentication;

import errors.Error;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 03/05/18 Time : 3:30 PM Project Name: inc.sarhad.CMS Class Name:
 * authentication.EmployeeLogin
 */
public class EmployeeLogin extends Application
{
	public boolean validateInput(String username, String password, Error error)
	{
		boolean noInputIsWrong = true;
		if( username.length()<3 )
		{
			
			error.getErrors().add("Username has to be larger than 3 characters.");
			noInputIsWrong = false;
		}
		
		//if username or password is longer
		if( username.length()>=10 )
		{
			error.getErrors().add("Username has to be smaller than 10 characters.");
			noInputIsWrong = false;
		}
		
		if( password.length()<6 )
		{
			error.getErrors().add("Password has to be larger than 6 characters.");
			noInputIsWrong = false;
		}
		
		if( password.length()>=12 )
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
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		//Load Layouts
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		//Set the scenes
		Parent root = FXMLLoader.load(cl.getResource("layout/EmployeeLogin.fxml"));
		primaryStage.setTitle("Specialized Hijama");
		primaryStage.setScene(new Scene(root, 600, 400));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
