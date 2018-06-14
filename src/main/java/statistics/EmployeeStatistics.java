package statistics;

import controllers.EmployeeStatisticsController;
import controllers.MenuBarController;
import controllers.PatientStatisticsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Employee;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 13/06/18 Time : 6:00 PM Project Name: ClientMS Class Name:
 * EmployeeStatistics
 */
public class EmployeeStatistics
{
	
	public void start(Stage parent, Employee empl) throws IOException
	{
		MenuBarController menuBarController = new MenuBarController();
		EmployeeStatisticsController employeeStatisticsController = new EmployeeStatisticsController();
		//Load Layouts
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		//load the language
		ResourceBundle rs = new i18n.i18n().getResourceBundle("en", "US");
		
		//Set the scenes
		FXMLLoader statScreen = new FXMLLoader(cl.getResource("layout/EmployeeStatistics.fxml"));
		FXMLLoader menu = new FXMLLoader(cl.getResource("layout/Menubar.fxml"));
		
		//add stat screen controller
		menu.setController(menuBarController);
		menu.setResources(rs);
		statScreen.setController(employeeStatisticsController);
		statScreen.setResources(rs);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menu.load());
		borderPane.setCenter(statScreen.load());
		
		Stage primaryStage = new Stage();
		primaryStage.setTitle(rs.getString("title_login_screen_stat"));
		primaryStage.setScene(new Scene(borderPane, 800, 600));
		primaryStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		primaryStage.initOwner(parent);
		
		primaryStage.setResizable(true);
		
		employeeStatisticsController.setAction();
		menuBarController.setEmployee(empl);
		menuBarController.setUpMenuBar();
		menuBarController.setMenuItemOptions(primaryStage);
		primaryStage.showAndWait();
		
	}
}
