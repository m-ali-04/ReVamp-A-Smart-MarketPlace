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

public class view_ratingsController {

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
    private TableColumn<Item, String> rating;

    @FXML
    private TableColumn<Item, String> rater;

    @FXML
    private Button next;

    @FXML
    private Button clear;

    @FXML
    private Button back;

    // Sample data
    private ObservableList<Item> sampleData = FXCollections.observableArrayList(
            new Item("1", "Laptop", "1200.00", "High-performance laptop", "4.5", "John"),
            new Item("2", "Phone", "800.00", "Latest smartphone", "4.7", "Alice"),
            new Item("3", "Headphones", "200.00", "Noise-cancelling headphones", "4.2", "Bob"),
            new Item("4", "Monitor", "300.00", "4K UHD monitor", "4.8", "Sophia"),
            new Item("5", "Keyboard", "100.00", "Mechanical keyboard", "4.1", "Michael")
    );

    @FXML
    public void initialize() {
        // Configure TableView columns
        item_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        item_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        item_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        item_desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        rater.setCellValueFactory(new PropertyValueFactory<>("rater"));

        // Add sample data to the TableView
        table.setItems(sampleData);

        // Enable multiple selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // Event Listener on Button[#next].onAction
    @FXML
    public void proceed(ActionEvent event) {
        ObservableList<Item> selectedItems = table.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("No items selected. Please select an item to proceed.");
            alert.showAndWait();
        } else {
            StringBuilder selectedDetails = new StringBuilder("Selected items:\n");
            for (Item item : selectedItems) {
                selectedDetails.append(String.format(
                        "ID: %s, Name: %s, Price: %s, Rating: %s, Rater: %s\n",
                        item.getId(), item.getName(), item.getPrice(), item.getRating(), item.getRater()
                ));
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selected Items");
            alert.setHeaderText(null);
            alert.setContentText(selectedDetails.toString());
            alert.showAndWait();
        }
    }

    // Event Listener on Button[#clear].onAction
    @FXML
    public void clear_selection(ActionEvent event) {
        table.getSelectionModel().clearSelection();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Selection Cleared");
        alert.setHeaderText(null);
        alert.setContentText("All selections have been cleared.");
        alert.showAndWait();
    }

    // Event Listener on Button[#back].onAction
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Retailer.fxml")); // Replace with your previous scene's FXML
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Data model for the table
    public static class Item {
        private String id;
        private String name;
        private String price;
        private String description;
        private String rating;
        private String rater;

        public Item(String id, String name, String price, String description, String rating, String rater) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.description = description;
            this.rating = rating;
            this.rater = rater;
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

        public String getRating() {
            return rating;
        }

        public String getRater() {
            return rater;
        }
    }
}
