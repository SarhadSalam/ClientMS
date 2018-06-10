package statistics;

import authentication.PreventClose;
import controllers.MenuBarController;
import controllers.PatientStatisticsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Employee;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 01/06/18 Time : 10:43 AM Project Name: ClientMS Class Name:
 * PatientStatisticsController
 */
public class PatientStatistics
{
	
	public void start(Stage parent, Employee empl) throws IOException
	{
		MenuBarController menuBarController = new MenuBarController();
		PatientStatisticsController patientStatisticsController = new PatientStatisticsController();
		//Load Layouts
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		//load the language
		ResourceBundle rs = new i18n.i18n().getResourceBundle("en", "US");
		
		//Set the scenes
		FXMLLoader statScreen = new FXMLLoader(cl.getResource("layout/PatientStatistics.fxml"));
		FXMLLoader menu = new FXMLLoader(cl.getResource("layout/Menubar.fxml"));
		
		//add stat screen controller
		menu.setController(menuBarController);
		statScreen.setController(patientStatisticsController);
		statScreen.setResources(rs);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menu.load());
		borderPane.setCenter(statScreen.load());
		
		Stage primaryStage = new Stage();
		primaryStage.setTitle(rs.getString("title_login_screen_stat"));
		primaryStage.setScene(new Scene(borderPane, 800, 800));
		primaryStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		primaryStage.initModality(Modality.WINDOW_MODAL);
		primaryStage.initOwner(parent);
		
		primaryStage.setResizable(true);
		
		patientStatisticsController.setButtonAction();
		menuBarController.setEmployee(empl);
		menuBarController.setUpMenuBar();
		menuBarController.setMenuItemOptions(primaryStage);
		primaryStage.showAndWait();
	}
}
