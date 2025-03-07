package tn.esprit.pitest11.Model;

import java.util.Objects;

public class Offre {
    private int id;
    private String titre;
    private String description;
    private int salaireMin;
    private int salaireMax;
    private Departement departement;

    public Offre(int id, String titre, String description, int salaireMin, int salaireMax, Departement departement) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.salaireMin = salaireMin;
        this.salaireMax = salaireMax;
        this.departement = departement;
    }

    public Offre() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSalaireMin() {
        return salaireMin;
    }

    public void setSalaireMin(int salaireMin) {
        this.salaireMin = salaireMin;
    }

    public int getSalaireMax() {
        return salaireMax;
    }

    public void setSalaireMax(int salaireMax) {
        this.salaireMax = salaireMax;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offre offre = (Offre) o;
        return id == offre.id && Objects.equals(departement, offre.departement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departement);
    }

    @Override
    public String toString() {
        return "Offre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", salaireMin=" + salaireMin +
                ", salaireMax=" + salaireMax +
                ", departement=" + departement +
                '}';
    }

    //attributs
    //constructeur
    //toString
    //hashcode
    //equals
    //representation de la table de la base de données en java
}

