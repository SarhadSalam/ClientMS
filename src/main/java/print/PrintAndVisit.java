package print;

import authentication.DatabaseConnection;
import models.Employee;
import models.Patient;
import properties.Properties;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import errors.Error;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 13/05/18 Time : 11:27 AM Project Name: ClientMS Class Name:
 * PrintAndVisit
 */
public class PrintAndVisit
{
	
	private Employee empl;
	private Patient patient;
	
	public PrintAndVisit(Patient patient, Employee empl)
	{
		this.patient = patient;
		this.empl = empl;
	}
	
	//add to database
	public void addVisit(String services, double amountPaid) throws SQLException, IOException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		String statement = "INSERT INTO patient_visit_details (patient_id, employee_entered, services, amount_paid) values (?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(statement);
		ps.setInt(1, patient.getId());
		ps.setString(2, empl.getUsername());
		ps.setString(3, services);
		ps.setBigDecimal(4, new BigDecimal(amountPaid));
		ps.execute();
		c.close();
	}
	
	public boolean validateInput(String services, String amountPaid, Error error)
	{
		//todo implement logic
		boolean isCorrect = true;
		if( services.length()>500 )
		{
			error.getErrors().add("Services rendered has to be smaller than 5 characters.");
			isCorrect = false;
		}
		
		if( !amountPaid.matches("^[0-9.]*$") )
		{
			error.getErrors().add("Amount paid has to be only numbers.");
			isCorrect = false;
		}
		return isCorrect;
	}
}
