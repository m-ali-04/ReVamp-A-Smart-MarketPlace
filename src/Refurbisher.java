package application;

import java.util.ArrayList;
import java.util.List;

public class Refurbisher extends User {
    private int refurbisher_id;
    private double rating;
    private List<Service> services;

    public Refurbisher(int user_id, String username, String email_address, String phone_number,
                       int rID, double rating) {
        super(user_id, username, email_address, phone_number);
        this.refurbisher_id=rID;
        this.rating=rating;
        this.services = new ArrayList<>(); // Initialize services list
    }

    public int getRefurbisher_id() {
        return refurbisher_id;
    }

    public void setRefurbisher_id(int refurbisher_id) {
        this.refurbisher_id = refurbisher_id;
    }

    public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public List<Service> getServices() {
        return services;
    }

    public void addService(Service service) {
        this.services.add(service);
    }

    public void removeService(Service service) {
        this.services.remove(service);
    }
}
