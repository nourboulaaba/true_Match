package org.example.pifinal.Services;

import org.example.pifinal.Interfaces.IService;
import org.example.pifinal.Model.Departement;
import org.example.pifinal.Utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class DepartementService implements IService<Departement> {

    private Connection connexion;
    private PreparedStatement pst;
    private static DepartementService instance;

    UserService us = new UserService();

    public DepartementService() {
        connexion = DataSource.getInstance().getConnection();
    }

    @Override
    public void add(Departement departement) {
        String request = "insert into departement (id,nom,Responsable_ID,Budget,NbEmploye) values (?,?,?,?,?)";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, departement.getId());
            pst.setString(2, departement.getNom());
            pst.setInt(3, departement.getResponsable().getId());
            pst.setInt(4, departement.getBudget());
            pst.setInt(5, departement.getNbEmployes());
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int id) {
        String request = "delete from departement where id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Departement departement, int id) {
        String request = "update departement set nom = ?, Responsable_ID = ?,Budget = ? , NbEmploye = ?  where id = ?";
        try {
            pst = connexion.prepareStatement(request);
            pst.setString(1, departement.getNom());

            pst.setInt(2, departement.getResponsable().getId());
            pst.setInt(3, departement.getBudget());
            pst.setInt(4, departement.getNbEmployes());
            pst.setInt(5, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Departement readById(int id) {
        String request = "select * from departement where id = ?";
        Departement departement = new Departement();
        try {
            pst = connexion.prepareStatement(request);
            pst.setInt(1, id);
            var result = pst.executeQuery();
            if (result.next()) {
                departement.setId(result.getInt("id"));
                departement.setNom(result.getString("nom"));
                departement.setBudget(result.getInt("Budget"));
                departement.setNbEmployes(result.getInt("NbEmploye"));
                departement.setResponsable(us.readById(result.getInt("Responsable_ID")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return departement;
    }

    @Override
    public List<Departement> readAll() {
        String request = "select * from departement";
        List<Departement> departements = new java.util.ArrayList<>();
        try {
            pst = connexion.prepareStatement(request);
            var result = pst.executeQuery();
            while (result.next()) {
                Departement departement = new Departement();
                departement.setId(result.getInt("id"));
                departement.setNom(result.getString("nom"));
                departement.setBudget(result.getInt("Budget"));
                departement.setNbEmployes(result.getInt("NbEmploye"));
                departement.setResponsable(us.readById(result.getInt("Responsable_ID")));
                departements.add(departement);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return departements;
    }
}

