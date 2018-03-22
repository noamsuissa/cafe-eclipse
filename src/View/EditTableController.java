package View;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditTableController implements Initializable{
	
	@FXML private Button newNumberButton;
	@FXML private Button newSeatButton;
	@FXML private javafx.scene.control.TextField newNumberText;
	@FXML private javafx.scene.control.TextField newNumberSeat;
	@FXML private Pane P1;
	@FXML private Pane P2;
	private Table selectedTable1;
	
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	loadCurrentTables();
    	updateBox("Select a table to edit.", Color.BLACK);
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
                	updateBox("Table " + selectedTable1.getNumber() + " selected. Current number of seats: " + selectedTable1.getCurrentSeats().size(), Color.BLACK);
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
	
	public void updateTableNumberButton(ActionEvent event) throws InvalidInputException{
		
		int newNumber = Integer.parseInt(newNumberText.getText());
		int newSeats = selectedTable1.getSeats().size();
		
    try {
    		updateTable(selectedTable1, newNumber, newSeats);
    		loadCurrentTables();
    		updateBox("Table number changed to " + selectedTable1.getNumber(), Color.BLUE);
    		System.out.println(("Table number changed to " + selectedTable1.getNumber()));
 
    		} catch(InvalidInputException e) {
    			System.out.println(e.getMessage());
    			updateBox(e.getMessage(),Color.RED);
    		}

}
	
public void updateSeatNumberButton(ActionEvent event) throws InvalidInputException{
		
		int newNumber = selectedTable1.getNumber();
		int newSeats = Integer.parseInt(newNumberSeat.getText());
		
				
    
    try {
    		updateTable(selectedTable1, newNumber, newSeats);
    		loadCurrentTables();
    		updateBox("Table " +newNumber+ " updated. Current number of seats: " + selectedTable1.getCurrentSeats().size(), Color.BLUE);
    		System.out.println("Table " +newNumber+ " now has " + selectedTable1.getCurrentSeats().size() + " seats");
    		
 
    		} catch(InvalidInputException e) {
    			System.out.println(e.getMessage());
    			updateBox(e.getMessage(),Color.RED);
    		}

}
	

    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();

    }
    public void returnToPreviousPage(ActionEvent event)throws IOException {
    	Parent tableViewParent = FXMLLoader.load(getClass().getResource("FloorPlanTableLocation.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();

    }	
    

    public static void updateTable (Table aTable, int aNumber, int seats) throws InvalidInputException {
        String error = "";
        if (aTable == null) {
            error = "Must enter a table to update. ";
        }
        if (aNumber < 0){
            error = error + "The table number must be positive. ";
        }
        if (seats < 0){
            error = error + "The number of seats must be positive. ";
        }
        boolean reserved = aTable.hasReservations();
        if(reserved) {
            error = error + "Cannot remove table because it is reserved. ";
        }
        if (error.length() > 0) {
            throw new InvalidInputException(error.trim());
        }

        RestoApp r = RestoAppApplication.getRestoApp();

        List<Order> currentOrders = r.getCurrentOrders();

        for(Order order : currentOrders) {

            List<Table> tables = order.getTables();

            boolean inUse = tables.contains(aTable);
            if(inUse) {
                throw new InvalidInputException("Cannot update table because it is in use.");
            }

        }

        try{
            aTable.setNumber(aNumber);
        }
        catch(RuntimeException e){
            throw new InvalidInputException("Table number cannot be a duplicate");
        }

        int n = aTable.numberOfCurrentSeats();

        for (int i=1;i <= seats-n; i++ ) {
            Seat seat = aTable.addSeat();
            aTable.addCurrentSeat(seat);
        }

        for (int i=1;i <= n-seats; i++ ) {
            Seat seat = aTable.getCurrentSeat(0);
            aTable.removeCurrentSeat(seat);
        }

        try{
            RestoAppApplication.save();
        }
        catch(RuntimeException e){
            throw new InvalidInputException(e.getMessage());
        }


    }
}

