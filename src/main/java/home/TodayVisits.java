package home;

import controllers.TodayVisitsController;
import global.Global;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Employee;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 17/06/18 Time : 3:53 PM Project Name: ClientMS Class Name:
 * TodayVisits
 */
public class TodayVisits
{
	public void start(Stage primaryStage, Employee empl) throws IOException
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		ResourceBundle rs = new i18n.i18n().getResourceBundle(Global.getLocale());
		
		TodayVisitsController todayVisitsController = new TodayVisitsController();
		FXMLLoader loader = new FXMLLoader(cl.getResource("layout/TodayVisits.fxml"));
		loader.setController(todayVisitsController);
		loader.setResources(rs);
		
		Parent root = loader.load();
		Stage childStage = new Stage();
		
		childStage.initOwner(primaryStage);
		childStage.initModality(Modality.WINDOW_MODAL);
		
		childStage.setTitle("Specialized Hijama - Today's Visits");
		childStage.setScene(new Scene(root, 600, 500));
		
		childStage.setResizable(false);
		childStage.getIcons().add(new Image(cl.getResourceAsStream("img/logo.png")));
		
		todayVisitsController.setEmpl(empl);
		todayVisitsController.loadTable();
		
		childStage.showAndWait();
		
		//wait for the visit to complete
	}
}
