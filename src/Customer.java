package application;

public class Customer extends User {
    int customer_id;
    String customer_type;
    String address;

    public Customer(int user_id, String username, String email_address, String phone_number, int customer_id, String customer_type, String address) {
        super(user_id, username, email_address, phone_number); // Pass values to the User constructor
        this.customer_id = customer_id;
        this.customer_type = customer_type;
        this.address = address;
    }

    public int getCustomer_id() {
        return customer_id;
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }
}
