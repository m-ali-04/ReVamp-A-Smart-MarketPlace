package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class LoginPageController {
    @FXML
    private TextField user_name;
    @FXML
    private TextField pass_word;
    @FXML
    private Button login_into;

    private String accountType; // To store the account type after successful login

    // Event Listener on Button[#login_into].onAction
    @FXML
    public void log_into_account(ActionEvent event) {
        String username = user_name.getText();
        String password = pass_word.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.", Alert.AlertType.ERROR);
            return;
        }

        try {
            if (DBHandler.isValidCredentials(username, password)) {
                accountType = DBHandler.getUserType(username);
                
                if (accountType != null) {
                    showAlert("Success", "Logged in as " + accountType, Alert.AlertType.INFORMATION);

                    DBHandler.storeLoggedInUser(username);
                    if ("retailer".equals(accountType))

                    if(accountType.toLowerCase().equals("retailer")) {
                    	int retailerId = DBHandler.getRetailerIdByUserId(SessionManager.getLoggedInUser().getUser_id());

                        // Store the retailer ID in the session manager
                        SessionManager.setRetailerId(retailerId);
                        
                        System.out.println("RETAILER ID: " + SessionManager.getRetailerId());
                    }
                    System.out.println("USERNAME: " + SessionManager.getLoggedInUser().getUsername());
                    loadNextPage(accountType); // Load the corresponding page
                } else {
                    showAlert("Error", "Account type could not be determined.", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Incorrect username or password.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void loadNextPage(String accountType) {
        String fxmlFile = switch (accountType.toLowerCase()) {
            case "customer" -> "customer.fxml";
            case "retailer" -> "retailer.fxml";
            case "refurbisher" -> "refurbisher.fxml";
            case "evaluator" -> "evaluator.fxml";
            default -> null;
        };
        
        

        if (fxmlFile == null) {
            showAlert("Error", "Invalid account type.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Load the next page
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) login_into.getScene().getWindow(); // Get current stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the page: " + fxmlFile, Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
