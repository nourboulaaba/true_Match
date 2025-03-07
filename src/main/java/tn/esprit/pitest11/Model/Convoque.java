package tn.esprit.pitest11.Model;


import java.util.Objects;

public class Convoque {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private Offre offre;
    private Entretien entretien;


    public Convoque(int id, String nom, String prenom, String email, Offre id_offre, Entretien entretien) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.offre = id_offre;
        this.entretien = entretien;
    }

    public Convoque() {
    }

    public Convoque(int id, String nom, String prenom, String email, Offre idOffre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.offre = idOffre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Offre getId_offre() {
        return offre;
    }

    public void setId_offre(Offre id_offre) {
        this.offre = id_offre;
    }

    public Entretien getEntretien() {
        return entretien;
    }

    public void setEntretien(Entretien entretien) {
        this.entretien = entretien;
    }

    @Override
    public String toString() {
        return "Convoque{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", offre=" + offre +
                ", entretien=" + entretien +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Convoque convoque = (Convoque) o;
        return id == convoque.id && offre == convoque.offre && Objects.equals(nom, convoque.nom) && Objects.equals(prenom, convoque.prenom) && Objects.equals(email, convoque.email) && Objects.equals(entretien, convoque.entretien);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, email, offre, entretien);
    }
}
