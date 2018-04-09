package View;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.Table.Status;
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
	RestoAppController c = new RestoAppController();
	
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
        	if (currentTable.getStatus().equals(Status.Available)) {
				btn.setStyle("-fx-background-color: black; ");
			
			} else {
				btn.setStyle("-fx-background-color: red; ");
			}
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
		
		
    try {
    	int newNumber = Integer.parseInt(newNumberText.getText());
		int newSeats = selectedTable1.getSeats().size();
		
		c.updateTable(selectedTable1, newNumber, newSeats);
		loadCurrentTables();
		updateBox("Table number changed to " + selectedTable1.getNumber(), Color.BLUE);
		System.out.println(("Table number changed to " + selectedTable1.getNumber()));
 
		} catch(InvalidInputException e) {
			System.out.println(e.getMessage());
			updateBox(e.getMessage(),Color.RED);
		} catch(RuntimeException e) {
        	updateBox("Please input a value in the field." , Color.RED);
        }

}
	
public void updateSeatNumberButton(ActionEvent event) throws InvalidInputException{
	
    try {
    	
		int newNumber = selectedTable1.getNumber();
		int newSeats = Integer.parseInt(newNumberSeat.getText());
		c.updateTable(selectedTable1, newNumber, newSeats);
		loadCurrentTables();
		updateBox("Table " +newNumber+ " updated. Current number of seats: " + selectedTable1.getCurrentSeats().size(), Color.BLUE);
		System.out.println("Table " +newNumber+ " now has " + selectedTable1.getCurrentSeats().size() + " seats");
	    		
	 
		} catch(InvalidInputException e) {
			System.out.println(e.getMessage());
			updateBox(e.getMessage(),Color.RED);
		}  catch(RuntimeException e) {
        	updateBox("Please input a value in the field.", Color.RED);
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
 
}

