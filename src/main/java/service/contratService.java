//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service;

import Entities.Contrat;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.dao.DBConnection;

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
