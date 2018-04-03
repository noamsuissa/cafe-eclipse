package View;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;
import ca.mcgill.ecse223.resto.model.PricedMenuItem;
import ca.mcgill.ecse223.resto.model.RestoApp;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyMenuItemsController implements Initializable{


	@FXML private TableView<MenuItem> tableView;
	@FXML private TableColumn<MenuItem, String> tableName;
	@FXML private TableColumn<MenuItem, Double> tablePrice; //change to string if uncommenting the bellow comment
	@FXML private TableColumn<MenuItem, ItemCategory> tableCategory;
	@FXML private ComboBox<ItemCategory> categoryDropDown1;
	@FXML private ComboBox<ItemCategory> categoryDropDown2;
	@FXML private Pane P1;
	@FXML private TextField firstName;
	@FXML private TextField firstPrice;
	@FXML private Button addItemButton;
	@FXML private Button updateItemButton;
	@FXML private TextField newName;
	@FXML private TextField newPrice;
	@FXML private Button removeItemButton;
	private MenuItem selectedMenuItem;
	RestoAppController c = new RestoAppController();



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableName.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("Name"));
		tableCategory.setCellValueFactory(new PropertyValueFactory<MenuItem, ItemCategory>("itemCategory"));
		tablePrice.setCellValueFactory(new PropertyValueFactory<MenuItem, Double>("price"));
		/*way to retrieve price directly from PMI
		tablePrice.setCellValueFactory(new Callback<CellDataFeatures<MenuItem, String>, ObservableValue<String>>() {
		    @Override
			public ObservableValue<String> call( CellDataFeatures<MenuItem, String> c) {
		      return new SimpleStringProperty(c.getValue().getValue().getCurrentPricedMenuItem().getPrice()+"");
		     }
		    });	
		*/
		categoryDropDown1.getItems().setAll(ItemCategory.values());
		categoryDropDown2.getItems().setAll(ItemCategory.values());

		tableView.setItems(loadCurrentMenuItems());
		updateBox("Select a menu item to edit.", Color.BLACK);
	}

	private ObservableList<MenuItem> loadCurrentMenuItems() {
		ObservableList<MenuItem> menuItemsForTable = FXCollections.observableArrayList();
		ItemCategory itemCategories;
		for(ItemCategory itemCategory : ItemCategory.values()) {
			List<MenuItem> menuItems;
			try {
				menuItems = RestoAppController.getMenuItems(itemCategory);
				for(MenuItem mI : menuItems) {
					mI.getPMIPrice();
					menuItemsForTable.add(mI);
				}
			} catch (InvalidInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return menuItemsForTable;
	}

	public void updateBox(String message, Color color) {
		Text txt = new Text(message);
		txt.setLayoutY(20);
		txt.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		txt.setFill(color);
		P1.getChildren().clear();
		P1.getChildren().add(txt);
	}

	public void addNewItemButton(ActionEvent event) throws InvalidInputException{
		try {
			ItemCategory cat = categoryDropDown1.getSelectionModel().getSelectedItem();
			categoryDropDown1.setPromptText(cat.name());
			String name = firstName.getText();
			double price = Double.parseDouble(firstPrice.getText());
			c.addMenuItem(name, cat, price);
			loadCurrentMenuItems();
			updateBox(name + " menu item created. ", Color.BLUE);
			System.out.println(name + " menu item created. ");
		}catch(InvalidInputException e) {
			System.out.println(e.getMessage());
			updateBox(e.getMessage(),Color.RED);
		}catch(RuntimeException e) {
			updateBox("Please input a value in the field." , Color.RED);
		}
	}

	public void removeAnItemButton(ActionEvent event) throws InvalidInputException{
		try {
			selectedMenuItem = tableView.getSelectionModel().getSelectedItem();
			c.removeMenuItem(selectedMenuItem);
			loadCurrentMenuItems();
			tableView.refresh(); //does not refresh, need to check more
			//tableView.getColumns().get(0).setVisible(false);
			//tableView.getColumns().get(0).setVisible(true);
			updateBox(selectedMenuItem.getName() + " has been removed. ", Color.BLUE);
			System.out.println(selectedMenuItem.getName() + " has been removed. ");

		}catch(InvalidInputException e) {
			System.out.println(e.getMessage());
			updateBox(e.getMessage(),Color.RED);
		}catch(RuntimeException e) {
			updateBox("Please input a value in the field." , Color.RED);
		}
	}

	public void updateAnItemButton(ActionEvent event) throws InvalidInputException{
		try {
			selectedMenuItem = tableView.getSelectionModel().getSelectedItem();
			ItemCategory cat = categoryDropDown2.getValue();
			String name = newName.getText();
			double price = Double.parseDouble(newPrice.getText());
			c.updateMenuItem(selectedMenuItem, name, cat, price);
			categoryDropDown2.setPromptText(cat.name());
			//loadCurrentMenuItems();
			tableView.refresh();
			updateBox(name + " menu item updated to: \n" + name + ", " + cat.toString() + ", " + price, Color.BLUE);
			System.out.println(name + " menu item updated to: " + name + ", " + cat.toString() + ", " + price);
		}catch(InvalidInputException e) {
			System.out.println(e.getMessage());
			updateBox(e.getMessage(),Color.RED);
		}catch(RuntimeException e) {
			updateBox("Please input a value in the field." , Color.RED);
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
		Parent tableViewParent = FXMLLoader.load(getClass().getResource("FullMenu.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);

		Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();

		window.setScene(tableViewScene);
		window.show();

	}
}
