package tn.esprit.pitest11.Entities;
import java.sql.Date;
public class Conges {
    private int idConge;
    private int idEmploye;
    private Date dateDebut;
    private Date dateFin;
    private String typeConge;
    private String statut;

    public Conges(int idConge, int idEmploye, Date dateDebut, Date dateFin, String typeConge, String statut) {
        this.idConge = idConge;
        this.idEmploye = idEmploye;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.typeConge = typeConge;
        this.statut = statut;
    }

    public int getIdConge() {
        return idConge;
    }

    public void setIdConge(int idConge) {
        this.idConge = idConge;
    }

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
