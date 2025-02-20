package tn.esprit.pitest11.Services;

import tn.esprit.pitest11.Interfaces.IService;
import tn.esprit.pitest11.Model.Recrutement;
import tn.esprit.pitest11.Model.User;
import tn.esprit.pitest11.Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecrutementService implements IService<Recrutement> {
    private Connection connexion;

    public RecrutementService() {
        connexion = DataSource.getInstance().getCnx();
    }

    // CREATE (ADD NEW RECRUTEMENT)
    @Override
    public void add(Recrutement recrutement) {
        String sql = "INSERT INTO Recrutement (nom) VALUES (?)";
        try (PreparedStatement pst = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, recrutement.getNom());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                recrutement.setId(rs.getInt(1)); // Assign generated ID to the object
            }
            System.out.println("Recrutement added successfully: " + recrutement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE RECRUTEMENT
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Recrutement WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Recrutement deleted successfully with ID: " + id);
            } else {
                System.out.println("No recrutement found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE RECRUTEMENT
    @Override
    public void update(Recrutement recrutement, int id) {
        String sql = "UPDATE Recrutement SET nom = ? WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setString(1, recrutement.getNom());
            pst.setInt(2, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Recrutement updated successfully: " + recrutement);
            } else {
                System.out.println("No recrutement found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // FIND RECRUTEMENT BY ID
    @Override
    public Recrutement readById(int id) {
        String sql = "SELECT * FROM Recrutement WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Recrutement(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // FIND ALL RECRUTEMENTS
    @Override
    public List<Recrutement> readAll() {
        List<Recrutement> recrutements = new ArrayList<>();
        String sql = "SELECT * FROM Recrutement";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                recrutements.add(new Recrutement(
                        rs.getInt("id"),
                        rs.getString("nom")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recrutements;
    }

    // ASSIGN USER TO RECRUTEMENT
    public void assignUserToRecrutement(int recrutementId, int userId) {
        String sql = "INSERT INTO Recrutement_User (recrutement_id, user_id) VALUES (?, ?)";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, recrutementId);
            pst.setInt(2, userId);
            pst.executeUpdate();
            System.out.println("User assigned to Recrutement successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GET USERS ASSIGNED TO A RECRUTEMENT
    public List<User> getUsersByRecrutement(int recrutementId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id, u.name, u.role FROM User u " +
                "JOIN Recrutement_User ru ON u.id = ru.user_id " +
                "WHERE ru.recrutement_id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, recrutementId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
