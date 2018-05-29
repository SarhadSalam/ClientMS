package print;

import authentication.DatabaseConnection;
import customers.PatientVisits;
import models.Employee;
import models.Patient;
import models.Visits;
import properties.Properties;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

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
	public Visits addVisit(String services, double amountPaid, Visits visits) throws SQLException, IOException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		String colName[] = new String[]{"visit_id"};
		
		String statement = "INSERT INTO patient_visit_details (patient_id, employee_entered, services, amount_paid) values (?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(statement, colName);
		ps.setInt(1, patient.getId());
		ps.setString(2, empl.getUsername());
		ps.setString(3, services);
		ps.setBigDecimal(4, new BigDecimal(amountPaid).setScale(new Visits().CURRENCY_PRECISION, BigDecimal.ROUND_HALF_UP));
		
		visits.setAmount_paid(new BigDecimal(amountPaid));
		visits.setEmployeeEntered(empl.getFirst_name()+" "+empl.getLast_name());
		visits.setPatientId(patient.getId());
		visits.setTimestamp(new Timestamp(System.currentTimeMillis()));
		visits.setServices(services);
		
		if( ps.executeUpdate()>0 )
		{
			ResultSet rs = ps.getGeneratedKeys();
			if( rs.next() ) visits.setVisitId(rs.getInt(1));
		}
		c.close();
		return visits;
	}
	
	public boolean validateInput(String services, String amountPaid, Error error)
	{
		boolean isCorrect = true;
		
		
		if(services==null || services.equals(""))
		{
			isCorrect=false;
			error.getErrors().add("Services cannot be empty.");
		}
		
		if(amountPaid==null || amountPaid.equals(""))
		{
			isCorrect=false;
			error.getErrors().add("Amount cannot be empty.");
		}
		
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


