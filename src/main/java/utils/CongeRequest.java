package utils;
import entite.Conges;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
public class CongeRequest {
    public int addCongeRequest(Conges conge) throws SQLException {
        String query = "INSERT INTO Conges (idEmploye, dateDebut, dateFin, typeConge, statut) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DataSource.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, conge.getIdEmploye());
            pstmt.setDate(2, conge.getDateDebut());
            pstmt.setDate(3, conge.getDateFin());
            pstmt.setString(4, conge.getTypeConge());
            pstmt.setString(5, "En attente"); // Toujours "En attente" par défaut

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de l'insertion du congé, aucune ligne affectée.");
            }

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retourne l'ID généré
                } else {
                    throw new SQLException("Échec de l'insertion du congé, aucun ID généré.");
                }
            }
        }
    }


    public void updateCongeRequest(Conges request) throws SQLException {
        String sql = "UPDATE Conges SET idEmploye=?, dateDebut=?, dateFin=?, typeConge=?, statut=? WHERE idConges=?";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, request.getIdEmploye());
            pstmt.setDate(2, new Date(request.getDateDebut().getTime()));
            pstmt.setDate(3, new Date(request.getDateFin().getTime()));
            pstmt.setString(4, request.getTypeConge());
            pstmt.setString(5, request.getStatut());
            pstmt.setInt(6, request.getIdConge());



            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la mise à jour du congé, aucune ligne affectée.");
            }
        }
    }

    public void deleteCongeRequest(int idConges) throws SQLException {
        String sql = "DELETE FROM Conges WHERE idConges=?";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idConges);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la suppression du congé, aucune ligne affectée.");
            }
        }
    }
    public void updateStatutConge(int idConges, String newStatut) throws SQLException {
        String sql = "UPDATE Conges SET statut=? WHERE idConges=?";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatut);
            pstmt.setInt(2, idConges);
            pstmt.executeUpdate();
        }
    }
    public List<Conges> getCongesEnAttente() throws SQLException {
        List<Conges> congesList = new ArrayList<>();
        String sql = "SELECT * FROM Conges WHERE statut='En attente'";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Conges conge = new Conges(
                        rs.getInt("idConges"),
                        rs.getInt("idEmploye"),
                        rs.getDate("dateDebut"),
                        rs.getDate("dateFin"),
                        rs.getString("typeConge"),
                        rs.getString("statut")
                );
                congesList.add(conge);
            }
        }
        return congesList;
    }
    public List<Conges> getAllConges() throws SQLException {
        List<Conges> congesList = new ArrayList<>();
        String sql = "SELECT * FROM Conges";  // Récupère tous les congés

        try (Connection conn = DataSource.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Conges conge = new Conges(
                        rs.getInt("idConges"),
                        rs.getInt("idEmploye"),
                        rs.getDate("dateDebut"),
                        rs.getDate("dateFin"),
                        rs.getString("typeConge"),
                        rs.getString("statut")
                );
                congesList.add(conge);
            }
        }
        return congesList;
    }
    public static List<Conges> getCongesByEmploye(int idEmploye) {
        List<Conges> congesList = new ArrayList<>();
        String query = "SELECT * FROM Conges WHERE idEmploye = ?";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idEmploye);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                congesList.add(new Conges(
                        rs.getInt("idConges"),
                        rs.getInt("idEmploye"),
                        rs.getDate("dateDebut"),
                        rs.getDate("dateFin"),
                        rs.getString("typeConge"),
                        rs.getString("statut")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return congesList;
    }


}



