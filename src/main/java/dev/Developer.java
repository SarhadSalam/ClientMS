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
import java.util.Arrays;
import java.util.Date;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 10:09 PM Project Name: ClientMS Class Name:
 * dev.Developer
 */
public class Developer
{
	
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
	
	public static void main(String[] args) throws GeneralSecurityException, IOException, URISyntaxException
	{
		byte b[] = {1, 33, 86, -33, -7, 97, -76, 83, 13, 30, 44, -71, 82, 105, -15, -19, -57, 111, 28, -7, -88, -27, 1, 83, 65, -74, -1, 123, 72, 23, 127, 59, 20, -106, 107, -106, -3, 118, -104, -19, -51, -22, -19};
		Crypto crypto = new Crypto();
		
		String x = "\u0001!V\uFFDF\uFFF9ﾁ\uFFF0ﾯ￭\"\uFFD9\"|HKWs&ￄ￼\f4 Tﾁ\uFFF6bￎ\"kﾗￃ\u0016ﾴ(ﾐz6\uFFF7\uFFFFￒ\uFFF2x";
		
		System.out.println(crypto.byteArrToString(crypto.encrypt("7lfbpibCYx")));
		System.out.println(crypto.byteArrToString(crypto.decrypt(x.getBytes())));
		
	}
}
