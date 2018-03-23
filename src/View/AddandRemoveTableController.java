package View;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddandRemoveTableController implements Initializable{

    @FXML private Button saveTable;
    @FXML private javafx.scene.control.TextField xCoord;
    @FXML private javafx.scene.control.TextField yCoord;
    @FXML private javafx.scene.control.TextField tableNumber;
    @FXML private javafx.scene.control.TextField tableLength;
    @FXML private javafx.scene.control.TextField tableWidth;
    @FXML private javafx.scene.control.TextField addSeats;
    @FXML private Pane P1;
    @FXML private Pane P2;
    RestoAppController c = new RestoAppController();
    private Table selectedTable1;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	loadGrid();
    	loadCurrentTables();
    	updateBox("The floor plan is 400 by 650 pixels.", Color.BLACK);
    }
    
    public void createRectangle(ActionEvent event) throws InvalidInputException{
    	
        
        try {
        	int x = Integer.parseInt(xCoord.getText());
            int y = Integer.parseInt(yCoord.getText());
            int tbleNumber = Integer.parseInt(tableNumber.getText());
            int tbleLength = Integer.parseInt(tableLength.getText());
            int tbleWidth = Integer.parseInt(tableWidth.getText());
            int addSeat = Integer.parseInt(addSeats.getText());
            
            if (x + tbleWidth> 400 || y + tbleLength > 650) {
            	updateBox("Input coordinates for table out of bound", Color.RED);
            	return;
            }
        	c.addTable(tbleNumber, x,y,tbleWidth,tbleLength,addSeat);
        	updateBox("Table " + tbleNumber + " was created with " + addSeat + " Seats.", Color.BLACK);
        	loadCurrentTables();
        	
     
        } catch(InvalidInputException e) {
        	updateBox(e.getMessage(), Color.RED);
        	System.out.println(e.getMessage());
        }
  
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
    
    public void deleteRectangle(ActionEvent event) throws InvalidInputException {
    	updateBox("Table " + selectedTable1.getNumber() + " was removed.", Color.BLACK);
    	c.removeTable(selectedTable1);
    	P1.getChildren().clear();
    	loadGrid();
    	loadCurrentTables();
        
    }
    
    public void deleteHistory(ActionEvent event) {
    	
    }
    
    public void updateBox(String message, Color color) {
    	Text txt = new Text(message);
    	txt.setLayoutY(20);
    	txt.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
    	txt.setFill(color);
    	P2.getChildren().clear();
    	P2.getChildren().add(txt);
    }

    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();

    }
    
    public void loadGrid() {
    	Line line1 = new Line();
		line1.setLayoutX(0);
		line1.setLayoutY(50);
		line1.setEndX(400);
		line1.setEndY(0);

		Line line2 = new Line();
		line2.setLayoutX(0);
		line2.setLayoutY(100);
		line2.setEndX(400);
		line2.setEndY(0);

		Line line3 = new Line();
		line3.setLayoutX(0);
		line3.setLayoutY(150);
		line3.setEndX(400);
		line3.setEndY(0);

		Line line4 = new Line();
		line4.setLayoutX(0);
		line4.setLayoutY(200);
		line4.setEndX(400);
		line4.setEndY(0);

		Line line5 = new Line();
		line5.setLayoutX(0);
		line5.setLayoutY(250);
		line5.setEndX(400);
		line5.setEndY(0);

		Line line6 = new Line();
		line6.setLayoutX(0);
		line6.setLayoutY(300);
		line6.setEndX(400);
		line6.setEndY(0);

		Line line7 = new Line();
		line7.setLayoutX(0);
		line7.setLayoutY(350);
		line7.setEndX(400);
		line7.setEndY(0);

		Line line8 = new Line();
		line8.setLayoutX(0);
		line8.setLayoutY(400);
		line8.setEndX(400);
		line8.setEndY(0);

		Line line9 = new Line();
		line9.setLayoutX(0);
		line9.setLayoutY(450);
		line9.setEndX(400);
		line9.setEndY(0);

		Line line10 = new Line();
		line10.setLayoutX(0);
		line10.setLayoutY(500);
		line10.setEndX(400);
		line10.setEndY(0);

		Line line11 = new Line();
		line11.setLayoutX(0);
		line11.setLayoutY(550);
		line11.setEndX(400);
		line11.setEndY(0);

		Line line12 = new Line();
		line12.setLayoutX(0);
		line12.setLayoutY(600);
		line12.setEndX(400);
		line12.setEndY(0);

		Line line13 = new Line();
		line13.setLayoutX(50);
		line13.setEndY(650);

		Line line14 = new Line();
		line14.setLayoutX(100);
		line14.setEndY(650);

		Line line15 = new Line();
		line15.setLayoutX(150);
		line15.setEndY(650);

		Line line16 = new Line();
		line16.setLayoutX(200);
		line16.setEndY(650);

		Line line17 = new Line();
		line17.setLayoutX(250);
		line17.setEndY(650);

		Line line18 = new Line();
		line18.setLayoutX(300);
		line18.setEndY(650);

		Line line19 = new Line();
		line19.setLayoutX(350);
		line19.setEndY(650);


		P1.getChildren().addAll(line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12, line13,line14,line15,line16,line17,line18,line19);
    }
    
    public void returnToPreviousPage(ActionEvent event)throws IOException {
    	Parent tableViewParent = FXMLLoader.load(getClass().getResource("FloorPlanTableLocation.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();

    }	  

}

