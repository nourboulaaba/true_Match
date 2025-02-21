package tn.esprit.pitest11.Services;

import tn.esprit.pitest11.Interfaces.IService;
import tn.esprit.pitest11.Model.Offre;
import tn.esprit.pitest11.Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffreService implements IService<Offre> {

    private Connection connexion;

    public OffreService() {
        connexion = DataSource.getInstance().getCnx();
    }

    // CREATE (ADD NEW OFFRE)
    @Override
    public void add(Offre offre) {
        String sql = "INSERT INTO Offre (titre) VALUES (?, ?)";
        try (PreparedStatement pst = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, offre.getTitre());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                offre.setId(rs.getInt(1)); // Assign generated ID to the object
            }
            System.out.println("Offre added successfully: " + offre);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE OFFRE
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Offre WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Offre deleted successfully with ID: " + id);
            } else {
                System.out.println("No offre found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE OFFRE
    @Override
    public void update(Offre offre, int id) {
        String sql = "UPDATE Offre SET titre = ? WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setString(1, offre.getTitre());
            pst.setInt(2, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Offre updated successfully: " + offre);
            } else {
                System.out.println("No offre found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // FIND OFFRE BY ID
    @Override
    public Offre readById(int id) {
        String sql = "SELECT * FROM offre WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Offre(
                        rs.getInt("id"),
                        rs.getString("titre")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // FIND ALL OFFRES
    @Override
    public List<Offre> readAll() {
        List<Offre> offres = new ArrayList<>();
        String sql = "SELECT * FROM offre";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                offres.add(new Offre(
                        rs.getInt("id"),
                        rs.getString("titre")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offres;
    }
}
