package View;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
import ca.mcgill.ecse223.resto.controller.RestoAppController;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.model.Waiter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInScreenController {

    @FXML private Button logInButton;
    @FXML private Button createWaiterButton;
    @FXML private TextField logInIDTF;
    @FXML private PasswordField logInPasswordTF;
    @FXML private TextField createIdTF;
    @FXML private TextField createNameTF;
    @FXML private PasswordField createPasswordTF;
    @FXML private Pane updatePane;
    RestoAppController c = new RestoAppController();
    

    public void logInButton(ActionEvent event) throws InvalidInputException {
        try {

            int waiterID = Integer.parseInt(logInIDTF.getText());
            String waiterPass = logInPasswordTF.getText();

            if (c.isValidWaiter(waiterID, waiterPass)) {
                Parent tableViewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                Scene tableViewScene = new Scene(tableViewParent);

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                window.setScene(tableViewScene);
                window.show();
            }
        }catch(InvalidInputException e) {
            updateBox(e.getMessage(), Color.RED);
            System.out.println(e.getMessage());
        } catch(RuntimeException e) {
            updateBox(e.getMessage(), Color.RED);
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        }
    }

 

    public void createWaiterButton(ActionEvent event) throws InvalidInputException{
    	
        try {
            int id = Integer.parseInt(createIdTF.getText());
            String name = createNameTF.getText();
            String password = createPasswordTF.getText();
            c.createWaiter(name, id, password);
            updateBox("Waiter Account Created Successfully", Color.BLACK);
            createIdTF.clear();
            createNameTF.clear();
            createPasswordTF.clear();
        }
        catch(InvalidInputException e) {
                updateBox(e.getMessage(), Color.RED);
                System.out.println(e.getMessage());
            } catch(RuntimeException e) {
                updateBox(e.getMessage(), Color.RED);
            }
        }

    public void updateBox(String message, Color color) {
        Text txt = new Text(message);
        txt.setLayoutY(20);
        txt.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        txt.setFill(color);
        updatePane.getChildren().clear();
        updatePane.getChildren().add(txt);
    }



}
