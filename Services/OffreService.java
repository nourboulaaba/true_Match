package tn.esprit.pitest22.Services;



import tn.esprit.pitest22.Interfaces.IService;
import tn.esprit.pitest22.Model.Offre;
import tn.esprit.pitest22.Utils.DataSource;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;



public class OffreService implements IService<Offre> {
    private Connection connexion;
    private PreparedStatement pst;
    private static OffreService instance;

    public  OffreService() {
        connexion = DataSource.getInstance().getCnx();
    }

    DepartementService ds = new DepartementService();


    @Override
    public void add(Offre offre) {
        String request = "insert into offre (id,titre,description,salaireMin,salaireMax,departement_id) values (?,?,?,?,?,?)";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, offre.getId());
            pst.setString(2, offre.getTitre());
            pst.setString(3, offre.getDescription());
            pst.setInt(4, offre.getSalaireMin());
            pst.setInt(5, offre.getSalaireMax());
            pst.setInt(6, offre.getDepartement().getId());
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int id) {
        String request = "delete from offre where id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Offre offre, int id) {
        String request = "update offre set titre = ? , description = ? , salaireMin = ? , salaireMax = ? , departement_id = ? where id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setString(1, offre.getTitre());
            pst.setString(2, offre.getDescription());
            pst.setInt(3, offre.getSalaireMin());
            pst.setInt(4, offre.getSalaireMax());
            pst.setInt(5, offre.getDepartement().getId());
            pst.setInt(6, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Offre readById(int id) {
        String request = "select * from offre where id = ?";
        Offre offre = new Offre();
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            var result = pst.executeQuery();
            if (result.next()) {
                offre.setId(result.getInt("id"));
                offre.setTitre(result.getString("titre"));
                offre.setDescription(result.getString("description"));
                offre.setSalaireMin(result.getInt("salaireMin"));
                offre.setSalaireMax(result.getInt("salaireMax"));
                offre.setDepartement(ds.readById(result.getInt("departement_id")));
                return offre;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return offre;
    }

    @Override
    public List<Offre> readAll() {
        String request = "select * from offre";
        List<Offre> offres = new ArrayList<>();
        try {
            pst = connexion.prepareStatement(request);
            var result = pst.executeQuery();
            while (result.next()) {
                Offre offre = new Offre();
                offre.setId(result.getInt("id"));
                offre.setTitre(result.getString("titre"));
                offre.setDescription(result.getString("description"));
                offre.setSalaireMin(result.getInt("salaireMin"));
                offre.setSalaireMax(result.getInt("salaireMax"));
                offre.setDepartement(ds.readById(result.getInt("departement_id")));
                offres.add(offre);
            }
            return offres;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//CRUD
    //la relation ou communication avec la base de données
    //requetes SQL : Ajout , Suppression , Modification , Selection
    //les methodes : add , delete , update , readAll , readById

}
