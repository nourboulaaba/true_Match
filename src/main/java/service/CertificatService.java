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
    public List<String> getAllCertificationsWithFormations() {
        List<String> result = new ArrayList<>();
        String query = "SELECT f.id, f.name, f.description, f.prix, c.idCertif " +
                "FROM formation f " +
                "JOIN certificat c ON f.id = c.idFormation";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idFormation = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double prix = rs.getDouble("prix");
                int idCertif = rs.getInt("idCertif");

                result.add("Formation: " + idFormation + " - " + name + " - " + description + " - " + prix +
                        " | Certificat: " + idCertif);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

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
        String query = "SELECT id FROM formation"; // Assurez-vous que la colonne 'id' existe bien

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) { // Ici, on utilise 'query' au lieu de 'sql'

            while (rs.next()) {
                list.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // Méthode pour obtenir tous les certificats sous forme d'objets
    public List<Certificat> getAllCertificats() {
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
    public void updateResultatExamen(int idCertif, String resultat) {
        String sql = "UPDATE Certificat SET resultatExamen = ? WHERE idCertif = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, resultat);
            pstmt.setInt(2, idCertif);
            pstmt.executeUpdate();
            System.out.println("Résultat mis à jour pour le certificat ID : " + idCertif);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour la date de reprogrammation
    public void updateDateReprogrammation(int idCertif, String newDate) {
        String sql = "UPDATE Certificat SET dateReprogrammation = ? WHERE idCertif = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newDate);
            pstmt.setInt(2, idCertif);
            pstmt.executeUpdate();
            System.out.println("Date de reprogrammation mise à jour pour le certificat ID : " + idCertif);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Certificat> getCertificatByFormation(int formationId) {
        List<Certificat> certificats = new ArrayList<>();
        String sql = "SELECT * FROM Certificat WHERE idFormation = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, formationId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Certificat certificat = new Certificat(
                        rs.getInt("idCertif"),
                        rs.getInt("idFormation"),
                        rs.getString("dateExamen"),
                        rs.getString("heure"),
                        rs.getInt("duree"),
                        rs.getString("prixExam"),
                        rs.getString("niveau")
                );
                certificats.add(certificat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur SQL lors de la recherche des certificats pour la formation " + formationId);
        }

        return certificats;
    }




}
