package dev;

import authentication.DatabaseConnection;
import crypto.Crypto;
import com.github.javafaker.Faker;
import customers.AddPatient;
import models.Employee;
import models.Patient;
import models.Visits;
import print.Print;
import print.PrintAndVisit;
import properties.Properties;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 10:09 PM Project Name: ClientMS Class Name:
 * dev.Developer
 */
public class Developer
{
	
	private Properties p = new Properties();
	
	public void createEmployee(String first_name, String last_name, String username, String password, Connection c) throws SQLException, GeneralSecurityException, IOException, URISyntaxException
	{
		Crypto crypto = new Crypto();
		String statement = "INSERT INTO employee (first_name, last_name, username, password) values (?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(statement);
		ps.setString(1, first_name);
		ps.setString(2, last_name);
		ps.setString(3, username);
		ps.setBytes(4, crypto.encrypt(password));
		ps.execute();
	}
	
	public Patient getPatient(int id) throws IOException, SQLException
	{
		Patient patient = new Patient();
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		String query = "SELECT * FROM patients where patient_id="+id;
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
			return patient;
		}
		c.close();
		return null;
	}
	
	private void createClusterVisits() throws IOException, SQLException, ParseException
	{
		for( int i = 16; i<=37; i++ )
		{
			System.out.println("Patient: "+i);
			Patient patient = getPatient(i);
			if( patient != null )
			{
				Faker faker = new Faker();
				for( int j = 0; j<faker.number().numberBetween(30, 400); j++ )
				{
					Visits visit = new Visits();
					Faker fakeVisit = new Faker();
					visit.setServices(fakeVisit.lorem().characters(10, 499));
					visit.setPatientId(patient.getId());
					visit.setAmount_paid(new BigDecimal(fakeVisit.commerce().price(0, 1000)));
					
					SimpleDateFormat sDF = new SimpleDateFormat("MM/dd/yyyy");
					Date begin = sDF.parse("06/01/2018");
					Date end = sDF.parse("06/31/2018");
					
					Date insertionTime = fakeVisit.date().between(begin, end);

					Timestamp timestamp = new Timestamp(insertionTime.getTime());
					
					visit.setTimestamp(timestamp);
					visit.setEmployeeEntered(( fakeVisit.random().nextBoolean() ) ? "boss" : "martin");
					
					Employee empl = new Employee();
					empl.setUsername(visit.getEmployeeEntered());
					empl.setFirst_name("Sardad");
					empl.setLast_name("luldad");
					
					PrintAndVisit printAndVisit = new PrintAndVisit(patient, empl);
					printAndVisit.addVisit(visit.getServices(), Double.valueOf(visit.getAmount_paid().toPlainString()), visit);
				}
			}
		}
	}
	
	
	
	private void createClusterPatients() throws IOException, SQLException
	{
		for( int i = 0; i<20; i++ )
		{
			Faker faker = new Faker();
			
			Patient patient = new Patient();
			patient.setName(faker.name().fullName());
			patient.setGovid(faker.idNumber().ssnValid());
			patient.setPhone(faker.phoneNumber().cellPhone());
			patient.setAge(faker.number().numberBetween(0, 100));
			patient.setGender(( faker.bool().bool() ) ? 'M' : 'F');
			patient.setEmployee_entered(( faker.bool().bool() ) ? "martin" : "boss");
			AddPatient.addPatientToDatabase(patient);
		}
	}
	
	public void createClusterOfEmployees(String[] args) throws IOException, SQLException, GeneralSecurityException, URISyntaxException
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(p.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), p.getProperty("dbPassword", Properties.PROPERTY_TYPE.env));
		Developer dev = new Developer();
		for( int i = 0; i<10; i++ )
		{
			Faker faker = new Faker();
			dev.createEmployee(faker.name().firstName(), faker.name().lastName(), faker.name().username(), faker.artist().name(), c);
		}
		c.close();
	}
	
	public void createASingleEmployee(String firstName, String lastName, String userName, String password) throws IOException, SQLException, GeneralSecurityException, URISyntaxException
	{
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(p.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), p.getProperty("dbPassword", Properties.PROPERTY_TYPE.env));
		Developer dev = new Developer();
		dev.createEmployee(firstName, lastName, userName, password, c);
		c.close();
	}
	
	public static void main(String[] args)
	{
		Developer developer = new Developer();
		try
		{
			developer.createClusterVisits();
		} catch( IOException|SQLException|ParseException e )
		{
			e.printStackTrace();
		}
	}
}
