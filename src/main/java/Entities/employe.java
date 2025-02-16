package Entities;

import java.util.Date;

public class employe {
    private int idEmploye; // Correction du nom de l'attribut (idEmployee → idEmploye pour uniformité)
    private String nom;
    private String prenom;
    private String faceID;
    private double salaire; // Changement du type de salaire en double pour plus de précision
    private Date dateEmbauche;
    private String poste;
    private String email;
    private int telephone;

    //  **Constructeur par défaut**
    public employe() {}

    // **Constructeur sans ID (utilisé pour ajouter un employé)**
    public employe(String nom, String prenom, String faceID, double salaire, Date dateEmbauche, String poste, String email, int telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.faceID = faceID;
        this.salaire = salaire;
        this.dateEmbauche = dateEmbauche;
        this.poste = poste;
        this.email = email;
        this.telephone = telephone;
    }

    // **Constructeur avec ID (utilisé pour la récupération depuis la base de données)**
    public employe(int idEmploye, String nom, String prenom, String faceID, double salaire, Date dateEmbauche, String poste, String email, int telephone) {
        this.idEmploye = idEmploye;
        this.nom = nom;
        this.prenom = prenom;
        this.faceID = faceID;
        this.salaire = salaire;
        this.dateEmbauche = dateEmbauche;
        this.poste = poste;
        this.email = email;
        this.telephone = telephone;
    }

    // 📌 **Getters**
    public int getIdEmploye() {
        return idEmploye;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getFaceID() {
        return faceID;
    }

    public double getSalaire() {
        return salaire;
    }

    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    public String getPoste() {
        return poste;
    }

    public String getEmail() {
        return email;
    }

    public int getTelephone() {
        return telephone;
    }

    // 📌 **Setters**
    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setFaceID(String faceID) {
        this.faceID = faceID;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    // 📌 **Méthode toString() pour affichage**
    @Override
    public String toString() {
        return "Employe{" +
                "idEmploye=" + idEmploye +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", faceID='" + faceID + '\'' +
                ", salaire=" + salaire +
                ", dateEmbauche=" + dateEmbauche +
                ", poste='" + poste + '\'' +
                ", email='" + email + '\'' +
                ", telephone=" + telephone +
                '}';
    }
}
