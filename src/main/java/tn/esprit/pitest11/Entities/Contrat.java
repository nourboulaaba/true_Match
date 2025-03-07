//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tn.esprit.pitest11.Entities;

import java.util.Date;

public class Contrat {
    private int idContrat;
    private int idEmploye;
    private String type;
    private Date dateDebut;
    private Date dateFin;
    private double salaire;

    public Contrat(int idContrat, int idEmploye, String type, Date dateDebut, Date dateFin, double salaire) {
        this.idContrat = idContrat;
        this.idEmploye = idEmploye;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.salaire = salaire;
    }

    public Contrat(int idEmploye, String type, Date dateDebut, Date dateFin, double salaire) {
        this.idEmploye = idEmploye;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.salaire = salaire;
    }

    public int getIdContrat() {
        return this.idContrat;
    }

    public void setIdContrat(int idContrat) {
        this.idContrat = idContrat;
    }

    public int getIdEmploye() {
        return this.idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateDebut() {
        return this.dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return this.dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public double getSalaire() {
        return this.salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public String toString() {
        int var10000 = this.idContrat;
        return "Contrat{idContrat=" + var10000 + ", idEmploye=" + this.idEmploye + ", type='" + this.type + "', dateDebut=" + String.valueOf(this.dateDebut) + ", dateFin=" + String.valueOf(this.dateFin) + ", salaire=" + this.salaire + "}";
    }
}
