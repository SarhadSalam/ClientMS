package errors;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

/**
 * Class Details:-
 * Author: Sarhad
 * User: sarhad
 * Date: 07/05/18
 * Time : 3:10 AM
 * Project Name: ClientMS
 * Class Name: ErrorPane
 */
public class ErrorPane
{
	
	public static void handleErrorPane(ListView<String> errorPane, Error error)
	{
		//if error pane is not visible, then this is the first tie, set the color text to red
		if( !errorPane.isVisible() ) errorPane.setCellFactory(param -> new ColorText());
			//if error pane is already visible, that means there was previous error, so clear it
		else errorPane.getItems().clear();
		
		//control the error pane and get the errors
		errorPane.setVisible(true);
		errorPane.setItems(FXCollections.observableArrayList(error.getErrors()));
		error.setErrors(null);
	}
	
	//color warning red for the listview
	static class ColorText extends ListCell<String>
	{
		
		@Override
		public void updateItem(String s, boolean b)
		{
			super.updateItem(s, b);
			
			if( s == null || b )
			{
				setText(null);
				setGraphic(null);
			} else
			{
				setText(s);
				this.setTextFill(Color.RED);
			}
		}
	}
}
