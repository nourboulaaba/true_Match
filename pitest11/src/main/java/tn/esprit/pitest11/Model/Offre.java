package tn.esprit.pitest11.Model;

import java.util.Objects;

public class Offre {
    private int id;
    private String titre;
    private Recrutement recrutement;

    public Offre(int id, String titre, Recrutement recrutement) {
        this.id = id;
        this.titre = titre;
        this.recrutement = recrutement;
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

    public Recrutement getRecrutement() {
        return recrutement;
    }

    public void setRecrutement(Recrutement recrutement) {
        this.recrutement = recrutement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offre offre = (Offre) o;
        return id == offre.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Offre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", recrutement=" + recrutement +
                '}';
    }
}
