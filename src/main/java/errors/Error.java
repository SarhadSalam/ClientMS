package errors;

import java.util.ArrayList;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 04/05/18 Time : 2:34 AM Project Name: inc.sarhad.CMS Class Name:
 * Error
 */
public class Error
{
	
	private ArrayList<String> errors = new ArrayList<>();
	
	public void setErrors(ArrayList<String> errors)
	{
		if( errors == null ) {
			this.errors.clear();
			return;
		}
		this.errors = errors;
	}
	
	public ArrayList<String> getErrors()
	{
		return errors;
	}
}
