package service;

import Entities.condidat;
import org.example.dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class condidatService {
    private Connection cnx;

    // Constructeur : Initialisation de la connexion
    public condidatService() {
        cnx = DBConnection.getInstance().getConnection();
    }

    /**
     *  Insère un nouveau candidat dans la base de données
     */
    public void insert(condidat c) {
        String requete = "INSERT INTO condidat (nom, prenom, email, telephone, cv) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, c.getNom());
            pst.setString(2, c.getPrenom());
            pst.setString(3, c.getEmail());
            pst.setInt(4, c.getTelephone());
            pst.setString(5, c.getCv());
            pst.executeUpdate();
            System.out.println(" Condidat ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    /**
     *  Met à jour un candidat existant dans la base de données
     */
    public void update(condidat c) {
        String requete = "UPDATE condidat SET nom = ?, prenom = ?, email = ?, telephone = ?, cv = ? WHERE idCondidat = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, c.getNom());
            pst.setString(2, c.getPrenom());
            pst.setString(3, c.getEmail());
            pst.setInt(4, c.getTelephone());
            pst.setString(5, c.getCv());
            pst.setInt(6, c.getIdCondidat());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(" Condidat mis à jour avec succès !");
            } else {
                System.out.println("⚠ Aucun candidat trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    /**
     *  Supprime un candidat de la base de données
     */
    public void delete(condidat c) {
        String requete = "DELETE FROM condidat WHERE idCondidat = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, c.getIdCondidat());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println(" Condidat supprimé avec succès !");
            } else {
                System.out.println("⚠ Aucun candidat trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    /**
     *  Récupère un candidat par son ID
     */
    public condidat getById(int id) {
        String requete = "SELECT * FROM condidat WHERE idCondidat = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new condidat(
                        rs.getInt("idCondidat"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getInt("telephone"),
                        rs.getString("cv")
                );
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la récupération : " + e.getMessage());
        }
        return null;
    }

    /**
     *  Récupère tous les candidats de la base de données
     */
    public List<condidat> getAll() {
        List<condidat> candidats = new ArrayList<>();
        String requete = "SELECT * FROM condidat";
        try (Statement ste = cnx.createStatement(); ResultSet rs = ste.executeQuery(requete)) {
            while (rs.next()) {
                candidats.add(new condidat(
                        rs.getInt("idCondidat"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getInt("telephone"),
                        rs.getString("cv")
                ));
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la récupération des candidats : " + e.getMessage());
        }
        return candidats;
    }

    /**
     Lis tous les candidats et les affiche
     */
    public void readAll() {
        List<condidat> candidats = getAll();
        if (candidats.isEmpty()) {
            System.out.println("⚠ Aucun candidat trouvé.");
        } else {
            for (condidat c : candidats) {
                System.out.println(c);
            }
        }
    }
}
