package application;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequestManager {
    private List<ServiceRequest> serviceRequests;

    public ServiceRequestManager() {
        serviceRequests = new ArrayList<>();
    }

    public void addServiceRequest(ServiceRequest request) {
        serviceRequests.add(request);
    }

    public ServiceRequest findServiceRequestById(int id) {
        for (ServiceRequest request : serviceRequests) {
            if (request.getRequest_id() == id) {
                return request;
            }
        }
        return null;
    }

    public List<ServiceRequest> getAllServiceRequests() {
        return new ArrayList<>(serviceRequests); // Return a copy to avoid accidental modifications
    }
}
