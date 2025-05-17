package application;

import java.util.ArrayList;
import java.util.List;

public class Evaluator extends User {
    private int evaluator_id;
    private double rating;
    private List<Evaluation> evaluations;

    public Evaluator(int user_id, String username, String email_address, String phone_number,
                     int rID, double rating) {
        super(user_id, username, email_address, phone_number);
        this.evaluator_id = rID;
        this.rating = 0.0;
        this.evaluations = new ArrayList<>(); // Initialize evaluations list
    }

    public int getEvaluator_id() {
        return evaluator_id;
    }

    public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setEvaluations(List<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public void setEvaluator_id(int evaluator_id) {
        this.evaluator_id = evaluator_id;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void addEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
    }

    public void removeEvaluation(Evaluation evaluation) {
        this.evaluations.remove(evaluation);
    }
}
