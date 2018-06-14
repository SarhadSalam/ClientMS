package controllers;

import employee.EmployeeManagement;
import errors.Error;
import errors.ErrorPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Employee;
import statistics.EmployeeStatistics;
import statistics.EmployeeStatisticsAlgorithm;
import statistics.PatientStatisticsAlgorithm;
import toasts.Toast;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 13/06/18 Time : 5:57 PM Project Name: ClientMS Class Name:
 * EmployeeStatisticsController
 */
public class EmployeeStatisticsController
{
	
	@FXML
	public LineChart amountChart;
	@FXML
	public LineChart patientsChart;
	@FXML
	public TableView tableView;
	@FXML
	public TableColumn usernameCol;
	@FXML
	public TableColumn earningCol;
	@FXML
	public TableColumn totalPatientsCol;
	@FXML
	public PieChart distributionChart;
	@FXML
	public DatePicker fromDate;
	@FXML
	public DatePicker toDate;
	@FXML
	public ChoiceBox employeeDropbox;
	@FXML
	public Button loadButton;
	@FXML
	public Button loadAllButton;
	@FXML
	public Button clearButton;
	
	@FXML
	public ListView errorPane;
	
	private HashMap<String, String[]> dateKeeper = new HashMap<>();
	private ObservableList<Employee> employees;
	
	@FXML
	public void initialize()
	{
		EmployeeManagement employeeManagement = new EmployeeManagement();
		try
		{
			employees = employeeManagement.getAllEmployees();
			ArrayList<String> username = new ArrayList<>();
			
			for( Employee employee : employees )
			{
				username.add(employee.getUsername());
			}
			
			employeeDropbox.setItems(FXCollections.observableArrayList(username));
		} catch( IOException|SQLException e )
		{
			e.printStackTrace();
		}
	}
	
	public void setAction()
	{
		clearButton.setOnAction(this::handleClear);
		loadButton.setOnAction(this::handleLoad);
		loadAllButton.setOnAction(this::handleLoadAll);
	}
	
	private void handleLoadAll(ActionEvent event)
	{
		handleClear(null);
		
		Error error = new Error();
		ErrorPane errorPaneHandler = new ErrorPane();
		final String from = fromDate.getEditor().getText();
		final String to = toDate.getEditor().getText();
		EmployeeStatisticsAlgorithm eSA = new EmployeeStatisticsAlgorithm();
		
		if( eSA.validate(from, to, null, error) && eSA.checkNumberOfDays(fromDate.getValue().toEpochDay(), toDate.getValue().toEpochDay(), error) )
		{
			if( dateKeeper.containsKey(from) && dateKeeper.get(from)[0].equals(to) && dateKeeper.get(from)[1].equals(employeeDropbox.getValue().toString()) )
			{
				Toast.makeText((Stage) clearButton.getScene().getWindow(), "Already added", 3000, 500, 500);
				return;
			}
			try
			{
				LinkedHashMap<String, Double> earningsMap = new LinkedHashMap<>();
				for( Employee empl : employees )
				{
					LinkedHashMap<String, PatientStatisticsAlgorithm.VisitGroupType> earning = eSA.getEmployeeEarning(empl.getUsername(), from, to);
					double total = setLineChart(earning, empl.getUsername());
					earningsMap.put(empl.getUsername(), total);
				}
				
				setPieCharts(earningsMap);
			} catch( IOException|SQLException e )
			{
				e.printStackTrace();
			}
			
		} else
		{
			errorPaneHandler.handleErrorPane(errorPane, error);
		}
		
	}
	
	private void setPieCharts(LinkedHashMap<String, Double> map)
	{
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		
		for( String d : map.keySet() )
		{
			pieChartData.add(new PieChart.Data(d, map.get(d)));
		}
		
		distributionChart.setData(pieChartData);
	}
	
	private void handleClear(ActionEvent event)
	{
		errorPane.getItems().clear();
		errorPane.setVisible(false);
		dateKeeper.clear();
		amountChart.getData().clear();
		patientsChart.getData().clear();
		distributionChart.getData().clear();
	}
	
	private void handleLoad(ActionEvent event)
	{
		if( errorPane.isVisible() ) errorPane.setVisible(false);
		
		EmployeeStatisticsAlgorithm eSA = new EmployeeStatisticsAlgorithm();
		Error error = new Error();
		ErrorPane errorPaneHandler = new ErrorPane();
		
		final String from = fromDate.getEditor().getText();
		final String to = toDate.getEditor().getText();
		
		if( eSA.validate(from, to, new AtomicBoolean(employeeDropbox.getSelectionModel().isEmpty()), error) && eSA.checkNumberOfDays(fromDate.getValue().toEpochDay(), toDate.getValue().toEpochDay(), error) )
		{
			if( dateKeeper.containsKey(from) && dateKeeper.get(from)[0].equals(to) && dateKeeper.get(from)[1].equals(employeeDropbox.getValue().toString()) )
			{
				Toast.makeText((Stage) clearButton.getScene().getWindow(), "Already added", 3000, 500, 500);
				return;
			}
			try
			{
				LinkedHashMap<String, PatientStatisticsAlgorithm.VisitGroupType> earning = eSA.getEmployeeEarning(employeeDropbox.getValue().toString(), from, to);
				setLineChart(earning, null);
				setPieChart(earning);
				
			} catch( IOException|SQLException e )
			{
				e.printStackTrace();
			}
		} else
		{
			errorPaneHandler.handleErrorPane(errorPane, error);
		}
	}
	
	private void setPieChart(LinkedHashMap<String, PatientStatisticsAlgorithm.VisitGroupType> earning)
	{
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		
		if( earning == null ) return;
		
		for( String d : earning.keySet() )
		{
			pieChartData.add(new PieChart.Data(d, earning.get(d).getTotalAmountPaid().intValue()));
		}
		
		
		
		distributionChart.setTitle(distributionChart.getTitle()+" "+fromDate.getEditor().getText()+" - "+toDate.getEditor().getText());
		distributionChart.setData(pieChartData);
	}
	
	private double setLineChart(LinkedHashMap<String, PatientStatisticsAlgorithm.VisitGroupType> earning, String username)
	{
		final String dropboxUsername = ( username == null ) ? employeeDropbox.getValue().toString() : username;
		if( earning == null )
		{
			Toast.makeText((Stage) loadButton.getScene().getWindow(), "No visits for "+dropboxUsername, 1000, 500, 500);
			return 0;
		}
		String data[] = {toDate.getEditor().getText(), dropboxUsername};
		dateKeeper.put(fromDate.getEditor().getText(), data);
		
		XYChart.Series amountChartSeries = new XYChart.Series();
		XYChart.Series patientChartSeries = new XYChart.Series();
		amountChartSeries.setName(dropboxUsername+": "+fromDate.getEditor().getText()+" - "+toDate.getEditor().getText());
		patientChartSeries.setName(dropboxUsername+": "+fromDate.getEditor().getText()+" - "+toDate.getEditor().getText());
		
		double total = 0;
		
		for( String b : earning.keySet() )
		{
			total += earning.get(b).getTotalAmountPaid().doubleValue();
			amountChartSeries.getData().add(new XYChart.Data<>(b, earning.get(b).getTotalAmountPaid().doubleValue()));
			patientChartSeries.getData().add(new XYChart.Data<>(b, earning.get(b).getNumberOfPatients()));
		}
		
		amountChart.getXAxis().setAnimated(false);
		amountChart.getYAxis().setAnimated(true);
		amountChart.setAnimated(true);
		
		patientsChart.getXAxis().setAnimated(false);
		patientsChart.getYAxis().setAnimated(true);
		patientsChart.setAnimated(true);
		
		amountChart.getData().add(amountChartSeries);
		patientsChart.getData().add(patientChartSeries);
		return total;
	}
}
