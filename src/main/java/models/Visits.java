package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 18/05/18 Time : 10:42 PM Project Name: ClientMS Class Name: Visits
 */
public class Visits
{
	public final int CURRENCY_PRECISION=2;
	private int patientId, visitId;
	private String employeeEntered, services;
	private BigDecimal amount_paid;
	private Timestamp timestamp;
	
	@Override
	public String toString()
	{
		System.out.printf("Patient ID: %d\n Visit ID: %d\n employee Entered: %s\n Services: %s\n Amount Paid: %s\n Timestamp: %s", patientId, visitId, employeeEntered, services,amount_paid.toString(), timestamp.toString());
		return null;
	}
	
	public String getServices()
	{
		return services;
	}
	
	public void setServices(String services)
	{
		this.services = services;
	}
	
	public int getPatientId()
	{
		return patientId;
	}
	
	public void setPatientId(int patientId)
	{
		this.patientId = patientId;
	}
	
	public int getVisitId()
	{
		return visitId;
	}
	
	public void setVisitId(int visitId)
	{
		this.visitId = visitId;
	}
	
	public String getEmployeeEntered()
	{
		return employeeEntered;
	}
	
	public void setEmployeeEntered(String employeeEntered)
	{
		this.employeeEntered = employeeEntered;
	}
	
	public BigDecimal getAmount_paid()
	{
		return amount_paid;
	}
	
	public void setAmount_paid(BigDecimal amount_paid)
	{
		this.amount_paid = amount_paid;
	}
	
	public Timestamp getTimestamp()
	{
		return timestamp;
	}
	
	public void setTimestamp(Timestamp timestamp)
	{
		this.timestamp = timestamp;
	}
}
