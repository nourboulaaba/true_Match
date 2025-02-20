package tn.esprit.pitest22.Services;



import tn.esprit.pitest22.Interfaces.IService;
import tn.esprit.pitest22.Model.User;
import tn.esprit.pitest22.Utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private Connection connexion;
    private PreparedStatement pst;
    private static UserService instance;

    public UserService() {
        connexion = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(User user) {
        String request = "insert into user (id,nom,role) values (?,?,?)";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, user.getId());
            pst.setString(2, user.getNom());
            pst.setString(3, user.getRole());
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int id) {
        String request = "delete from user where id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(User user, int id) {
        String request = "update user set nom = ? , role = ? where id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setString(1, user.getNom());
            pst.setString(2, user.getRole());
            pst.setInt(3, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User readById(int id) {
        String request = "select * from user where id = ?";
        User user = new User();

        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            var result = pst.executeQuery();
            if (result.next()) {
                user.setId(result.getInt("id"));
                user.setNom(result.getString("nom"));
                user.setRole(result.getString("role"));
                return user;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public List<User> readAll() {
        String request = "select * from user";
        List <User> users = new ArrayList<>();
        try {
            pst = connexion.prepareStatement(request);
            var result = pst.executeQuery();
            while (result.next()) {
                User user = new User();
                user.setId(result.getInt("id"));
                user.setNom(result.getString("nom"));
                user.setRole(result.getString("role"));
                users.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
