package entite;

public class Certificat {
    private int idCertif;
    private int idFormation;
    private String dateExamen;
    private String heure;
    private int duree;
    private String prixExam; // Changement en String
    private String niveau;

    // Constructeur sans idCertif (pour l'ajout d'un certificat)
    public Certificat(int idFormation, String dateExamen, String heure, int duree, String prixExam, String niveau) {
        this.idFormation = idFormation;
        this.dateExamen = dateExamen;
        this.heure = heure;
        this.duree = duree;
        this.prixExam = prixExam;
        this.niveau = niveau;
    }

    // Constructeur avec idCertif (pour récupérer un certificat existant)
    public Certificat(int idCertif, int idFormation, String dateExamen, String heure, int duree, String prixExam, String niveau) {
        this.idCertif = idCertif;
        this.idFormation = idFormation;
        this.dateExamen = dateExamen;
        this.heure = heure;
        this.duree = duree;
        this.prixExam = prixExam;
        this.niveau = niveau;
    }

    // Getters et Setters
    public int getIdCertif() {
        return idCertif;
    }

    public void setIdCertif(int idCertif) {
        this.idCertif = idCertif;
    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public String getDateExamen() {
        return dateExamen;
    }

    public void setDateExamen(String dateExamen) {
        this.dateExamen = dateExamen;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getPrixExam() {
        return prixExam;
    }

    public void setPrixExam(String prixExam) {
        this.prixExam = prixExam;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    @Override
    public String toString() {
        return "Certificat{" +
                "idCertif=" + idCertif +
                ", idFormation=" + idFormation +
                ", dateExamen='" + dateExamen + '\'' +
                ", heure='" + heure + '\'' +
                ", duree=" + duree +
                ", prixExam='" + prixExam + '\'' +
                ", niveau='" + niveau + '\'' +
                '}';
    }
}
