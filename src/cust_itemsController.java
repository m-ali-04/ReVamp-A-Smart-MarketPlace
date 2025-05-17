


package application;

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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class cust_itemsController {

    @FXML
    private TableView<Product> table; // Use Product objects for rows

    @FXML
    private TableColumn<Product, String> item_id;

    @FXML
    private TableColumn<Product, String> item_name;

    @FXML
    private TableColumn<Product, String> item_price;

    @FXML
    private TableColumn<Product, String> item_desc;

    @FXML
    private Button next;

    @FXML
    private Button clear;

    @FXML
    private Button back;

    // Event handler for the "Back" button
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to initialize the TableView with actual product data
    @FXML
    public void initialize() {
        // Configure TableView columns
        item_id.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        item_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        item_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        item_desc.setCellValueFactory(new PropertyValueFactory<>("desc"));

        // Get the products from Catalogue
        ObservableList<Product> productItems = FXCollections.observableArrayList();

        // Get products from the catalogue (singleton instance)
        for (Product product : Catalogue.getInstance().getProducts()) {
            // Add product directly to the list
            productItems.add(product);
        }

        // Set the products in the TableView
        table.setItems(productItems);

        // Enable multiple selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // Event handler for the "Proceed" button
    @FXML
    public void add_to_cart(ActionEvent event) {
        // Get the selected items
        ObservableList<Product> selectedItems = table.getSelectionModel().getSelectedItems();

        if (selectedItems.size() != 1) {
            // Show alert if no item or multiple items are selected
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invalid Selection");
            alert.setHeaderText(null);
            if (selectedItems.isEmpty()) {
                alert.setContentText("No item selected. Please select one item to add to the cart.");
            } else {
                alert.setContentText("Multiple items selected. Please select only one item to add to the cart.");
            }
            alert.showAndWait();
        } else {
            // Get the single selected product
            Product selectedProduct = selectedItems.get(0);

            // Store the product ID in the SessionManager
            SessionManager.setSelectedItemId(selectedProduct.getProduct_id());
            
            // Load the cart.fxml file
            try {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("cart.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to load the cart page.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void proceed(ActionEvent event) {
        DBHandler.storeOrder();
    }

    
}



