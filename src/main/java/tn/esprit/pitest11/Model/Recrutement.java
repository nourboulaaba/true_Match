package tn.esprit.pitest11.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Recrutement {
    private int id;
    private Offre offre;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int NbEntretien;

    private List<User> users;

    private List<Entretien> entretiens;

    public List<Entretien> getEntretiens() {
        return entretiens;
    }

    public void setEntretiens(List<Entretien> entretiens) {
        this.entretiens = entretiens;
    }

    public Recrutement(int id, Offre offre, LocalDate dateDebut, LocalDate dateFin, int nbEntretien) {
        this.id = id;
        this.offre = offre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        NbEntretien = nbEntretien;
    }

    public Recrutement() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getNbEntretien() {
        return NbEntretien;
    }

    public void setNbEntretien(int nbEntretien) {
        NbEntretien = nbEntretien;
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
        return id == that.id && Objects.equals(offre, that.offre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, offre);
    }

    @Override
    public String toString() {
        return "Recrutement{" +
                "id=" + id +
                ", offre=" + offre +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", NbEntretien=" + NbEntretien +
                '}';
    }
}