package View;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.Bill;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.OrderItem;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;
import ca.mcgill.ecse223.resto.model.Table.Status;

public class BillController implements Initializable{
	@FXML private Pane P1;
	@FXML private Button returnToMain;
	@FXML private Button createBillButton;
	@FXML private Pane billTotalPane;
	@FXML private TableView<Seat> tableViewSeat;
	@FXML private TableView<Seat> allSeatsTableView;
	@FXML private TableColumn<Seat,Number> tableSeatName;
	@FXML private TableColumn<Seat,Number> allSeatsTableViewId;
	@FXML private Button clearSeatsButton;
	@FXML private Pane updateBox;
	@FXML private TableColumn<Seat, Number> allSeatsTableId;
	@FXML private TableView<OrderItem> tableView;
	@FXML private TableColumn<OrderItem, String> orderName;
	@FXML private TableColumn<OrderItem, Number> orderQuantity;
	@FXML private TableColumn<OrderItem, Number> tablePrice;
	@FXML private TableColumn<OrderItem, Number> sharedBy;
	
	
	
	RestoAppController c = new RestoAppController();

	private List<Seat> selectedSeats = new ArrayList<Seat>();
	ObservableList<Seat> selectedSeatsViewList = FXCollections.observableArrayList();

	private Table selectedTable1 = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loadCurrentTables();
		orderName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPricedMenuItem().getMenuItem().getName()));
	    orderQuantity.setCellValueFactory(new PropertyValueFactory<OrderItem, Number>("quantity"));
	    tablePrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPricedMenuItem().getPrice()));
	    sharedBy.setCellValueFactory(cellData -> new SimpleDoubleProperty((Integer)(cellData.getValue().numberOfSeats())));

		tableSeatName.setCellValueFactory(new PropertyValueFactory<Seat, Number>("Id"));
		allSeatsTableViewId.setCellValueFactory(new PropertyValueFactory<Seat, Number>("Id"));
		allSeatsTableId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTable().getNumber()));

	}

	private void loadSeatsinTableView(Table table) {
		ObservableList<Seat> listOfSeats = FXCollections.observableArrayList();
		if (table != null) {
			for(Seat seat : table.getCurrentSeats()) {

				listOfSeats.add(seat);
				//System.out.println(seat.toString());

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
			if (currentTable.getStatus().equals(Status.Available)) {
				btn.setStyle("-fx-background-color: black; ");
			
			} else {
				btn.setStyle("-fx-background-color: red; ");
			}
			btn.setTextFill(Color.WHITE);
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					selectedTable1 = currentTable;
					loadSeatsinTableView(selectedTable1);
					updateBox("Table " + selectedTable1.getNumber() + " selected. It has " + selectedTable1.getCurrentSeats().size()+" seats.", Color.BLACK);
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


	public void addToListPressed(ActionEvent event) {
		try {
			Seat selectedSeat =tableViewSeat.getSelectionModel().getSelectedItem();

			if (selectedSeat == null) {
				updateBox("No seat selected.", Color.RED);
				return;
			}
			selectedSeats.add(selectedSeat);
			selectedSeatsViewList.add(selectedSeat);
			allSeatsTableView.setItems(selectedSeatsViewList);


		} catch (RuntimeException e) {
			updateBox("No seat selected.", Color.RED);
		}

	}

	public void createBillPressed(ActionEvent event) {
		try {
			c.issueBill(selectedSeats);
			Seat seat1 = selectedSeats.get(0);
			loadOrdersInTableView(seat1);
			calculateBillTotal(seat1);
			
			updateBox("Bill created for seats", Color.GREEN);
			clearSeatsPressed(null);
			
			
		} catch (InvalidInputException e) {
			updateBox(e.getMessage(), Color.RED);

		}	
		
	}
	 private void loadOrdersInTableView(Seat seat) {
			ObservableList<OrderItem> listOfOrderItems = FXCollections.observableArrayList();
			List<OrderItem> orderItems;
			orderItems = seat.getOrderItems();
			for(OrderItem order: orderItems) {
			listOfOrderItems.add(order);
			updateBox("list"+ listOfOrderItems, Color.BLACK);
			}
			  
			tableView.setItems(listOfOrderItems);
	    }
	 public static double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
		}
	
	public void calculateBillTotal(Seat seat) {
		double billTotal = 0;
		List<OrderItem> orderItems;
		orderItems = seat.getOrderItems();
		for(OrderItem order: orderItems) {
			if(order.getQuantity() == 1) {
				int sharedByInt = order.numberOfSeats();
				System.out.println(sharedByInt);
				billTotal = billTotal + ((order.getPricedMenuItem().getPrice())/sharedByInt);
			} else if(order.getQuantity()>1) {
				int sharedByInt = order.numberOfSeats();
				System.out.println(sharedByInt);
				billTotal += (((order.getQuantity())*(order.getPricedMenuItem().getPrice()))/sharedByInt);
		}}
		
		billTotal = round(billTotal, 2);
		String str = String.valueOf(billTotal)+" $";
		Text txt = new Text(str);
		txt.setLayoutY(20);
		txt.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		txt.setFill(Color.BLACK);
    		billTotalPane.getChildren().clear();
    		billTotalPane.getChildren().add(txt);
		
	}

	public void clearSeatsPressed(ActionEvent event) {
		selectedSeats.clear();
		selectedSeatsViewList.clear();
		allSeatsTableView.setItems(selectedSeatsViewList);
		System.out.println("cleared seats");
	}

	public void updateBox(String message, Color color) {
		Text txt = new Text(message);
		txt.setLayoutY(20);
		txt.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		txt.setFill(color);
		updateBox.getChildren().clear();
		updateBox.getChildren().add(txt);
	}


}
