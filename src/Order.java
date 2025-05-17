package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
	List<OrderItem> order_items;
    int order_id;
    int customer_id;
    float amount;
    LocalDate date;

    public Order(int order_id, int customer_id, float amount, LocalDate date) {
    	this.order_items = new ArrayList<>();
        this.order_id = order_id;
        this.customer_id = customer_id;
        this.amount = amount;
        this.date = date;
    }

    public List<OrderItem> getOrder_items() {
		return order_items;
	}

	public void setOrder_items(List<OrderItem> order_items) {
		this.order_items = order_items;
	}

	// Getters and setters
    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
