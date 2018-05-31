package controllers;

import errors.Error;
import errors.ErrorPane;
import events.LoginEvent;
import home.Home;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Employee;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import static authentication.EmployeeLogin.validateInput;
import static authentication.EmployeeLogin.userExists;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 1:01 AM Project Name: inc.sarhad.CMS Class Name:
 * EmployeeLoginController
 */
public class EmployeeLoginController
{
	
	/*
	 * Sets the employeeLogin UI here.
	 * */
	private Error error = new Error();
	private Home home = new Home();
	private ErrorPane errorPaneHandler = new ErrorPane();
	
	@FXML
	public AnchorPane menubarInclude;
	@FXML
	public ResourceBundle resources;
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
	
	public void setButtonAction()
	{
		signIn.setOnAction(this::signUserIn);
	}
	
	private void setLocale()
	{
		//set the properties of the user
		RadioButton radioButton = (RadioButton) languageRadio.getSelectedToggle();
		if( !radioButton.getText().equalsIgnoreCase("english") )
		{
			global.Global.setLocale(new Locale("ar", "SA"));
		}
	}
	
	/*When the user signs in, this method is triggered*/
	private void signUserIn(ActionEvent event)
	{
		setLocale();
		//if input is invalid
		if( !validateInput(username.getText(), password.getText(), error) )
		{
			errorPaneHandler.handleErrorPane(errorPane, error);
		} else
		{
			//if input was successfull, popup a dialog and start the login code
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(resources.getString("signing_in"));
			alert.setContentText(resources.getString("sign_in_wait"));
			alert.setResizable(false);
			alert.initStyle(StageStyle.UTILITY);
			alert.show();
			
			//The login was a success
			
			Employee empl = null;
			try
			{
				try
				{
					empl = userExists(username.getText(), password.getText());
				} catch( GeneralSecurityException|URISyntaxException e )
				{
					e.printStackTrace();
				}
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
				noUser.setHeaderText(resources.getString("no_user"));
				noUser.setContentText(resources.getString("sign_in_fail"));
				noUser.initStyle(StageStyle.UTILITY);
				noUser.setResizable(false);
				alert.close();
				noUser.showAndWait();
			}
		}
	}
}


