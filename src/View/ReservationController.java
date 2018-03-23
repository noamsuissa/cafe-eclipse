package View;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Table;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReservationController {

	@FXML private Label L1;
	@FXML private Pane P1;
	private Table selectedTable1 = null;
	@FXML private Pane P2;
	@FXML private Pane SelectedTablesPane;
	@FXML private Button ReserveButton;
	@FXML private javafx.scene.control.TextField yearTF;
	@FXML private javafx.scene.control.TextField monthTF;
	@FXML private javafx.scene.control.TextField dayTF;
	@FXML private javafx.scene.control.TextField hourTF;
	@FXML private javafx.scene.control.TextField minuteTF;
	@FXML private javafx.scene.control.TextField numInPartyTF;
	@FXML private javafx.scene.control.TextField contactTF;
	@FXML private javafx.scene.control.TextField emailTF;
	@FXML private javafx.scene.control.TextField phoneNumberTF;



	private int minute;
	private int hour;
	private int second;
	private String ampm;
	

	public void initialize() {

	    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {            
	        Calendar cal = Calendar.getInstance();
	        second = cal.get(Calendar.SECOND);
	        minute = cal.get(Calendar.MINUTE);
	        hour = cal.get(Calendar.HOUR_OF_DAY);
	        
	        //System.out.println(hour + ":" + (minute) + ":" + second);
	        L1.setText(hour + ":" + (minute) + ":" + second);
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();


	}
	
	 public void createReservation(ActionEvent event) throws InvalidInputException{
	    	
	        
	        try {
	        		int year  = Integer.parseInt(yearTF.getText());
	            int  month= Integer.parseInt(monthTF.getText());
	            int  day = Integer.parseInt(dayTF.getText());
	            Date userDate = new Date(year, month, day);
	            int hour = Integer.parseInt(hourTF.getText());
	            int minute = Integer.parseInt(minuteTF.getText());
	            Time userTime = new Time(hour, minute, 0);
	            int numberInParty = Integer.parseInt(numInPartyTF.getText());
	            
	            String contact = contactTF.getText();
	            String email = emailTF.getText();
	            String phoneNumber = phoneNumberTF.getText();
	            
	            
	           
	        	c.reservateTable(userDate, userTime, numberInParty, contact, email, phoneNumber, );
	        	updateBox("Table " + tbleNumber + " was created with " + addSeat + " Seats.", Color.BLACK);
	        	loadCurrentTables();
	        	
	     
	        } catch(InvalidInputException e) {
	        	updateBox(e.getMessage(), Color.RED);
	        	System.out.println(e.getMessage());
	        } catch(RuntimeException e) {
	        	updateBox("Please input values in all fields", Color.RED);
	        }
	    }
	
	
	
    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

	public void loadCurrentTables() {
		RestoApp r = RestoAppApplication.getRestoApp();
		List<Table> currentTables = r.getCurrentTables();
		for (Table currentTable : currentTables) {
			Button btn = new Button(((Integer)currentTable.getNumber()).toString());
			btn.setId(((Integer)currentTable.getNumber()).toString());
			btn.setLayoutX(currentTable.getX());
			btn.setLayoutY(currentTable.getY());
			btn.setMinWidth(currentTable.getWidth());
			btn.setMinHeight(currentTable.getLength());
			btn.setStyle("-fx-background-color: black; ");
			btn.setTextFill(Color.WHITE);
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					selectedTable1 = currentTable;
					updateBox("Table " + selectedTable1.getNumber() + " selected at location (x,y): " + selectedTable1.getX()+" " + selectedTable1.getY(), Color.BLACK);
				}
			});
			P1.getChildren().add(btn);
		}
	}
	public void updateBox(String message, Color color) {
		Text txt = new Text(message);
		txt.setLayoutY(20);
		txt.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		txt.setFill(color);
		P2.getChildren().clear();
		P2.getChildren().add(txt);
	}
}

