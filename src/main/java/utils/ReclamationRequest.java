package utils;
import entite.Reclamations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ReclamationRequest {
    private final Connection conn;

    public ReclamationRequest() {
        conn = DataSource.getInstance().getConnection();
    }

    // Vérifier si l'identifiant existe dans la table user
    public boolean identifierExiste(String identifier) {
        String query = "SELECT COUNT(*) FROM user WHERE identifier = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, identifier);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean ajouterReclamation(Reclamations reclamation) {
        String query = "INSERT INTO Reclamations (identifier, sujet, description, imagePath, date, classification) VALUES (?, ?, ?, ?, NOW(), ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, reclamation.getIdentifier());
            ps.setString(2, reclamation.getSujet());
            ps.setString(3, reclamation.getDescription());
            ps.setString(4, reclamation.getImagePath());
            ps.setString(5, reclamation.getClassification());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Retrieve all reclamations from the database
    public static ObservableList<Reclamations> getAllReclamations() {
        ObservableList<Reclamations> reclamationList = FXCollections.observableArrayList();
        Connection connection = DataSource.getInstance().getConnection();
        String query = "SELECT id, identifier, sujet, description, imagePath, date, classification FROM Reclamations ORDER BY classification ASC";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reclamationList.add(new Reclamations(
                        rs.getInt("id"),
                        rs.getString("identifier"),
                        rs.getString("sujet"),
                        rs.getString("description"),
                        rs.getString("imagePath") != null ? rs.getString("imagePath") : "",
                        rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : LocalDate.now(),
                        rs.getString("classification") != null ? rs.getString("classification") : "Non classifiée"
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamationList;
    }
    public static ObservableList<Reclamations> getReclamationsByClassification(String classification) {
        ObservableList<Reclamations> reclamationList = FXCollections.observableArrayList();
        Connection connection = DataSource.getInstance().getConnection();
        String query = "SELECT id, identifier, sujet, description, imagePath, date, classification FROM Reclamations WHERE classification = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, classification);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reclamationList.add(new Reclamations(
                        rs.getInt("id"),
                        rs.getString("identifier"),
                        rs.getString("sujet"),
                        rs.getString("description"),
                        rs.getString("imagePath") != null ? rs.getString("imagePath") : "",
                        rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : LocalDate.now(),
                        rs.getString("classification") != null ? rs.getString("classification") : "Non classifiée"
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamationList;
    }

}
