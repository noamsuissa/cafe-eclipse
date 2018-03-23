package View;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Calendar;

public class ReservationController {
	
	@FXML private Label L1;
	
	private int minute;
	private int hour;
	private int second;
	private String ampm;
	
	@FXML
	public void initialize() {

	    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {            
	        Calendar cal = Calendar.getInstance();
	        second = cal.get(Calendar.SECOND);
	        minute = cal.get(Calendar.MINUTE);
	        hour = cal.get(Calendar.HOUR);
	        ampm = cal.get(Calendar.AM_PM) == 0 ? "AM": "PM";
	        System.out.println(hour + ":" + (minute) + ":" + second);
	        L1.setText(hour + ":" + (minute) + ":" + second + " " + ampm);
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();
	}
	
    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }
}

