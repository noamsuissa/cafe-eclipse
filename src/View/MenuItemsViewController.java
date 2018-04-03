package View;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.Menu;
import ca.mcgill.ecse223.resto.model.MenuItem;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MenuItemsViewController implements Initializable{
	
	@FXML private Label label1;
	@FXML private Button returnMain;
	@FXML private Pane P1;
	@FXML private GridPane G1;
	RestoAppController c = new RestoAppController();
	private Table selectedTable1;
	
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		loadCurrentTables();
    }
	
    public void returnToMainMenu(ActionEvent event)throws IOException {
    	Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();

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
