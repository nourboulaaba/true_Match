package tn.esprit.pitest11.Services;

import tn.esprit.pitest11.Interfaces.IService;
import tn.esprit.pitest11.Model.Entretien;
import tn.esprit.pitest11.Model.Recrutement;
import tn.esprit.pitest11.Model.User;
import tn.esprit.pitest11.Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntretienService implements IService<Entretien> {

    private Connection connexion;

    public EntretienService() {
        connexion = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(Entretien entretien) {
        String sql = "INSERT INTO entretien (user_id, date, lieu, longitude, latitude, recrutement_id, approved) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, entretien.getUser().getId());
            pst.setDate(2, Date.valueOf(entretien.getDate()));
            pst.setString(3, entretien.getLieu());
            pst.setDouble(4, entretien.getLongitude());
            pst.setDouble(5, entretien.getLatitude());
            pst.setInt(6, entretien.getRecrutement().getId());

            if (entretien.isApproved()) {
                pst.setBoolean(7, entretien.isApproved());
            } else {
                pst.setNull(7, Types.BOOLEAN);
            }

            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                entretien.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM entretien WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Entretien entretien, int id) {
        String sql = "UPDATE entretien SET user_id = ?, date = ?, lieu = ?, longitude = ?, latitude = ?, recrutement_id = ?, approved = ? WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, entretien.getUser().getId());
            pst.setDate(2, Date.valueOf(entretien.getDate()));
            pst.setString(3, entretien.getLieu());
            pst.setDouble(4, entretien.getLongitude());
            pst.setDouble(5, entretien.getLatitude());
            pst.setInt(6, entretien.getRecrutement().getId());

            if (entretien.isApproved()) {
                pst.setBoolean(7, entretien.isApproved());
            } else {
                pst.setNull(7, Types.BOOLEAN);
            }

            pst.setInt(8, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Entretien readById(int id) {
        String sql = "SELECT * FROM entretien WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Entretien(
                        rs.getInt("id"),
                        new UserService().readById(rs.getInt("user_id")),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("lieu"),
                        rs.getDouble("longitude"),
                        rs.getDouble("latitude"),
                        new RecrutementService().readById(rs.getInt("recrutement_id")),
                        rs.getObject("approved") != null && rs.getBoolean("approved")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Entretien> readAll() {
        String sql = "SELECT * FROM entretien";
        RecrutementService rss = new RecrutementService();
        List<Entretien> entretiens = new ArrayList<>();
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                entretiens.add(new Entretien(
                        rs.getInt("id"),
                        new UserService().readById(rs.getInt("user_id")),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("lieu"),
                        rs.getDouble("longitude"),
                        rs.getDouble("latitude"),
                        (rss.readById(rs.getInt("recrutement_id"))),
                        rs.getObject("approved") != null && rs.getBoolean("approved")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entretiens;
    }

    /**
     * Fetches all entretiens filtered by user ID and recrutement ID.
     */
    public List<Entretien> readAllByUserIdAndRecrutementId(int userId, int recrutementId) {
        String sql = "SELECT * FROM entretien WHERE user_id = ? AND recrutement_id = ?";
        List<Entretien> entretiens = new ArrayList<>();

        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, userId);
            pst.setInt(2, recrutementId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                entretiens.add(new Entretien(
                        rs.getInt("id"),
                        new UserService().readById(rs.getInt("user_id")),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("lieu"),
                        rs.getDouble("longitude"),
                        rs.getDouble("latitude"),
                        new RecrutementService().readById(rs.getInt("recrutement_id")),
                        rs.getObject("approved") != null && rs.getBoolean("approved")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entretiens;
    }

    /**
     * Fetches all entretiens associated with a specific recrutement ID.
     */
    public List<Entretien> readAllByRecrutementId(int recrutementId) {
        String sql = "SELECT * FROM entretien WHERE recrutement_id = ?";
        List<Entretien> entretiens = new ArrayList<>();

        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, recrutementId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                entretiens.add(new Entretien(
                        rs.getInt("id"),
                        new UserService().readById(rs.getInt("user_id")),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("lieu"),
                        rs.getDouble("longitude"),
                        rs.getDouble("latitude"),
                        new RecrutementService().readById(rs.getInt("recrutement_id")),
                        rs.getObject("approved") != null && rs.getBoolean("approved")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entretiens;
    }

    /**
     * Fetches all entretiens associated with a specific user ID.
     */
    public List<Entretien> entretienByUserId(int userId) {
        String sql = "SELECT * FROM entretien WHERE user_id = ?";
        List<Entretien> entretiens = new ArrayList<>();

        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                entretiens.add(new Entretien(
                        rs.getInt("id"),
                        new UserService().readById(rs.getInt("user_id")),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("lieu"),
                        rs.getDouble("longitude"),
                        rs.getDouble("latitude"),
                        new RecrutementService().readById(rs.getInt("recrutement_id")),
                        rs.getObject("approved") != null && rs.getBoolean("approved")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entretiens;
    }

    // ========= STATISTICS METHODS =========

    /**
     * Returns the total number of entretiens for a given recrutement.
     */
    public int getTotalEntretiensByRecrutementId(int recrutementId) {
        String sql = "SELECT COUNT(*) FROM entretien WHERE recrutement_id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, recrutementId);
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
     * Returns a map of lieu (location) to the count of entretiens for a given recrutement.
     */
    public Map<String, Integer> getEntretiensCountByLieu(int recrutementId) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT lieu, COUNT(*) as count FROM entretien WHERE recrutement_id = ? GROUP BY lieu";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, recrutementId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                stats.put(rs.getString("lieu"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }


}
