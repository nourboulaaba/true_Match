package tn.esprit.pitest11.Services;

import tn.esprit.pitest11.Interfaces.IService;
import tn.esprit.pitest11.Model.User;
import tn.esprit.pitest11.Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {
    private Connection connexion;
    private static UserService instance;

    public UserService() {
        connexion = DataSource.getInstance().getCnx();
    }

    // CREATE (ADD NEW USER)
    @Override
    public void add(User user) {
        String sql = "INSERT INTO User (name, role) VALUES (?, ?)";
        try (PreparedStatement pst = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, user.getName());
            pst.setString(2, user.getRole());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1)); // Assign generated ID to the object
            }
            System.out.println("User added successfully: " + user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE USER
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM User WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully with ID: " + id);
            } else {
                System.out.println("No user found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE USER
    @Override
    public void update(User user, int id) {
        String sql = "UPDATE User SET name = ?, role = ? WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setString(1, user.getName());
            pst.setString(2, user.getRole());
            pst.setInt(3, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully: " + user);
            } else {
                System.out.println("No user found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // FIND USER BY ID
    @Override
    public User readById(int id) {
        String sql = "SELECT * FROM User WHERE id = ?";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // FIND ALL USERS
    @Override
    public List<User> readAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (PreparedStatement pst = connexion.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
