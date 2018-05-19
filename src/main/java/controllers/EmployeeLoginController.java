package controllers;

import authentication.EmployeeLogin;
import errors.Error;
import errors.ErrorPane;
import events.LoginEvent;
import home.Home;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Employee;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 1:01 AM Project Name: inc.sarhad.CMS Class Name:
 * EmployeeLoginController
 */
public class EmployeeLoginController
{
	
	@FXML
	public AnchorPane menubarInclude;
	/*
	 * Sets the employeeLogin UI here.
	 * */
	private EmployeeLogin employeeLogin = new EmployeeLogin();
	private Error error = new Error();
	private Home home = new Home();
	private ErrorPane errorPaneHandler = new ErrorPane();
	
	@FXML
	public AnchorPane anchorPane;
	@FXML
	public RadioButton arabicRadio;
	@FXML
	public RadioButton englishRadio;
	@FXML
	public Button signIn;
	@FXML
	public TextField username;
	@FXML
	public TextField password;
	@FXML
	public ToggleGroup languageRadio;
	@FXML
	public ListView<String> errorPane;
	
	/*When the user signs in, this method is triggered*/
	@FXML
	private void signUserIn(ActionEvent event)
	{
		//if input is invalid
		if( !employeeLogin.validateInput(username.getText(), password.getText(), error) )
		{
			errorPaneHandler.handleErrorPane(errorPane, error);
		} else
		{
			//if login was succesfull, popup a dialog and start the login code
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Signing In");
			alert.setContentText("Please wait while I sign you in");
			alert.setResizable(false);
			alert.initStyle(StageStyle.UTILITY);
			alert.show();
			//The login was a success
			Employee empl = null;
			try
			{
				empl = employeeLogin.userExists(username.getText(), password.getText());
			} catch( IOException|SQLException e )
			{
				e.printStackTrace();
			}

			//if the employee exists
			if( empl != null )
			{
				//load new activity
				alert.close();
				//add to event bus
				//create new stage and close
				Stage stage = (Stage) signIn.getScene().getWindow();
				try
				{
					stage.close();
					home.start(stage);
					
					EventBus.getDefault().post(new LoginEvent(empl, true, new Date(System.currentTimeMillis())));
				} catch( Exception e )
				{
					stage.show();
					e.printStackTrace();
				}
				
			} else //if employee does not exist
			{
				Alert noUser = new Alert(Alert.AlertType.ERROR);
				noUser.setHeaderText("No such user exists. Oops");
				noUser.setContentText("Sign in failed. Try Again.");
				noUser.initStyle(StageStyle.UTILITY);
				noUser.setResizable(false);
				alert.close();
				noUser.showAndWait();
			}
			
		}
	}
	
	public EmployeeLoginController()
	{
		if(menubarInclude==null) System.out.println(0);
		
		menubarInclude.getChildren();
	}
}


