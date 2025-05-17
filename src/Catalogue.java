package application;

import java.util.ArrayList;
import java.util.List;

public class Catalogue {

    // Static field to hold the single instance of the Catalogue
    private static Catalogue instance;

    // Static fields to hold products and services
    private static List<Product> products;
    private static List<Service> services;

    private static int ItemId;
    private static int ServiceId;

    public static void setInstance(Catalogue instance) {
		Catalogue.instance = instance;
	}

	public static void setProducts(List<Product> products) {
		Catalogue.products = products;
	}

	public static void setServices(List<Service> services) {
		Catalogue.services = services;
	}

	// Private constructor to prevent instantiation
    private Catalogue() {
        products = new ArrayList<>();
        services = new ArrayList<>();
    }

    // Public method to provide access to the single instance
    public static Catalogue getInstance() {
        if (instance == null) {
            synchronized (Catalogue.class) {
                if (instance == null) {
                    instance = new Catalogue();
                }
            }
        }
        return instance;
    }

    public static int getItemId() {
        return ItemId;
    }

    public static void setItemId(int itemId) {
        ItemId = itemId;
    }

    public static int getServiceId() {
        return ServiceId;
    }

    public static void setServiceId(int serviceId) {
        ServiceId = serviceId;
    }

    // Methods to manage products
    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(int productId) {
        products.removeIf(product -> product.getProduct_id() == productId);
    }

    public static Product findProductById(int productId) {
        return products.stream()
                .filter(product -> product.getProduct_id() == productId)
                .findFirst()
                .orElse(null);
    }

    public List<Product> getProducts() {
        return products;
    }

    // Methods to manage services
    public static void addService(Service service) {
        services.add(service);
    }

    public void removeService(int serviceId) {
        services.removeIf(service -> service.getService_id() == serviceId);
    }

    public static Service findServiceById(int serviceId) {
        return services.stream()
                .filter(service -> service.getService_id() == serviceId)
                .findFirst()
                .orElse(null);
    }

    public List<Service> getServices() {
        return services;
    }
}
