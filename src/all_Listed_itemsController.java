package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

public class all_Listed_itemsController {

    @FXML
    private TableView<Item> table;

    @FXML
    private TableColumn<Item, String> item_id;

    @FXML
    private TableColumn<Item, String> item_name;

    @FXML
    private TableColumn<Item, String> item_price;

    @FXML
    private TableColumn<Item, String> item_desc;

    @FXML
    private Button next;

    @FXML
    private Button clear;

    @FXML
    private Button back;

    // ObservableList to hold the filtered products
    private ObservableList<Item> productData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configure TableView columns
        item_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        item_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        item_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        item_desc.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Enable multiple selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Load data for the current retailer
        loadProductsForRetailer();
    }

    private void loadProductsForRetailer() {
        int retailerId = SessionManager.getRetailerId(); // Assume this is set in SessionManager

        try (Connection conn = DatabaseConnection.getConnection(); // Replace with your connection method
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT p.product_id, p.name, p.price, p.description " +
                 "FROM products p " +
                 "JOIN retailerproduct rp ON p.product_id = rp.product_id " +
                 "WHERE rp.retailer_id = ?"
             )) {
        	stmt.setInt(1, retailerId);

            ResultSet rs = stmt.executeQuery();
            List<Item> items = new ArrayList<>();

            while (rs.next()) {
                items.add(new Item(
                    rs.getString("product_1id"),
                    rs.getString("name"),
                    rs.getString("price"),
                    rs.getString("description")
                ));
            }

            productData.setAll(items);
            table.setItems(productData);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load products");
            alert.setContentText("An error occurred while fetching the product list.");
            alert.showAndWait();
        }
    }

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Retailer.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void clear_selection(ActionEvent event) {
        table.getSelectionModel().clearSelection();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Selection Cleared");
        alert.setHeaderText(null);
        alert.setContentText("All selections have been cleared.");
        alert.showAndWait();
    }

    @FXML
    public void proceed(ActionEvent event) {
        ObservableList<Item> selectedItems = table.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("No items selected.");
            alert.showAndWait();
        } else {
            StringBuilder selectedItemDetails = new StringBuilder("Selected items:\n");
            for (Item item : selectedItems) {
                selectedItemDetails.append(String.format(
                    "ID: %s, Name: %s, Price: %s, Description: %s\n",
                    item.getId(), item.getName(), item.getPrice(), item.getDescription()));
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selected Items");
            alert.setHeaderText(null);
            alert.setContentText(selectedItemDetails.toString());
            alert.showAndWait();
        }
    }

    public static class Item {
        private String id;
        private String name;
        private String price;
        private String description;

        public Item(String id, String name, String price, String description) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getDescription() {
            return description;
        }
    }
}
