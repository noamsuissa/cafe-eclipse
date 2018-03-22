package ca.mcgill.ecse223.resto.application;

import View.Main;
import ca.mcgill.ecse223.resto.model.RestoApp;
import ca.mcgill.ecse223.resto.persistance.PersistenceObjectStream;
import javafx.application.Application;

public class RestoAppApplication {

    private static RestoApp restoApp;
    private static String filename = "menu.resto";

    public static void main(String[] args) {
    	Application.launch(Main.class, args);
    }

    public static RestoApp getRestoApp() {
        if (restoApp == null) {
            // load model
            restoApp = load();
        }
        return restoApp;
    }

    public static void save() {
        PersistenceObjectStream.serialize(restoApp);
    }
    
    public static RestoApp load() {
        PersistenceObjectStream.setFilename(filename);
        restoApp = (RestoApp) PersistenceObjectStream.deserialize();
        // model cannot be loaded - create empty RestoApp
        if (restoApp == null) {
            restoApp = new RestoApp();
        }
        else {
            restoApp.reinitialize();
        }
        return restoApp;
    }

    public static void setFilename(String newFilename) {
        filename = newFilename;
    }
}

