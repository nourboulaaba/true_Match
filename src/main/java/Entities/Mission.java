package Entities;

import java.util.Date;

public class Mission {
    private int idMission;
    private String titre ;
    private Date date ;
    private String destination;
    private int idEmploye ;

    public Mission(int idMission, String titre, Date date, String destination, int idEmploye) {
        this.idMission = idMission;
        this.titre = titre;
        this.date = date;
        this.destination = destination;
        this.idEmploye = idEmploye;
    }


    public Mission(String titre, Date date, String destination, int idEmploye) {
        this.titre = titre;
        this.date = date;
        this.destination = destination;
        this.idEmploye = idEmploye;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getIdMission() {
        return idMission;
    }

    public void setIdMission(int idMission) {
        this.idMission = idMission;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "idMission=" + idMission +
                ", titre='" + titre + '\'' +
                ", date=" + date +
                ", destination='" + destination + '\'' +
                ", idEmploye=" + idEmploye +
                '}';
    }


}
