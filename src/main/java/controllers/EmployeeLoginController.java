package controllers;

import authentication.EmployeeLogin;
import errors.Error;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import models.Employee;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 1:01 AM Project Name: inc.sarhad.CMS Class Name:
 * EmployeeLoginController
 */
public class EmployeeLoginController
{
	
	/*
	 * Sets the employeeLogin UI here.
	 * */
	private EmployeeLogin employeeLogin = new EmployeeLogin();
	private Error error = new Error();
	
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
	private void signUserIn()
	{
		//if input is invalid
		if( !employeeLogin.validateInput(username.getText(), password.getText(), error) )
		{
			//if error pane is not visible, then this is the first tie, set the color text to red
			if( !errorPane.isVisible() ) errorPane.setCellFactory(param -> new ColorText());
				//if error pane is already visible, that means there was previous error, so clear it
			else errorPane.getItems().clear();
			
			//control the error pane and get the errors
			errorPane.setVisible(true);
			errorPane.setItems(FXCollections.observableArrayList(error.getErrors()));
			error.setErrors(null);
		} else
		{
			//if login was succesffull, popup a dialog and start the login code
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
			if( empl != null )
			{
				//load new activity
				System.out.println("Well that works daddy");
				alert.close();
				
			} else
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
	
	//color warning red for the listview
	static class ColorText extends ListCell<String>
	{
		
		@Override
		public void updateItem(String s, boolean b)
		{
			super.updateItem(s, b);
			
			if( s == null || b )
			{
				setText(null);
				setGraphic(null);
			} else
			{
				setText(s);
				this.setTextFill(Color.RED);
			}
		}
	}
}
