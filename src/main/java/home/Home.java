package home;

import com.sun.javafx.stage.StageHelper;
import controllers.HomeController;
import controllers.MenuBarController;
import events.LoginEvent;
import events.LogoutEvent;
import events.MessageEvent;
import global.Global;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Employee;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import toasts.Toast;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

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
		
		ResourceBundle rs = new i18n.i18n().getResourceBundle(Global.getLocale());
		
		//Set the scenes
		FXMLLoader mainScreen = new FXMLLoader(cl.getResource("layout/Home.fxml"));
		FXMLLoader menu = new FXMLLoader(cl.getResource("layout/Menubar.fxml"));
		
		mainScreen.setResources(rs);
		menu.setResources(rs);
		
		mainScreen.setController(homeController);
		menu.setController(menuBarController);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menu.load());
		borderPane.setCenter(mainScreen.load());
		
		primaryStage.setTitle(rs.getString("title")+" - "+rs.getString("home"));
		primaryStage.setScene(new Scene(borderPane, 600, 400));
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		
		homeController.setButtonAction();
		menuBarController.setMenuItemOptions(primaryStage);
		
		primaryStage.show();
	}
	
	@Subscribe
	public void onMessageEvent(MessageEvent event)
	{
		menuBarController.showToast(event.message);
	}
	
	@Subscribe
	public void onLoginEvent(LoginEvent event)
	{
		empl = event.employee;
		homeController.setEmpl(empl);
		homeController.setEmployeeInformation();
		menuBarController.setEmployee(empl);
		homeController.handleAmountRefresh();
		menuBarController.setUpMenuBar();
	}
	
	@Subscribe
	public void onLogoutEvent(LogoutEvent event)
	{
		ObservableList<Stage> stages = StageHelper.getStages();
	
		for(int i=stages.size()-1; i>=0; i--)
		{
			stages.get(i).close();
		}
	}
}
