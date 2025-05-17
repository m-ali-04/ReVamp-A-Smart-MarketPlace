package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SignUpPageController {

    public static String cemail;
    public static String cphone;
    public static String ctype;
    @FXML
    private TextField user_name;
    @FXML
    private TextField email;
    @FXML
    private Button signUP;
    @FXML
    private TextField phone;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<String> acc_type;

    @FXML
    public void initialize() {
        acc_type.setItems(FXCollections.observableArrayList("Customer", "Retailer", "Refurbisher", "Evaluator"));
    }

    @FXML
    public void sign_up(ActionEvent event) {
        // Get the input values
        String username = user_name.getText();
        cemail = email.getText();
        cphone = phone.getText();
        String passwordText = password.getText();
        ctype = acc_type.getValue();

        if (username.isEmpty() || cemail.isEmpty() || cphone.isEmpty() || passwordText.isEmpty() || ctype == null) {
            showAlert("Please fill in all required fields.");
            return;
        }

        if (!DBHandler.isUsernameUnique(username)) {
            showAlert("Username is already taken. Please choose another.");
            return;
        }

        if (!DBHandler.addUserAuthentication(username, passwordText)) {
            showAlert("Error while adding authentication details. Try again.");
            return;
        }

        UserManager userManager = UserManager.getInstance();

        switch (ctype) {
        case "Customer":
            Customer customer = (Customer) UserFactory.createUser(
                "customer", userManager, username, cemail, cphone, "Regular", ""
            );

            // Save to session manager without adding to DB
            SessionManager.setLoggedInUser(customer);
            System.out.println("USERNAME: " + SessionManager.getLoggedInUser().getUsername());
            openNewPage("Customer_SignUp.fxml", "Customer Sign-Up");
            break;

        case "Retailer":
            Retailer retailer = (Retailer) UserFactory.createUser(
                "retailer", userManager, username, cemail, cphone, "", 0.0f, 0.0f
            );

            // Save to session manager without adding to DB
            SessionManager.setLoggedInUser(retailer);
            System.out.println("USERNAME: " + SessionManager.getLoggedInUser().getUsername());
            openNewPage("Retailer_SignUp.fxml", "Retailer Sign-Up");
            break;

            case "Refurbisher":
                Refurbisher refurbisher = (Refurbisher) UserFactory.createUser(
                    "refurbisher", userManager, username, cemail, cphone, "Default Company", 5.0f
                );

                if (DBHandler.storeUser(refurbisher)) {
                    // Set the logged-in user
                    SessionManager.setLoggedInUser(refurbisher);
                    System.out.println("USERNAME: " + SessionManager.getLoggedInUser().getUsername());
                    openNewPage("Refurbisher.fxml", "Refurbisher");
                    showAlert("Refurbisher account created successfully!");
                } else {
                    showAlert("Error creating refurbisher account.");
                }
                break;

            case "Evaluator":
                Evaluator evaluator = (Evaluator) UserFactory.createUser(
                    "evaluator", userManager, username, cemail, cphone, "Default Company", 4.0f
                );

                if (DBHandler.storeUser(evaluator)) {
                    // Set the logged-in user
                    SessionManager.setLoggedInUser(evaluator);
                    System.out.println("USERNAME: " + SessionManager.getLoggedInUser().getUsername());
                    openNewPage("Evaluator.fxml", "Evaluator");
                    showAlert("Evaluator account created successfully!");
                } else {
                    showAlert("Error creating evaluator account.");
                }
                break;

            default:
                showAlert("Invalid account type selected.");
        }
    }

    // Helper method to open a new page
    private void openNewPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) signUP.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error loading the new page: " + e.getMessage());
        }
    }

    // Helper method to show alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
