package statistics;

import database.GetResultSet;
import errors.Error;
import models.Employee;
import models.Visits;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 12/06/18 Time : 4:29 PM Project Name: ClientMS Class Name:
 * EmployeeStatisticsAlgorithm
 */
public class EmployeeStatisticsAlgorithm
{
	
	public String getEarning(Employee employee, String from, String to) throws IOException, SQLException
	{
		ArrayList<Visits> visits = getVisits(employee, from, to);
		
		if( !visits.isEmpty() )
		{
			BigDecimal totalEarned = new BigDecimal(0);
			
			for( Visits visit : visits )
			{
				totalEarned = totalEarned.add(visit.getAmount_paid());
			}
			
			return totalEarned.setScale(2, BigDecimal.ROUND_UP).toPlainString();
		}
		
		return null;
	}
	
	private ArrayList<Visits> getVisits(Employee employee, String from, String to) throws IOException, SQLException
	{
		//todo: alternate query is remove DATE cast and se >= and < instead of between
		String query = "SELECT visit_id, patient_id, employee_entered, services, amount_paid, creation_date FROM patient_visit_details where employee_entered='"+employee.getUsername()+"' AND DATE(creation_date) BETWEEN STR_TO_DATE('"+from+"', '%d/%m/%Y') AND STR_TO_DATE('"+to+"', '%d/%m/%Y') ORDER BY creation_date asc";
		
		return new GetResultSet().getVisits(query);
	}
	
	public boolean validate(String from, String to, AtomicBoolean isEmpty, Error error)
	{
		boolean cont=true;
		
		if(to==null || to.equals("") || from==null || from.equals(""))
		{
			cont=false;
			error.getErrors().add("To and From cannot be empty.");
		}
		
		//if isEmpty = false, cont = true,
		if(isEmpty!=null && isEmpty.get())
		{
			cont=false;
			error.getErrors().add("Choose an employee.");
		}
		
		return cont;
	}
	
	public boolean checkNumberOfDays(long from, long to, Error error)
	{
		if(from>to){
			error.getErrors().add("From date has to be greater than to date.");
			return false;
		}
		return true;
	}
	
	public LinkedHashMap<String, PatientStatisticsAlgorithm.VisitGroupType> getEmployeeEarning(String username, String from, String to) throws IOException, SQLException
	{
		Employee empl = new Employee();
		empl.setUsername(username);
		
		ArrayList<Visits> employeeVisit= getVisits(empl, from, to);
		
		PatientStatisticsAlgorithm psa = new PatientStatisticsAlgorithm();
		return psa.getPatientVisitMap(employeeVisit);
	}
}
