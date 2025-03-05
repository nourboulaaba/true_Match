package utils;
import entite.Conges;
import service.MailService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
public class CongeRequest {
    public boolean doesEmployeExist(int idEmploye) {
        String query = "SELECT COUNT(*) FROM user WHERE id = ?"; // Changement ici de idEmploye à id
        Connection conn = DataSource.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idEmploye);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Si le résultat est supérieur à 0, l'employé existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la vérification de l'existence de l'employé : " + e.getMessage());
        }

        return false; // Si une erreur se produit ou l'employé n'existe pas
    }

    // Méthode pour ajouter une demande de congé
    public int addCongeRequest(Conges conge) {
        String query = "INSERT INTO Conges (idEmploye, dateDebut, dateFin, typeConge, statut) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DataSource.getInstance().getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, conge.getIdEmploye());
            pstmt.setDate(2, conge.getDateDebut());
            pstmt.setDate(3, conge.getDateFin());
            pstmt.setString(4, conge.getTypeConge());
            pstmt.setString(5, "En attente");

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de l'insertion du congé.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Aucun ID généré.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout d'une demande de congé : " + e.getMessage());
            return -1; // Retourne une valeur d'erreur au lieu de planter
        }
    }
    public boolean employeExiste(int employeId) {
        String query = "SELECT COUNT(*) FROM user WHERE id = ?";
        Connection conn = DataSource.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, employeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si le COUNT(*) est > 0, l'employé existe
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log l'erreur
        }

        return false; // Par défaut, considère que l'employé n'existe pas
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
    public int getTotalCongesPris(int employeId) {
        String sql = "SELECT SUM(DATEDIFF(dateFin, dateDebut) + 1) AS joursPris FROM Conges WHERE idEmploye = ? AND statut = 'Accepté'";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("joursPris");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}



