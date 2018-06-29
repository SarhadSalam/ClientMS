package controllers;

import authentication.Privileges;
import customers.AddPatient;
import customers.PatientVisits;
import errors.Error;
import errors.ErrorPane;
import events.MessageEvent;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mail.SendMail;
import models.Employee;
import models.Patient;
import models.Visits;
import org.greenrobot.eventbus.EventBus;
import print.CreateInvoice;
import print.Print;
import print.PrintAndVisit;
import toasts.Toast;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 08/05/18 Time : 10:40 PM Project Name: ClientMS Class Name:
 * PatientVisitsController
 */
public class PatientVisitsController
{
	
	private final int maxCharacterCount = 500;
	private final double vatAmount = 1.05;
	
	private Error error = new Error();
	private Patient patient;
	private Employee empl;
	private ErrorPane errorPaneHandler = new ErrorPane();
	private Visits visits = new Visits();
	private Privileges privileges;
	
	@FXML
	public ResourceBundle resources;
	
	@FXML
	public RadioButton maleRadio, femaleRadio;
	
	@FXML
	public TextArea servicesText;
	
	@FXML
	public Label characterCount, totalVat, fileNo;
	
	@FXML
	public TextField amount, printCustomerAmount, printHospitalAmount, age, id, phone, name;
	
	@FXML
	public ToggleGroup genderToggle;
	
	@FXML
	public Button cancelButton, recordButton, recordAndPrintButton, updateInformation;
	
	@FXML
	public CheckBox printCustomerCheck, printHospitalCheck;
	
	@FXML
	public ListView errorPane;
	
	@FXML
	public TabPane tabPane;
	
	@FXML
	public TableView prevTable;
	
	@FXML
	public TableColumn visitCol, dateCol, serviceCol, employeeCol, amountCol;
	
	public void setPatient(Patient patient)
	{
		this.patient = patient;
		fileNo.setText(String.valueOf(patient.getId()));
		name.setText(patient.getName());
		if( String.valueOf(patient.getGender()).equals("F") ) genderToggle.selectToggle(femaleRadio);
		age.setText(String.valueOf(patient.getAge()));
		id.setText(( patient.getGovid() != null ) ? patient.getGovid() : resources.getString("not_available"));
		phone.setText(( patient.getPhone() != null ) ? patient.getPhone() : resources.getString("not_available"));
	}
	
	public void setEmpl(Employee empl)
	{
		this.empl = empl;
	}
	public void setButtonAction(Stage childStage)
	
	{
		cancelButton.setOnAction(event -> {
			childStage.close();
		});
		
		addPatient(childStage, recordButton, false);
		addPatient(childStage, recordAndPrintButton, true);
	}
	
	private void addPatient(Stage childStage, Button recordButton, boolean print)
	{
		recordButton.setOnAction(event -> {
			Print printFile = new Print();
			
			PrintAndVisit printAndVisit = new PrintAndVisit(patient, empl);
			if( printAndVisit.validateInput(servicesText.getText(), amount.getText(), error) && printFile.validate(printCustomerAmount, printCustomerCheck, printHospitalAmount, printHospitalCheck, error, print) )
			{
				try
				{
					printAndVisit.addVisit(servicesText.getText(), Double.valueOf(amount.getText()), visits);
				} catch( SQLException|IOException e )
				{
					Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
					SendMail sendMail = new SendMail();
					sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
				}
				childStage.close();
				EventBus.getDefault().post(new MessageEvent(resources.getString("recorded")));
				if( print )
				{
					String filename = null;
					CreateInvoice createInvoice = new CreateInvoice();
					try
					{
						filename = createInvoice.createInvoiceDetails(patient, visits);
					} catch( IOException e )
					{
						Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
						SendMail sendMail = new SendMail();
						sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
					}
					try
					{
						String jobName = printFile.print(filename, visits, Integer.valueOf(printHospitalAmount.getText()));
						
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setResizable(false);
						alert.setHeaderText(resources.getString("printing_job_header"));
						alert.setContentText(resources.getString("print_job_name")+jobName);
						alert.setTitle(resources.getString("print_title"));
						alert.initOwner(recordButton.getScene().getWindow());
						alert.initModality(Modality.WINDOW_MODAL);
						alert.getDialogPane().requestFocus();
						Thread thread = new Thread(() -> {
							try
							{
								// Wait for 5 secs
								Thread.sleep(5000);
								if( alert.isShowing() )
								{
									Platform.runLater(alert::close);
								}
							} catch( Exception exp )
							{
								Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
								SendMail sendMail = new SendMail();
								sendMail.sendErrorMail(Arrays.toString(exp.getStackTrace()));
							}
						});
						thread.setDaemon(true);
						thread.start();
						alert.showAndWait();
						printFile.print(filename, visits, Integer.valueOf(printCustomerAmount.getText()));
					} catch( IOException|PrinterException e )
					{
						Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
						SendMail sendMail = new SendMail();
						sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
					}
				}
				
			} else
			{
				//view the error pane
				errorPaneHandler.handleErrorPane(errorPane, error);
			}
		});
	}
	
	public void setTextChangeListener(Stage childStage)
	{
		servicesText.textProperty().addListener(( (observable, oldValue, newValue) -> {
			if( newValue.length()>maxCharacterCount )
			{
				servicesText.setText(oldValue);
				characterCount.setStyle("-fx-text-fill: red;");
				recordButton.setDisable(true);
				recordAndPrintButton.setDisable(true);
				errorPaneHandler.handleErrorPane(errorPane, error);
				error.getErrors().add("Cannot be over 500 characters.");
			} else
			{
				characterCount.setText(String.valueOf(maxCharacterCount-newValue.length()));
				recordButton.setDisable(false);
				recordAndPrintButton.setDisable(false);
				characterCount.setStyle("-fx-text-fill: green");
			}
		} ));
		
		amount.textProperty().addListener(( ( (observable, oldValue, newValue) ->
		{
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);
			boolean cont = true;
			if( amount.getText().equals("") || amount.getText() == null )
			{
				error.getErrors().add("Cannot be empty");
				cont = false;
			}
			if( amount.getText().matches("^[0-9.]*$") && cont )
			{
				totalVat.setText(String.valueOf(df.format(Double.valueOf(amount.getText())*vatAmount)));
			} else
			{
				error.getErrors().add("Amount paid can only be numbers.");
				amount.setText("0.00");
				totalVat.setText("0.00");
				errorPaneHandler.handleErrorPane(errorPane, error);
			}
		} ) ));
	}
	
	public void setPreviousPatientsListener()
	{
		AtomicBoolean firstTime = new AtomicBoolean(false);
		tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if( newValue.getText().equals("Previous Visits") && !firstTime.get() )
			{
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Loading");
				alert.setHeaderText("Loading previous visits.");
				alert.setContentText("Please wait while the visits are being loaded.");
				alert.getButtonTypes().removeAll();
				alert.initModality(Modality.WINDOW_MODAL);
				alert.initOwner(recordButton.getScene().getWindow());
				alert.setResizable(false);
				alert.getDialogPane().requestFocus();
				alert.show();
				firstTime.set(true);
				
				visitCol.setCellValueFactory(new PropertyValueFactory<>("visitId"));
				dateCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
				serviceCol.setCellValueFactory(new PropertyValueFactory<>("services"));
				employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeEntered"));
				amountCol.setCellValueFactory(new PropertyValueFactory<>("amount_paid"));
				
				privileges = new Privileges(empl);
				
				if( privileges.hasMoreThanUserStatus() )
				{
					setEditableCells();
				}
				try
				{
					ObservableList<Visits> data = PatientVisits.getAllPreviousVisits(patient);
					prevTable.setItems(data);
				} catch( IOException|SQLException e )
				{
					Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
					SendMail sendMail = new SendMail();
					sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
				}
				alert.close();
			}
		});
	}
	
	private void setEditableCells()
	{
		PrintAndVisit printAndVisit = new PrintAndVisit(patient, empl);
		amountCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<BigDecimal>()
		{
			@Override
			public String toString(BigDecimal object)
			{
				return object.toString();
			}
			
			@Override
			public BigDecimal fromString(String string)
			{
				return new BigDecimal(string);
			}
		}));
		amountCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>()
		{
			@Override
			public void handle(TableColumn.CellEditEvent event)
			{
				Visits visits = (Visits) prevTable.getSelectionModel().getSelectedItem();
				BigDecimal newValue = (BigDecimal) event.getNewValue();
				
				visits.setAmount_paid(newValue);
				( (Visits) event.getTableView().getItems().get(
						event.getTablePosition().getRow()) ).setAmount_paid(newValue);
				event.getTableView().refresh();
				try
				{
					printAndVisit.updateVisit(visits);
					Toast.makeText((Stage) recordButton.getScene().getWindow(), "Updated", 3000, 500, 500);
					
				} catch( IOException|SQLException e )
				{
					Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
					SendMail sendMail = new SendMail();
					sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
				}
			}
		});
		serviceCol.setCellFactory(TextFieldTableCell.forTableColumn());
		serviceCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>()
		{
			@Override
			public void handle(TableColumn.CellEditEvent event)
			{
				Visits visits = (Visits) prevTable.getSelectionModel().getSelectedItem();
				
				( (Visits) event.getTableView().getItems().get(
						event.getTablePosition().getRow()) ).setServices((String) event.getNewValue());
				visits.setServices((String) event.getNewValue());
				event.getTableView().refresh();
				try
				{
					Toast.makeText((Stage) recordButton.getScene().getWindow(), "Updated", 3000, 500, 500);
					printAndVisit.updateVisit(visits);
				} catch( IOException|SQLException e )
				{
					Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
					SendMail sendMail = new SendMail();
					sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
				}
			}
		});
		
	}
	
	public void setUpdatePatientsListener(Stage childStage)
	{
		AtomicBoolean firstTime = new AtomicBoolean(false);
		tabPane.getSelectionModel().selectedItemProperty().addListener(( (observable, oldValue, newValue) -> {
			if( newValue.getText().equals("Patient Information") && !firstTime.get() )
			{
				privileges = new Privileges(empl);
				if( privileges.hasMoreThanUserStatus() )
				{
					firstTime.set(true);
					name.setDisable(false);
					age.setDisable(false);
					maleRadio.setDisable(false);
					femaleRadio.setDisable(false);
					id.setDisable(false);
					phone.setDisable(false);
					updateInformation.setDisable(false);
					
					updateInformation.setOnAction(event -> {
						Patient patient = new Patient();
						patient.setId(this.patient.getId());
						patient.setGovid(id.getText());
						patient.setPhone(phone.getText());
						patient.setAge(Integer.valueOf(age.getText()));
						patient.setName(name.getText());
						patient.setGender(( (RadioButton) genderToggle.getSelectedToggle() ).getText().charAt(0));
						
						AddPatient addPatient = new AddPatient();
						try
						{
							if( addPatient.searchForPatient(patient.getGovid(), null, patient) && addPatient.searchForPatient(patient.getPhone(), null, patient) )
							{
								addPatient.updatePatientToDatabase(patient);
								Toast.makeText(childStage, "Updated!", 3000, 500, 500);
							} else
							{
								Toast.makeText(childStage, "Record exists. Failed to update.", 3000, 500, 500);
							}
						} catch( IOException|SQLException e )
						{
							Toast.makeText(null, resources.getString("typical_catch_statement"), 5000, 500, 500);
							SendMail sendMail = new SendMail();
							sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
						}
					});
				}
			}
		} ));
	}
}