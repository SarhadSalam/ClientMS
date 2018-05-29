package dev;

import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 29/05/18 Time : 12:09 PM Project Name: ClientMS Class Name:
 * scratch
 */

import i18n.i18n;
public class scratch
{
	
	public static void main(String[] args)
	{
		ResourceBundle resourceBundle = new i18n().getResourceBundle("en", "US");
		System.out.println(resourceBundle.getString("arabic"));
	}
}
