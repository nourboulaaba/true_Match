package tn.esprit.pitest11.Entities;


import tn.esprit.pitest11.Entities.User;

import java.time.LocalDate;
import java.util.Objects;

public class Entretien {

    private int id;
    private User user;
    private LocalDate date;
    private String Lieu;
    private double longitude;
    private double latitude;
    private Recrutement recrutement;
    private boolean approved;




    public Recrutement getRecrutement() {
        return recrutement;
    }

    public void setRecrutement(Recrutement recrutement) {
        this.recrutement = recrutement;
    }

    public Entretien(int id,User user, LocalDate date, String lieu, double longitude, double latitude, Recrutement recrutement, boolean approved) {
        this.id = id;
        this.user = user;
        this.date = date;
        Lieu = lieu;
        this.longitude = longitude;
        this.latitude = latitude;
        this.recrutement = recrutement;
        this.approved = approved;
    }

    public Entretien() {
    }

    public Entretien(int id, User user, LocalDate date, String lieu, Recrutement recrutement) {
        this.id = id;
        this.user = user;
        this.date = date;
        Lieu = lieu;
        this.recrutement = recrutement;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Entretien{" +
                "id=" + id +
                ", user=" + user +
                ", date=" + date +
                ", Lieu='" + Lieu + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", recrutement=" + recrutement +
                ", approved=" + approved +
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
