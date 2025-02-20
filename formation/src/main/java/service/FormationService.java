package service;

import entite.formations;
import org.example.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormationService implements IService<formations> {

    private Connection connection;

    public FormationService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void insert(formations f) {
        String sql = "INSERT INTO formations (name, description, prix) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, f.getName());
            pstmt.setString(2, f.getDescription());
            pstmt.setString(3, f.getPrix());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    f.setId(generatedKeys.getInt(1));
                }
                System.out.println("✅ Formation ajoutée : " + f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Erreur SQL lors de l'insertion : " + e.getMessage());
        }
    }

    @Override
    public void update(formations f) {
        String sql = "UPDATE formations SET name = ?, description = ?, prix = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, f.getName());
            pstmt.setString(2, f.getDescription());
            pstmt.setString(3, f.getPrix());
            pstmt.setInt(4, f.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Formation mise à jour : " + f);
            } else {
                System.out.println("⚠️ Aucune formation trouvée avec l'ID " + f.getId());
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(formations f) {
        String sql = "DELETE FROM formations WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, f.getId());

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Formation supprimée : " + f);
            } else {
                System.out.println("⚠️ Aucune formation trouvée avec l'ID " + f.getId());
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<formations> getAll() {
        List<formations> list = new ArrayList<>();
        String sql = "SELECT * FROM formations";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                formations f = new formations(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("prix")
                );
                list.add(f);
            }
            System.out.println("✅ Nombre de formations récupérées : " + list.size());

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la récupération des formations : " + e.getMessage());
        }
        return list;
    }

    @Override
    public formations getById(int id) {
        String sql = "SELECT * FROM formations WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new formations(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("prix")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la recherche : " + e.getMessage());
        }
        return null;
    }
}
