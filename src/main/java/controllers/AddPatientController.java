package controllers;

import customers.AddPatient;
import errors.Error;
import errors.ErrorPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Employee;
import models.Patient;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 07/05/18 Time : 11:57 AM Project Name: ClientMS Class Name:
 * AddPatientController
 */
public class AddPatientController
{
	
	private Employee empl;
	private ErrorPane errorPaneHandler = new ErrorPane();
	
	@FXML
	public TextField nameField;
	
	@FXML
	public TextField ageField;
	
	@FXML
	public TextField phoneField;
	
	@FXML
	public TextField idField;
	
	@FXML
	public Button addButton, cancelButton;
	
	@FXML
	public ListView errorPane;
	
	@FXML
	public ToggleGroup genderGroup;
	
	public void setButtonAction(Stage childStage, Patient p)
	{
		cancelButton.setOnAction(event -> {
			childStage.close();
		});
		
		addButton.setOnAction(event -> {
			Error error = new Error();
			if( AddPatient.checkPatientInfo(nameField.getText(), ageField.getText(), phoneField.getText(), idField.getText(), error) )
			{
				RadioButton gender = (RadioButton) genderGroup.getSelectedToggle();
				
				p.setName(nameField.getText());
				p.setAge(Integer.valueOf(ageField.getText()));
				p.setEmployee_entered(empl.getUsername());
				p.setGender(gender.getText().charAt(0));
				p.setPhone(phoneField.getText());
				p.setGovid(idField.getText());
				
				try
				{
					//check for duplicates
					AddPatient addPatient = new AddPatient();
					if( !addPatient.searchForPatient(phoneField.getText(), null, p) && !addPatient.searchForPatient(idField.getText(), null, p) )
					{
						AddPatient.addPatientToDatabase(p);
						System.out.println("Patient found");
					} else
					{
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.initStyle(StageStyle.UTILITY);
						alert.setResizable(false);
						alert.setHeaderText("ID/Phone duplicate.");
						alert.setContentText("The patient with the following phone/id number already exists. Search for the patient again.");
						childStage.close();
						alert.showAndWait();
					}
				} catch( IOException|SQLException e )
				{
					//todo add constructive feedback about how it failed
					//oops failed
					e.printStackTrace();
				}
				
				childStage.close();
			} else
			{
				errorPaneHandler.handleErrorPane(errorPane, error);
			}
		});
	}
	
	public void setEmpl(Employee empl)
	{
		this.empl = empl;
	}
}
