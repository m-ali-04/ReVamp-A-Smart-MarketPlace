package application;



import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static User loggedInUser; // Static instance to store the logged-in user
    private static int selectedItemId;
    private static List<OrderItem> cart = new ArrayList<>(); // Initialize cart to avoid null pointer exceptions
    private static int currentOrderId = 1;
    private static List<Order> orders = new ArrayList<>(); // List to store orders
    private static int retailerId;
    private static ServiceRequest SR;
    
    public static int getRetailerId() {
		return retailerId;
	}

	public static void setRetailerId(int retailerId) {
		SessionManager.retailerId = retailerId;
	}

	public static ServiceRequest getSR() {
		return SR;
	}

	public static void setSR(ServiceRequest sR) {
		SR = sR;
	}

	public static void setOrders(List<Order> orders) {
		SessionManager.orders = orders;
	}

	public static int getCurrentOrderId() {
        return currentOrderId;
    }

    public static void setCurrentOrderId(int currentOrderId) {
        SessionManager.currentOrderId = currentOrderId;
    }

    // Getter for the cart
    public static List<OrderItem> getCart() {
        return cart;
    }

    // Setter for the cart (optional)
    public static void setCart(List<OrderItem> cart) {
        SessionManager.cart = cart;
    }

    // Add an item to the cart
    public static void addToCart(OrderItem orderItem) {
        cart.add(orderItem);
    }

    // Remove an item from the cart
    public static void removeFromCart(OrderItem orderItem) {
        cart.remove(orderItem);
    }

    // Clear the cart (optional)
    public static void clearCart() {
        cart.clear();
    }

    public static int getSelectedItemId() {
        return selectedItemId;
    }

    public static void setSelectedItemId(int itemId) {
        selectedItemId = itemId;
    }

    // Getter for the logged-in user
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    // Setter for the logged-in user
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    // Clear the session (optional, for logout purposes)
    public static void clearSession() {
        loggedInUser = null;
        selectedItemId = 0;
        cart.clear();
    }

    // Create a new order
    public static void createOrder() {
        if (loggedInUser == null) {
            throw new IllegalStateException("User is not logged in");
        }

        // Calculate the total amount from cart items
        float totalAmount = 0;
        for (OrderItem item : cart) {
            totalAmount += item.getTotal(); // Assuming OrderItem has a method to get the total price
        }

        // Create the order
        Order newOrder = new Order(currentOrderId, loggedInUser.getUser_id(), totalAmount, java.time.LocalDate.now());
     
        newOrder.setOrder_items(new ArrayList<>(cart)); // Add cart items to the order

        // Add the new order to the orders list
        orders.add(newOrder);

        // Clear the cart after placing the order
        clearCart();

        // Increment the current order ID for the next order
        incrementOrderId();
    }

    // Get the list of all orders
    public static List<Order> getOrders() {
        return orders;
    }

    private static void incrementOrderId() {
        currentOrderId++;
    }
}
