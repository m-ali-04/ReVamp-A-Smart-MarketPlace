package application;


public class UserFactory {

    public static User createUser(String userType, UserManager userManager, String username, String email, String phone, Object... extraParams) {
        int userId = userManager.getUID(); // Retrieve and increment the User ID

        switch (userType.toLowerCase()) {
            case "customer":
                int customerId = userManager.getCID(); // Increment customer-specific ID
                String customerType = extraParams.length > 0 ? (String) extraParams[0] : "Regular"; // Default to "Regular" if not provided
                String address = extraParams.length > 1 ? (String) extraParams[1] : "Unknown Address"; // Default address
                Customer customer = new Customer(userId, username, email, phone, customerId, customerType, address);
                System.out.println("Created Customer: " + customer);
                return customer;

            case "retailer":
                int retailerId = userManager.getRID(); // Increment retailer-specific ID
                String retailName = extraParams.length > 0 ? (String) extraParams[0] : "Default Retailer";
                float balance = extraParams.length > 1 ? (float) extraParams[1] : 0.0f; // Default balance
                float rating = extraParams.length > 2 ? (float) extraParams[2] : 0.0f;  // Default rating
                Retailer retailer = new Retailer(userId, username, email, phone, retailerId, retailName, balance, rating);
                System.out.println("Created Retailer: " + retailer);
                return retailer;

            case "refurbisher":
                int refurbisherId = userManager.getRFID(); // Increment refurbisher-specific ID
                Refurbisher refurbisher = new Refurbisher(userId, username, email, phone, refurbisherId, 0.0);
                System.out.println("Created Refurbisher: " + refurbisher);
                System.out.println("ID== " + refurbisher.getRefurbisher_id());
                return refurbisher;

            case "evaluator":
                int evaluatorId = userManager.getEID(); // Increment evaluator-specific ID
                Evaluator evaluator = new Evaluator(userId, username, email, phone, evaluatorId, 0.0);
                System.out.println("Created Evaluator: " + evaluator);
                return evaluator;

            default:
                throw new IllegalArgumentException("Invalid user type!");
        }
    }
}
