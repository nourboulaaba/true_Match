package tn.esprit.pitest11.Services;


import tn.esprit.pitest11.Interfaces.IService;
import tn.esprit.pitest11.Model.Entretien;
import tn.esprit.pitest11.Utils.DataSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class EntretienService implements IService<Entretien> {

    private Connection connexion;
    private PreparedStatement pst;

    private static EntretienService instance;

    public EntretienService() {
        connexion = DataSource.getInstance().getCnx();
    }

    @Override
    public void add(Entretien t) {
        String request = "insert into entretien (nom,Date,duree,lieu,longitude,latitude,responsable_id,recrutement_id) values(?,?,?,?,?,?,?,?)";

        try {
            pst = connexion.prepareStatement(request);
            pst.setString(1, t.getNom());
            pst.setDate(2, Date.valueOf(t.getDate()));
            pst.setInt(3, t.getDuree());
            pst.setString(4, t.getLieu());
            pst.setDouble(5, t.getLongitude());
            pst.setDouble(6, t.getLatitude());
            pst.setInt(7, t.getResponsable().getId());
            pst.setInt(8, t.getRecrutement().getId());
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void delete(int id) {
        String request = "delete from entretien where id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void update(Entretien t, int id) {
        String request = "update entretien set nom = ?, Date = ?, duree = ?, lieu = ?, longitude = ?, latitude = ?, responsable_id = ?,recrutement_id = ? where id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setString(1, t.getNom());
            pst.setDate(2, Date.valueOf(t.getDate()));
            pst.setInt(3, t.getDuree());
            pst.setString(4, t.getLieu());
            pst.setDouble(5, t.getLongitude());
            pst.setDouble(6, t.getLatitude());
            pst.setInt(7, t.getResponsable().getId());
            pst.setInt(8, t.getRecrutement().getId());
            pst.setInt(9, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Entretien readById(int id) {
        String request = "select * from entretien where id = ?";
        Entretien entretien = new Entretien();
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            var result = pst.executeQuery();
            if (result.next()) {
                entretien.setId(result.getInt("id"));
                entretien.setNom(result.getString("nom"));
                entretien.setDate(result.getDate("Date").toLocalDate());
                entretien.setDuree(result.getInt("duree"));
                entretien.setLieu(result.getString("lieu"));
                entretien.setLongitude(result.getDouble("longitude"));
                entretien.setLatitude(result.getDouble("latitude"));
                entretien.setResponsable(new UserService().readById(result.getInt("responsable_id")));
                entretien.setRecrutement(new RecrutementService().readById(result.getInt("recrutement_id")));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entretien;
    }

    @Override
    public List<Entretien> readAll() {
        String request = "select * from entretien";
        List<Entretien> entretiens = new ArrayList<>();
        try{
            pst = connexion.prepareStatement(request);
            var result = pst.executeQuery();
            while (result.next()) {
                Entretien entretien = new Entretien();
                entretien.setId(result.getInt("id"));
                entretien.setNom(result.getString("nom"));
                entretien.setDate(result.getDate("Date").toLocalDate());
                entretien.setDuree(result.getInt("duree"));
                entretien.setLieu(result.getString("lieu"));
                entretien.setLongitude(result.getDouble("longitude"));
                entretien.setLatitude(result.getDouble("latitude"));
                entretien.setResponsable(new UserService().readById(result.getInt("responsable_id")));
                entretien.setRecrutement(new RecrutementService().readById(result.getInt("recrutement_id")));
                entretiens.add(entretien);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entretiens;
    }


}

