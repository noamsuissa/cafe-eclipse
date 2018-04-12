package View;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.Order;
import ca.mcgill.ecse223.resto.model.OrderItem;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;

public class OrderController implements Initializable{

	@FXML private Pane P1;
	@FXML private TableView<OrderItem> tableView;
	@FXML private TableColumn<OrderItem, String> orderName;
	@FXML private Pane updateBox;
	@FXML private TableView<Seat> tableViewSeat;
	@FXML private TableColumn<Seat,Number> tableSeatName;
	@FXML private Button deleteAllTableItemsButton;
	@FXML private Button deleteAllOrderItemsButton;
	ObservableList<OrderItem> selectedTableViewList = FXCollections.observableArrayList();
	@FXML private TableColumn<OrderItem, Number> orderQuantity;
	private OrderItem selectedSeat;


	private Table selectedTable1 = null;
	RestoAppController c = new RestoAppController();

	private List<OrderItem> selectedItem = new ArrayList<OrderItem>();
	ObservableList<OrderItem> selectedItemViewList = FXCollections.observableArrayList();

	private List<Seat> selectedSeats = new ArrayList<Seat>();
	ObservableList<Seat> selectedSeatsViewList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		orderName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPricedMenuItem().getMenuItem().getName()));
		orderQuantity.setCellValueFactory(new PropertyValueFactory<OrderItem, Number>("quantity"));
		tableSeatName.setCellValueFactory(new PropertyValueFactory<Seat, Number>("Id"));
		loadCurrentTables();

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

	private void loadOrdersInTableView(Table table) {
		ObservableList<OrderItem> listOfOrderItems = FXCollections.observableArrayList();
		if (table != null) {
			List<OrderItem> orderItems;
			try {
				orderItems = c.getOrderItems(table);
				for(OrderItem order: orderItems) {
					listOfOrderItems.add(order);
					//updateBox("list"+ listOfOrderItems, Color.BLACK);
				} 
			} catch (InvalidInputException e) {
				updateBox(e.getMessage(), Color.RED);
			}
		}
		tableView.setItems(listOfOrderItems);
	}

	@SuppressWarnings("static-access")
	public void deleteAllOrderItemsAction(ActionEvent event) throws InvalidInputException {

		Seat selectedSeat = tableViewSeat.getSelectionModel().getSelectedItem();
		List<OrderItem> items = selectedSeat.getOrderItems();

		List<OrderItem> nItems = new ArrayList<OrderItem>(items);

		for(OrderItem orderItem : nItems) {
			try {

				updateBox("All order items cancelled at seat " + selectedSeat.getId(), Color.BLACK);
				c.cancelOrderItem(orderItem);
				tableView.getItems().clear();
				loadOrdersInTableView(selectedTable1);
				
			}
			catch(InvalidInputException e) {
				updateBox(e.getMessage(), Color.RED);
			}
		}

	}

	//Load all the tables into the floor plan
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
					updateBox("Selected table "+ selectedTable1.getNumber(), Color.BLACK);
					loadOrdersInTableView(selectedTable1);
					loadSeatsinTableView(selectedTable1);
				}
			});
			P1.getChildren().add(btn);
		}
	}

	public void updateBox(String message, Color color) {
		Text txt = new Text(message);
		txt.setLayoutY(20);
		txt.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
		txt.setFill(color);
		updateBox.getChildren().clear();
		updateBox.getChildren().add(txt);
	}

	public void returnToMainMenu(ActionEvent event) throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);

		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		window.setScene(tableViewScene);
		window.show();
	}

	public void deleteAllTableItemsAction(ActionEvent event) throws InvalidInputException  {

		try {
			c.cancelOrder(selectedTable1);
			tableView.refresh();
			updateBox("Cancelled all orderitems at table " + selectedTable1.getNumber(), Color.BLACK);
		}
		catch (InvalidInputException e){
			updateBox(e.getMessage(), Color.RED);
		}

	}

}