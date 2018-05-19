package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import models.Employee;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 19/05/18 Time : 11:24 AM Project Name: ClientMS Class Name:
 * MenuBarController
 */
public class MenuBarController
{
	private Employee empl;
	private boolean userInfoAvailable = false;
	
	@FXML
	public MenuBar menubar;
	
	@FXML
	public Menu userMenu;
	
	public MenuBarController(){
		this.empl=null;
	}
	
	public MenuBarController(Employee empl){
		this.empl = empl;
		userInfoAvailable=true;
	}
	
	public void setUpMenuBar(){
		//if there is no user available, dont show user options.
		if(!userInfoAvailable)
		{
			if(userMenu==null) System.out.println("shit");
			System.out.println("kk");
			menubar.getMenus().remove(userMenu);
		}
	}
}
