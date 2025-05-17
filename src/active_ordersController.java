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

public class active_ordersController {

    @FXML
    private TableView<Order> table; // Table to hold Order objects

    @FXML
    private TableColumn<Order, String> order_id;

    @FXML
    private TableColumn<Order, String> cust_id;

    @FXML
    private TableColumn<Order, String> total_price;

    @FXML
    private TableColumn<Order, String> order_desc;

    @FXML
    private Button next;

    @FXML
    private Button clear; // Button to clear selection

    @FXML
    private Button back; // Button to go back

    // Sample data
    private ObservableList<Order> sampleData = FXCollections.observableArrayList(
        new Order("101", "C001", "500.00", "Electronics"),
        new Order("102", "C002", "150.00", "Clothing"),
        new Order("103", "C003", "1200.00", "Furniture"),
        new Order("104", "C004", "250.00", "Groceries"),
        new Order("105", "C005", "800.00", "Books")
    );

    @FXML
    public void initialize() {
        // Configure TableView columns
        order_id.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        cust_id.setCellValueFactory(new PropertyValueFactory<>("customerId"));
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
        // Get the selected orders
        ObservableList<Order> selectedOrders = table.getSelectionModel().getSelectedItems();

        if (selectedOrders.isEmpty()) {
            // Show alert if no orders are selected
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("No orders selected.");
            alert.showAndWait();
        } else {
            // Show alert with details of selected orders
            StringBuilder selectedOrderDetails = new StringBuilder("Selected orders:\n");
            for (Order order : selectedOrders) {
                selectedOrderDetails.append(String.format("Order ID: %s, Customer ID: %s, Total Price: %s, Description: %s\n",
                        order.getOrderId(), order.getCustomerId(), order.getTotalPrice(), order.getOrderDescription()));
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selected Orders");
            alert.setHeaderText(null);
            alert.setContentText(selectedOrderDetails.toString());
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
    public static class Order {
        private String orderId;
        private String customerId;
        private String totalPrice;
        private String orderDescription;

        public Order(String orderId, String customerId, String totalPrice, String orderDescription) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.totalPrice = totalPrice;
            this.orderDescription = orderDescription;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public String getOrderDescription() {
            return orderDescription;
        }
    }
}
