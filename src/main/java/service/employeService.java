package service;

import Entities.employe;
import org.example.dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class employeService {
    private Connection cnx;

    // 📌 Constructeur : Initialisation de la connexion
    public employeService() {
        cnx = DBConnection.getInstance().getConnection();
    }

    /**
     * 📌 Ajouter un employé dans la base de données
     */
    public void insert(employe e) {
        String requete = "INSERT INTO employe (nom, prenom, faceID, salaire, dateEmbauche, poste, email, telephone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, e.getNom());
            pst.setString(2, e.getPrenom());
            pst.setString(3, e.getFaceID());
            pst.setDouble(4, e.getSalaire());
            pst.setDate(5, new java.sql.Date(e.getDateEmbauche().getTime()));
            pst.setString(6, e.getPoste());
            pst.setString(7, e.getEmail());
            pst.setInt(8, e.getTelephone());
            pst.executeUpdate();
            System.out.println("✅ Employé ajouté avec succès !");
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de l'ajout de l'employé : " + ex.getMessage());
        }
    }

    /**
     * 📌 Modifier un employé existant
     */
    public void update(employe e) {
        String requete = "UPDATE employe SET nom = ?, prenom = ?, faceID = ?, salaire = ?, dateEmbauche = ?, poste = ?, email = ?, telephone = ? WHERE idEmploye = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, e.getNom());
            pst.setString(2, e.getPrenom());
            pst.setString(3, e.getFaceID());
            pst.setDouble(4, e.getSalaire());
            pst.setDate(5, new java.sql.Date(e.getDateEmbauche().getTime()));
            pst.setString(6, e.getPoste());
            pst.setString(7, e.getEmail());
            pst.setInt(8, e.getTelephone());
            pst.setInt(9, e.getIdEmploye());
            pst.executeUpdate();
            System.out.println("✅ Employé mis à jour avec succès !");
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la mise à jour de l'employé : " + ex.getMessage());
        }
    }

    /**
     * 📌 Supprimer un employé par ID
     */
    public void delete(int idEmploye) {
        String requete = "DELETE FROM employe WHERE idEmploye = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, idEmploye);
            pst.executeUpdate();
            System.out.println("✅ Employé supprimé avec succès !");
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la suppression de l'employé : " + ex.getMessage());
        }
    }

    /**
     * 📌 Récupérer un employé par son ID
     */
    public employe getById(int idEmploye) {
        String requete = "SELECT * FROM employe WHERE idEmploye = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, idEmploye);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new employe(
                        rs.getInt("idEmploye"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("faceID"),
                        rs.getDouble("salaire"),
                        rs.getDate("dateEmbauche"),
                        rs.getString("poste"),
                        rs.getString("email"),
                        rs.getInt("telephone")
                );
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la récupération de l'employé : " + ex.getMessage());
        }
        return null;
    }

    /**
     * 📌 Récupérer tous les employés sous forme de liste
     */
    public List<employe> getAll() {
        List<employe> liste = new ArrayList<>();
        String requete = "SELECT * FROM employe";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(requete)) {
            while (rs.next()) {
                liste.add(new employe(
                        rs.getInt("idEmploye"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("faceID"),
                        rs.getDouble("salaire"),
                        rs.getDate("dateEmbauche"),
                        rs.getString("poste"),
                        rs.getString("email"),
                        rs.getInt("telephone")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la récupération des employés : " + ex.getMessage());
        }
        return liste;
    }

    /**
     * 📌 Lire et afficher tous les employés
     */
    public void readAll() {
        List<employe> liste = getAll();
        for (employe e : liste) {
            System.out.println(e);
        }
    }
}
