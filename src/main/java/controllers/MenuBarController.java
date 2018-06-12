package controllers;

import authentication.EmployeeLogin;
import authentication.PreventClose;
import authentication.Privileges;
import global.Global;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import models.Employee;
import statistics.PatientStatistics;
import toasts.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 19/05/18 Time : 11:24 AM Project Name: ClientMS Class Name:
 * MenuBarController
 */
public class MenuBarController
{
	
	private Employee empl;
	private boolean userInfoAvailable = false;
	
	@FXML
	public ResourceBundle resources;
	
	@FXML
	public MenuBar menubar;
	
	@FXML
	public Menu userMenu, timeLabel, managementMenu;
	
	@FXML
	public MenuItem closeItem, signOutItem, statItem, fileBugItem, aboutItem, emploteeStatesItem;
	
	public void setUpMenuBar()
	{
		//if there is no user available, dont show user options.
		if( empl == null )
		{
			menubar.getMenus().remove(userMenu);
			menubar.getMenus().remove(managementMenu);
		}
		
		if( empl != null )
		{
			Privileges privileges = new Privileges(empl);
			if( privileges.hasUserStatus() )
			{
				menubar.getMenus().remove(managementMenu);
			}
		}
		
		DateFormat timeFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a");
		final Timeline timeline = new Timeline(
				new KeyFrame(
						Duration.millis(1000),
						event -> timeLabel.setText(timeFormat.format(System.currentTimeMillis()))));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	public void setMenuItemOptions(Stage stage)
	{
		closeItem.setOnAction(event -> {
			PreventClose preventClose = new PreventClose();
			event.consume();
			preventClose.createAlert(stage);
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
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(resources.getString("title")+" - "+ resources.getString("software_name")+Global.getVersion());
			
			alert.setContentText(resources.getString("about_license"));
			alert.setTitle(resources.getString("about"));
			alert.initModality(Modality.WINDOW_MODAL);
			alert.initOwner(stage);
			alert.setResizable(true);
			alert.getDialogPane().requestFocus();
			alert.showAndWait();
		});
		
		statItem.setOnAction(event -> {
			PatientStatistics ps = new PatientStatistics();
			try
			{
				ps.start((Stage) menubar.getScene().getWindow(), empl);
			} catch( IOException e )
			{
				e.printStackTrace();
			}
		});
	}
	
	public void setEmployee(Employee empl)
	{
		this.empl = empl;
	}
	
	public void showToast(String message)
	{
		Toast.makeText((Stage) menubar.getScene().getWindow(), message, 3000, 500, 500);
		
	}
}
