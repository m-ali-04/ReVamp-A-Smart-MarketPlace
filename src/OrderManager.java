package application;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private List<Order> orders;

    public OrderManager() {
        orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Order findOrderById(int id) {
        for (Order order : orders) {
            if (order.getOrder_id() == id) {
                return order;
            }
        }
        return null;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders); // Return a copy to avoid accidental modifications
    }

    // Add methods to update or remove orders as needed
}
