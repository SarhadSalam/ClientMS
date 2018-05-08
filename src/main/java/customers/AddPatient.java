package customers;

import authentication.DatabaseConnection;
import controllers.AddPatientController;
import errors.Error;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Employee;
import models.Patient;
import properties.Properties;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 07/05/18
 * Time : 11:57 AM
 * Project Name: ClientMS
 * Class Name: AddPatient
 */
public class AddPatient
{
	
	private AddPatientController addPatientController = new AddPatientController();
	
	public void start(Stage parent, Employee empl) throws IOException
	{
		System.out.println(empl);
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		FXMLLoader loader = new FXMLLoader(cl.getResource("layout/AddPatient.fxml"));
		loader.setController(addPatientController);
		
		Parent root = loader.load();
		Stage childStage = new Stage();
		
		childStage.initOwner(parent);
		childStage.initModality(Modality.WINDOW_MODAL);
		
		childStage.setTitle("Specialized Hijama - Add Patient");
		childStage.setScene(new Scene(root, 600, 400));
		childStage.setResizable(false);
		childStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		addPatientController.setEmpl(empl);
		addPatientController.setButtonAction(childStage);
		childStage.showAndWait();
	}
	
	public static void addPatientToDatabase(Patient patient) throws IOException, SQLException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		
		String statement = "INSERT INTO patients (name, age, sex, govid, phone, employee_entered) values (?,?,?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(statement);
		ps.setString(1, patient.getName());
		ps.setInt(2, patient.getAge());
		ps.setString(3, String.valueOf(patient.getGender()));
		ps.setInt(4, patient.getGovid());
		ps.setInt(5, patient.getPhone());
		ps.setString(6, patient.getEmployee_entered());
		ps.execute();
		
		c.close();
		
	}
	
	public static boolean checkPatientInfo(String name, String age, String phone, String id, Error error)
	{
		boolean isCorrect = true;
		
		if( !name.matches("^[.,;/a-zA-Z\\s]*$") )
		{
			error.getErrors().add("Name can only contain letters, comma, semicolon and slash.");
			isCorrect = false;
		}
		
		if( !age.matches("^[0-9]*$") )
		{
			error.getErrors().add("Age can only contain numbers.");
			isCorrect = false;
		}
		
		if( !phone.matches("^[0-9]*$") )
		{
			error.getErrors().add("Phone can only contain numbers.");
			isCorrect = false;
		}
		
		if( !id.matches("^[a-zA-Z0-9]*$") )
		{
			error.getErrors().add("ID can only be alphanumeric.");
			isCorrect = false;
		}
		
		return isCorrect;
	}
}
