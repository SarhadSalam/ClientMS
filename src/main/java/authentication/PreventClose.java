package authentication;

import database.GetDatabaseLogin;
import events.LogoutEvent;
import global.Global;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import mail.SendMail;
import models.Employee;
import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.events.EventException;
import toasts.Toast;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import static authentication.EmployeeLogin.userExists;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 05/06/18 Time : 6:14 PM Project Name: ClientMS Class Name:
 * PreventClose
 */
public class PreventClose
{
	
	public void createAlert(Stage primaryStage)
	{
		ResourceBundle rs = new i18n.i18n().getResourceBundle(Global.getLocale());
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(rs.getString("close_app"));
		dialog.setHeaderText(rs.getString("close_the_app"));
		dialog.setContentText(rs.getString("prompt_enter_username_pass"));

// Set the button types.
		ButtonType loginButtonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField username = new TextField();
		username.setPromptText(rs.getString("username"));
		PasswordField password = new PasswordField();
		password.setPromptText(rs.getString("password"));
		
		grid.add(new Label(rs.getString("username")), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label(rs.getString("password")), 0, 1);
		grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});
		
		dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
		Platform.runLater(username::requestFocus);

// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
			if( dialogButton == loginButtonType )
			{
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});
		
		Optional<Pair<String, String>> result = dialog.showAndWait();
		
		result.ifPresent(usernamePassword -> {
			try
			{
				Employee empl = userExists(usernamePassword.getKey(), usernamePassword.getValue());
				if( empl == null )
				{
					Toast.makeText(primaryStage, rs.getString("no_user"), 3000, 500, 500);
					return;
				}
				
				Privileges privileges = new Privileges(empl);
				
				if( privileges.hasMoreThanUserStatus() )
				{
					if( !EventBus.getDefault().hasSubscriberForEvent(LogoutEvent.class) )
					{
						primaryStage.close();
					} else
					{
						EventBus.getDefault().post(new LogoutEvent(true));
					}

                    GetDatabaseLogin databaseLogin = new GetDatabaseLogin();
                    databaseLogin.removeFile();
				} else
				{
					Toast.makeText(primaryStage, rs.getString("not_enough_permission"), 3000, 500, 500);
				}
			} catch( IOException|SQLException|GeneralSecurityException|URISyntaxException e )
			{
				Toast.makeText(null, "Failed", 5000, 500, 500);
				SendMail sendMail = new SendMail();
				sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
			}
		});
		
	}
}
