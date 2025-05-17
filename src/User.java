package application;

public class User {
	int user_id;
	String username;
	String email_address;
	String phone_number;
	
	public User() {}
	
	public User(int user_id, String username, String email_address, String phone_number) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.email_address = email_address;
		this.phone_number = phone_number;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	
	
}
