package service;

import entite.Review;
import java.util.*;

public class ReviewService {

    private List<Review> reviews;

    public ReviewService() {
        this.reviews = new ArrayList<>();
    }

    // Ajouter un avis sur une formation
    public boolean addReview(Review review) {
        reviews.add(review);
        return true; // Ajouter l'avis avec succès
    }

    // Obtenir tous les avis sur une formation
    public List<Review> getReviewsByFormation(int formationId) {
        List<Review> formationReviews = new ArrayList<>();

        for (Review review : reviews) {
            if (review.getFormationId() == formationId) {
                formationReviews.add(review);
            }
        }

        return formationReviews;
    }
}
