package tn.esprit.pitest11.service;

import tn.esprit.pitest11.Interfaces.IService;
import tn.esprit.pitest11.Entities.Offre;
import tn.esprit.pitest11.Entities.Recrutement;
import tn.esprit.pitest11.example.dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecrutementService implements IService<Recrutement> {
    private Connection connexion;

    public RecrutementService() {
        connexion =DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(Recrutement recrutement) {
        String sql = "INSERT INTO Recrutement (offre_id, dateDebut, dateFin, nbEntretien) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, recrutement.getOffre().getId());
            pst.setDate(2, Date.valueOf(recrutement.getDateDebut()));
            // Handle null dateFin
            if (recrutement.getDateFin() != null) {
                pst.setDate(3, Date.valueOf(recrutement.getDateFin()));
            } else {
                pst.setNull(3, Types.DATE);
            }
            pst.setInt(4, recrutement.getNbEntretien());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                recrutement.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Recrutement WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Recrutement recrutement, int id) {
        String sql = "UPDATE Recrutement SET offre_id = ?, dateDebut = ?, dateFin = ?, nbEntretien = ? WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, recrutement.getOffre().getId());
            pst.setDate(2, Date.valueOf(recrutement.getDateDebut()));
            if (recrutement.getDateFin() != null) {
                pst.setDate(3, Date.valueOf(recrutement.getDateFin()));
            } else {
                pst.setNull(3, Types.DATE);
            }
            pst.setInt(4, recrutement.getNbEntretien());
            pst.setInt(5, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Recrutement readById(int id) {
        String sql = "SELECT * FROM recrutement WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                OffreService os = new OffreService();
                Offre offre = os.readById(rs.getInt("offre_id")); // Fetch Offre separately
                return new Recrutement(
                        rs.getInt("id"),
                        offre,
                        rs.getDate("dateDebut") != null ? rs.getDate("dateDebut").toLocalDate() : null,
                        rs.getDate("dateFin") != null ? rs.getDate("dateFin").toLocalDate() : null,

                        rs.getInt("NbEntretien")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Recrutement> readAll() {
        List<Recrutement> recrutements = new ArrayList<>();
        String sql = "SELECT * FROM recrutement";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            OffreService os = new OffreService(); // Create once instead of in loop
            while (rs.next()) {
                Offre offre = os.readById(rs.getInt("offre_id")); // Fetch Offre separately
                recrutements.add(new Recrutement(
                        rs.getInt("id"),
                        offre,
                        rs.getDate("dateDebut") != null ? rs.getDate("dateDebut").toLocalDate() : null,
                        rs.getDate("dateFin") != null ? rs.getDate("dateFin").toLocalDate() : null,
                        rs.getInt("NbEntretien")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recrutements;
    }

    // ========= STATISTICS METHODS =========

    /**
     * Returns the total number of recrutements.
     */
    public int getTotalRecrutements() {
        String sql = "SELECT COUNT(*) FROM recrutement";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Returns the average number of entretiens per recrutement.
     */
    public double getAverageEntretienCount() {
        String sql = "SELECT AVG(nbEntretien) FROM recrutement";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Returns the recrutement with the maximum number of entretiens.
     */
    public Recrutement getRecrutementWithMaxEntretien() {
        String sql = "SELECT * FROM recrutement ORDER BY nbEntretien DESC LIMIT 1";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                OffreService os = new OffreService();
                Offre offre = os.readById(rs.getInt("offre_id"));
                return new Recrutement(
                        rs.getInt("id"),
                        offre,
                        rs.getDate("dateDebut") != null ? rs.getDate("dateDebut").toLocalDate() : null,
                        rs.getDate("dateFin") != null ? rs.getDate("dateFin").toLocalDate() : null,
                        rs.getInt("nbEntretien")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
