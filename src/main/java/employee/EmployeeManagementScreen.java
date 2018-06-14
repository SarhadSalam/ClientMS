package employee;

import controllers.ManageEmployeeController;
import controllers.MenuBarController;
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
 * Class Details:- Author: Sarhad User: sarhad Date: 12/06/18 Time : 11:17 PM Project Name: ClientMS Class Name:
 * EmployeeManagementScreen
 */
public class EmployeeManagementScreen
{
	public void start(Stage parent, Employee empl) throws IOException
	{
		MenuBarController menuBarController = new MenuBarController();
		ManageEmployeeController manageEmployeeController = new ManageEmployeeController();
		//Load Layouts
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		//load the language
		ResourceBundle rs = new i18n.i18n().getResourceBundle("en", "US");
		
		//Set the scenes
		FXMLLoader addEmplScreen = new FXMLLoader(cl.getResource("layout/EmployeeManagement.fxml"));
		FXMLLoader menu = new FXMLLoader(cl.getResource("layout/Menubar.fxml"));
		
		//add stat screen controller
		menu.setController(menuBarController);
		addEmplScreen.setController(manageEmployeeController);
		addEmplScreen.setResources(rs);
		
		menu.setResources(rs);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(menu.load());
		borderPane.setCenter(addEmplScreen.load());
		
		Stage primaryStage = new Stage();
		primaryStage.setTitle(rs.getString("title_add_empl"));
		primaryStage.setScene(new Scene(borderPane, 600, 600));
		primaryStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		primaryStage.initModality(Modality.WINDOW_MODAL);
		primaryStage.initOwner(parent);
		
		primaryStage.setResizable(true);
		
		manageEmployeeController.setAction();
		menuBarController.setEmployee(empl);
		menuBarController.setUpMenuBar();
		menuBarController.setMenuItemOptions(primaryStage);
		primaryStage.showAndWait();
	}
}
