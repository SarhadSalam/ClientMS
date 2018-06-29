package controllers;

import customers.PatientVisits;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mail.SendMail;
import models.Employee;
import models.Patient;
import models.Visits;
import statistics.EmployeeStatisticsAlgorithm;
import toasts.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 17/06/18 Time : 3:53 PM Project Name: ClientMS Class Name:
 * TodayVisitsController
 */
public class TodayVisitsController
{
	
	@FXML
	public TableView tableView;
	
	@FXML
	public TableColumn visitCol, amountCol, timeCol;
	
	private Employee empl;
	
	public void setEmpl(Employee empl)
	{
		this.empl = empl;
	}
	
	public void loadTable()
	{
		visitCol.setCellValueFactory(new PropertyValueFactory<>("visitId"));
		timeCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("amount_paid"));
		EmployeeStatisticsAlgorithm esa = new EmployeeStatisticsAlgorithm();
		PatientVisits patientVisits = new PatientVisits();
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			
			ObservableList<Visits> data = esa.getPatientList(empl, simpleDateFormat.format(date)+" 00:00:00", simpleDateFormat.format(date)+" 23:59:59");
			tableView.setItems(data);
		} catch( IOException|SQLException e )
		{
			Toast.makeText(null, "Failed", 5000, 500, 500);
			SendMail sendMail = new SendMail();
			sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
		}
	}
}
