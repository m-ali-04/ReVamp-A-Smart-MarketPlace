package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

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

public class cust_servicesController {

    @FXML
    private TableView<Service> table; // Use Service objects for rows

    @FXML
    private TableColumn<Service, String> ser_id;

    @FXML
    private TableColumn<Service, String> ser_name;

    @FXML
    private TableColumn<Service, String> ser_price;

    @FXML
    private TableColumn<Service, String> ser_desc;

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
		 Parent root = FXMLLoader.load(getClass().getResource("Customer.fxml")); 
		 Scene scene = new Scene(root);
		 //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); 
		 primaryStage.setScene(scene); primaryStage.show();
	}
	
    
    // Sample data
    

	@FXML
	public void initialize() {
	    // Configure TableView columns
	    ser_id.setCellValueFactory(new PropertyValueFactory<>("service_id"));
	    ser_name.setCellValueFactory(new PropertyValueFactory<>("name"));
	    ser_price.setCellValueFactory(new PropertyValueFactory<>("price"));
	    ser_desc.setCellValueFactory(new PropertyValueFactory<>("description"));

	    // Fetch data from the database and populate the TableView
	    ObservableList<Service> services = fetchServicesFromDatabase();

	    // Set the items for the TableView
	    table.setItems(services);

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
    public ObservableList<Service> fetchServicesFromDatabase() {
        ObservableList<Service> serviceList = FXCollections.observableArrayList();
        String sql = "SELECT service_id, type, rate, description FROM services";  // Adjust the query if necessary

        try (Connection conn = DatabaseConnection.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("service_id");
                String name = rs.getString("type");
                float price = rs.getFloat("rate");
                String description = rs.getString("description");

                Service service = new Service(id, name, price, description);
                serviceList.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Unable to fetch services");
            alert.setContentText("An error occurred while fetching data from the database.");
            alert.showAndWait();
        }

        return serviceList;
    }



    public class Service {
        private int service_id;  // Assuming the column name is `service_id`
        private String name;     // Assuming the column name is `name`
        private float price;     // Assuming the column name is `price`
        private String description;  // Assuming the column name is `description`

        public Service(int service_id, String name, float price, String description) {
            this.service_id = service_id;
            this.name = name;
            this.price = price;
            this.description = description;
        }

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

		public int getService_provider() {
			// TODO Auto-generated method stub
			return 0;
		}
    }

    @FXML
    public void proceed(ActionEvent event) {
//        // Get the selected items
//        ObservableList<Service> selectedItems = table.getSelectionModel().getSelectedItems();
//
//        if (selectedItems.isEmpty()) {
//            // Show alert if no items are selected
//            Alert alert = new Alert(AlertType.WARNING);
//            alert.setTitle("No Selection");
//            alert.setHeaderText(null);
//            alert.setContentText("No services selected.");
//            alert.showAndWait();
//        } else {
//            // Show alert with details of selected items
//            StringBuilder selectedItemDetails = new StringBuilder("Selected services:\n");
//            for (Service service : selectedItems) {
//                selectedItemDetails.append(String.format("ID: %d, Name: %s, Price: %.2f, Description: %s\n",
//                        service.getService_id(), service.getName(), service.getPrice(), service.getDescription()));
//            }
//
//            Alert alert = new Alert(AlertType.INFORMATION);
//            alert.setTitle("Selected Services");
//            alert.setHeaderText(null);
//            alert.setContentText(selectedItemDetails.toString());
//            alert.showAndWait();
//
//            // Proceed to create a ServiceRequest
//            Service selectedService = selectedItems.get(0);  // Get the first selected service
//
//            // Assuming serviceRequestId and customerId are generated or fetched correctly
//            int serviceRequestId = getNextServiceRequestId();
//
//         // Get the customerId (from logged in user)
//         int customerId = DBHandler.getCustomerIdByUserId(SessionManager.getLoggedInUser().getUser_id());
//
//      // Assuming service provider is part of Service class
//         int provider = selectedItems.get(0).getService_provider();
//
//            // Create a new ServiceRequest object
//            ServiceRequest serviceRequest = new ServiceRequest(
//                selectedService,               // The selected service
//                serviceRequestId,              // The service request ID
//                customerId,                    // The customer ID
//                provider,                      // Service provider ID
//                LocalDate.now(),               // Request date is the current date
//                null,                          // Service date will be selected later
//                selectedService.getPrice(),    // The price of the service
//                false                          // Status is initially false
//            );
//
//            // Store the serviceRequest in a session or relevant object for future use
//            SessionManager.setSR(serviceRequest);
//
//            // Proceed to next page or functionality (e.g., date_picker.fxml)
//            // Example: changeScene("/path/to/date_picker.fxml");
//        }
    }


    public static int getNextServiceRequestId() {
        int nextId = 0;
        String query = "SELECT LastUsedId FROM IdTracker WHERE Category = 'servicerequestid'";

        try (Connection conn = DatabaseConnection.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                nextId = rs.getInt("LastUsedId") + 1; // Increment the current LastUsedId
            }

            // Update the IdTracker table to set the new LastUsedId
            String updateQuery = "UPDATE IdTracker SET LastUsedId = " + nextId + " WHERE Category = 'servicerequestid'";
            stmt.executeUpdate(updateQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextId;
    }


}





