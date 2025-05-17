package application;

public class OrderItem {
    Product product;  // Product object representing the ordered product
    int qty;  // Quantity of the product (for product orders)
    boolean rent = false;  // Whether the product is rented
    int days_of_rent;  // Number of days for renting
    float total;  // Total price for the order item (calculated based on quantity or rental period)

    // Constructor for order item (Product-based order)
    public OrderItem(Product product, int qty) {
        this.product = product;
        this.qty = qty;
        this.setTotal();  // Initially set total to 0
    }

    // Constructor for rent item (Rental order)
    public OrderItem(Product product, int qty, int days_of_rent) {
        this.product = product;
        this.qty = qty;
        this.rent = true;
        this.days_of_rent = days_of_rent;
        this.setTotal();  // Initially set total to 0
    }
    
    public void setTotal(float total) {
        this.total = total;
    }


    // Getters and Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isRent() {
        return rent;
    }

    public void setRent(boolean rent) {
        this.rent = rent;
    }

    public int getDays_of_rent() {
        return days_of_rent;
    }

    public void setDays_of_rent(int days_of_rent) {
        this.days_of_rent = days_of_rent;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal() {
        if(!rent) calculateTotal();
        else calculateRentTotal();
    }

    // Method to calculate total for product order (based on quantity)
    public void calculateTotal() {
        this.total = product.getPrice() * this.qty;
    }

    // Method to calculate total for rent order (based on daily rent price)
    public void calculateRentTotal() {
        this.total = product.getRent_price() * this.days_of_rent;
    }
    
    @Override
    public String toString() {
        return String.format("Product: %s, Quantity: %d, Rent Days: %d, Total: %.2f",
                product.getName(), qty, days_of_rent, total);
    }

    
}
