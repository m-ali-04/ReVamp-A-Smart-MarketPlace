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

public class list_itemsController {

    @FXML
    private TableView<Item> table; // Use Item objects for rows

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
    private Button clear; // Button to clear selection

	@FXML 
	private Button back;

	@FXML
	public void goBack(ActionEvent event) throws IOException{
		 Stage stage = (Stage) back.getScene().getWindow(); stage.close(); 
		 Stage primaryStage = new Stage(); 
		 Parent root = FXMLLoader.load(getClass().getResource("Retailer.fxml")); 
		 Scene scene = new Scene(root);
		 //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); 
		 primaryStage.setScene(scene); primaryStage.show();
	}
	
    // Sample data
    private ObservableList<Item> sampleData = FXCollections.observableArrayList(
        new Item("1", "Laptop", "1200.00", "High-performance laptop"),
        new Item("2", "Phone", "800.00", "Latest smartphone"),
        new Item("3", "Headphones", "200.00", "Noise-cancelling headphones"),
        new Item("4", "Monitor", "300.00", "4K UHD monitor"),
        new Item("5", "Keyboard", "100.00", "Mechanical keyboard")
    );

    @FXML
    public void initialize() {
        // Configure TableView columns
        item_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        item_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        item_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        item_desc.setCellValueFactory(new PropertyValueFactory<>("description"));

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
        // Get the selected items
        ObservableList<Item> selectedItems = table.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            // Show alert if no items are selected
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("No items selected.");
            alert.showAndWait();
        } else {
            // Show alert with details of selected items
            StringBuilder selectedItemDetails = new StringBuilder("Selected items:\n");
            for (Item item : selectedItems) {
                selectedItemDetails.append(String.format("ID: %s, Name: %s, Price: %s, Description: %s\n",
                        item.getId(), item.getName(), item.getPrice(), item.getDescription()));
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Selected Items");
            alert.setHeaderText(null);
            alert.setContentText(selectedItemDetails.toString());
            alert.showAndWait();
        }
    }

    // Data model for table rows
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
