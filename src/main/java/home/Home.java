package home;

import authentication.DatabaseConnection;
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
import properties.Properties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 06/05/18 Time : 11:32 PM Project Name: ClientMS Class Name: Home
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
		empl = event.employee;
		homeController.setEmpl(empl);
		homeController.setEmployeeInformation();
	}
}
