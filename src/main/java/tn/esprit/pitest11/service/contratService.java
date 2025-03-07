//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tn.esprit.pitest11.service;

import tn.esprit.pitest11.Entities.Contrat;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.pitest11.example.dao.DBConnection;

public class contratService implements IService<Contrat> {
    private Connection connection = DBConnection.getInstance().getConnection();

    public contratService() {
    }

    public void insert(Contrat contrat) {
        String query = "INSERT INTO Contrat (idEmploye, type, dateDébut, dateFin, salaire) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(query, 1)) {
            pstmt.setInt(1, contrat.getIdEmploye());
            pstmt.setString(2, contrat.getType());
            pstmt.setDate(3, new Date(contrat.getDateDebut().getTime()));
            pstmt.setDate(4, new Date(contrat.getDateFin().getTime()));
            pstmt.setDouble(5, contrat.getSalaire());
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Contrat ajouté avec succès !");
            } else {
                System.out.println("⚠️ Échec de l'ajout du contrat !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update(Contrat contrat) {
        String query = "UPDATE Contrat SET idEmploye=?, type=?, dateDébut=?, dateFin=?, salaire=? WHERE idContrat=?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setInt(1, contrat.getIdEmploye());
            pstmt.setString(2, contrat.getType());
            pstmt.setDate(3, new Date(contrat.getDateDebut().getTime()));
            pstmt.setDate(4, new Date(contrat.getDateFin().getTime()));
            pstmt.setDouble(5, contrat.getSalaire());
            pstmt.setInt(6, contrat.getIdContrat());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Contrat mis à jour !");
            } else {
                System.out.println("⚠️ Contrat non trouvé !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(Contrat contrat) {
        String query = "DELETE FROM Contrat WHERE idContrat=?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setInt(1, contrat.getIdContrat());
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Contrat supprimé !");
            } else {
                System.out.println("⚠️ Contrat non trouvé !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Contrat> getAll() {
        List<Contrat> contrats = new ArrayList();
        String query = "SELECT * FROM Contrat";

        try (
                Statement stmt = this.connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
        ) {
            while(rs.next()) {
                Contrat contrat = new Contrat(rs.getInt("idContrat"), rs.getInt("idEmploye"), rs.getString("type"), rs.getDate("DateDébut"), rs.getDate("DateFin"), rs.getDouble("salaire"));
                contrats.add(contrat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contrats;
    }
    
    public List<Contrat> getAllPaginated(int page, int size) {
        List<Contrat> contrats = new ArrayList();
        String query = "SELECT * FROM Contrat LIMIT ? OFFSET ?";
        int offset = (page - 1) * size;

        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                Contrat contrat = new Contrat(rs.getInt("idContrat"), rs.getInt("idEmploye"), rs.getString("type"), rs.getDate("DateDébut"), rs.getDate("DateFin"), rs.getDouble("salaire"));
                contrats.add(contrat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contrats;
    }
    
    public int getTotalCount() {
        String query = "SELECT COUNT(*) FROM Contrat";
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
    
    public List<Contrat> searchContrats(String keyword) {
        List<Contrat> contrats = new ArrayList();
        String query = "SELECT * FROM Contrat WHERE type LIKE ? OR idEmploye LIKE ? OR salaire LIKE ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            String searchTerm = "%" + keyword + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                Contrat contrat = new Contrat(rs.getInt("idContrat"), rs.getInt("idEmploye"), rs.getString("type"), rs.getDate("DateDébut"), rs.getDate("DateFin"), rs.getDouble("salaire"));
                contrats.add(contrat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contrats;
    }
    
    public List<Contrat> getAllSorted(String sortField, boolean ascending) {
        List<Contrat> contrats = new ArrayList();
        String direction = ascending ? "ASC" : "DESC";
        String query = "SELECT * FROM Contrat ORDER BY " + sortField + " " + direction;

        try (Statement stmt = this.connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()) {
                Contrat contrat = new Contrat(rs.getInt("idContrat"), rs.getInt("idEmploye"), rs.getString("type"), rs.getDate("DateDébut"), rs.getDate("DateFin"), rs.getDouble("salaire"));
                contrats.add(contrat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contrats;
    }

    public Contrat getById(int id) {
        String query = "SELECT * FROM Contrat WHERE idContrat=?";
        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Contrat(
                        rs.getInt("idContrat"),
                        rs.getInt("idEmploye"),
                        rs.getString("type"),
                        rs.getDate("DateDébut"), // Utilisation de DateDébut
                        rs.getDate("DateFin"), // Utilisation de DateFin
                        rs.getDouble("salaire")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("⚠️ Contrat avec ID " + id + " non trouvé !");
        return null;
    }
}
