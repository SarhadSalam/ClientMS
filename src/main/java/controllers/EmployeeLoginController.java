package controllers;

import authentication.EmployeeLogin;
import errors.Error;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 1:01 AM Project Name: inc.sarhad.CMS Class Name:
 * EmployeeLoginController
 */
public class EmployeeLoginController
{
	
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
	
	@FXML
	private void signUserIn()
	{
		//if input is invalid
		if( !employeeLogin.validateInput(username.getText(), password.getText(), error))
		{
			if( !errorPane.isVisible() ) errorPane.setCellFactory(param -> new ColorText());
			else errorPane.getItems().clear();
			errorPane.setVisible(true);
			errorPane.setItems(FXCollections.observableArrayList(error.getErrors()));
			error.setErrors(null);
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("Signing In");
			alert.setContentText("Please wait while I sign you in");
			alert.getButtonTypes().clear();
			alert.initStyle(StageStyle.UNDECORATED);
			alert.show();
			//The login was a success
		}
	}
	
	//color warning red
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
