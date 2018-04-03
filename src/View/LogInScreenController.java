package View;

import ca.mcgill.ecse223.resto.application.RestoAppApplication;
import ca.mcgill.ecse223.resto.controller.InvalidInputException;
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


    public void logInButton(ActionEvent event) throws InvalidInputException {
        try {

            int waiterID = Integer.parseInt(logInIDTF.getText());
            String waiterPass = logInPasswordTF.getText();

            if (isValidWaiter(waiterID, waiterPass)) {
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

    public boolean isValidWaiter( int aId, String aPassword) throws InvalidInputException {
        String errorMessage = "";
        if (aId <= 0) {
            errorMessage += "Please input valid credentials";
        }
        if (aPassword == null || aPassword == "") {
            errorMessage += "Please input valid credentials";
        }
        if (errorMessage.length() > 1)
            throw new InvalidInputException(errorMessage);

        RestoApp r = RestoAppApplication.getRestoApp();
        try {
            Waiter waiter = Waiter.getWithId(aId);
            if (waiter == null) {
                throw new InvalidInputException("This ID does not exist");
            }
            String waiterPass = waiter.getPassword();
            if (waiterPass.equals(aPassword)) {
                r.setCurrentWaiter(waiter);
                RestoAppApplication.save();
                return true;
            } else
                {throw new InvalidInputException("Incorrect Password");

                }

        } catch (RuntimeException e){
            throw new InvalidInputException(e.getMessage());
        }

    }

    public void createWaiterButton(ActionEvent event) throws InvalidInputException{
    	
        try {
            int id = Integer.parseInt(createIdTF.getText());
            String name = createNameTF.getText();
            String password = createPasswordTF.getText();
            createWaiter(name, id, password);
            updateBox("Waiter Account Created Successfully", Color.BLACK);
        }
        catch(InvalidInputException e) {
                updateBox(e.getMessage(), Color.RED);
                System.out.println(e.getMessage());
            } catch(RuntimeException e) {
                updateBox(e.getMessage(), Color.RED);
            }
        }

    public void createWaiter(String aName, int aId, String aPassword)  throws InvalidInputException {

        String errorMessage = "";

        if(aName == null || aName.equals("")){
            errorMessage = "Please enter in your name";
        }
        if(aId<=0){
            errorMessage = "Please enter in a valid ID number";
        }
        if(aPassword == null || aPassword.equals("")){
            errorMessage = "Please enter a password";
        }
        if(errorMessage.length()>1){
            throw new InvalidInputException(errorMessage);
        }
        RestoApp r = RestoAppApplication.getRestoApp();

        try{
        Waiter w = new Waiter(aId, aName, aPassword, r);
        RestoAppApplication.save();
    } catch (RuntimeException e){
            throw new InvalidInputException(e.getMessage());
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
