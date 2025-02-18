package customers;

import authentication.DatabaseConnection;
import controllers.PatientVisitsController;
import global.Global;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Employee;
import models.Patient;
import models.Visits;
import properties.Properties;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 08/05/18 Time : 10:24 PM Project Name: ClientMS Class Name:
 * PatientVisits
 */
public class PatientVisits
{
	
	private Patient patient = null;
	private Employee empl;
	
	public PatientVisits(Patient patient, Employee empl)
	{
		this.patient = patient;
		this.empl = empl;
	}
	
	@Deprecated
	public PatientVisits()
	{
	
	}
	
	private PatientVisitsController patientVisitsController = new PatientVisitsController();
	
	public void start(Stage primaryStage) throws IOException
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		ResourceBundle rs = new i18n.i18n().getResourceBundle(Global.getLocale());
		
		FXMLLoader loader = new FXMLLoader(cl.getResource("layout/PatientVisits.fxml"));
		loader.setController(patientVisitsController);
		loader.setResources(rs);
		
		Parent root = loader.load();
		Stage childStage = new Stage();
		
		childStage.initOwner(primaryStage);
		childStage.initModality(Modality.WINDOW_MODAL);
		
		childStage.setTitle("Specialized Hijama - Add Patient Visit");
		childStage.setScene(new Scene(root, 600, 500));
		
		childStage.setResizable(false);
		childStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		
		patientVisitsController.setPatient(patient);
		patientVisitsController.setEmpl(empl);
		patientVisitsController.setButtonAction(childStage);
		patientVisitsController.setTextChangeListener(childStage);
		patientVisitsController.setPreviousPatientsListener();
		patientVisitsController.setUpdatePatientsListener(childStage);
		
		childStage.showAndWait();
		
		//wait for the visit to complete
	}
	
	public static ObservableList<Visits> getAllPreviousVisits(Patient patient) throws IOException, SQLException
	{
		String query = "select visit_id, employee_entered, services, amount_paid, creation_date from patient_visit_details where patient_id='"+patient.getId()+"'";
		
		return getVisits(patient, query);
	}
	
	private static ObservableList<Visits> getVisits(Patient patient, String query) throws SQLException, IOException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		Statement statement = c.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		ArrayList<Visits> visitList = new ArrayList<>();
		
		while( rs.next() )
		{
			Visits visit = new Visits();
			visit.setServices(rs.getString("services"));
			visit.setVisitId(rs.getInt("visit_id"));
			visit.setTimestamp(rs.getTimestamp("creation_date"));
			visit.setEmployeeEntered(rs.getString("employee_entered"));
			visit.setAmount_paid(rs.getBigDecimal("amount_paid"));
			visit.setPatientId(patient.getId());
			visitList.add(visit);
		}
		
		return FXCollections.observableArrayList(visitList);
	}
}