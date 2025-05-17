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

public class order_historyController {

    @FXML
    private TableView<OrderHistory> table; // Table to hold OrderHistory objects

    @FXML
    private TableColumn<OrderHistory, String> order_id;

    @FXML
    private TableColumn<OrderHistory, String> cust_id;

    @FXML
    private TableColumn<OrderHistory, String> total_price;

    @FXML
    private TableColumn<OrderHistory, String> order_desc;

    @FXML
    private Button next;

    @FXML
    private Button clear; // Button to clear selection

    @FXML
    private Button back; // Button to go back

    // Sample data
    private ObservableList<OrderHistory> sampleData = FXCollections.observableArrayList(
        new OrderHistory("101", "C001", "300.00", "Electronics Purchase"),
        new OrderHistory("102", "C002", "150.00", "Groceries"),
        new OrderHistory("103", "C003", "500.00", "Clothing"),
        new OrderHistory("104", "C004", "200.00", "Books"),
        new OrderHistory("105", "C005", "350.00", "Furniture")
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
        ObservableList<OrderHistory> selectedOrders = table.getSelectionModel().getSelectedItems();

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
            for (OrderHistory order : selectedOrders) {
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
    public static class OrderHistory {
        private String orderId;
        private String customerId;
        private String totalPrice;
        private String orderDescription;

        public OrderHistory(String orderId, String customerId, String totalPrice, String orderDescription) {
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
