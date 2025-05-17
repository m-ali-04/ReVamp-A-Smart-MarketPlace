package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database URL with username and password already included
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Revamp;user=aliwhaa;password=aliwhaa;encrypt=false;";

    // Method to establish and return a connection
    public static Connection getConnection() throws SQLException {
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish and return the connection
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("SQL Server JDBC Driver not found.");
        }
    }
}
