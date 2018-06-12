package database;

import authentication.DatabaseConnection;
import models.Visits;
import properties.Properties;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 12/06/18 Time : 4:40 PM Project Name: ClientMS Class Name:
 * GetResultSet
 */
public class GetResultSet
{
	public ArrayList<Visits> getVisits(String query) throws IOException, SQLException
	{
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		ResultSet rs = c.createStatement().executeQuery(query);
		
		ArrayList<Visits> visitsInTime = new ArrayList<>();
		
		while(rs.next())
		{
			Visits visit = new Visits();
			visit.setEmployeeEntered(rs.getString("employee_entered"));
			visit.setAmount_paid(rs.getBigDecimal("amount_paid"));
			visit.setPatientId(rs.getInt("patient_id"));
			visit.setServices(rs.getString("services"));
			visit.setTimestamp(rs.getTimestamp("creation_date"));
			visit.setVisitId(rs.getInt("visit_id"));
			
			visitsInTime.add(visit);
		}
		return visitsInTime;
	}
}
