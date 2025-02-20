package utils;
import entite.Reclamations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ReclamationRequest {
    private final Connection conn;

    public ReclamationRequest() {
        conn = DataSource.getInstance().getConnection();
    }

    // Add a new reclamation
    public boolean ajouterReclamation(Reclamations reclamation) {
        String query = "INSERT INTO Reclamations (identifier, sujet, description, imagePath, date) VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, reclamation.getIdentifier());
            ps.setString(2, reclamation.getSujet());
            ps.setString(3, reclamation.getDescription());
            ps.setString(4, reclamation.getImagePath());

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
        String query = "SELECT id, identifier, sujet, description, imagePath, date FROM Reclamations";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reclamationList.add(new Reclamations(
                        rs.getInt("id"),
                        rs.getString("identifier"),
                        rs.getString("sujet"),
                        rs.getString("description"),
                        rs.getString("imagePath"),
                        rs.getDate("date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamationList;
    }
}
