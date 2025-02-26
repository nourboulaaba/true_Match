package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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


        String requete = "INSERT INTO utilisateur (lastName, firstName, identifier , email,password, CIN, role, faceId, salary, hireDate, phoneNumber, cv, profilePhoto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, obj.getLastName());
            pst.setString(2, obj.getFirstName());
            pst.setString(3, obj.getIdentifier());
            pst.setString(4, obj.getEmail());
            pst.setString(5, BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt()));
            pst.setString(6, obj.getCIN());
            pst.setString(7, obj.getRole().name());
            pst.setString(8, obj.getFaceId());
            pst.setDouble(9, obj.getSalary());
            pst.setString(10, obj.getHireDate());
            pst.setString(11, obj.getPhoneNumber());
            pst.setString(12, obj.getCv());
            pst.setString(13, obj.getProfilePhoto());

            pst.executeUpdate();
            System.out.println("Utilisateur inséré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void update(utilisateur obj) {


        String requete = "UPDATE utilisateur SET lastName = ?, firstName = ?, identifier = ?, email = ?, password = ?, CIN = ?, role = ?, faceId = ?, salary = ?, hireDate = ?, phoneNumber = ?, cv = ?, profilePhoto = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, obj.getLastName());
            pst.setString(2, obj.getFirstName());
            pst.setString(3, obj.getIdentifier());
            pst.setString(4, obj.getEmail());
            pst.setString(5, BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt()));
            pst.setString(6, obj.getCIN());
            pst.setString(7, obj.getRole().name());
            pst.setString(8, obj.getFaceId());
            pst.setDouble(9, obj.getSalary());
            pst.setString(10, obj.getHireDate());
            pst.setString(11, obj.getPhoneNumber());
            pst.setString(12, obj.getCv());
            pst.setString(13, obj.getProfilePhoto());
            pst.setInt(14, obj.getId());
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
    public void deleteByID(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID utilisateur invalide : " + id);
        }
        String requete = "DELETE FROM utilisateur WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Utilisateur avec l'ID " + id + " supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID " + id + ".");
                throw new SQLException("Aucun utilisateur trouvé avec l'ID " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    @Override
    public List<utilisateur> getAll() {
        List<utilisateur> utilisateurs = new ArrayList<>();
        String requete = "SELECT * FROM utilisateur";

        try (PreparedStatement pst = cnx.prepareStatement(requete);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                utilisateur u = mapResultSetToUtilisateur(rs);
                if (u != null) {
                    utilisateurs.add(u);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }
    public utilisateur getByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, storedPassword)) {
                    return mapResultSetToUtilisateur(rs);  // Mapper le ResultSet à un objet utilisateur
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Aucun utilisateur trouvé ou mot de passe incorrect
    }



    @Override
    public utilisateur getById(int id) {
        String requete = "SELECT * FROM utilisateur WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur ID " + id + ": " + e.getMessage());
        }
        return null;
    }

    private utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        try {
            // Récupérer les valeurs du ResultSet
            String lastName = rs.getString("lastName");
            String firstName = rs.getString("firstName");
            String identifier = rs.getString("identifier");
            String email = rs.getString("email");
            String password = rs.getString("password");
            String phoneNumber = rs.getString("phoneNumber");  // Nouveau champ ajouté
            String CIN = rs.getString("CIN");
            String faceId = rs.getString("faceId");
            double salary = rs.getDouble("salary");

            // Gestion sécurisée du rôle
            Role role = null;
            try {
                String roleString = rs.getString("role");
                if (roleString != null) {
                    role = Role.valueOf(roleString.toUpperCase());  // Conversion sécurisée du rôle
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Erreur de conversion du rôle pour l'utilisateur ID " + rs.getInt("id") + ": " + e.getMessage());
            }

            String hireDate = rs.getString("hireDate");
            String cv = rs.getString("cv");
            String profilePhoto = rs.getString("profilePhoto");

            // Créer et retourner l'objet utilisateur
            return new utilisateur(
                    lastName, firstName, identifier, email, password,
                    phoneNumber, CIN, faceId, salary,
                    role, hireDate, cv, profilePhoto
            );
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            throw e; // Lancer l'exception si une erreur se produit
        }
    }
    public utilisateur getConnectedUser() {
        utilisateur user = null;
        String query = "SELECT * FROM utilisateur WHERE id = 1"; // Adapte selon ta base de données

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                user = new utilisateur();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("prenom"));
                user.setLastName(resultSet.getString("nom"));
                user.setEmail(resultSet.getString("email"));
                user.setProfilePhoto(resultSet.getString("photo_profil")); // Vérifie le nom de la colonne
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }




}
