package print;

import models.Patient;
import models.Visits;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateInvoice
{
	
	private ClassLoader cl = Thread.currentThread().getContextClassLoader();
	
	private String createInvoice(ArrayList<String> customerDetails) throws IOException
	{
		PDDocument pdDocument = PDDocument.load(cl.getResourceAsStream("invoice/Invoice.pdf"));
		
		PDDocumentCatalog documentCatalog = pdDocument.getDocumentCatalog();
		PDAcroForm acroForm = documentCatalog.getAcroForm();
		
		List<PDField> forms = acroForm.getFields();
		
		for( int i = 0; i<forms.size(); i++ )
		{
			forms.get(i).setValue(customerDetails.get(i));
		}
		
		//set the patientNameField
		String filename = System.getProperty("user.dir")+"/print_dump/invoice_"+System.currentTimeMillis()+".pdf";
		pdDocument.save(filename);
		pdDocument.close();
		
		return filename;
	}
	
	public String createInvoiceDetails(Patient patient, Visits patientVisits) throws IOException
	{
		CreateInvoice createInvoice = new CreateInvoice();
		createInvoice.setSystemProperty();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		ArrayList<String> customerDetails = new ArrayList<>();
		
		customerDetails.add(patient.getName());
		customerDetails.add(String.valueOf(patient.getAge()));
		customerDetails.add(String.valueOf(patient.getGender()));
		customerDetails.add(patient.getGovid());
		customerDetails.add(patient.getPhone());
		customerDetails.add(patient.getEmployee_entered());
		customerDetails.add(simpleDateFormat.format(patientVisits.getTimestamp()));
		customerDetails.add(patientVisits.getServices());
		customerDetails.add(patientVisits.getAmount_paid().toString());
		customerDetails.add(String.valueOf(patientVisits.getVisitId()));
		customerDetails.add(( simpleDateFormat.format(new Date(System.currentTimeMillis())) ));
		customerDetails.add(patientVisits.getAmount_paid().multiply(new BigDecimal(1.05).setScale(2, RoundingMode.CEILING)).toPlainString());
		
		return createInvoice(customerDetails);
	}
	
	private void setSystemProperty()
	{
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
	}
}