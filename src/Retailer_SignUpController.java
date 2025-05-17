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

import javafx.event.ActionEvent;

public class Retailer_SignUpController {
    @FXML
    private TextField retail_name;
    @FXML
    private Button confirm;

    // Event Listener on Button[#confirm].onAction
    @FXML
    public void gotoCoverPage(ActionEvent event) throws IOException {
        // Get the retail name from the form
        String retailName = retail_name.getText();

        if (retailName.isEmpty()) {
            showAlert("Please fill in all fields before proceeding.");
            return;
        }

        // Retrieve the currently logged-in user from the SessionManager
        User loggedInUser = SessionManager.getLoggedInUser();

        // Ensure the user is of type Retailer (you may adjust this based on your session management)
        if (loggedInUser instanceof Retailer) {
            Retailer retailer = (Retailer) loggedInUser;

            // Set the retailer name
            retailer.setRetail_name(retailName);

            // Now store the updated retailer in the database
            if (DBHandler.storeUser(retailer)) {
                // Retrieve the retailer ID from the retailer table based on user_id
                int retailerId = DBHandler.getRetailerIdByUserId(loggedInUser.getUser_id());

                // Store the retailer ID in the session manager
                SessionManager.setRetailerId(retailerId);
                
                System.out.println("RETAILER ID: " + SessionManager.getRetailerId());

                // Inform the user that the account creation was successful
                showAlert("Account successfully created!");
            } else {
                showAlert("Error saving retailer details to the database. Please try again.");
                return;
            }
        } else {
            showAlert("No valid retailer found in session.");
            return;
        }

        // Close the current stage
        Stage stage = (Stage) confirm.getScene().getWindow();
        stage.close();

        // Load the new stage (Cover Page)
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Retailer.fxml"));
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
