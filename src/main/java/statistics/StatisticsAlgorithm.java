package statistics;

import authentication.DatabaseConnection;
import models.Visits;
import properties.Properties;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/06/18 Time : 12:31 PM Project Name: ClientMS Class Name:
 * StatisticsAlgorithm
 */
public class StatisticsAlgorithm
{
	
	public class VisitGroupType{
		private BigDecimal totalAmountPaid;
		private String date;
		private int numberOfPatients;
		
		public BigDecimal getTotalAmountPaid()
		{
			return totalAmountPaid;
		}
		
		public void setTotalAmountPaid(BigDecimal totalAmountPaid)
		{
			this.totalAmountPaid = totalAmountPaid;
		}
		
		public String getDate()
		{
			return date;
		}
		
		public void setDate(String date)
		{
			this.date = date;
		}
		
		public int getNumberOfPatients()
		{
			return numberOfPatients;
		}
		
		public void setNumberOfPatients(int numberOfPatients)
		{
			this.numberOfPatients = numberOfPatients;
		}
	}
	
	public LinkedHashMap<String, VisitGroupType> getList(long numberOfDays, String to, String from) throws IOException, SQLException
	{
		ArrayList<Visits> visits = getVisits(from, to);
		
		LinkedHashMap<String, VisitGroupType> map = new LinkedHashMap<>();
		
		if(visits.isEmpty())
		{
			return null;
		}
		
		//comes here only if numberOfDays is greater than 1, in which case I wanna group by day and not individual visits
		for( Visits visit : visits )
		{
			String date = visit.getTimestamp().toLocalDateTime().toLocalDate().toString();
			if( map.containsKey(date) )
			{
				VisitGroupType visitGroupType = map.get(date);
				visitGroupType.setTotalAmountPaid(visitGroupType.getTotalAmountPaid().add(visit.getAmount_paid()));
				visitGroupType.setNumberOfPatients(visitGroupType.getNumberOfPatients()+1);
				map.replace(date, visitGroupType);
			} else
			{
				VisitGroupType visitGroupType = new VisitGroupType();
				visitGroupType.setDate(date);
				visitGroupType.setTotalAmountPaid(visit.getAmount_paid());
				visitGroupType.setNumberOfPatients(1);
				map.put(date, visitGroupType);
			}
		}
		return map;
	}
	
	private ArrayList<Visits> getVisits(String from, String to) throws IOException, SQLException
	{
		//todo: alternate query is remove DATE cast and se >= and < instead of between
		String query = "SELECT visit_id, patient_id, employee_entered, services, amount_paid, creation_date FROM patient_visit_details where DATE(creation_date) BETWEEN STR_TO_DATE('"+from+"', '%d/%m/%Y') AND STR_TO_DATE('"+to+"', '%d/%m/%Y') ORDER BY creation_date asc";
		
		Properties prop = new Properties();
		DatabaseConnection databaseConnection = new DatabaseConnection();
		Connection c = databaseConnection.getConnection(prop.getProperty("dbUsername", Properties.PROPERTY_TYPE.env), prop.getProperty(( "dbPassword" ), Properties.PROPERTY_TYPE.env));
		
		ResultSet resultSet = c.createStatement().executeQuery(query);
		
		ArrayList<Visits> visitsInTime = new ArrayList<>();
		
		while(resultSet.next())
		{
			Visits visits = new Visits();
			visits.setEmployeeEntered(resultSet.getString("employee_entered"));
			visits.setAmount_paid(resultSet.getBigDecimal("amount_paid"));
			visits.setPatientId(resultSet.getInt("patient_id"));
			visits.setServices(resultSet.getString("services"));
			visits.setTimestamp(resultSet.getTimestamp("creation_date"));
			visits.setVisitId(resultSet.getInt("visit_id"));
			
			visitsInTime.add(visits);
		}
		return visitsInTime;
	}
}
