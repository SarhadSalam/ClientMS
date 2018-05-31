package controllers;

import authentication.EmployeeLogin;
import authentication.Privileges;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import models.Employee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
	public Menu userMenu, timeLabel;
	
	@FXML
	public MenuItem closeItem, signOutItem, statItem, fileBugItem, aboutItem;
	
	public void setUpMenuBar()
	{
		//if there is no user available, dont show user options.
		if( empl == null )
		{
			menubar.getMenus().remove(userMenu);
		}
		
		if( empl != null )
		{
			Privileges privileges = new Privileges(empl);
			if( privileges.hasUserStatus() )
			{
				userMenu.getItems().remove(statItem);
			}
		}
		
		DateFormat timeFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a");
		final Timeline timeline = new Timeline(
				new KeyFrame(
						Duration.millis(1000),
						event -> timeLabel.setText(timeFormat.format(System.currentTimeMillis()))
				)
		);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
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
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Specialized Hijama - Client Management System - 1.0.0");
			
			alert.setContentText("The following software is used to maintain the clients of the Hijama clinic. \n The software is developed by Sarhad Maisoon Salam.+\n"+
					"Copyright (c) [2018] [Sarhad Salam] Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\n"+
					"The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\n"+
					"THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.");
			alert.setTitle("About");
			alert.initStyle(StageStyle.UTILITY);
			alert.showAndWait();
		});
	}
	
	public void setEmployee(Employee empl)
	{
		this.empl = empl;
	}
}
