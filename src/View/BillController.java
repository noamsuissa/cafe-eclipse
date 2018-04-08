package View;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
	@FXML private Pane billTotalPane;
	@FXML private TableView<Seat> tableViewSeat;
	@FXML private TableView<Seat> allSeatsTableView;
	@FXML private TableColumn<Seat,Number> tableSeatName;
	@FXML private TableColumn<Seat,Number> allSeatsTableViewId;
	@FXML private Button clearSeatsButton;
	@FXML private Pane updateBox;
	@FXML private TableColumn<Seat, Number> allSeatsTableId;
	RestoAppController c = new RestoAppController();

	private List<Seat> selectedSeats = new ArrayList<Seat>();
	ObservableList<Seat> selectedSeatsViewList = FXCollections.observableArrayList();

	private Table selectedTable1 = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loadCurrentTables();


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
			btn.setStyle("-fx-background-color: black; ");
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
			updateBox("Bill created for seats", Color.GREEN);
			clearSeatsPressed(null);
		} catch (InvalidInputException e) {
			updateBox(e.getMessage(), Color.RED);

		}
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
