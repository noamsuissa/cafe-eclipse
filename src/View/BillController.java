package View;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;



import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.OrderItem;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;

public class BillController implements Initializable{
	@FXML private Pane P1;
    @FXML private Button returnToMain;
    @FXML private Button createBillButton;
    @FXML private Pane displaySeatsPane;
    @FXML private Pane displaySelectedTablePane;
    @FXML private Pane billTotalPane;
    @FXML private TableView<Seat> tableViewSeat;
    @FXML private TableColumn<Seat, Integer > tableSeatName;
    private Table selectedTable1 = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	loadCurrentTables();
    

    	tableSeatName.setCellValueFactory(new PropertyValueFactory<Seat, Integer>("Seat Number"));
    	
    }
    
    private void loadSeatsinTableView(Table table) {
    			ObservableList<Seat> listOfSeats = FXCollections.observableArrayList();
    	if (table != null) {
			for(Seat seat : table.getCurrentSeats()) {
			
				listOfSeats.add(seat);
				System.out.println(seat.toString());
				
			}
			}
    		
    		tableViewSeat.setItems(listOfSeats);
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
                	loadSeatsinTableView(selectedTable1);
                }
            });
            P1.getChildren().add(btn);
        }
    }
    

    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }
    
    


}
