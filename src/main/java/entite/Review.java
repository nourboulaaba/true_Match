package entite;

public class Review {
    private int idReview;
    private int formationId; // ID de la formation sur laquelle l'avis est donné
    private int userId; // ID de l'utilisateur qui donne l'avis
    private int rating; // Note de l'avis (par exemple de 1 à 5)
    private String comment; // Commentaire de l'avis

    // Constructeur
    public Review(int formationId, int userId, int rating, String comment) {
        this.formationId = formationId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters et Setters
    public int getIdReview() {
        return idReview;
    }

    public void setIdReview(int idReview) {
        this.idReview = idReview;
    }

    public int getFormationId() {
        return formationId;
    }

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
