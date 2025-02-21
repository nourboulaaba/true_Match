package tn.esprit.pitest11.Services;

import tn.esprit.pitest11.Interfaces.IService;
import tn.esprit.pitest11.Model.Offre;
import tn.esprit.pitest11.Model.Recrutement;
import tn.esprit.pitest11.Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecrutementService implements IService<Recrutement> {
    private Connection connexion;

    public RecrutementService() {
        connexion = DataSource.getInstance().getCnx();
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
                System.out.printf("offre : " + offre);


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



}
