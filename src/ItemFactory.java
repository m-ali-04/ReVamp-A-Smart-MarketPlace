package application;

public class ItemFactory {

    public static Product createProduct(int id, String name, float price, String desc, boolean rentable, int stock, float rentPrice, int retailer_id) {
        if (rentable) {
            return new Product(id, name, price, desc, rentable, stock, rentPrice, retailer_id);
        } else {
            return new Product(id, name, price, desc, rentable, stock, 0, retailer_id);
        }
    }

    public static OrderItem createOrderItem(int qty, boolean isRent, int daysOfRent) {
        // Retrieve the selected product ID from the SessionManager
        int selectedProductId = SessionManager.getSelectedItemId();

        // Fetch the product from the catalogue using the selectedProductId
        Product product = Catalogue.findProductById(selectedProductId);

        // Validate if the product exists
        if (product == null) {
            throw new IllegalArgumentException("Product with ID " + selectedProductId + " not found in the catalogue.");
        }

        // Create and return an OrderItem based on the rental flag
        if (isRent) {
            return new OrderItem(product, qty, daysOfRent);
        } else {
            return new OrderItem(product, qty);
        }
    }
}
