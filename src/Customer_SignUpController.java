package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;

public class Customer_SignUpController {
	@FXML
	private ComboBox<String> cust_type;
	@FXML
	private TextField address;
	@FXML
	private Button confirm;

	@FXML
	public void initialize() {
		cust_type.setItems(FXCollections.observableArrayList("Regular","Premium","Life-Time"));
	}
	
	// Event Listener on Button[#confirm].onAction
	@FXML
	public void gotoCoverPage(ActionEvent event) throws IOException {
	    // Get the values from the form
	    String customerType = cust_type.getValue();
	    String customerAddress = address.getText();

	    if (customerType == null || customerAddress.isEmpty()) {
	        showAlert("Please fill in all fields before proceeding.");
	        return;
	    }

	    // Retrieve the currently logged-in user from the SessionManager
	    User loggedInUser = SessionManager.getLoggedInUser();
        DBHandler.loadCustomerOrders();
	    // Ensure the user is of type Customer
	    if (loggedInUser instanceof Customer) {
	        Customer customer = (Customer) loggedInUser;

	        // Set the customer type and address
	        customer.setCustomer_type(customerType); // Use the selected type from ComboBox
	        customer.setAddress(customerAddress);

	        // Now store the updated customer in the database
	        if (DBHandler.storeUser(customer)) {
	            // Inform the user that the account creation was successful
	            showAlert("Account successfully created!");
	        } else {
	            showAlert("Error saving customer details to the database. Please try again.");
	            return;
	        }
	    } else {
	        showAlert("No valid customer found in session.");
	        return;
	    }
	    
	    
	    
	    // Close the current stage
	    Stage stage = (Stage) confirm.getScene().getWindow();
	    stage.close();

	    // Load the new stage (Cover Page)
	    Stage primaryStage = new Stage();
	    Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
	    Scene scene = new Scene(root);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	// Helper method to show alerts
	private void showAlert(String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Account Creation");
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}

}
