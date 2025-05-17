package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class cartController {

    @FXML
    private TextField num_items; // Field to enter the number of items

    @FXML
    private CheckBox rent; // CheckBox for rent option

    @FXML
    private CheckBox purchase; // CheckBox for purchase option

    @FXML
    private TextField duration_rent; // Field to enter rent duration

    @FXML
    private Button proceed; // Button to proceed

    @FXML
    public void initialize() {
        // Add listener to enforce mutual exclusivity between "rent" and "purchase"
        rent.setOnAction(e -> handleRentSelection());
        purchase.setOnAction(e -> handlePurchaseSelection());
    }

    // Ensure only one of "rent" or "purchase" is selected
    private void handleRentSelection() {
        if (rent.isSelected()) {
            purchase.setSelected(false); // Uncheck purchase
            num_items.setDisable(true); // Disable item number field
            duration_rent.setDisable(false); // Enable rent duration field
        } else {
            duration_rent.setDisable(true); // Disable rent duration field when rent is unchecked
        }
    }

    private void handlePurchaseSelection() {
        if (purchase.isSelected()) {
            rent.setSelected(false); // Uncheck rent
            num_items.setDisable(false); // Enable item number field
            duration_rent.setDisable(true); // Disable rent duration field
        } else {
            num_items.setDisable(true); // Disable item number field when purchase is unchecked
        }
    }

    // Event listener for the "proceed" button
    @FXML
    public void proceed_to_items(ActionEvent event) throws IOException {
        try {
            // Validate selection
            if (!rent.isSelected() && !purchase.isSelected()) {
                showAlert(AlertType.WARNING, "Selection Required", "Please select either Rent or Purchase before proceeding.");
                return;
            }

            // Fetch the selected product using the ID stored in SessionManager
            int selectedItemId = SessionManager.getSelectedItemId();
            Product selectedProduct = Catalogue.findProductById(selectedItemId); // Assume `Catalogue` is globally accessible

            if (selectedProduct == null) {
                showAlert(AlertType.ERROR, "Product Not Found", "The selected product could not be found in the catalogue.");
                return;
            }

            // Handle Purchase
            if (purchase.isSelected()) {
                // Validate quantity input
                int quantity = Integer.parseInt(num_items.getText().trim());
                if (quantity <= 0) {
                    showAlert(AlertType.WARNING, "Invalid Quantity", "Please enter a valid quantity greater than 0.");
                    return;
                }

                // Calculate total and show alert
                float totalAmount = selectedProduct.getPrice() * quantity;
                showAlert(AlertType.INFORMATION, "Total Amount", String.format("Total cost for purchase: %.2f", totalAmount));

                // Create and store OrderItem
                OrderItem orderItem = new OrderItem(selectedProduct, quantity);
                SessionManager.addToCart(orderItem);

            } else if (rent.isSelected()) {
                // Validate rental duration input
                int rentalDays = Integer.parseInt(duration_rent.getText().trim());
                if (rentalDays <= 0) {
                    showAlert(AlertType.WARNING, "Invalid Rental Duration", "Please enter a valid rental duration greater than 0.");
                    return;
                }

                // Calculate total and show alert
                float totalAmount = selectedProduct.getRent_price() * rentalDays;
                showAlert(AlertType.INFORMATION, "Total Amount", String.format("Total cost for rent: %.2f", totalAmount));

                // Create and store OrderItem
                OrderItem orderItem = new OrderItem(selectedProduct, 1, rentalDays);
                SessionManager.addToCart(orderItem);
            }

            // Print all items in the cart
            System.out.println("Cart Contents:");
            for (OrderItem item : SessionManager.getCart()) {
                System.out.println(item);
            }

            // Close the stage after proceeding
            Stage stage = (Stage) proceed.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter valid numeric values for quantity or rental duration.");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
        
        Stage stage = (Stage) rent.getScene().getWindow(); stage.close(); 
		 Stage primaryStage = new Stage(); 
		 Parent root = FXMLLoader.load(getClass().getResource("cust_items.fxml")); 
		 Scene scene = new Scene(root);
		 //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); 
		 primaryStage.setScene(scene); primaryStage.show();
    }


    // Utility method to show alerts
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
