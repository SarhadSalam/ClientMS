package controllers;

import employee.EmployeeManagement;
import authentication.DatabaseConnection;
import authentication.EmployeeLogin;
import errors.Error;
import errors.ErrorPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Employee;
import properties.Properties;
import toasts.Toast;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 12/06/18 Time : 9:46 PM Project Name: ClientMS Class Name:
 * ManageEmployeeController
 */
public class ManageEmployeeController
{
	
	@FXML
	public TextField firstNameInput, lastNameInput, usernameInput, usernameUpdateField, usernameDisableField;
	
	@FXML
	public PasswordField passwordInput, confirmPasswordInput, confirmNewPasswordField, newPasswordField;
	
	@FXML
	public Button addButton, updateButton, disableButton;
	
	@FXML
	public ListView errorPane, errorPaneUpdate;
	
	@FXML
	public TableColumn firstnameCol, lastnameCol, usernameCol, roleCol, disabledCol;
	
	@FXML
	public TableView employeeTable;
	
	@FXML
	public TabPane tabPane;
	
	@FXML
	public Tab allEmployeesTab;
	
	@FXML
	public ChoiceBox roleDropbox;
	
	@FXML
	public void initialize()
	{
		EmployeeManagement employeeManagement = new EmployeeManagement();
		roleDropbox.setItems(employeeManagement.getRoles());
		roleDropbox.getSelectionModel().selectFirst();
	}
	
	public void setAction()
	{
		addButton.setOnAction(this::addButtonAction);
		updateButton.setOnAction(this::addUpdateButtonAction);
		disableButton.setOnAction(this::disableButtonAction);
		
		tabPane.getSelectionModel().selectedItemProperty().addListener(( (observable, oldValue, newValue) -> {
			if( newValue.getId() != null && newValue.getId().equals("allEmployeesTab") )
			{
				
				viewEmployees();
			}
		} ));
	}
	
	private void viewEmployees()
	{
		firstnameCol.setCellValueFactory(new PropertyValueFactory<>("first_name"));
		lastnameCol.setCellValueFactory(new PropertyValueFactory<>("last_name"));
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
		disabledCol.setCellValueFactory(new PropertyValueFactory<>("disabled"));
		
		EmployeeManagement employeeManagement = new EmployeeManagement();
		try
		{
			employeeTable.setEditable(false);
			employeeTable.setItems(employeeManagement.getAllEmployees());
		} catch( IOException|SQLException e )
		{
			e.printStackTrace();
		}
	}
	
	private void disableButtonAction(ActionEvent event)
	{
		Employee employee = new Employee();
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		try
		{
			Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
			if( EmployeeLogin.getEmployee(usernameDisableField.getText(), c, employee) )
			{
				c.close();
				EmployeeManagement employeeManagement = new EmployeeManagement();
				employeeManagement.disable(employee);
				Toast.makeText((Stage) updateButton.getScene().getWindow(), employee.isDisabled() ? "Enabled" : "Disabled", 3000, 500, 500);
			} else
			{
				Toast.makeText((Stage) addButton.getScene().getWindow(), "No such user!", 3000, 500, 500);
			}
			
		} catch( SQLException|IOException e )
		{
			e.printStackTrace();
		}
	}
	
	private void addUpdateButtonAction(ActionEvent event)
	{
		ErrorPane errorPaneHandler = new ErrorPane();
		EmployeeManagement employeeManagement = new EmployeeManagement();
		Error error = new Error();
		if( employeeManagement.validate(usernameUpdateField.getText(), newPasswordField.getText(), confirmNewPasswordField.getText(), error) )
		{
			try
			{
				Employee empl = new Employee();
				Properties prop = new Properties();
				DatabaseConnection databaseConnection = new DatabaseConnection();
				Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
				if( !EmployeeLogin.getEmployee(usernameUpdateField.getText(), c, empl) )
				{
					Toast.makeText((Stage) addButton.getScene().getWindow(), "No such user!", 3000, 500, 500);
				} else
				{
					employeeManagement.updatePassword(empl, newPasswordField.getText());
					Toast.makeText((Stage) addButton.getScene().getWindow(), "Password Changed!", 3000, 500, 500);
				}
			} catch( IOException|SQLException|URISyntaxException|GeneralSecurityException e )
			{
				e.printStackTrace();
			}
		} else
		{
			errorPaneHandler.handleErrorPane(errorPaneUpdate, error);
		}
	}
	
	private void addButtonAction(ActionEvent event)
	{
		ErrorPane errorPaneHandler = new ErrorPane();
		EmployeeManagement employeeManagement = new EmployeeManagement();
		Error error = new Error();
		if( employeeManagement.validate(firstNameInput.getText()+lastNameInput.getText(), usernameInput.getText(), passwordInput.getText(), confirmPasswordInput.getText(), error) )
		{
			try
			{
				Properties prop = new Properties();
				DatabaseConnection databaseConnection = new DatabaseConnection();
				Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
				Employee employee = new Employee();
				if( !EmployeeLogin.getEmployee(usernameInput.getText(), c, employee) )
				{
					employeeManagement.createASingleEmployee(firstNameInput.getText(), lastNameInput.getText(), usernameInput.getText(), passwordInput.getText(), roleDropbox.getValue().toString());
					Toast.makeText((Stage) updateButton.getScene().getWindow(), "Added", 3000, 500, 500);
				} else
				{
					Toast.makeText((Stage) addButton.getScene().getWindow(), "User already exists.", 3000, 500, 500);
				}
			} catch( IOException|SQLException|URISyntaxException|GeneralSecurityException e )
			{
				e.printStackTrace();
			}
		} else
		{
			errorPaneHandler.handleErrorPane(errorPane, error);
		}
	}
}
