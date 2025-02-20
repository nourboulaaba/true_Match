package service;

import entite.Certificat;
import org.example.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificatService implements IService<Certificat> {
    private Connection connection;

    public CertificatService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void insert(Certificat c) {
        String sql = "INSERT INTO Certificat (idFormation, dateExamen, heure, duree, prixExam, niveau) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, c.getIdFormation());
            pstmt.setString(2, c.getDateExamen());
            pstmt.setString(3, c.getHeure());
            pstmt.setInt(4, c.getDuree());
            pstmt.setString(5, c.getPrixExam());
            pstmt.setString(6, c.getNiveau());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Certificat ajouté : " + c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur SQL lors de l'insertion : " + e.getMessage());
        }
    }

    @Override
    public void update(Certificat c) {
        String sql = "UPDATE Certificat SET idFormation = ?, dateExamen = ?, heure = ?, duree = ?, prixExam = ?, niveau = ? WHERE idCertif = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, c.getIdFormation());
            pstmt.setString(2, c.getDateExamen());
            pstmt.setString(3, c.getHeure());
            pstmt.setInt(4, c.getDuree());
            pstmt.setString(5, c.getPrixExam());
            pstmt.setString(6, c.getNiveau());
            pstmt.setInt(7, c.getIdCertif());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Certificat mis à jour : " + c);
            } else {
                System.out.println("Aucun certificat trouvé avec l'ID " + c.getIdCertif());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur SQL lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(Certificat c) {
        String sql = "DELETE FROM Certificat WHERE idCertif = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, c.getIdCertif());

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Certificat supprimé : " + c);
            } else {
                System.out.println("Aucun certificat trouvé avec l'ID " + c.getIdCertif());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur SQL lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Certificat> getAll() {
        List<Certificat> list = new ArrayList<>();
        String sql = "SELECT * FROM Certificat";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Certificat c = new Certificat(
                        rs.getInt("idCertif"),
                        rs.getInt("idFormation"),
                        rs.getString("dateExamen"),
                        rs.getString("heure"),
                        rs.getInt("duree"),
                        rs.getString("prixExam"),
                        rs.getString("niveau")
                );
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur SQL lors de la récupération des certificats : " + e.getMessage());
        }
        return list;
    }

    @Override
    public Certificat getById(int id) {
        String sql = "SELECT * FROM Certificat WHERE idCertif = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Certificat(
                        rs.getInt("idCertif"),
                        rs.getInt("idFormation"),
                        rs.getString("dateExamen"),
                        rs.getString("heure"),
                        rs.getInt("duree"),
                        rs.getString("prixExam"),
                        rs.getString("niveau")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur SQL lors de la recherche : " + e.getMessage());
        }
        return null;
    }
    public List<Integer> getAllFormationIds() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT idFormation FROM formations"; // Assurez-vous que la table existe
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(rs.getInt("idFormation"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
