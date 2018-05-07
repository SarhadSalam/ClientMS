package controllers;

import errors.Error;
import errors.ErrorPane;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Employee;

import java.awt.event.ActionEvent;

import static home.Home.searchForUser;
import static home.Home.validateInput;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 06/05/18
 * Time : 11:29 PM
 * Project Name: ClientMS
 * Class Name: HomeController
 */
public class HomeController
{
	
	private Employee empl;
	
	@FXML
	private ToggleGroup searchMethod;
	
	@FXML
	private Label userNameLabel;
	
	@FXML
	private Button searchButton;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private ListView<String> errorPane;
	
	public void setButtonAction()
	{
		searchButton.setOnAction(event -> {
			RadioButton selectedButton = (RadioButton) searchMethod.getSelectedToggle();
			
			boolean resultOfSearch;
			Error error = new Error();
			if( validateInput(searchBar.getText(), error) )
			{
				if( selectedButton.getText().equals("Phone") )
					resultOfSearch = searchForUser(searchBar.getText(), "phone");
				else resultOfSearch = searchForUser(searchBar.getText(), "id");
				
				//patient found
				if( resultOfSearch )
				{
					System.out.println("Patient Found");
				} else
				{
					System.out.println("Patient not found");
					
					//create an alert and ask if they want a new patient to be added
				}
			} else
			{
				ErrorPane.handleErrorPane(errorPane, error);
			}
		});
	}
	
	public void setEmpl(Employee empl)
	{
		this.empl = empl;
	}
	
	public void setEmployeeInformation()
	{
		userNameLabel.setText("Hello, "+empl.getFirst_name()+" "+empl.getLast_name());
	}
}
