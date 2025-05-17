package application;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private int UID;
    private int CID;
    private int RID;
    private int RFID;
    private int EID;
    private static UserManager instance;
    
    private List<Customer> customers;
    private List<Retailer> retailers;
    private List<Refurbisher> refurbishers;
    private List<Evaluator> evaluators;

    public UserManager() {
        customers = new ArrayList<>();
        retailers = new ArrayList<>();
        refurbishers = new ArrayList<>();
        evaluators = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // Registration method for user types
    public User registerUser(String userType, String username, String email, String phone, Object... extraParams) {
        // Get the next available user ID from UserManager
        // Use the UserFactory to create the appropriate user
        User user = UserFactory.createUser(userType, this, username, email, phone, extraParams);
        
        // Add user to the corresponding list based on userType
        switch (userType.toLowerCase()) {
            case "customer":
                addCustomer((Customer) user);
                break;
            case "retailer":
                addRetailer((Retailer) user);
                break;
            case "refurbisher":
                addRefurbisher((Refurbisher) user);
                break;
            case "evaluator":
                addEvaluator((Evaluator) user);
                break;
            default:
                throw new IllegalArgumentException("Invalid user type!");
        }
        DBHandler.storeUser(user);
        return user;
    }

    public int getUID() {
		return UID;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	public int getCID() {
		return CID;
	}

	public void setCID(int cID) {
		CID = cID;
	}

	public int getRID() {
		return RID;
	}

	public void setRID(int rID) {
		RID = rID;
	}

	public int getRFID() {
		return RFID;
	}

	public void setRFID(int rFID) {
		RFID = rFID;
	}

	public int getEID() {
		return EID;
	}

	public void setEID(int eID) {
		EID = eID;
	}

	public static void setInstance(UserManager instance) {
		UserManager.instance = instance;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public void setRetailers(List<Retailer> retailers) {
		this.retailers = retailers;
	}

	public void setRefurbishers(List<Refurbisher> refurbishers) {
		this.refurbishers = refurbishers;
	}

	public void setEvaluators(List<Evaluator> evaluators) {
		this.evaluators = evaluators;
	}

	// Add user to corresponding lists
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void addRetailer(Retailer retailer) {
        retailers.add(retailer);
    }

    public void addRefurbisher(Refurbisher refurbisher) {
        refurbishers.add(refurbisher);
    }

    public void addEvaluator(Evaluator evaluator) {
        evaluators.add(evaluator);
    }

    // Getter methods for user lists
    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Retailer> getRetailers() {
        return retailers;
    }

    public List<Refurbisher> getRefurbishers() {
        return refurbishers;
    }

    public List<Evaluator> getEvaluators() {
        return evaluators;
    }
}
