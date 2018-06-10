package controllers;

import errors.Error;
import errors.ErrorPane;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import statistics.StatisticsAlgorithm;
import toasts.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 03/06/18 Time : 5:08 PM Project Name: ClientMS Class Name:
 * PatientStatisticsController
 */
public class PatientStatisticsController
{
	
	@FXML
	public DatePicker toDate;
	
	@FXML
	public DatePicker fromDate;
	
	@FXML
	public LineChart lineChart;
	
	@FXML
	public TableView patientViewsTable;
	
	@FXML
	public TableColumn amountCol;
	
	@FXML
	public TableColumn dateCol;
	
	@FXML
	public Button submitButton, clearButton;
	
	@FXML
	public TableColumn patientCountCol;
	
	@FXML
	public TableColumn patientCountDateCol;
	
	@FXML
	public TableView patientCountTable;
	
	@FXML
	public LineChart patientChart;
	
	@FXML
	public ListView errorPane;
	
	private HashMap<String, String> dateKeeper = new HashMap<>();
	private ErrorPane errorPaneHandler = new ErrorPane();
	
	public void setButtonAction()
	{
		clearButton.setOnAction(event -> {
			lineChart.getData().clear();
			patientChart.getData().clear();
			patientViewsTable.getItems().clear();
			patientCountTable.getItems().clear();
			dateKeeper.clear();
			errorPane.setVisible(false);
		});
		
		submitButton.setOnAction(event -> {
			errorPane.setVisible(false);
			Error error = new Error();
			if( validateEntry(fromDate.getEditor().getText(), toDate.getEditor().getText(), error) )
			{
				long numberOfDays = getNumberOfDays();
				
				StatisticsAlgorithm statisticsAlgorithm = new StatisticsAlgorithm();
				try
				{
					HashMap<String, StatisticsAlgorithm.VisitGroupType> map = statisticsAlgorithm.getList(numberOfDays, toDate.getEditor().getText(), fromDate.getEditor().getText());
					if( map == null )
					{
						Toast.makeText((Stage) submitButton.getScene().getWindow(), "No patients in that period.", 3000, 500, 500);
						return;
					}
					setChart(map);
					setTable(map);
					
				} catch( IOException|SQLException e )
				{
					e.printStackTrace();
				}
			} else
			{
				errorPaneHandler.handleErrorPane(errorPane, error);
			}
		});
		
	}
	
	private boolean validateEntry(String from, String to, Error error)
	{
		if( from==null || from.equals("") || to==null || to.equals(""))
		{
			error.getErrors().add("From and To cannot be empty.");
			return false;
		}
		if( getNumberOfDays()<0 )
		{
			error.getErrors().add("To has to be greater then from date.");
			return false;
		}
		return true;
	}
	
	private void setTable(HashMap<String, StatisticsAlgorithm.VisitGroupType> map)
	{
		ArrayList<StatisticsAlgorithm.VisitGroupType> values = new ArrayList<>(map.values());
		patientCountTable.getItems().clear();
		patientViewsTable.getItems().clear();
		
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		amountCol.setCellValueFactory(new PropertyValueFactory<>("totalAmountPaid"));
		patientViewsTable.setItems(FXCollections.observableArrayList(values));
		
		patientCountDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		patientCountCol.setCellValueFactory(new PropertyValueFactory<>("numberOfPatients"));
		patientCountTable.setItems(FXCollections.observableArrayList(values));
	}
	
	private void setChart(HashMap<String, StatisticsAlgorithm.VisitGroupType> map)
	{
		String from = fromDate.getEditor().getText();
		String to = toDate.getEditor().getText();
		
		if( dateKeeper.containsKey(from) && dateKeeper.get(from).equals(to) )
		{
			return;
		}
		dateKeeper.put(from, to);
		XYChart.Series moneyChartSeries = new XYChart.Series();
		XYChart.Series patientChartSeries = new XYChart.Series();
		moneyChartSeries.setName("From: "+from+" To: "+to);
		patientChartSeries.setName("From: "+from+" To: "+to);
		
		for( String s : map.keySet() )
		{
			moneyChartSeries.getData().add(new XYChart.Data(s, map.get(s).getTotalAmountPaid().doubleValue()));
			patientChartSeries.getData().add(new XYChart.Data(s, map.get(s).getNumberOfPatients()));
		}
		
		patientChart.getXAxis().setAnimated(false);
		patientChart.getYAxis().setAnimated(true);
		patientChart.setAnimated(true);
		
		lineChart.getXAxis().setAnimated(false);
		lineChart.getYAxis().setAnimated(true);
		lineChart.setAnimated(true);
		
		lineChart.getData().add(moneyChartSeries);
		patientChart.getData().add(patientChartSeries);
	}
	
	private long getNumberOfDays()
	{
		long to = toDate.getValue().toEpochDay();
		long from = fromDate.getValue().toEpochDay();
		return to-from;
	}
}
