package toasts;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import mail.SendMail;

import java.util.Arrays;

/**
 * Class Details:- Author: Sarhad User: sarhad Date: 31/05/18 Time : 5:35 PM Project Name: ClientMS Class Name: Toast
 */
public class Toast
{
	
	public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay)
	{
		Stage toastStage = new Stage();
		if( ownerStage != null ) toastStage.initOwner(ownerStage);
		toastStage.setResizable(false);
		toastStage.initStyle(StageStyle.TRANSPARENT);
		
		Text text = new Text(toastMsg);
		text.setFont(Font.font("Verdana", 40));
		text.setFill(Color.RED);
		
		StackPane root = new StackPane(text);
		root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 50px;");
		root.setOpacity(0);
		
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		toastStage.setScene(scene);
		toastStage.show();
		
		Timeline fadeInTimeline = new Timeline();
		KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
		fadeInTimeline.getKeyFrames().add(fadeInKey1);
		fadeInTimeline.setOnFinished((ae) ->
		{
			new Thread(() -> {
				try
				{
					Thread.sleep(toastDelay);
				} catch( InterruptedException e )
				{
					SendMail sendMail = new SendMail();
					sendMail.sendErrorMail(Arrays.toString(e.getStackTrace()));
					System.out.println("failed");
				}
				
				Timeline fadeOutTimeline = new Timeline();
				KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
				fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
				fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
				fadeOutTimeline.play();
			}).start();
		});
		fadeInTimeline.play();
	}
}
