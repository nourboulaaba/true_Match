package tn.esprit.pitest11.service;

import tn.esprit.pitest11.Entities.Mission;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.pitest11.example.dao.DBConnection;

public class missionService implements IService<Mission> {
    private Connection connection = DBConnection.getInstance().getConnection();

    public missionService() {
    }

    public void insert(Mission mission) {
        String query = "INSERT INTO Mission (titre, date, destination, idEmploye) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, mission.getTitre());
            pstmt.setDate(2, new Date(mission.getDate().getTime()));
            pstmt.setString(3, mission.getDestination());
            pstmt.setInt(4, mission.getIdEmploye());
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    mission.setIdMission(generatedKeys.getInt(1)); // Assigner l'ID généré
                }
                System.out.println("✅ Mission ajoutée avec succès !");
            } else {
                System.out.println("⚠️ Échec de l'ajout de la mission !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Mission mission) {
        String query = "UPDATE Mission SET titre=?, date=?, destination=?, idEmploye=? WHERE idMission=?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setString(1, mission.getTitre());
            pstmt.setDate(2, new Date(mission.getDate().getTime()));
            pstmt.setString(3, mission.getDestination());
            pstmt.setInt(4, mission.getIdEmploye());
            pstmt.setInt(5, mission.getIdMission());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Mission mise à jour !");
            } else {
                System.out.println("⚠️ Mission non trouvée !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Mission mission) {
        System.out.println("Tentative de suppression de la mission ID : " + mission.getIdMission());
        String query = "DELETE FROM Mission WHERE idMission=?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setInt(1, mission.getIdMission());
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Mission supprimée !");
            } else {
                System.out.println("⚠️ Mission non trouvée !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Mission> getAll() {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM Mission";

        try (Statement stmt = this.connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Mission mission = new Mission(
                        rs.getInt("idMission"),
                        rs.getString("titre"),
                        rs.getDate("date"),
                        rs.getString("destination"),
                        rs.getInt("idEmploye")
                );
                missions.add(mission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return missions;
    }
    
    public List<Mission> getAllPaginated(int page, int size) {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM Mission LIMIT ? OFFSET ?";
        int offset = (page - 1) * size;

        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Mission mission = new Mission(
                        rs.getInt("idMission"),
                        rs.getString("titre"),
                        rs.getDate("date"),
                        rs.getString("destination"),
                        rs.getInt("idEmploye")
                );
                missions.add(mission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return missions;
    }
    
    public int getTotalCount() {
        String query = "SELECT COUNT(*) FROM Mission";
        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public List<Mission> searchMissions(String keyword) {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT * FROM Mission WHERE titre LIKE ? OR destination LIKE ? OR idEmploye LIKE ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            String searchTerm = "%" + keyword + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Mission mission = new Mission(
                        rs.getInt("idMission"),
                        rs.getString("titre"),
                        rs.getDate("date"),
                        rs.getString("destination"),
                        rs.getInt("idEmploye")
                );
                missions.add(mission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return missions;
    }
    
    public List<Mission> getAllSorted(String sortField, boolean ascending) {
        List<Mission> missions = new ArrayList<>();
        String direction = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM Mission ORDER BY " + sortField + " " + direction;

        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Mission mission = new Mission(
                        rs.getInt("idMission"),
                        rs.getString("titre"),
                        rs.getDate("date"),
                        rs.getString("destination"),
                        rs.getInt("idEmploye")
                );
                missions.add(mission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return missions;
    }

    public Mission getById(int id) {
        String query = "SELECT * FROM Mission WHERE idMission=?";
        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Mission(
                        rs.getInt("idMission"),
                        rs.getString("titre"),
                        rs.getDate("date"),
                        rs.getString("destination"),
                        rs.getInt("idEmploye")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("⚠️ Mission avec ID " + id + " non trouvée !");
        return null;
    }
}
