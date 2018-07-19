package controllers;

import customers.AddPatient;
import customers.PatientVisits;
import errors.Error;
import errors.ErrorPane;
import global.Global;
import home.Home;
import home.TodayVisits;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mail.SendMail;
import models.Employee;
import models.Patient;
import org.apache.commons.lang3.time.DateUtils;
import statistics.EmployeeStatisticsAlgorithm;
import toasts.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private Label userNameLabel, amountEarnedLabel;
	
	@FXML
	private Button searchButton, refreshAmountButton, viewVisitsButton;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private ListView<String> errorPane;
	
	private AtomicBoolean firstTimeLoad = new AtomicBoolean(false);
	
	//when button is clicked
	public void setButtonAction()
	{
		( (Stage) searchButton.getScene().getWindow() ).addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
			if( ev.getCode() == KeyCode.ENTER )
			{
				searchButton.fire();
			}
		});
		
		searchButton.setOnAction(event -> handleSearch());
		
		refreshAmountButton.setOnAction(event -> handleAmountRefresh());
		
		viewVisitsButton.setOnAction(event -> {
			TodayVisits todayVisits = new TodayVisits();
			try
			{
				todayVisits.start((Stage) searchButton.getScene().getWindow(), empl);
			} catch( IOException e )
			{
				Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
				SendMail sendMail = new SendMail();
				sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
			}
		});
	}
	
	public void handleAmountRefresh()
	{
		EmployeeStatisticsAlgorithm employeeStatisticsAlgorithm = new EmployeeStatisticsAlgorithm();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = DateUtils.addHours(new Date(), new Global().getCurrentTimeZoneHours());
		date = DateUtils.addMinutes(date, new Global().getCurrentTimeZoneMinutes());
		try
		{
			String total = employeeStatisticsAlgorithm.getEarning(empl, simpleDateFormat.format(date)+" 00:00:00", simpleDateFormat.format(date)+" 23:59:59");
			if( total != null && !total.equals("") )
			{
				amountEarnedLabel.setTextFill(Color.GREEN);
				amountEarnedLabel.setText(total+" SAR");
			} else
			{
				amountEarnedLabel.setTextFill(Color.RED);
				amountEarnedLabel.setText("No earnings today");
			}
			if( firstTimeLoad.get() )
				Toast.makeText((Stage) refreshAmountButton.getScene().getWindow(), "Refreshed!", 1000, 500, 500);
			else firstTimeLoad.set(true);
		} catch( IOException|SQLException e )
		{
			Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
			SendMail sendMail = new SendMail();
			sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
		}
	}
	
	private void handleSearch()
	{
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
				Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
				SendMail sendMail = new SendMail();
				sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
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
							Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
							SendMail sendMail = new SendMail();
							sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
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
					Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
					SendMail sendMail = new SendMail();
					sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
				}
			}
		} else
		{
			errorPaneHandler.handleErrorPane(errorPane, error);
		}
	}
	
	public void setEmpl(Employee empl)
	{
		this.empl = empl;
	}
	
	public void setEmployeeInformation()
	{
		userNameLabel.setText(resources.getString("greeting_empl")+empl.getFirst_name()+" "+empl.getLast_name()+": ");
	}
}
