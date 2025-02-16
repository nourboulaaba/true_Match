package Entities;

public class condidat {
    private int idCondidat;
    private String nom;
    private String prenom;
    private String email;
    private int telephone; // Correction du nom de l'attribut (tetphone → telephone)
    private String cv;

    // Constructeur par défaut
    public condidat() {}

    // Constructeur avec tous les attributs (sans idCondidat, car il est souvent auto-incrémenté dans la BD)
    public condidat(String nom, String prenom, String email, int telephone, String cv) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.cv = cv;
    }

    // Constructeur avec ID (utile pour la récupération depuis la BD)
    public condidat(int idCondidat, String nom, String prenom, String email, int telephone, String cv) {
        this.idCondidat = idCondidat;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.cv = cv;
    }

    // Getters
    public int getIdCondidat() {
        return idCondidat;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public int getTelephone() {
        return telephone;
    }

    public String getCv() {
        return cv;
    }

    // Setters
    public void setIdCondidat(int idCondidat) {
        this.idCondidat = idCondidat;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    // Méthode toString pour affichage
    @Override
    public String toString() {
        return "Condidat{" +
                "idCondidat=" + idCondidat +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone=" + telephone +
                ", cv='" + cv + '\'' +
                '}';
    }
}
