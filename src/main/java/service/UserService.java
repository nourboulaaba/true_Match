package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Entities.User;
import Entities.Role;
import org.example.dao.DBConnection;
import org.mindrot.jbcrypt.BCrypt;


public class UserService implements IService<User> {
    private final Connection cnx = DBConnection.getInstance().getConnection();


    //@Override
    public boolean insert(User obj) {
        System.out.println("_______________________________>insert1 ");

        if (!validateUtilisateur(obj)) {
            System.out.println("❌ Validation échouée. L'utilisateur n'a pas été ajouté.");
            return false;
        }
        System.out.println("_______________________________>insert ");
        if (isEmailAlreadyRegistered(obj.getEmail())) return false;

        String requete = "INSERT INTO user (last_name, first_name, identifier , email, password, cin, role, face_id, salary, hire_date, phone_number, cv, profile_photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, obj.getLastName());
            pst.setString(2, obj.getFirstName());
            pst.setString(3, obj.getIdentifier());
            pst.setString(4, obj.getEmail());
            pst.setString(5, BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt()));
            pst.setString(6, obj.getCin());
            pst.setString(7, obj.getRole().name());
            pst.setString(8, obj.getFaceId());
            pst.setDouble(9, obj.getSalary());
            pst.setString(10, obj.getHireDate());
            pst.setString(11, obj.getPhoneNumber());
            pst.setString(12, obj.getCv());
            pst.setString(13, obj.getProfilePhoto());

            pst.executeUpdate();
            System.out.println("Utilisateur inséré avec succès !");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(User obj) {
        if (!validateUtilisateur(obj)) {
            System.out.println("❌ Validation échouée. L'utilisateur n'a pas été mis à jour.");
            return;
        }

        String requete = "UPDATE user SET last_name = ?, first_name = ?, identifier = ?, email = ?, password = ?, cin = ?, role = ?, face_id = ?, salary = ?, hire_date = ?, phone_number = ?, cv = ?, profile_photo = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, obj.getLastName());
            pst.setString(2, obj.getFirstName());
            pst.setString(3, obj.getIdentifier());
            pst.setString(4, obj.getEmail());
            pst.setString(5, BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt()));
            pst.setString(6, obj.getCin());
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
    public void delete(User obj) {
        String requete = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, obj.getId());
            pst.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByID(int id) throws SQLException {

        String query = "DELETE FROM user WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();

    }

    @Override
    public List<User> getAll() {
        List<User> utilisateurs = new ArrayList<>();
        String requete = "SELECT * FROM user";

        try (PreparedStatement pst = cnx.prepareStatement(requete);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                User u = mapResultSetToUtilisateur(rs);
                if (u != null) {
                    utilisateurs.add(u);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    public User getByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, storedPassword)) {
                    User user = mapResultSetToUtilisateur(rs);
                    UserSession.setCurrentUser(user); // Définir l'utilisateur connecté
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getById(int id) {
        String requete = "SELECT * FROM user WHERE id = ?";
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

    private User mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        try {
            String lastName = rs.getString("last_name");
            String firstName = rs.getString("first_name");
            String email = rs.getString("email");
            String phoneNumber = rs.getString("phone_number");
            String password = rs.getString("password");

            // Vous pouvez définir des valeurs par défaut pour les autres attributs
            String cin = rs.getString("cin"); // Valeur par défaut
            String faceId = rs.getString("face_id");// Valeur par défaut
            double salary = rs.getDouble("salary"); // Valeur par défaut
            String hireDate = rs.getString("hire_date"); // Valeur par défaut
            String cv = rs.getString("cv");// Valeur par défaut
            String profilePhoto =rs.getString("profile_photo"); // Valeur par défaut
            String identifier =rs.getString("identifier"); // Valeur par défaut
            int id =rs.getInt("id"); // Valeur par défaut
            Role role = Role.valueOf(rs.getString("role")); // Valeur par défaut

            // Créer l'objet utilisateur avec les 5 premiers paramètres
            return new User(id, lastName, firstName, identifier, email, cin, role, faceId, salary, hireDate, phoneNumber, cv, profilePhoto, password);

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            throw e;
        }
    }

    // Vérifier si l'email est déjà enregistré
    public boolean isEmailAlreadyRegistered(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Si le count est plus grand que 0, l'email existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /*
    public boolean registerUser(utilisateur user) {
        if (isEmailAlreadyRegistered(user.getEmail())) return false;

        String sql = "INSERT INTO utilisateur (lastName, firstName, email, phoneNumber, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, user.getLastName());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getPassword()); // Le mot de passe déjà haché

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Si l'insertion réussit, retourner true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /
 */


    // Méthode de validation des données saisies
    public boolean validateUtilisateur(User obj) {
        if (obj.getFirstName() == null || obj.getFirstName().trim().isEmpty()) {
            System.out.println("❌ Le prénom ne peut pas être vide !");
            return false;
        }
        if (obj.getLastName() == null || obj.getLastName().trim().isEmpty()) {
            System.out.println("❌ Le nom ne peut pas être vide !");
            return false;
        }
        if (obj.getEmail() == null || !obj.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.println("❌ Email invalide !");
            return false;
        }
        if (obj.getPassword() == null || obj.getPassword().length() < 8) {
            System.out.println("❌ Le mot de passe doit contenir au moins 8 caractères !");
            return false;
        }
        if (obj.getPhoneNumber() == null || !obj.getPhoneNumber().matches("^\\d{8,15}$")) {
            System.out.println("❌ Numéro de téléphone invalide !");
            return false;
        }
        if (obj.getCin() == null || !obj.getCin().matches("^\\d{8,15}$")) {
            System.out.println("❌ CIN invalide !");
            return false;
        }
        return true;
    }


    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");
                String identifier = rs.getString("identifier");
                String password = rs.getString("password");
                String CIN = rs.getString("cin");
                String faceId = rs.getString("face_id");
                double salary = rs.getDouble("salary");
                String hireDate = rs.getString("hire_date");
                String phoneNumber = rs.getString("phone_number");
                String cv = rs.getString("cv");
                String profilePhoto = rs.getString("profile_photo");
                int id = rs.getInt("id");
                Role role = Role.valueOf(rs.getString("role"));

                return new User(id, lastName, firstName, identifier, email, CIN,
                        role, faceId, salary, hireDate, phoneNumber, cv, profilePhoto, password);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by email: " + e.getMessage());
        }
        return null;
    }

    public void updateOne(User user) throws SQLException {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        String req = "UPDATE user SET last_name = ?, first_name = ?, identifier = ?, email = ?, password = ?, cin = ?, role = ?, face_id = ?, salary = ?, hire_date = ?, phone_number = ?, cv = ?, profile_photo = ? WHERE id = ?";


        PreparedStatement pst = cnx.prepareStatement(req);

        pst.setString(1, user.getLastName());
        pst.setString(2, user.getFirstName());
        pst.setString(3, user.getIdentifier());
        pst.setString(4, user.getEmail());
       // pst.setString(5, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        pst.setString(5, user.getPassword());
        pst.setString(6, user.getCin());
        pst.setString(7, user.getRole().name());
        pst.setString(8, user.getFaceId());
        pst.setDouble(9, user.getSalary());
        pst.setString(10, user.getHireDate());
        pst.setString(11, user.getPhoneNumber());
        pst.setString(12, user.getCv());
        pst.setString(13, user.getProfilePhoto());
        pst.setInt(14, user.getId());


        pst.executeUpdate();


    }


    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // If count > 0, email exists
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        return false; // Default to false in case of exceptions or no result
    }



    public String changePassword(User user)throws SQLException{
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        String req = "UPDATE `user` SET `password`=? WHERE `id`=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);

            ps.setString(1, user.getPassword());
            ps.setInt(2, user.getId());

            ps.executeUpdate();
            return hashedPassword;
        } catch (SQLException e){
            // Handle SQLException appropriately, e.g., log or propagate
            System.err.println("Error adding user: " + e.getMessage());
            return null;
        }
    }
    public User insertGoogleSignup(User user){
        String insertQuery = "INSERT INTO user (last_name, first_name, email, password, role, profile_photo, salary) VALUES (?, ?, ?, ?, ?, ?, 50)";

        try (PreparedStatement stmt = cnx.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {  // Include RETURN_GENERATED_KEYS here
            stmt.setString(1, user.getLastName());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, null);  // No password needed for Google login
            stmt.setString(5, user.getRole().name());  // Default role for Google login
            stmt.setString(6, user.getProfilePhoto());

            int rowsAffected = stmt.executeUpdate();

            // If insert is successful, retrieve the generated ID
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);  // Get the generated ID
                        user.setId(generatedId);  // Set the ID on the User object
                    }
                }
            }

            return user;

        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return null;
        }
    }
}
