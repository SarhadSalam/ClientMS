package home;

import controllers.HomeController;
import controllers.MenuBarController;
import events.LoginEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Employee;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 06/05/18 Time : 11:32 PM Project Name: ClientMS Class Name: Home
 */

public class Home
{
	
	private Employee empl;
	private HomeController homeController = new HomeController();
	private MenuBarController menuBarController = new MenuBarController();
	
	public void start(Stage primaryStage) throws Exception
	{
		//register event bus
		EventBus.getDefault().register(this);
		
		//Load Layouts
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		//Set the scenes
		FXMLLoader mainScreen = new FXMLLoader(cl.getResource("layout/Home.fxml"));
		FXMLLoader menu = new FXMLLoader(cl.getResource("layout/Menubar.fxml"));
		
		mainScreen.setController(homeController);
		menu.setController(menuBarController);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menu.load());
		borderPane.setCenter(mainScreen.load());
		
		primaryStage.setTitle("Specialized Hijama - Home");
		primaryStage.setScene(new Scene(borderPane, 600, 400));
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		
		homeController.setButtonAction();
		menuBarController.setMenuItemOptions(primaryStage);
		
		primaryStage.show();
	}
	
	@Subscribe
	public void onLoginEvent(LoginEvent event)
	{
		empl = event.employee;
		homeController.setEmpl(empl);
		homeController.setEmployeeInformation();
		menuBarController.setEmployee(empl);
		menuBarController.setUpMenuBar();
	}
}
