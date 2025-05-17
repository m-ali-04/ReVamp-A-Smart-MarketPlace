package application;

import java.time.LocalDate;

public class ServiceRequest {

    private Service service;                   // The service for which the request is made
    private int request_id;                    // Unique ID for the service request
    private int customer_id;                   // Customer ID who made the request
    private int service_provider_id;           // Service provider ID who will fulfill the request
    private LocalDate request_date;            // Date when the request is made
    private LocalDate service_date;            // Date when the service will be provided (can be null)
    private float amount;                      // Amount for the service (price)
    private boolean status;                    // Status of the service request (e.g., pending, completed)

    // Constructor for ServiceRequest
    public ServiceRequest(Service service, int serviceRequestId, int customerId, int provider, 
                          LocalDate requestDate, LocalDate serviceDate, float price, boolean status) {
        this.service = service;                // Assign the service object
        this.request_id = serviceRequestId;    // Assign the service request ID
        this.customer_id = customerId;         // Assign the customer ID
        this.service_provider_id = provider;   // Assign the service provider ID
        this.request_date = requestDate;       // Assign the request date (e.g., today's date)
        this.service_date = serviceDate;       // Assign the service date (could be null initially)
        this.amount = price;                   // Assign the service price
        this.status = status;                  // Assign the status (false initially)
    }

    // Getters and Setters for each field
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getService_provider_id() {
        return service_provider_id;
    }

    public void setService_provider_id(int service_provider_id) {
        this.service_provider_id = service_provider_id;
    }

    public LocalDate getRequest_date() {
        return request_date;
    }

    public void setRequest_date(LocalDate request_date) {
        this.request_date = request_date;
    }

    public LocalDate getService_date() {
        return service_date;
    }

    public void setService_date(LocalDate service_date) {
        this.service_date = service_date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "service=" + service +
                ", request_id=" + request_id +
                ", customer_id=" + customer_id +
                ", service_provider_id=" + service_provider_id +
                ", request_date=" + request_date +
                ", service_date=" + service_date +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}
