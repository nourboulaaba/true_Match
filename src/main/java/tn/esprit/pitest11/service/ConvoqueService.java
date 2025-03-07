package tn.esprit.pitest11.service;



import tn.esprit.pitest11.Interfaces.IService;
import tn.esprit.pitest11.Entities.Convoque;
import tn.esprit.pitest11.utils.DataSource;
import tn.esprit.pitest11.example.dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class ConvoqueService implements IService<Convoque> {

    private Connection connexion;
    private PreparedStatement pst;

    private static ConvoqueService instance;

    public ConvoqueService () {
        connexion = DBConnection.getInstance().getConnection();

    }

    @Override
    public void add(Convoque convoque) {
        String request = "insert into convoque (nom,prenom,email,id_offre,id_entretien) values(?,?,?,?,?)";


        try {
            pst = connexion.prepareStatement(request);
            pst.setString(1, convoque.getNom());
            pst.setString(2, convoque.getPrenom());
            pst.setString(3, convoque.getEmail());
            pst.setInt(4, convoque.getId_offre().getId());
            pst.setInt(5, convoque.getEntretien().getId());
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String request = "delete from convoque where id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Convoque convoque, int id) {
        String request = "update convoque set nom = ?, prenom = ?, email = ?, id_offre = ? where id = ? ";
        try {
            pst = connexion.prepareStatement(request);
            pst.setString(1, convoque.getNom());
            pst.setString(2, convoque.getPrenom());
            pst.setString(3, convoque.getEmail());
            pst.setInt(4, convoque.getId_offre().getId());
            pst.setInt(5, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Convoque readById(int id) {
        String request = "select * from convoque where id = ?";
        Convoque convoque = new Convoque();
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            var result = pst.executeQuery();
            if(result.next()) {
                convoque.setId(result.getInt("id"));
                convoque.setNom(result.getString("nom"));
                convoque.setPrenom(result.getString("prenom"));
                convoque.setEmail(result.getString("email"));
                convoque.setId_offre(new OffreService().readById(result.getInt("id_offre")));
                convoque.setEntretien(new EntretienService().readById(result.getInt("id_entretien")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return convoque;

    }

    @Override
    public List<Convoque> readAll() {
        String query = "select * from convoque";
        List<Convoque> list = new ArrayList<>();
        try {
            var ste = connexion.createStatement();
            var rs = ste.executeQuery(query);
            while(rs.next()) {
                list.add(new Convoque(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        new OffreService().readById(rs.getInt("id_offre"))
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
