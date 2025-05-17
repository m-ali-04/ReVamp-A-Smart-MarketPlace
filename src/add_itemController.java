package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public class add_itemController {

    @FXML
    private TextField price;
    @FXML
    private TextField desc;
    @FXML
    private TextField itemname;
    @FXML
    private TextField quantity;
    @FXML
    private CheckBox rentable;
    @FXML
    private TextField rentprice;
    @FXML
    private Button items;
    @FXML
    private Button bc;

    // Event Listener on Button[#items].onAction
    @FXML
    public void gotoItems(ActionEvent event) {
    	
    	System.out.println("RETAILER ID: " + SessionManager.getRetailerId());
        // Retrieve values from the form fields
        String name = itemname.getText();
        String description = desc.getText();
        float priceValue = Float.parseFloat(price.getText());
        int stock = Integer.parseInt(quantity.getText());
        boolean isRentable = rentable.isSelected();
        float rentPriceValue = isRentable ? Float.parseFloat(rentprice.getText()) : 0;
        int retailerid= SessionManager.getRetailerId();

        // Retrieve the current itemId from the Catalogue Singleton instance
        int itemId = Catalogue.getItemId();

        // Create a new product using the factory
        Product newProduct = ItemFactory.createProduct(itemId, name, priceValue, description, isRentable, stock, rentPriceValue, retailerid);

        // Perform any additional actions (e.g., save the product to the database, etc.)
        if (newProduct != null) {
            showAlert("Product created successfully!");

            // Increment the ItemId for the next product
            Catalogue.setItemId(itemId + 1);
            
            // Store the product in the database
            DBHandler.storeProduct(newProduct);
            
            // Retrieve the retailer_id of the currently logged-in retailer (assuming SessionManager and Retailer classes are already set up)

                
                int retailerId = SessionManager.getRetailerId();
                
                // Store the relationship between the product and the retailer in the RetailerProduct table
                DBHandler.storeRetailerProduct(newProduct.getProduct_id(), retailerId);
            

            // Update the last ID in the database for future reference
            DBHandler.updateLastIdInDatabase("ItemId", itemId + 1);
        } else {
            showAlert("Error creating the product. Please check your input.");
        }
    }

    
    @FXML
    private void back(ActionEvent event) throws IOException {
    	Stage stage = (Stage) bc.getScene().getWindow(); stage.close(); 
		 Stage primaryStage = new Stage(); 
		 Parent root = FXMLLoader.load(getClass().getResource("Retailer.fxml")); 
		 Scene scene = new Scene(root);
		 //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); 
		 primaryStage.setScene(scene); primaryStage.show();
    }

    // Helper method to show alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Creation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
