package home;

import controllers.HomeController;
import errors.Error;
import events.LoginEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Employee;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 06/05/18
 * Time : 11:32 PM
 * Project Name: ClientMS
 * Class Name: Home
 */

//TODO: Remove Application
public class Home extends Application
{
	
	private Employee empl;
	private HomeController homeController = new HomeController();
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		//register event bus
		EventBus.getDefault().register(this);
		
		//Load Layouts
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		//Set the scenes
		FXMLLoader loader = new FXMLLoader(cl.getResource("layout/Home.fxml"));
		loader.setController(homeController);
		
		Parent root = loader.load();
		primaryStage.setTitle("Specialized Hijama - Home");
		primaryStage.setScene(new Scene(root, 600, 400));
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		homeController.setButtonAction();
		primaryStage.show();
	}
	
	//TODO: Remove this method
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Subscribe
	public void onLoginEvent(LoginEvent event)
	{
		try
		{
			empl = event.employee;
			empl.print();
			homeController.setEmpl(empl);
			homeController.setEmployeeInformation();
		} catch( GeneralSecurityException|IOException|URISyntaxException e )
		{
			e.printStackTrace();
		}
	}
	
	public static boolean searchForUser(String identifier, String method)
	{
		//TODO: Perform query
		return false;
	}
	
	public static boolean validateSearchPatientInput(String identifier, Error error)
	{
		boolean valid = true;
		
		if(identifier==null || identifier.equals("") || identifier.length()<=0)
		{
			valid=false;
			error.getErrors().add("Search cannot be empty.");
		}
		if( identifier.length()>15 )
		{
			error.getErrors().add("Length cannot be greater than 15.");
			valid = false;
		}
		
		if( !identifier.matches("^[0-9]*$") )
		{
			error.getErrors().add("Has to be purely numbers.");
			valid = false;
		}
		return valid;
	}
}
