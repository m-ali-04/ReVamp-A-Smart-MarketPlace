package application;

import java.io.IOException;

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

public class active_servicesController {

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

    // Sample data
    private ObservableList<Service> sampleData = FXCollections.observableArrayList(
        new Service("201", "Cleaning", "100.00", "Regular cleaning service"),
        new Service("202", "Electric Repair", "250.00", "Fix electrical issues"),
        new Service("203", "Plumbing", "150.00", "Pipe leak fix"),
        new Service("204", "Gardening", "80.00", "Garden maintenance"),
        new Service("205", "Pest Control", "120.00", "Extermination service")
    );

    @FXML
    public void initialize() {
        // Configure TableView columns
        order_id.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        ser_name.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        order_desc.setCellValueFactory(new PropertyValueFactory<>("orderDescription"));

        // Add sample data to the TableView
        table.setItems(sampleData);

        // Enable multiple selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
		 Stage stage = (Stage) back.getScene().getWindow(); stage.close(); 
		 Stage primaryStage = new Stage(); 
		 Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml")); 
		 Scene scene = new Scene(root);
		 //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); 
		 primaryStage.setScene(scene); primaryStage.show();
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
}
