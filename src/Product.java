package application;

public class Product {
	int product_id;
	String name;
	float price;
	float rent_price;
	String desc;
	boolean rent;
	int stock;
	int retailer_id;
	
	public Product(int product_id, String name, float price, String desc, boolean rent, int stock, float rentPrice, int retailer_id) {
        super();
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.rent = rent;
        this.stock = stock;
        this.rent_price = rentPrice;
        this.retailer_id =  retailer_id;// Assign rentPrice
    }
	public String getName() {
		return name;
	}
	public int getRetailer_id() {
		return retailer_id;
	}
	public void setRetailer_id(int retailer_id) {
		this.retailer_id = retailer_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isRent() {
		return rent;
	}
	public void setRent(boolean rent) {
		this.rent = rent;
	}
	public float getRent_price() {
		return rent_price;
	}
	public void setRent_price(float rent_price) {
		this.rent_price = rent_price;
	}
	
}
