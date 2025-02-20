package tn.esprit.pitest11.Model;

import java.util.List;
import java.util.Objects;

public class Recrutement {
    private int id;
    private String nom;
    private List<User> users;

    private List<Entretien> entretiens;

    public List<Entretien> getEntretiens() {
        return entretiens;
    }

    public void setEntretiens(List<Entretien> entretiens) {
        this.entretiens = entretiens;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Recrutement(int id, List<User> users) {
        this.id = id;
        this.users = users;
    }

    public Recrutement(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Recrutement() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recrutement that = (Recrutement) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recrutement{" +
                "id=" + id +
                ", users=" + users +
                ",nom = "+ nom+
                '}';
    }
}
