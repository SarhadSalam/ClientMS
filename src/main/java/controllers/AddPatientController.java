package controllers;

import customers.AddPatient;
import customers.PatientVisits;
import errors.Error;
import errors.ErrorPane;
import events.MessageEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mail.SendMail;
import models.Employee;
import models.Patient;
import org.greenrobot.eventbus.EventBus;
import toasts.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 07/05/18 Time : 11:57 AM Project Name: ClientMS Class Name:
 * AddPatientController
 */
public class AddPatientController
{
	
	private Employee empl;
	private ErrorPane errorPaneHandler = new ErrorPane();
	
	@FXML
	public ResourceBundle resources;
	
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
		cancelButton.setOnAction(event -> childStage.close());
		
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
					} else
					{
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setResizable(false);
						alert.setHeaderText(resources.getString("id_phone_duplicate"));
						alert.setContentText(resources.getString("duplicate_explanation"));
						alert.setTitle(resources.getString("duplicate_found"));
						alert.initOwner(addButton.getScene().getWindow());
						alert.initModality(Modality.WINDOW_MODAL);
						alert.getDialogPane().requestFocus();
						alert.showAndWait();
					}
				} catch( IOException|SQLException e )
				{
					//oops failed
					Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
					SendMail sendMail = new SendMail();
					sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
				}
				
				Stage stage = (Stage) childStage.getScene().getWindow();
				PatientVisits visits = new PatientVisits(p, empl);
				try
				{
					visits.start(stage);
				} catch( IOException e )
				{
					Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
					SendMail sendMail = new SendMail();
					sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
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
