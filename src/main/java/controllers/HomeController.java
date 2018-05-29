package controllers;

import customers.AddPatient;
import customers.PatientVisits;
import errors.Error;
import errors.ErrorPane;
import home.Home;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Employee;
import models.Patient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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
					ButtonType addPatientButton = new ButtonType("Add Patient", ButtonBar.ButtonData.OK_DONE);
					ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
					
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setHeaderText("Patient Not Found.");
					alert.setContentText("Please check your information is correct or \nadd a new patient.");
					alert.initStyle(StageStyle.UTILITY);
					alert.setResizable(false);
					alert.getButtonTypes().remove(ButtonType.OK);
					alert.getButtonTypes().add(addPatientButton);
					alert.getButtonTypes().add(cancel);
					alert.initModality(Modality.APPLICATION_MODAL);
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
								resultOfSearch = true;
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
		userNameLabel.setText("Hello, "+empl.getFirst_name()+" "+empl.getLast_name());
	}
}
