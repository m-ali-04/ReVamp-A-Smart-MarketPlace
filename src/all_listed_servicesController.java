package application;

import java.io.IOException;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class all_listed_servicesController {

    @FXML
    private TableView<Service> table; // Table to hold Service objects

    @FXML
    private TableColumn<Service, String> order_id;

    @FXML
    private TableColumn<Service, String> ser_name;

    @FXML
    private TableColumn<Service, String> total_price;

    @FXML
    private TableColumn<Service, String> order_desc;

    @FXML
    private Button next;

    @FXML
    private Button clear; // Button to clear selection

    @FXML
    private Button back; // Button to go back

    // Observable list for storing filtered services
    private ObservableList<Service> serviceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configure TableView columns
        order_id.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        ser_name.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        order_desc.setCellValueFactory(new PropertyValueFactory<>("orderDescription"));

        // Load services based on the refurbisher ID
        loadServicesForRefurbisher();
        
        // Add filtered services to the TableView
        table.setItems(serviceList);

        // Enable multiple selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadServicesForRefurbisher() {
        int refurbisherId = SessionManager.getLoggedInUser().getUser_id(); // Fetch refurbisher ID based on user ID
        String query = "SELECT * FROM Services WHERE service_provider = ?"; // Assuming services have a refurbisher_id column

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the refurbisher ID parameter
            stmt.setInt(1, refurbisherId);

            // Execute the query to get the services
            ResultSet rs = stmt.executeQuery();

            // Clear existing data in the service list
            serviceList.clear();

            // Populate the list with services
            while (rs.next()) {
                String orderId = rs.getString("order_id");
                String serviceName = rs.getString("service_name");
                String totalPrice = rs.getString("total_price");
                String orderDescription = rs.getString("order_description");

                serviceList.add(new Service(orderId, serviceName, totalPrice, orderDescription));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("An error occurred while loading services.");
        }
    }

    private int getRefurbisherIdFromUser() {
        // Fetch the current user's ID and retrieve the refurbisher ID from the refurbisher table
        int userId = SessionManager.getLoggedInUser().getUser_id(); // Implement getCurrentUserId() based on your app's authentication system
        int refurbisherId = -1;

        String query = "SELECT refurbisher_id FROM Refurbishers WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the user ID parameter
            stmt.setInt(1, userId);

            // Execute the query to get the refurbisher ID
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                refurbisherId = rs.getInt("refurbisher_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("An error occurred while fetching refurbisher ID.");
        }
        System.out.println("REFURBISHER ID: " + refurbisherId);
        return refurbisherId;
    }

    // Event handler for the "Clear Selection" button
    @FXML
    public void clear_selection(ActionEvent event) {
        // Clear all selected rows in the TableView
        table.getSelectionModel().clearSelection();
        
        // Show alert when selections are cleared
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Selection Cleared");
        alert.setHeaderText(null);
        alert.setContentText("All selections have been cleared.");
        alert.showAndWait();
    }

    // Event handler for the "Proceed" button
    @FXML
    public void proceed(ActionEvent event) {
        // Get the selected services
        ObservableList<Service> selectedServices = table.getSelectionModel().getSelectedItems();

        if (selectedServices.isEmpty()) {
            // Show alert if no services are selected
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("No services selected.");
            alert.showAndWait();
        } else {
            // Show alert with details of selected services
            StringBuilder selectedServiceDetails = new StringBuilder("Selected services:\n");
            for (Service service : selectedServices) {
                selectedServiceDetails.append(String.format("Order ID: %s, Service Name: %s, Total Price: %s, Description: %s\n",
                        service.getOrderId(), service.getServiceName(), service.getTotalPrice(), service.getOrderDescription()));
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selected Services");
            alert.setHeaderText(null);
            alert.setContentText(selectedServiceDetails.toString());
            alert.showAndWait();
        }
    }

    // Event handler for the "Back" button
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) back.getScene().getWindow(); 
        stage.close(); 
        
        Stage primaryStage = new Stage(); 
        Parent root = FXMLLoader.load(getClass().getResource("Refurbisher.fxml")); 
        Scene scene = new Scene(root); 
        primaryStage.setScene(scene); 
        primaryStage.show();
    }

    // Data model for table rows
    public static class Service {
        private String orderId;
        private String serviceName;
        private String totalPrice;
        private String orderDescription;

        public Service(String orderId, String serviceName, String totalPrice, String orderDescription) {
            this.orderId = orderId;
            this.serviceName = serviceName;
            this.totalPrice = totalPrice;
            this.orderDescription = orderDescription;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public String getOrderDescription() {
            return orderDescription;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
