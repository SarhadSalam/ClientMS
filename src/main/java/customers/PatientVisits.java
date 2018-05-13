package customers;

import controllers.PatientVisitsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Employee;
import models.Patient;

import java.io.IOException;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 08/05/18
 * Time : 10:24 PM
 * Project Name: ClientMS
 * Class Name: PatientVisits
 */
public class PatientVisits
{
	private Patient patient = null;
	private Employee empl;
	public PatientVisits(Patient patient, Employee empl)
	{
		this.patient = patient;
		this.empl=empl;
	}
	
	private PatientVisitsController patientVisitsController = new PatientVisitsController();
	
	public void start(Stage primaryStage) throws IOException
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		FXMLLoader loader = new FXMLLoader(cl.getResource("layout/PatientVisits.fxml"));
		loader.setController(patientVisitsController);
		
		Parent root = loader.load();
		Stage childStage = new Stage();
		
		childStage.initOwner(primaryStage);
		childStage.initModality(Modality.WINDOW_MODAL);
		
		childStage.setTitle("Specialized Hijama - Add Patient Visit");
		childStage.setScene(new Scene(root, 600, 800));
		
		childStage.setResizable(false);
		childStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		
		patientVisitsController.setPatient(patient);
		patientVisitsController.setEmpl(empl);
		patientVisitsController.setButtonAction(childStage);
		patientVisitsController.setTextChangeListener(childStage);
		
		childStage.showAndWait();
		
		//wait for the visit to complete
	}
}