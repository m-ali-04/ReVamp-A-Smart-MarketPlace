package application;

public class Retailer extends User {
    int retail_id;
    String retail_name;
    // Item list can be added here later
    float balance;
    float rating;

    public Retailer(int user_id, String username, String email_address, String phone_number, int retail_id, String retail_name, float balance, float rating) {
        super(user_id, username, email_address, phone_number); // Pass values to the User constructor
        this.retail_id = retail_id;
        this.retail_name = retail_name;
        this.balance = balance;
        this.rating = rating;
    }

    public int getRetail_id() {
        return retail_id;
    }

    public void setRetail_id(int retail_id) {
        this.retail_id = retail_id;
    }

    public String getRetail_name() {
        return retail_name;
    }

    public void setRetail_name(String retail_name) {
        this.retail_name = retail_name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
