package View;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Seat;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;
import ca.mcgill.ecse223.resto.model.Table.Status;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MenuItemsViewController implements Initializable{

	@FXML private Label label1;
	@FXML private Button returnMain;
	@FXML private Button addToList;
	@FXML private Pane P1;
	@FXML private Pane updateBox;
	@FXML private GridPane G1;
	@FXML private TableView<Seat> tableView;
	@FXML private  TextField qtyText;
	@FXML private Button addOrderButton;
	RestoAppController c = new RestoAppController();
	private Table selectedTable1=null;
	private MenuItem selectedMenuItem;
	@FXML private TableView<Seat> tableViewSeat;
	@FXML private Pane seatView;
	@FXML private TableView<Seat> allSeatsTableView;
	@FXML private TableColumn<Seat,Number> tableSeatName;
	@FXML private TableColumn<Seat,Number> allSeatsTableViewId;
	@FXML private TableColumn<Seat,Number> SeatsTableId;
	private List<Seat> selectedSeats = new ArrayList<Seat>();
	ObservableList<Seat> selectedSeatsViewList = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadCurrentTables();
		tableSeatName.setCellValueFactory(new PropertyValueFactory<Seat, Number>("Id"));
		allSeatsTableViewId.setCellValueFactory(new PropertyValueFactory<Seat, Number>("Id"));
		SeatsTableId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTable().getNumber()));
	}

	public void returnToMainMenu(ActionEvent event)throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);

		Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();

		window.setScene(tableViewScene);
		window.show();

	}
	
	public void updateBox(String message, Color color) {
		Text txt = new Text(message);
		txt.setLayoutY(20);
		txt.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
		txt.setFill(color);
		updateBox.getChildren().clear();
		updateBox.getChildren().add(txt);
	}

	public void addOrderAction(ActionEvent event) {
		try {
			int qty  =Integer.parseInt(qtyText.getText());
			//Seat [] seatArr = new Seat[selectedSeats.size()];
			c.orderMenuItem(selectedMenuItem, qty, selectedSeats); //it means u should do RestoAppController.orderMenuItem(...) but its fine just like this
			updateBox("Order item " + selectedMenuItem.getName() + " added to order", Color.BLACK);
		} catch (InvalidInputException e) {
			updateBox(e.getMessage(), Color.RED);

		}
	}
	
	public void addtoListButton(ActionEvent event) {
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


	public void returnToPreviousPage(ActionEvent event)throws IOException {
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("FullMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);

		Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();

		window.setScene(tableViewScene);
		window.show();

	}	
	private void loadSeatsInTableView(Table table) {
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
			if (currentTable.getStatus().equals(Status.Available)) {
				btn.setStyle("-fx-background-color: black; ");
			
			} else {
				btn.setStyle("-fx-background-color: red; ");
			}
			btn.setTextFill(Color.WHITE);
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent e) {
					selectedTable1 = currentTable;
					loadSeatsInTableView(selectedTable1);
					updateBox("Table " + selectedTable1.getNumber() + " selected. It has " + selectedTable1.getCurrentSeats().size()+" seats.", Color.BLACK);
				}
			});
			P1.getChildren().add(btn);
		}
	}



	public void initData(ItemCategory itemCategory) throws InvalidInputException  {
		//ITEM CATEGORY TEXT
		String CategoryName = "";
		String[] r = (itemCategory.name().split("(?=\\p{Upper})"));
		for (int i = 0; i < r.length; i++) {
			CategoryName += r[i] + " ";
		}
		label1.setText(CategoryName);

		//ITEMS
		List<MenuItem> menuItems = c.getMenuItems(itemCategory);
		int g1x = 0, g1y = 0;

		for(MenuItem menuItem: menuItems) {

			Button btn = new Button(menuItem.getName() + " " + menuItem.getCurrentPricedMenuItem().getPrice() + "$");
			btn.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
			btn.setWrapText(true);
			btn.setStyle("-fx-border-color: black; -fx-font-weight: bold");
			btn.setTextAlignment(TextAlignment.CENTER);
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					selectedMenuItem = menuItem;
					updateBox("Menu Item " + selectedMenuItem.getName() + " chosen", Color.BLACK);
				}
			});
			if (g1x == 3) {
				g1x = 0;
				g1y++;
			}
			G1.add(btn, g1x++, g1y);
		}
	}
}
