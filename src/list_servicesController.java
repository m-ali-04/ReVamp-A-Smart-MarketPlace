package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.lang.classfile.components.ClassPrinter.Node;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class list_servicesController {

    @FXML
    private TextField service_desc;
    @FXML
    private TextField service_provider;
    @FXML
    private Button services;
    @FXML
    private Button bc;
    @FXML
    private ComboBox<String> service_type;
    @FXML
    private TextField rate;

    // Initialize the ComboBox with predefined values
    @FXML
    public void initialize() {
        ObservableList<String> serviceTypes = FXCollections.observableArrayList("Evaluator", "Refurbisher");
        service_type.setItems(serviceTypes);
    }

    @FXML
    public void proceed(ActionEvent event) {
        String type = service_type.getValue();
        String providerText = service_provider.getText(); // This will now be an integer provider ID
        String description = service_desc.getText();
        String rateText = rate.getText();

        // Validate inputs
        if (type == null || providerText.isEmpty() || description.isEmpty() || rateText.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Input Validation");
            alert.setHeaderText(null);
            alert.setContentText("All fields are required. Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        // Validate service provider ID
        int providerId;
        try {
            providerId = Integer.parseInt(providerText);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid integer for the service provider ID.");
            alert.showAndWait();
            return;
        }

        // Check if the provider exists based on the selected service type (Evaluators or Refurbishers)
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkProviderQuery = "";

            // Conditional query based on service type
            if ("Evaluator".equals(type)) {
                checkProviderQuery = "SELECT COUNT(*) FROM Evaluators WHERE evaluator_id = ?";
            } else if ("Refurbisher".equals(type)) {
                checkProviderQuery = "SELECT COUNT(*) FROM Refurbishers WHERE refurbisher_id = ?";
            }

            if (checkProviderQuery.isEmpty()) {
                // In case the service type is invalid or not handled
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Service Type");
                alert.setHeaderText(null);
                alert.setContentText("The selected service type is not valid.");
                alert.showAndWait();
                return;
            }

            try (PreparedStatement checkProviderStmt = conn.prepareStatement(checkProviderQuery)) {
                checkProviderStmt.setInt(1, providerId);
                ResultSet rs = checkProviderStmt.executeQuery();

                if (rs.next() && rs.getInt(1) == 0) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Provider Not Found");
                    alert.setHeaderText(null);
                    alert.setContentText("The provided provider ID does not exist in the selected category.");
                    alert.showAndWait();
                    return;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while validating the provider.");
            alert.showAndWait();
            return;
        }

        // Validate rate input
        float rateValue;
        try {
            rateValue = Float.parseFloat(rateText);
            if (rateValue < 0) throw new NumberFormatException(); // Ensure rate is positive
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid positive number for the rate.");
            alert.showAndWait();
            return;
        }

        // Process the service ID and insert the service
        try (Connection conn = DatabaseConnection.getConnection();
        	     PreparedStatement getIdStmt = conn.prepareStatement("SELECT LastUsedId FROM IdTracker WHERE Category = ?");
        	     PreparedStatement updateIdStmt = conn.prepareStatement("UPDATE IdTracker SET LastUsedId = ? WHERE Category = ?")) {

        	    // Fetch current service ID
        	    getIdStmt.setString(1, "ServiceId");  // Specify that we are looking for the "ServiceId" category
        	    ResultSet rs = getIdStmt.executeQuery();
        	    int serviceId = 0;
        	    if (rs.next()) {
        	        serviceId = rs.getInt("LastUsedId");
        	    }

        	    // Increment the service ID
        	    int nextServiceId = serviceId + 1;

        	    // Update the LastUsedId for the "ServiceId" category
        	    updateIdStmt.setInt(1, nextServiceId);   // Set the new LastUsedId
        	    updateIdStmt.setString(2, "ServiceId");  // Set the Category (ServiceId)
        	    updateIdStmt.executeUpdate();

            // Create the Service object
            Service service = new Service(serviceId, type, providerId, description, rateValue, true);
            DBHandler.storeService(service); // Store the service in DB

            // Add the service to the catalogue or any other list
            Catalogue.addService(service);

            // Show confirmation alert
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Service Listed");
            alert.setHeaderText(null);
            alert.setContentText("The service has been successfully listed!");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while adding the service.");
            alert.showAndWait();
        }
    }




    // Event Listener on Button[#bc].onAction
    @FXML
    public void back(ActionEvent event) {
//        int userId = getCurrentUserId();  // Fetch current user ID, implement this method as needed
//
//        // SQL query to check user type
//        String query = "SELECT user_type FROM Users WHERE user_id = ?";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            // Set the user_id parameter
//            stmt.setInt(1, userId);
//
//            // Execute the query to get the user type
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                String userType = rs.getString("user_type");
//
//                // Check user type and load the corresponding window
//                if ("refurbisher".equalsIgnoreCase(userType)) {
//                    // Load refurbisher window
//                    loadFXML(event, "refurbisher.fxml");
//                } else if ("evaluator".equalsIgnoreCase(userType)) {
//                    // Load evaluator window
//                    loadFXML(event, "evaluator.fxml");
//                } else {
//                    // If user type is unknown or invalid, show an error
//                    showError("Unknown user type, unable to navigate.");
//                }
//
//            } else {
//                // If no user found with the provided user_id
//                showError("The current user could not be found in the database.");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showError("An error occurred while retrieving the user type.");
//        }
    }

//    private void loadFXML(ActionEvent event, String fxmlFile) {
//        try {
//            // Load the FXML file
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
//            Parent root = loader.load();
//
//            // Get the current stage from the event's source
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//
//            // Set the new scene and show the stage
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            showError("An error occurred while loading the FXML file.");
//        }
//    }
//
//    private void showError(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setContentText(message);
//        alert.showAndWait();
//    }




}
