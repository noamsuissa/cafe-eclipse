package View;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.Reservation;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.Table.Status;
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
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Time;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.sql.Date;
import java.util.List;


public class ReservationController {

	@FXML private Label L1;
	@FXML private Pane P1;
	private List<Table> selectedTables= new ArrayList<Table>();
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
	@FXML private Button ToggleButton1;
	@FXML private Button ToggleAvailableButton;
	RestoAppController c = new RestoAppController();
	String[] months = {"Jan", "Feb","Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep" ,"Oct", "Nov", "Dec"};
	private int minute;
	private int hour;
	private int second;
	private int date;
	private String ampm;
	private long localTimeInMillis;

	public void initialize() {
		loadCurrentTables();
	    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {            
	        Calendar cal = Calendar.getInstance();
	        localTimeInMillis= new java.sql.Date(Calendar.getInstance().getTime().getTime()).getTime();
	        //System.out.println(localTimeInMillis);
	        date = cal.get(Calendar.DATE);
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
	            Date userDate = new Date(year - 1900, month, day);
	    
	            int hour = Integer.parseInt(hourTF.getText());
	            int minute = Integer.parseInt(minuteTF.getText());
	            Time userTime = new Time(hour, minute, 0);
	            int numberInParty = Integer.parseInt(numInPartyTF.getText());
	            
	            String contact = contactTF.getText();
	            String email = emailTF.getText();
	            String phoneNumber = phoneNumberTF.getText();
	            if (hour > 24 || minute > 60 || hour < 0 || minute < 0 || day < 0 || day > 31 || month >12 || month < 0) {
	            	updateBox("Please input a valid date", Color.RED);
	            	return;
	            }
	            //System.out.println(userDate.getTime());
	            //System.out.println(userTime.getTime());
	           

	        	c.reserveTable(userDate, userTime, numberInParty, contact, email, phoneNumber, selectedTables);
	        	
	        	updateBox("Table reservation created.", Color.BLACK);

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
		P1.getChildren().clear();
		RestoApp r = RestoAppApplication.getRestoApp();
		List<Table> currentTables = r.getCurrentTables();
		for (Table currentTable : currentTables) {
			Button btn = new Button(((Integer)currentTable.getNumber()).toString());
			btn.setId(((Integer)currentTable.getNumber()).toString());
			btn.setLayoutX(currentTable.getX());
			btn.setLayoutY(currentTable.getY());
			btn.setMinWidth(currentTable.getWidth());
			btn.setMinHeight(currentTable.getLength());
			
			if (currentTable.getStatus().equals(Status.Available)) {
				btn.setStyle("-fx-background-color: black; ");
			
			} else {
				btn.setStyle("-fx-background-color: red; ");
			}
			
			btn.setTextFill(Color.WHITE);
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					if (!selectedTables.contains(currentTable)){
						selectedTables.add(currentTable);
						//System.out.println(Arrays.toString(selectedTables.toArray()));
						Text txt = new Text("Table " + ((Integer)currentTable.getNumber()).toString());
						txt.setLayoutY(selectedTables.size()*13);
						txt.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
						SelectedTablesPane.getChildren().add(txt);
					}
					int i = 0;
					//Collections.sort(currentTable.getReservations(), Comparator.comparingLong((Reservation::getTime)::getTime));
					for (i = 0; i<currentTable.getReservations().size(); i++) {
						System.out.println((currentTable.getReservation(i).getTime()).getTime());
						System.out.println(currentTable.getReservation(i).getDate().getTime());
						System.out.println(localTimeInMillis);
						if ((currentTable.getReservation(i).getTime()).getTime() + currentTable.getReservation(i).getDate().getTime()< localTimeInMillis) {
							continue;
						} else {
							break;
						}
					}
					
					if (currentTable.hasReservations()) {
						String resStatus =( (currentTable.getReservation(i).getTime()).getTime() + currentTable.getReservation(i).getDate().getTime()< localTimeInMillis)&&(currentTable.getReservation(i).getTime()).getTime() + currentTable.getReservation(i).getDate().getTime() + 7200000> localTimeInMillis? "Current Reservation": "Next Reservation";
						String txt2;
						updateBox("Table " + currentTable.getNumber() + " - "+ resStatus + ": " + currentTable.getReservation(i).getTime().getHours()+ ":" + currentTable.getReservation(i).getTime().getMinutes() + " " + months[currentTable.getReservation(i).getDate().getMonth()-1] + " " +currentTable.getReservation(i).getDate().getDate() , Color.BLACK);
						
						if (i+1 < currentTable.getReservations().size()) {
							txt2 = "Next Reservation: " + currentTable.getReservation(i+1).getTime().getHours()+ ":" + currentTable.getReservation(i+1).getTime().getMinutes() + " " + months[currentTable.getReservation(i+1).getDate().getMonth()-1] + " " +currentTable.getReservation(i+1).getDate().getDate();
							
						} else {
							txt2 = "There are no following reservations";		
						}
						Text txt = new Text(txt2);
						txt.setLayoutY(35);
						txt.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
						txt.setFill(Color.BLACK);
						P2.getChildren().add(txt);
						
					} else {
						updateBox("Table " + currentTable.getNumber() + "- There are no current reservations." , Color.BLACK);
					}	
					
				}
			});
			P1.getChildren().add(btn);
		}
	}
	
	public void clearSelection(ActionEvent event) throws IOException{
		selectedTables.clear();
		//System.out.println(Arrays.toString(selectedTables.toArray()));
		SelectedTablesPane.getChildren().clear();
		P2.getChildren().clear();
	}
	
	public void toggleTableUse() throws InvalidInputException {
		try{
			c.startOrder(selectedTables);
			updateBox("Table status toggled.", Color.BLACK);
			P1.getChildren().clear();
			loadCurrentTables();
		} 
		catch(InvalidInputException e) {
        	updateBox(e.getMessage(), Color.RED);
        	System.out.println(e.getMessage());
        }
	}
	
	public void toggleTableAvailability() throws InvalidInputException, InterruptedException {
		try {
			for (Table table: selectedTables) {
				List<Order> orders = table.getOrders();
				for (Order order: orders) {
					c.endOrder(order);
				}
			}
			updateBox("Table status toggled.", Color.BLACK);
			
			P1.getChildren().clear();
			loadCurrentTables();
			
		} catch (InvalidInputException e) {
			updateBox(e.getMessage(), Color.RED);
		} catch (RuntimeException e) {
			updateBox(e.getMessage(), Color.RED);
		}
		
	}
	public void updateBox(String message, Color color) {
		Text txt = new Text(message);
		txt.setLayoutY(20);
		txt.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		txt.setFill(color);
		P2.getChildren().clear();
		P2.getChildren().add(txt);
	}

}

