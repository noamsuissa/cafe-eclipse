package View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Table;
import ca.mcgill.ecse223.resto.model.MenuItem.ItemCategory;

public class FullMenuController implements Initializable{

	@FXML private GridPane G1;
	@FXML private GridPane G2;
    @FXML private Pane P1;
    private Table selectedTable1;
    RestoAppController c = new RestoAppController();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	loadCurrentTables();
    	loadCategoryItems();
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
	  
    public void returnToMainMenu(ActionEvent event)throws IOException {
    	Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();

    }	
    
    public void loadCategoryItems() {
    	List<ItemCategory> itemCategories = c.getItemCategory();
    	int g2x = 0, g2y = 0, g1x = 0, g1y = 0;
    	
    	for(ItemCategory itemCategory: itemCategories) {
    		String CategoryName = "";
    		String[] r = (itemCategory.name().split("(?=\\p{Upper})"));
    		for (int i = 0; i < r.length; i++) {
    			CategoryName += r[i] + " ";
    		}
    		Button btn = new Button(CategoryName);
    		btn.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
    		
            btn.setStyle("-fx-border-color: black; -fx-font-weight: bold");
            btn.setWrapText(true);
            btn.setTextAlignment(TextAlignment.CENTER);
    		btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent event) {
					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("MenuItemsView.fxml"));
						Parent tableViewParent = loader.load();
						
						Scene tableViewScene = new Scene(tableViewParent);
						MenuItemsViewController mc1 = loader.getController();
						
						try{
							mc1.initData(itemCategory);
						} catch (InvalidInputException e){
							System.out.print(e.getMessage());
						}
						
						Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
						
						window.setScene(tableViewScene);
					    window.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            });
    		if (itemCategory.name().contains("Beverage")) {
    			if (g2x == 3) {
    				g2x = 0;
    				g2y++;
    			}
    			G2.add(btn, g2x++,g2y);
    			
    		}
    		else {
    			if (g1x == 3) {
    				g1x = 0;
    				g1y++;
    			}
    			G1.add(btn, g1x++,g1y);
    		}
    	}
    	
    }
 
}
