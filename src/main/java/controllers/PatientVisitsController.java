package controllers;

import authentication.Privileges;
import customers.AddPatient;
import customers.PatientVisits;
import errors.Error;
import errors.ErrorPane;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import models.Employee;
import models.Patient;
import models.Visits;
import print.CreateInvoice;
import print.Print;
import print.PrintAndVisit;
import toasts.Toast;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
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
		id.setText(( patient.getGovid() != null ) ? patient.getGovid() : "Not available");
		phone.setText(( patient.getPhone() != null ) ? patient.getPhone() : "Not available");
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
					printAndVisit.addVisit(servicesText.getText(), Double.valueOf(totalVat.getText()), visits);
				} catch( SQLException|IOException e )
				{
					e.printStackTrace();
				}
				childStage.close();
				
				if( print )
				{
					String filename = null;
					CreateInvoice createInvoice = new CreateInvoice();
					try
					{
						filename = createInvoice.createInvoiceDetails(patient, visits);
					} catch( IOException e )
					{
						e.printStackTrace();
					}
					try
					{
						String jobName = printFile.print(filename, visits, Integer.valueOf(printHospitalAmount.getText()));
						
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setResizable(false);
						alert.setHeaderText("Printing in progress.");
						alert.setContentText("Print job name: "+jobName);
						alert.initStyle(StageStyle.UTILITY);
						
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
								exp.printStackTrace();
							}
						});
						thread.setDaemon(true);
						thread.start();
						alert.showAndWait();
						printFile.print(filename, visits, Integer.valueOf(printCustomerAmount.getText()));
					} catch( IOException|PrinterException e )
					{
						e.printStackTrace();
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
			boolean cont = true;
			if( amount.getText().equals("") || amount.getText() == null )
			{
				error.getErrors().add("Cannot be empty");
				cont = false;
			}
			if( amount.getText().matches("^[0-9.]*$") && cont )
			{
				totalVat.setText(String.valueOf(Double.valueOf(amount.getText())*vatAmount));
			} else
			{
				error.getErrors().add("Amount paid can only be numbers.");
				amount.setText("0");
				totalVat.setText("0");
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
				alert.initStyle(StageStyle.UTILITY);
				alert.setResizable(false);
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
					ObservableList<Visits> data = PatientVisits.getPreviousVisits(patient);
					prevTable.setItems(data);
				} catch( IOException|SQLException e )
				{
					e.printStackTrace();
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
				//todo update the field
				try
				{
					printAndVisit.updateVisit(visits);
				} catch( IOException|SQLException e )
				{
					e.printStackTrace();
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
					printAndVisit.updateVisit(visits);
				} catch( IOException|SQLException e )
				{
					e.printStackTrace();
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
							addPatient.updatePatientToDatabase(patient);
							Toast.makeText(childStage, "Updated!", 3000, 500, 500);
						} catch( IOException|SQLException e )
						{
							e.printStackTrace();
						}
					});
				}
			}
		} ));
	}
}