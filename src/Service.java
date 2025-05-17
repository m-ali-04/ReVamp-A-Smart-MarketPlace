package application;

public class Service {
	int service_id;
	String type;
	int service_provider;
	String desc;
	float rate;
	boolean available;
	
	public Service(int service_id, String type, int service_provider, String desc, float rate, boolean available) {
		super();
		this.service_id = service_id;
		this.type = type;
		this.service_provider = service_provider;
		this.desc = desc;
		this.rate = rate;
		this.available = available;
	}

	public int getService_id() {
		return service_id;
	}

	public void setService_id(int service_id) {
		this.service_id = service_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getService_provider() {
		return service_provider;
	}

	public void setService_provider(int service_provider) {
		this.service_provider = service_provider;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	
}
