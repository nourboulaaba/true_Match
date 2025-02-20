package entite;

import java.time.LocalDate;

public class Reclamations {
    private int id;
    private String identifier;
    private String sujet;
    private String description;
    private String imagePath;
    private LocalDate date;

    // Nouveau constructeur
    public Reclamations(int id, String identifier, String sujet, String description, String imagePath, LocalDate date) {
        this.id = id;
        this.identifier = identifier;
        this.sujet = sujet;
        this.description = description;
        this.imagePath = imagePath;
        this.date = date;
    }

    // Ancien constructeur
    public Reclamations(String identifier, String sujet, String description, String imagePath, LocalDate date) {
        this.identifier = identifier;
        this.sujet = sujet;
        this.description = description;
        this.imagePath = imagePath;
        this.date = date;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public LocalDate getDateReclamation() { return date; }
    public void setDateReclamation(LocalDate date) { this.date = date; }
}
