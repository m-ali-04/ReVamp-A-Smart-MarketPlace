package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Cover_Page.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        DBHandler.createTables();
        UserManager userManager = UserManager.getInstance();
        Catalogue catalogue = Catalogue.getInstance();  // This is the first time the instance is created

        // Load users into the UserManager
        DBHandler.loadUsers(userManager);
        DBHandler.loadCatalogue(catalogue);
        // Print out the counts of loaded users for each type
        System.out.println(userManager.getCID());
        System.out.println(userManager.getEID());
        System.out.println(userManager.getRID());
        System.out.println(userManager.getRFID());

        System.out.println(userManager.getUID());
        System.out.println(Catalogue.getItemId());
        System.out.println(Catalogue.getServiceId());
        
        launch(args);        
    }
}
