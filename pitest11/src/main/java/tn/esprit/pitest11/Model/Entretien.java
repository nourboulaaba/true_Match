package tn.esprit.pitest11.Model;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Entretien {

    private int id;
    private String nom;
    private List<Convoque> convoques;
    private LocalDate date;
    private int duree;
    private String Lieu;
    private double longitude;
    private double latitude;
    private User Responsable;
    private Recrutement recrutement;




    public Recrutement getRecrutement() {
        return recrutement;
    }

    public void setRecrutement(Recrutement recrutement) {
        this.recrutement = recrutement;
    }

    public Entretien(int id, String nom, List<Convoque> convoques, LocalDate date, int duree, String lieu, double longitude, double latitude, User responsable) {
        this.id = id;
        this.nom = nom;
        this.convoques = convoques;
        this.date = date;
        this.duree = duree;
        Lieu = lieu;
        this.longitude = longitude;
        this.latitude = latitude;
        Responsable = responsable;
    }

    public Entretien(int id,String nom, LocalDate date, int duree, String lieu, double longitude, double latitude) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.duree = duree;
        Lieu = lieu;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Entretien() {
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

    public List<Convoque> getConvoques() {
        return convoques;
    }

    public void setConvoques(List<Convoque> convoques) {
        this.convoques = convoques;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getLieu() {
        return Lieu;
    }

    public void setLieu(String lieu) {
        Lieu = lieu;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public User getResponsable() {
        return Responsable;
    }

    public void setResponsable(User responsable) {
        Responsable = responsable;
    }

    @Override
    public String toString() {
        return "Entretien{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", convoques=" + convoques +
                ", date=" + date +
                ", duree=" + duree +
                ", Lieu='" + Lieu + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", Responsable=" + Responsable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entretien entretien = (Entretien) o;
        return id == entretien.id && Objects.equals(date, entretien.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
