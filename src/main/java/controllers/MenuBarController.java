package controllers;

import authentication.EmployeeLogin;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Employee;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 19/05/18 Time : 11:24 AM Project Name: ClientMS Class Name:
 * MenuBarController
 */
public class MenuBarController
{
	private Employee empl;
	private boolean userInfoAvailable = false;
	
	@FXML
	public MenuBar menubar;
	
	@FXML
	public Menu userMenu;
	
	@FXML
	public MenuItem closeItem, signOutItem, restartItem, fileBugItem, aboutItem;
	
	public void setUpMenuBar(){
		//if there is no user available, dont show user options.
		if(empl==null)
		{
			menubar.getMenus().remove(userMenu);
		}
	}
	
	public void setMenuItemOptions(Stage stage)
	{
		closeItem.setOnAction(event -> {
			stage.close();
		});
		
		signOutItem.setOnAction(event -> {
			stage.close();
			EmployeeLogin employeeLogin = new EmployeeLogin();
			try
			{
				employeeLogin.start(stage);
			} catch( Exception e )
			{
				e.printStackTrace();
			}
		});
		
		fileBugItem.setOnAction(event -> {
			//todo: create a modal and allow for filing bug
		});
		
		aboutItem.setOnAction(event -> {
			//todo: open an about screen
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Specialized Hijama - Client Management System");
			//todo: insert license
			alert.setContentText("The following software is used to maintain the clients of the Hijama clinic. \n The software is developed by Sarhad Maisoon Salam. MIT LICENSE ");
			alert.setResizable(false);
			alert.initStyle(StageStyle.UTILITY);
			alert.showAndWait();
		});
	}
	public void setEmployee(Employee empl)
	{
		this.empl=empl;
	}
}
