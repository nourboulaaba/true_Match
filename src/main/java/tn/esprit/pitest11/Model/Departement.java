package tn.esprit.pitest11.Model;

import java.util.Objects;

public class Departement {
    private int id;
    private String nom;
    private int budget;
    private int nbEmployes;
    private User responsable;

    public Departement(int id, String nom, int budget, int nbEmployes, User responsable) {
        this.id = id;
        this.nom = nom;
        this.budget = budget;
        this.nbEmployes = nbEmployes;
        this.responsable = responsable;
    }

    public Departement() {
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

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getNbEmployes() {
        return nbEmployes;
    }

    public void setNbEmployes(int nbEmployes) {
        this.nbEmployes = nbEmployes;
    }

    public User getResponsable() {
        return responsable;
    }

    public void setResponsable(User responsable) {
        this.responsable = responsable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Departement that = (Departement) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Departement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", budget=" + budget +
                ", nbEmployes=" + nbEmployes +
                ", responsable=" + responsable +
                '}';
    }
}

