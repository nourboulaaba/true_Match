package tn.esprit.pitest11.service;
import tn.esprit.pitest11.Entities.Conges;
import tn.esprit.pitest11.utils.CongeRequest;
import java.sql.SQLException;
import java.util.List;

public class CongeService {

    private final CongeRequest congeRequest = new CongeRequest();
    private static final int TOTAL_CONGES_ANNUELS = 30;

    public List<Conges> getAllConges() throws SQLException {
        return congeRequest.getAllConges();
    }

    public int calculerSoldeConges(int employeId) {
        if (!congeRequest.employeExiste(employeId)) {
            return -1; // Retourne -1 si l'employé n'existe pas
        }

        int joursPris = congeRequest.getTotalCongesPris(employeId);
        return TOTAL_CONGES_ANNUELS - joursPris;
    }

}
