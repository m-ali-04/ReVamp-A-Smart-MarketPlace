package application;


import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBHandler {
	
	public static void loadUsers(UserManager userManager) {
	    String userQuery = "SELECT * FROM Users";
	    String customerQuery = "SELECT * FROM Customers";
	    String retailerQuery = "SELECT * FROM Retailers";
	    String refurbisherQuery = "SELECT * FROM Refurbishers";
	    String evaluatorQuery = "SELECT * FROM Evaluators";
	    String idTrackerQuery = "SELECT Category, LastUsedId FROM IdTracker";

	    try (Connection conn = DatabaseConnection.getConnection();
	         Statement stmt = conn.createStatement()) {

	        // Load IDs from IdTracker
	        ResultSet idTrackerRs = stmt.executeQuery(idTrackerQuery);
	        while (idTrackerRs.next()) {
	            String category = idTrackerRs.getString("Category");
	            int lastUsedId = idTrackerRs.getInt("LastUsedId");

	            switch (category.toLowerCase()) {
	                case "userid":
	                    userManager.setUID(lastUsedId);
	                    break;
	                case "customerid":
	                    userManager.setCID(lastUsedId);
	                    break;
	                case "retailerid":
	                    userManager.setRID(lastUsedId);
	                    break;
	                case "refurbisherid":
	                    userManager.setRFID(lastUsedId);
	                    break;
	                case "evaluatorid":
	                    userManager.setEID(lastUsedId);
	                    break;
	                
	            }
	        }

	        // Load Users
	        ResultSet userRs = stmt.executeQuery(userQuery);
	        while (userRs.next()) {
	            int userId = userRs.getInt("user_id");
	            String username = userRs.getString("username");
	            String email = userRs.getString("email");
	            String phone = userRs.getString("phone");
	            String userType = userRs.getString("user_type");

	            switch (userType.toLowerCase()) {
		            case "customer":
		                // Load Customer Details
		                try (PreparedStatement ps = conn.prepareStatement(customerQuery + " WHERE user_id = ?")) {
		                    ps.setInt(1, userId);
		                    ResultSet customerRs = ps.executeQuery();
		                    if (customerRs.next()) {
		                        int customerId = customerRs.getInt("customer_id");
		                        String customerType = customerRs.getString("customer_type");
		                        String address = customerRs.getString("address"); // New field
	
		                        // Create Customer with the new address field
		                        Customer customer = new Customer(userId, username, email, phone, customerId, customerType, address);
		                        userManager.addCustomer(customer);
		                    }
		                }
		                break;
	                case "retailer":
	                    // Load Retailer Details
	                    try (PreparedStatement ps = conn.prepareStatement(retailerQuery + " WHERE user_id = ?")) {
	                        ps.setInt(1, userId);
	                        ResultSet retailerRs = ps.executeQuery();
	                        if (retailerRs.next()) {
	                            int retailerId = retailerRs.getInt("retailer_id");
	                            String retailName = retailerRs.getString("retail_name");
	                            float balance = retailerRs.getFloat("balance");
	                            float rating = retailerRs.getFloat("rating");

	                            Retailer retailer = new Retailer(userId, username, email, phone, retailerId, retailName, balance, rating);
	                            userManager.addRetailer(retailer);
	                        }
	                    }
	                    break;

	                case "refurbisher":
	                    // Load Refurbisher Details
	                    try (PreparedStatement ps = conn.prepareStatement(refurbisherQuery + " WHERE user_id = ?")) {
	                        ps.setInt(1, userId);
	                        ResultSet refurbisherRs = ps.executeQuery();
	                        if (refurbisherRs.next()) {
	                            int refurbisherId = refurbisherRs.getInt("refurbisher_id");
	                            double rating = refurbisherRs.getFloat("rating");

	                            Refurbisher refurbisher = new Refurbisher(userId, username, email, phone, refurbisherId, rating);
	                            userManager.addRefurbisher(refurbisher);
	                        }
	                    }
	                    break;

	                case "evaluator":
	                    // Load Evaluator Details
	                    try (PreparedStatement ps = conn.prepareStatement(evaluatorQuery + " WHERE user_id = ?")) {
	                        ps.setInt(1, userId);
	                        ResultSet evaluatorRs = ps.executeQuery();
	                        if (evaluatorRs.next()) {
	                            int evaluatorId = evaluatorRs.getInt("evaluator_id");
	                            double rating = evaluatorRs.getFloat("rating");

	                            Evaluator evaluator = new Evaluator(userId, username, email, phone, evaluatorId, rating);
	                            userManager.addEvaluator(evaluator);
	                        }
	                    }
	                    break;

	                default:
	                    throw new IllegalArgumentException("Unknown user type: " + userType);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to load users: " + e.getMessage());
	    }
	}
	
	public static void loadCatalogue(Catalogue catalogue) {
	    String productQuery = "SELECT * FROM Products"; // Query to fetch all products
	    String idTrackerQuery = "SELECT * FROM IdTracker"; // Query to fetch IDs from IdTracker
	    String serviceQuery = "SELECT * FROM Services"; // Query to fetch all services

	    try (Connection conn = DatabaseConnection.getConnection();
	         Statement stmt = conn.createStatement()) {

	        // Load products
	        ResultSet productRs = stmt.executeQuery(productQuery);

	        while (productRs.next()) {
	            int itemId = productRs.getInt("product_id"); // Assuming the product table has this column
	            String name = productRs.getString("name");
	            String description = productRs.getString("description");
	            float price = productRs.getFloat("price");
	            int quantity = productRs.getInt("stock");
	            boolean isRentable = productRs.getBoolean("rentable");
	            float rentPrice = isRentable ? productRs.getFloat("rent_price") : 0;
	            int retailerId = productRs.getInt("retailer_id");

	            // Create the product using the factory method or constructor
	            Product product = ItemFactory.createProduct(itemId, name, price, description, isRentable, quantity, rentPrice, retailerId);

	            // Add the product to the catalogue
	            catalogue.addProduct(product); // Assuming Catalogue has this method
	        }

	        // Load ID tracker values
	        ResultSet idTrackerRs = stmt.executeQuery(idTrackerQuery);

	        while (idTrackerRs.next()) {
	            String category = idTrackerRs.getString("Category");
	            int lastUsedId = idTrackerRs.getInt("LastUsedId");

	            if ("ItemId".equals(category)) {
	                Catalogue.setItemId(lastUsedId); // Set the static ItemId
	            } else if ("ServiceId".equals(category)) {
	                Catalogue.setServiceId(lastUsedId); // Set the static ServiceId
	            }
	        }

	        // Load services
	        ResultSet serviceRs = stmt.executeQuery(serviceQuery);

	        while (serviceRs.next()) {
	            int serviceId = serviceRs.getInt("service_id");
	            String type = serviceRs.getString("type");
	            int serviceProvider = serviceRs.getInt("service_provider");
	            String description = serviceRs.getString("description");
	            float rate = serviceRs.getFloat("rate");
	            boolean available = serviceRs.getBoolean("available");

	            // Create the service using a factory method or constructor
	            Service service = new Service(serviceId, type, serviceProvider, description, rate, available);

	            // Add the service to the catalogue
	            catalogue.addService(service); // Assuming Catalogue has this method
	        }

	        System.out.println("Catalogue loaded successfully with products and services.");

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to load catalogue: " + e.getMessage());
	    }
	}
	
	public static void createTables() {
	    String createAuthenticationTable = """
	            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Authentication' AND xtype='U')
	            CREATE TABLE Authentication (
	                username VARCHAR(100) PRIMARY KEY,
	                password VARCHAR(255) NOT NULL
	            );
	            """;
	    
	    String createServicesTable = """
	    	    IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='services' AND xtype='U')
	    	    CREATE TABLE services (
	    	        service_id INT PRIMARY KEY, -- Unique ID for each service
	    	        type VARCHAR(50) NOT NULL, -- Type of service (e.g., Evaluator, Refurbisher)
	    	        service_provider INT NOT NULL, -- Name of the service provider
	    	        description TEXT NOT NULL, -- Description of the service
	    	        rate FLOAT NOT NULL, -- Service rate
	    	        available BIT NOT NULL -- Availability status (true/false)
	    	    );
	    	    """;

	    
	    String createUsersTable = """
	            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Users' AND xtype='U')
	            CREATE TABLE Users (
	                user_id INT PRIMARY KEY,
	                username VARCHAR(100) NOT NULL,
	                email VARCHAR(100) NOT NULL,
	                phone VARCHAR(15) NOT NULL,
	                user_type VARCHAR(50) NOT NULL
	            );
	            """;

	    String createCustomersTable = """
	            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Customers' AND xtype='U')
	            CREATE TABLE Customers (
	                user_id INT PRIMARY KEY,
	                customer_id INT UNIQUE NOT NULL,
	                customer_type VARCHAR(50) NOT NULL,
	                address VARCHAR(255) NOT NULL,
	                FOREIGN KEY (user_id) REFERENCES Users(user_id)
	            );
	            """;

	    String createRetailersTable = """
	            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Retailers' AND xtype='U')
	            CREATE TABLE Retailers (
	                user_id INT PRIMARY KEY,
	                retailer_id INT UNIQUE NOT NULL,
	                retail_name VARCHAR(100) NOT NULL,
	                balance FLOAT DEFAULT 0,
	                rating FLOAT DEFAULT 0,
	                FOREIGN KEY (user_id) REFERENCES Users(user_id)
	            );
	            """;

	    String createRefurbishersTable = """
	            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Refurbishers' AND xtype='U')
	            CREATE TABLE Refurbishers (
	                user_id INT PRIMARY KEY,
	                refurbisher_id INT UNIQUE NOT NULL,
	                rating FLOAT DEFAULT 0,
	                FOREIGN KEY (user_id) REFERENCES Users(user_id)
	            );
	            """;

	    String createEvaluatorsTable = """
	            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Evaluators' AND xtype='U')
	            CREATE TABLE Evaluators (
	                user_id INT PRIMARY KEY,
	                evaluator_id INT UNIQUE NOT NULL,
	                rating FLOAT DEFAULT 0,
	                FOREIGN KEY (user_id) REFERENCES Users(user_id)
	            );
	            """;

	    String createProductTable = """
	            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Products' AND xtype='U')
	            CREATE TABLE Products (
	                product_id INT PRIMARY KEY,
	                name VARCHAR(100) NOT NULL,
	                price FLOAT NOT NULL,
	                rent_price FLOAT DEFAULT 0,
	                description VARCHAR(255) NOT NULL,
	                rentable BIT,
	                stock INT DEFAULT 0,
	                retailer_id INT 
	            );
	            """;

	    // Create Orders Table
	    String createOrdersTable = """
	            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Orders' AND xtype='U')
	            CREATE TABLE Orders (
	                order_id INT PRIMARY KEY,
	                customer_id INT,
	                amount FLOAT NOT NULL,
	                order_date DATE NOT NULL,
	                FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
	            );
	            """;

	    // Create OrderItems Table
	    String createOrderItemsTable = """
	    	    IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='OrderItems' AND xtype='U')
	    	    CREATE TABLE OrderItems (
	    	        order_item_id INT PRIMARY KEY,         -- Unique ID for each order item
	    	        order_id INT,                          -- Foreign key referencing Orders table
	    	        product_id INT,                        -- Foreign key referencing Products table
	    	        quantity INT NOT NULL,                 -- Quantity of the product
	    	        rent BIT NOT NULL,                     -- Whether the item is rented (1 for true, 0 for false)
	    	        days_of_rent INT DEFAULT 0,            -- Number of days for which the item is rented
	    	        total_price FLOAT NOT NULL,            -- Total price for this order item
	    	        FOREIGN KEY (order_id) REFERENCES Orders(order_id),
	    	        FOREIGN KEY (product_id) REFERENCES Products(product_id)
	    	    );
	    	    """;

	    
	    String createRetailerProductTable = """
	            IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='RetailerProduct' AND xtype='U')
	            CREATE TABLE RetailerProduct (
	                product_id INT,
	                retailer_id INT,
	                PRIMARY KEY (product_id, retailer_id),
	                FOREIGN KEY (product_id) REFERENCES Products(product_id),
	                FOREIGN KEY (retailer_id) REFERENCES Retailers(retailer_id)
	            );
	            """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         Statement stmt = conn.createStatement()) {

	        stmt.execute(createUsersTable);
	        stmt.execute(createCustomersTable);
	        stmt.execute(createRetailersTable);
	        stmt.execute(createRefurbishersTable);
	        stmt.execute(createEvaluatorsTable);
	        stmt.execute(createAuthenticationTable);
	        stmt.execute(createServicesTable);
	        stmt.execute(createProductTable);
	        stmt.execute(createOrdersTable);      // Create Orders Table
	        stmt.execute(createOrderItemsTable);  // Create OrderItems Table
	        stmt.execute(createRetailerProductTable);
	        
	        System.out.println("Tables created successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to create tables: " + e.getMessage());
	    }
	}

	public static boolean addUserAuthentication(String username, String password) {
	    String insertQuery = "INSERT INTO Authentication (username, password) VALUES (?, ?)";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
	        
	        stmt.setString(1, username);
	        stmt.setString(2, password);
	        stmt.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public static boolean isUsernameUnique(String username) {
	    String checkQuery = "SELECT COUNT(*) FROM Authentication WHERE username = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
	        
	        stmt.setString(1, username);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) == 0; // Return true if username is unique
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public static boolean validateUsernamePassword(String username, String password) {
	    String checkQuery = "SELECT password FROM Authentication WHERE username = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
	        
	        stmt.setString(1, username);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            String storedPassword = rs.getString("password");
	            return storedPassword.equals(password); // Check if passwords match
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // Return false if username does not exist or passwords do not match
	}
	
	public static void storeLoggedInUser(String username) {
	    // Query to retrieve user details from 'users' table by matching the username
	    String query = "SELECT user_id, email, phone FROM users WHERE username = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	        
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            int userId = rs.getInt("user_id");
	            String email = rs.getString("email");
	            String phone = rs.getString("phone");
	            
	            // Create a new User object and populate it with retrieved information
	            User loggedInUser = new User(userId, username, email, phone);
	            
	            // Set the newly created user in the SessionManager
	            SessionManager.setLoggedInUser(loggedInUser);
	            
	            System.out.println("User logged in and session updated: " + loggedInUser);
	        } else {
	            System.out.println("User not found in 'users' table for username: " + username);
	        }
	    } catch (SQLException e) {
	        System.out.println("Error occurred while retrieving and setting logged-in user: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	public static String getUserType(String username) {
	    String query = """
	        SELECT user_type FROM Users 
	        INNER JOIN Authentication ON Users.username = Authentication.username 
	        WHERE Authentication.username = ?
	    """;
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, username);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("user_type");
	        }
	        return null; // If no record is found
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to get user type: " + e.getMessage());
	    }
	}

	public static boolean isValidCredentials(String username, String password) {
	    String query = "SELECT * FROM Authentication WHERE username = ? AND password = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, username);
	        stmt.setString(2, password);
	        ResultSet rs = stmt.executeQuery();
	        return rs.next(); // Returns true if a record is found
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to validate credentials: " + e.getMessage());
	    }
	}

	public static boolean storeUser(User user) {
	    Connection conn = null;
	    PreparedStatement userStmt = null;
	    PreparedStatement specificStmt = null;
	    PreparedStatement idTrackerStmt = null;

	    try {
	        // Get a connection
	        conn = DatabaseConnection.getConnection();

	        // Begin a transaction
	        conn.setAutoCommit(false);

	        // Insert into the general User table
	        String userQuery = "INSERT INTO Users (user_id, username, email, phone, user_type) VALUES (?, ?, ?, ?, ?)";
	        userStmt = conn.prepareStatement(userQuery);
	        
	        // Log the values being inserted into the Users table
	        System.out.println("Attempting to insert into Users table with:");
	        System.out.println("user_id = " + user.getUser_id());
	        System.out.println("username = " + user.getUsername());
	        System.out.println("email = " + user.getEmail_address());
	        System.out.println("phone = " + user.getPhone_number());
	        System.out.println("user_type = " + user.getClass().getSimpleName().toLowerCase());

	        userStmt.setInt(1, user.getUser_id());
	        userStmt.setString(2, user.getUsername());
	        userStmt.setString(3, user.getEmail_address());
	        userStmt.setString(4, user.getPhone_number());
	        userStmt.setString(5, user.getClass().getSimpleName().toLowerCase());

	        userStmt.executeUpdate();

	        // Insert into type-specific table
	        String specificQuery = getSpecificInsertQuery(user.getClass().getSimpleName().toLowerCase());
	        specificStmt = conn.prepareStatement(specificQuery);

	        if (user instanceof Customer) {
	        	    Customer customer = (Customer) user;

	        	    // Log the values being inserted into the Customer table
	        	    System.out.println("Attempting to insert into Customer table with:");
	        	    System.out.println("user_id = " + customer.getUser_id());
	        	    System.out.println("customer_id = " + customer.getCustomer_id());
	        	    System.out.println("customer_type = " + customer.getCustomer_type());
	        	    System.out.println("address = " + customer.getAddress()); // Log new field

	        	    specificStmt.setInt(1, customer.getUser_id());
	        	    specificStmt.setInt(2, customer.getCustomer_id());
	        	    specificStmt.setString(3, customer.getCustomer_type());
	        	    specificStmt.setString(4, customer.getAddress()); // Bind new field
	        	}
	         else if (user instanceof Retailer) {
	            Retailer retailer = (Retailer) user;

	            // Log the values being inserted into the Retailer table
	            System.out.println("Attempting to insert into Retailer table with:");
	            System.out.println("user_id = " + retailer.getUser_id());
	            System.out.println("retail_id = " + retailer.getRetail_id());
	            System.out.println("retail_name = " + retailer.getRetail_name());
	            System.out.println("balance = " + retailer.getBalance());
	            System.out.println("rating = " + retailer.getRating());

	            specificStmt.setInt(1, retailer.getUser_id());
	            specificStmt.setInt(2, retailer.getRetail_id());
	            specificStmt.setString(3, retailer.getRetail_name());
	            specificStmt.setFloat(4, retailer.getBalance());
	            specificStmt.setFloat(5, retailer.getRating());
	        } else if (user instanceof Refurbisher) {
	            Refurbisher refurbisher = (Refurbisher) user;

	            // Log the values being inserted into the Refurbisher table
	            System.out.println("Attempting to insert into Refurbisher table with:");
	            System.out.println("user_id = " + refurbisher.getUser_id());
	            System.out.println("refurbisher_id = " + refurbisher.getRefurbisher_id());
	            System.out.println("rating = " + refurbisher.getRating());

	            specificStmt.setInt(1, refurbisher.getUser_id());
	            specificStmt.setInt(2, refurbisher.getRefurbisher_id());
	            specificStmt.setDouble(3, refurbisher.getRating());
	        } else if (user instanceof Evaluator) {
	            Evaluator evaluator = (Evaluator) user;

	            // Log the values being inserted into the Evaluator table
	            System.out.println("Attempting to insert into Evaluator table with:");
	            System.out.println("user_id = " + evaluator.getUser_id());
	            System.out.println("evaluator_id = " + evaluator.getEvaluator_id());
	            System.out.println("rating = " + evaluator.getRating());

	            specificStmt.setInt(1, evaluator.getUser_id());
	            specificStmt.setInt(2, evaluator.getEvaluator_id());
	            specificStmt.setDouble(3, evaluator.getRating());
	        } else {
	            throw new IllegalArgumentException("Invalid user type!");
	        }

	        specificStmt.executeUpdate();

	     // Now, update the IdTracker table for 'UserId'
	        String updateIdQuery = "UPDATE IdTracker SET LastUsedId = LastUsedId + 1 WHERE Category = 'UserId'";
	        idTrackerStmt = conn.prepareStatement(updateIdQuery);
	        idTrackerStmt.executeUpdate();

	        // Dynamically update based on the user type
	        updateIdQuery = "UPDATE IdTracker SET LastUsedId = LastUsedId + 1 WHERE Category = ?";
	        idTrackerStmt = conn.prepareStatement(updateIdQuery);

	        String userType = user.getClass().getSimpleName().toLowerCase();
	        switch (userType) {
	            case "customer":
	                idTrackerStmt.setString(1, "CustomerId");
	                break;
	            case "retailer":
	                idTrackerStmt.setString(1, "RetailerId");
	                break;
	            case "refurbisher":
	                idTrackerStmt.setString(1, "RefurbisherId");
	                break;
	            case "evaluator":
	                idTrackerStmt.setString(1, "EvaluatorId");
	                break;
	            default:
	                throw new IllegalArgumentException("Invalid user type for updating IdTracker!");
	        }
	        idTrackerStmt.executeUpdate();
	        conn.commit();

	        return true;


	    } catch (SQLException e) {
	        // Rollback in case of failure
	        if (conn != null) {
	            try {
	                conn.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        try {
	            if (specificStmt != null) specificStmt.close();
	            if (userStmt != null) userStmt.close();
	            if (idTrackerStmt != null) idTrackerStmt.close();
	            if (conn != null) conn.setAutoCommit(true);
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

    private static String getSpecificInsertQuery(String userType) {
        switch (userType) {
            case "customer":
                return "INSERT INTO Customers (user_id, customer_id, customer_type, address) VALUES (?, ?, ?, ?)";
            case "retailer":
                return "INSERT INTO Retailers (user_id, retailer_id, retail_name, balance, rating) VALUES (?, ?, ?, ?, ?)";
            case "refurbisher":
                return "INSERT INTO Refurbishers (user_id, refurbisher_id, rating) VALUES (?, ?, ?)";
            case "evaluator":
                return "INSERT INTO Evaluators (user_id, evaluator_id, rating) VALUES (?, ?, ?)";
            default:
                throw new IllegalArgumentException("Invalid user type!");
        }
    }
    
    public static void updateLastIdInDatabase(String category, int newValue) {
        String checkQuery = "SELECT COUNT(*) FROM IdTracker WHERE Category = ?";
        String updateQuery = "UPDATE IdTracker SET LastUsedId = ? WHERE Category = ?";
        String insertQuery = "INSERT INTO IdTracker (Category, LastUsedId) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            
            checkStmt.setString(1, category);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // If the category exists, update the LastUsedId
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, newValue);
                    updateStmt.setString(2, category);
                    updateStmt.executeUpdate();
                }
            } else {
                // If the category doesn't exist, insert a new record
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, category);
                    insertStmt.setInt(2, newValue);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update last used ID in the database: " + e.getMessage());
        }
    }

    public static void storeProduct(Product product) {
        // SQL insert query for adding a new product to the Products table
        String insertProductQuery = """
            INSERT INTO Products (name, description, price, stock, rentable, rent_price, product_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertProductQuery)) {

            // Set parameters in the PreparedStatement based on the product's properties
        	ps.setInt(7, product.getProduct_id());
            ps.setString(1, product.getName()); // Name of the product
            ps.setString(2, product.getDesc()); // Description of the product
            ps.setFloat(3, product.getPrice()); // Price of the product
            ps.setInt(4, product.getStock()); // Quantity of the product in stock
            ps.setBoolean(5, product.isRent()); // Whether the product is rentable
            ps.setFloat(6, product.getRent_price()); // Rent price (if rentable)

            // Execute the insert query
            ps.executeUpdate();

            System.out.println("Product stored successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store product: " + e.getMessage());
        }
    }

    public static void loadCustomerOrders() {
        // Get the logged-in user's ID
        int userId = SessionManager.getLoggedInUser().getUser_id();

        // SQL query to load orders for the current logged-in user (matching user_id)
        String sql = "SELECT order_id, customer_id, amount, order_date FROM Orders WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the logged-in user's ID as the customer_id in the query
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                // Clear the previous orders in session
                SessionManager.getOrders().clear();

                // Process the result set
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int customerId = rs.getInt("customer_id");
                    float amount = rs.getFloat("amount");
                    Date orderDate = rs.getDate("order_date");

                    // Create an Order object and add it to the list in SessionManager
                    Order order = new Order(orderId, customerId, amount, orderDate.toLocalDate());
                    SessionManager.getOrders().add(order);
                }

                System.out.println("Orders loaded successfully for user ID " + userId);
            } catch (SQLException e) {
                System.err.println("Error processing the result set: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Error loading customer orders: " + e.getMessage());
        }
    }
    
    public static void storeOrder() {
        System.out.println("DEBUG: Starting storeOrder function");

        int customerId = DBHandler.getCustomerIdByUserId(SessionManager.getLoggedInUser().getUser_id());
        System.out.println("DEBUG: Retrieved customer ID: " + customerId);

        List<OrderItem> cartItems = SessionManager.getCart();
        System.out.println("DEBUG: Retrieved cart items: " + cartItems);

        float totalAmount = 0;
        for (OrderItem item : cartItems) {
            totalAmount += item.getTotal();
        }
        System.out.println("DEBUG: Calculated total order amount: " + totalAmount);

        LocalDate orderDate = LocalDate.now();
        System.out.println("DEBUG: Generated order date: " + orderDate);

        String getNextOrderIdSQL = "SELECT LastUsedId FROM IdTracker WHERE Category = 'OrderId'";
        String updateNextOrderIdSQL = "UPDATE IdTracker SET LastUsedId = ? WHERE Category = 'OrderId'";
        String insertOrderSQL = "INSERT INTO Orders (order_id, customer_id, amount, order_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("DEBUG: Database connection established");

            conn.setAutoCommit(false);

            int orderId = 0;
            try (PreparedStatement getOrderIdStmt = conn.prepareStatement(getNextOrderIdSQL);
                 ResultSet resultSet = getOrderIdStmt.executeQuery()) {

                if (resultSet.next()) {
                    orderId = resultSet.getInt("LastUsedId");
                    System.out.println("DEBUG: Fetched current order ID: " + orderId);

                    try (PreparedStatement updateOrderIdStmt = conn.prepareStatement(updateNextOrderIdSQL)) {
                        updateOrderIdStmt.setInt(1, orderId + 1);
                        updateOrderIdStmt.executeUpdate();
                        System.out.println("DEBUG: Updated IdTracker with next order ID: " + (orderId + 1));
                    }
                } else {
                    conn.rollback();
                    System.err.println("ERROR: Order ID not found in IdTracker");
                    throw new SQLException("Order ID not found in IdTracker");
                }
            }

            try (PreparedStatement insertOrderStmt = conn.prepareStatement(insertOrderSQL)) {
                insertOrderStmt.setInt(1, orderId);
                insertOrderStmt.setInt(2, customerId);
                insertOrderStmt.setFloat(3, totalAmount);
                insertOrderStmt.setDate(4, Date.valueOf(orderDate));
                int rowsInserted = insertOrderStmt.executeUpdate();

                if (rowsInserted == 0) {
                    conn.rollback();
                    System.err.println("ERROR: Failed to insert order");
                    throw new SQLException("Creating order failed, no rows affected");
                }

                System.out.println("DEBUG: Order inserted successfully with ID: " + orderId);

                for (OrderItem item : cartItems) {
                    boolean success = storeOrderItem(item, orderId, conn);
                    System.out.println("DEBUG: Storing order item result: " + success);

                    if (!success) {
                        conn.rollback();
                        System.err.println("ERROR: Failed to store order item");
                        throw new SQLException("Failed to store order item");
                    }
                }

                conn.commit();
                System.out.println("DEBUG: Order and all items stored successfully");
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("ERROR: Exception during order storage: " + e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Exception in database transaction: " + e.getMessage());
        }
    }
    
    public static boolean storeOrderItem(OrderItem orderItem, int orderId, Connection conn) {
        System.out.println("DEBUG: Starting storeOrderItem function for order ID: " + orderId);

        String getIdSql = "SELECT LastUsedId FROM IdTracker WHERE Category = 'OrderItemId'";
        String updateIdSql = "UPDATE IdTracker SET LastUsedId = ? WHERE Category = 'OrderItemId'";
        String insertOrderItemSql = """
            INSERT INTO OrderItems (order_item_id, order_id, product_id, quantity, rent, days_of_rent, total_price)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (conn) {
            System.out.println("DEBUG: Database connection established");

            conn.setAutoCommit(false);

            try (PreparedStatement getIdStmt = conn.prepareStatement(getIdSql);
                 ResultSet rs = getIdStmt.executeQuery()) {

                if (!rs.next()) {
                    conn.rollback();
                    System.err.println("ERROR: IdTracker entry for OrderItemId not found");
                    throw new SQLException("IdTracker entry for OrderItemId not found");
                }

                int orderItemId = rs.getInt("LastUsedId") + 1;
                System.out.println("DEBUG: Fetched and incremented order item ID: " + orderItemId);

                try (PreparedStatement updateIdStmt = conn.prepareStatement(updateIdSql)) {
                    updateIdStmt.setInt(1, orderItemId);
                    updateIdStmt.executeUpdate();
                    System.out.println("DEBUG: Updated IdTracker for OrderItemId");
                }

                try (PreparedStatement insertOrderItemStmt = conn.prepareStatement(insertOrderItemSql)) {
                    insertOrderItemStmt.setInt(1, orderItemId);
                    insertOrderItemStmt.setInt(2, orderId);
                    insertOrderItemStmt.setInt(3, orderItem.getProduct().getProduct_id());
                    insertOrderItemStmt.setInt(4, orderItem.getQty());
                    insertOrderItemStmt.setBoolean(5, orderItem.isRent());
                    insertOrderItemStmt.setInt(6, orderItem.getDays_of_rent());
                    insertOrderItemStmt.setFloat(7, orderItem.getTotal());

                    int rowsInserted = insertOrderItemStmt.executeUpdate();

                    if (rowsInserted == 0) {
                        conn.rollback();
                        System.err.println("ERROR: Failed to insert OrderItem");
                        throw new SQLException("Failed to insert OrderItem");
                    }

                    conn.commit();
                    System.out.println("DEBUG: OrderItem successfully stored with ID: " + orderItemId);
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("ERROR: Exception during OrderItem storage: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Database connection or transaction issue: " + e.getMessage());
            return false;
        }
    }

    public static void storeRetailerProduct(int productId, int retailerId) {
        String insertRetailerProduct = """
                INSERT INTO RetailerProduct (product_id, retailer_id)
                VALUES (?, ?);
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertRetailerProduct)) {

            // Set the values for the retailer-product relationship
            pstmt.setInt(1, productId);
            pstmt.setInt(2, retailerId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store retailer-product relationship: " + e.getMessage());
        }
    }

    public static int getRetailerIdByUserId(int userId) {
        String query = "SELECT retailer_id FROM retailers WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("retailer_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if no retailer ID found
    }
    
    public static int getCustomerIdByUserId(int userId) {
        String query = "SELECT customer_id FROM customers WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("customer_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if no retailer ID found
    }

    public static void storeService(Service service) {
        // Get the current user's user_id
        int currentUserId = SessionManager.getLoggedInUser().getUser_id();  // Implement this method to retrieve the current user's ID

        // SQL query to insert the service into the services table
        String insertServiceQuery = "INSERT INTO Services (service_id, Type, Service_provider, Description, Rate, Available) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertServiceQuery)) {

            // Set the parameters for the insert statement
            stmt.setInt(1, service.service_id);         // ServiceId
            stmt.setString(2, service.type);            // Type
            stmt.setInt(3, currentUserId);              // ServiceProvider (replaced with current user's user_id)
            stmt.setString(4, service.desc);            // Description
            stmt.setFloat(5, service.rate);             // Rate
            stmt.setBoolean(6, service.available);      // Available

            // Execute the insert query
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Service stored successfully.");
            } else {
                System.out.println("Failed to store the service.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store the service: " + e.getMessage());
        }
    }
}
