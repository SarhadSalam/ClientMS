package print;

import errors.Error;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import models.Visits;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 29/05/18 Time : 2:29 PM Project Name: ClientMS Class Name: Print
 */
public class Print
{
	public String print(String filename, Visits visit, int copies) throws IOException, PrinterException
	{
		String jobName = visit.getEmployeeEntered()+" - File No:"+visit.getVisitId()+" - Patient No:"+visit.getPatientId();
		PDDocument doc = PDDocument.load(new File(filename));
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setJobName(jobName);
		job.setCopies(copies);
		job.setPageable(new PDFPageable(doc));
		job.print();
		
		return jobName;
	}
	
	public boolean validate(TextField printCustomerAmount, CheckBox printCustomerCheck, TextField printHospitalAmount, CheckBox printHospitalCheck, Error error, boolean print)
	{
		boolean cont = true;
		if(print)
		{
			if(!printCustomerCheck.isSelected() && !printHospitalCheck.isSelected())
			{
				cont=false;
				error.getErrors().add("Nothing is selected to print.");
			}
			
			if(!printCustomerAmount.getText().matches("^[0-9]*$") || !printHospitalAmount.getText().matches("^[0-9.]*$"))
			{
				cont=false;
				error.getErrors().add("Can only be positive numbers.");
			}
		}
		return cont;
	}
}
