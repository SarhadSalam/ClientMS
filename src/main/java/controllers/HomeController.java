package controllers;

import customers.AddPatient;
import customers.PatientVisits;
import errors.Error;
import errors.ErrorPane;
import home.Home;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Employee;
import models.Patient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 06/05/18 Time : 11:29 PM Project Name: ClientMS Class Name:
 * HomeController
 */
public class HomeController
{
	
	private Employee empl;
	private AddPatient addPatient = new AddPatient();
	private ErrorPane errorPaneHandler = new ErrorPane();
	
	@FXML
	public ResourceBundle resources;
	
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
	
	//when button is clicked
	public void setButtonAction()
	{
		( (Stage) searchButton.getScene().getWindow() ).addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if( ev.getCode() == KeyCode.ENTER )
			{
				searchButton.fire();
			}
		});
		
		searchButton.setOnAction(event -> {
			RadioButton selectedButton = (RadioButton) searchMethod.getSelectedToggle();
			
			Patient patient = new Patient();
			boolean resultOfSearch = false;
			Error error = new Error();
			if( addPatient.validateSearchPatientInput(searchBar.getText(), error) )
			{
				try
				{
					resultOfSearch = addPatient.searchForPatient(searchBar.getText(), selectedButton.getText(), patient);
				} catch( IOException|SQLException e )
				{
					e.printStackTrace();
				}
				
				//patient found
				if( !resultOfSearch )
				{
					ButtonType addPatientButton = new ButtonType(resources.getString("add_patient"), ButtonBar.ButtonData.OK_DONE);
					ButtonType cancel = new ButtonType(resources.getString("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
					
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText(resources.getString("patient_not_found"));
					alert.setContentText(resources.getString("check_info_patient_not_found"));
					alert.setTitle(resources.getString("add_patient"));
					alert.setResizable(false);
					alert.getButtonTypes().remove(ButtonType.OK);
					alert.getButtonTypes().add(cancel);
					alert.getButtonTypes().add(addPatientButton);
					alert.initOwner(searchBar.getScene().getWindow());
					alert.initModality(Modality.WINDOW_MODAL);
					alert.getDialogPane().requestFocus();
					
					Optional<ButtonType> userOption = alert.showAndWait();
					
					if( userOption.isPresent() )
					{
						if( userOption.get().getButtonData() == ButtonBar.ButtonData.OK_DONE )
						{
							//start a new scene to add a patient, the old scene should become unclickable on addition of new one
							AddPatient addPatient = new AddPatient();
							try
							{
								alert.close();
								addPatient.start((Stage) searchBar.getScene().getWindow(), empl, patient);
							} catch( IOException e )
							{
								patient = null;
								//in case cannot be inserted
								resultOfSearch = false;
								e.printStackTrace();
							}
						}
					}
				}
				if( resultOfSearch )
				{
					//start a service screen
					Stage stage = (Stage) searchButton.getScene().getWindow();
					PatientVisits visits = new PatientVisits(patient, empl);
					try
					{
						visits.start(stage);
					} catch( IOException e )
					{
						e.printStackTrace();
					}
				}
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
	
	public void setEmployeeInformation()
	{
		userNameLabel.setText(resources.getString("greeting_empl")+empl.getFirst_name()+" "+empl.getLast_name());
	}
}
