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
import models.Employee;
import models.Patient;
import properties.Properties;

import java.io.IOException;
import java.sql.*;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 07/05/18 Time : 11:57 AM Project Name: ClientMS Class Name:
 * AddPatient
 */
public class AddPatient
{
	
	private AddPatientController addPatientController = new AddPatientController();
	
	public void start(Stage parent, Employee empl, Patient patient) throws IOException
	{
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
		addPatientController.setButtonAction(childStage, patient);
		childStage.showAndWait();
	}
	
	public void updatePatientToDatabase(Patient patient) throws IOException, SQLException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		String statement = "UPDATE patients SET name = ?, age=?, sex=?, govid=?, phone=? where patient_id = ?";
		PreparedStatement ps = c.prepareStatement(statement);
		ps.setString(1, patient.getName());
		ps.setInt(2, patient.getAge());
		ps.setString(3, String.valueOf(patient.getGender()));
		ps.setString(4, patient.getGovid());
		ps.setString(5, patient.getPhone());
		ps.setInt(6, patient.getId());
		ps.executeUpdate();
		c.close();
	}
	
	public static void addPatientToDatabase(Patient patient) throws IOException, SQLException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		String statement = "INSERT INTO patients (name, age, sex, govid, phone, employee_entered) values (?,?,?,?,?,?)";
		
		String colName[] = new String[]{"patient_id"};
		
		PreparedStatement ps = c.prepareStatement(statement, colName);
		ps.setString(1, patient.getName());
		ps.setInt(2, patient.getAge());
		ps.setString(3, String.valueOf(patient.getGender()));
		ps.setString(4, patient.getGovid());
		ps.setString(5, patient.getPhone());
		ps.setString(6, patient.getEmployee_entered());
		
		if( ps.executeUpdate()>0 )
		{
			ResultSet rs = ps.getGeneratedKeys();
			if( rs.next() )
			{
				patient.setId(rs.getInt(1));
			}
		}
		c.close();
	}
	
	public static boolean checkPatientInfo(String name, String age, String phone, String id, Error error)
	{
		boolean isCorrect = true;
		
		if( name == null || name.equals("") || !name.matches("^[.,;/a-zA-Z\\s]*$") )
		{
			error.getErrors().add("Name can only contain letters, comma, semicolon and slash.");
			isCorrect = false;
		}
		
		if( age == null || age.equals("") || !age.matches("^[0-9]*$") )
		{
			error.getErrors().add("Age can only contain numbers.");
			isCorrect = false;
		}
		if( phone == null || phone.equals("") || !phone.matches("^[0-9]*$") )
		{
			error.getErrors().add("Phone can only contain numbers.");
			isCorrect = false;
		}
		
		if( id == null || id.equals("") || !id.matches("^[a-zA-Z0-9]*$") )
		{
			error.getErrors().add("ID can only be alphanumeric.");
			isCorrect = false;
		}
		
		return isCorrect;
	}
	
	public boolean searchForPatient(String identifier, String method, Patient patient) throws IOException, SQLException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		String query = "SELECT * FROM patients where phone"+"='"+identifier+"' OR govid"+"='"+identifier+"'";
		
		if( method!=null && method.equals("File No.") )
			query = "SELECT * FROM patients where patient_id ='"+identifier+"'";
		
		ResultSet resultSet = c.createStatement().executeQuery(query);
		
		if( resultSet.next() )
		{
			patient.setName(resultSet.getString("name"));
			patient.setAge(resultSet.getInt("age"));
			patient.setGender(resultSet.getString("sex").charAt(0));
			patient.setGovid(resultSet.getString("govid"));
			patient.setPhone(resultSet.getString("phone"));
			patient.setEmployee_entered(resultSet.getString("employee_entered"));
			patient.setId(resultSet.getInt("patient_id"));
			return true;
		}
		c.close();
		patient = null;
		return false;
	}
	
	public boolean validateSearchPatientInput(String identifier, Error error)
	{
		boolean valid = true;
		
		if( identifier == null || identifier.equals("") || identifier.length()<=0 )
		{
			valid = false;
			error.getErrors().add("Search cannot be empty.");
		}
		if( identifier.length()>15 )
		{
			error.getErrors().add("Length cannot be greater than 15.");
			valid = false;
		}
		
		if( !identifier.matches("^[0-9]*$") )
		{
			error.getErrors().add("Has to be purely numbers.");
			valid = false;
		}
		return valid;
	}
}
