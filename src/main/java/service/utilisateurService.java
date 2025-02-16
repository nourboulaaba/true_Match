package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Entities.utilisateur;
import Entities.Role;
import org.example.dao.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

public class utilisateurService implements IService<utilisateur> {
    private Connection cnx;

    public utilisateurService() {
        cnx = DBConnection.getInstance().getConnection();
    }

    @Override
    public void insert(utilisateur obj) {
        String requete = "INSERT INTO utilisateur (lastName, firstName, identifier, password, jobPosition, role, email, employeeId, faceId, salary, hireDate, phoneNumber, cv, profilePhoto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, obj.getLastName());
            pst.setString(2, obj.getFirstName());
            pst.setString(3, obj.getIdentifier());
            String hashedPassword = BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt());
            pst.setString(4, hashedPassword);
            pst.setString(5, obj.getJobPosition());
            pst.setString(6, obj.getRole().name()); // Utilisation de .name() pour l'énumération
            pst.setString(7, obj.getEmail());
            pst.setString(8, obj.getEmployeeId());
            pst.setString(9, obj.getFaceId());
            pst.setDouble(10, obj.getSalary());
            pst.setString(11, obj.getHireDate());
            pst.setString(12, obj.getPhoneNumber());
            pst.setString(13, obj.getCv());
            pst.setString(14, obj.getProfilePhoto());
            pst.executeUpdate();
            System.out.println("Utilisateur inséré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(utilisateur obj) {
        String requete = "UPDATE utilisateur SET lastName = ?, firstName = ?, identifier = ?, email = ?, password = ?, jobPosition = ?, role = ?, employeeId = ?, faceId = ?, salary = ?, hireDate = ?, phoneNumber = ?, cv = ?, profilePhoto = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, obj.getLastName());
            pst.setString(2, obj.getFirstName());
            pst.setString(3, obj.getIdentifier());
            pst.setString(4, obj.getEmail());
            String hashedPassword = BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt());
            pst.setString(5, hashedPassword);
            pst.setString(6, obj.getJobPosition());
            pst.setString(7, obj.getRole().name()); // Utilisation de .name() pour l'énumération
            pst.setString(8, obj.getEmployeeId());
            pst.setString(9, obj.getFaceId());
            pst.setDouble(10, obj.getSalary());
            pst.setString(11, obj.getHireDate());
            pst.setString(12, obj.getPhoneNumber());
            pst.setString(13, obj.getCv());
            pst.setString(14, obj.getProfilePhoto());
            pst.setInt(15, obj.getId());
            pst.executeUpdate();
            System.out.println("Utilisateur mis à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(utilisateur obj) {
        String requete = "DELETE FROM utilisateur WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, obj.getId());
            pst.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<utilisateur> getAll() {
        List<utilisateur> utilisateurs = new ArrayList<>();
        String requete = "SELECT * FROM utilisateur";
        try (PreparedStatement pst = cnx.prepareStatement(requete);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                utilisateur u = new utilisateur(
                        rs.getInt("id"),
                        rs.getString("lastName"),
                        rs.getString("firstName"),
                        rs.getString("identifier"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("jobPosition"),
                        Role.valueOf(rs.getString("role")), // Conversion de String à Role
                        rs.getString("employeeId"),
                        rs.getString("faceId"),
                        rs.getDouble("salary"),
                        rs.getString("hireDate"),
                        rs.getString("phoneNumber"),
                        rs.getString("cv"),
                        rs.getString("profilePhoto")
                );
                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }

    @Override
    public utilisateur getById(int id) {
        String requete = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new utilisateur(
                            rs.getInt("id"),
                            rs.getString("lastName"),
                            rs.getString("firstName"),
                            rs.getString("identifier"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("jobPosition"),
                            Role.valueOf(rs.getString("role")), // Conversion de String à Role
                            rs.getString("employeeId"),
                            rs.getString("faceId"),
                            rs.getDouble("salary"),
                            rs.getString("hireDate"),
                            rs.getString("phoneNumber"),
                            rs.getString("cv"),
                            rs.getString("profilePhoto")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}