package tn.esprit.pitest11.Services;

import tn.esprit.pitest11.Interfaces.IService;
import tn.esprit.pitest11.Model.User;
import tn.esprit.pitest11.Utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private final Connection connexion;
    private PreparedStatement pst;

    public UserService() {
        connexion = DataSource.getInstance().getConnection();
    }

    @Override
    public void add(User user) {
        String request = "INSERT INTO user (id, nom, role) VALUES (?, ?, ?)";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, user.getId());
            pst.setString(2, user.getNom());
            pst.setString(3, user.getRole());
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String request = "DELETE FROM user WHERE id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(User user, int id) {
        String request = "UPDATE user SET nom = ?, role = ? WHERE id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setString(1, user.getNom());
            pst.setString(2, user.getRole());
            pst.setInt(3, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage(), e);
        }
    }

    @Override
    public User readById(int id) {
        String request = "SELECT * FROM user WHERE id = ?";
        User user = null;
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            ResultSet result = pst.executeQuery();
            if (result.next()) {
                user = new User();
                user.setId(result.getInt("id"));
                user.setNom(result.getString("nom"));
                user.setRole(result.getString("role"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur: " + e.getMessage(), e);
        }
        return user;
    }

    @Override
    public List<User> readAll() {
        String request = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try {
            pst = connexion.prepareStatement(request);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                User user = new User();
                user.setId(result.getInt("id"));
                user.setNom(result.getString("last_name"));
                user.setRole(result.getString("role"));
                users.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des utilisateurs: " + e.getMessage(), e);
        }
        return users;
    }
}
