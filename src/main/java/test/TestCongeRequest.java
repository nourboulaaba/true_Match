package test;

import entite.Conges;
import utils.CongeRequest;

import java.sql.SQLException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestCongeRequest {
    public static void main(String[] args) {
        CongeRequest congeRequest = new CongeRequest();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Conversion des dates
            Date dateDebut = new Date(dateFormat.parse("2025-03-01").getTime());
            Date dateFin = new Date(dateFormat.parse("2025-03-10").getTime());

            // 1️⃣ Ajouter un congé avec un type valide
            Conges newConge = new Conges(0, 1, dateDebut, dateFin, "Annuel", "En attente");
            int idCongeGenere= congeRequest.addCongeRequest(newConge);
            System.out.println("✅ Congé ajouté avec ID : " + idCongeGenere);

            // 2️⃣ Modifier le congé (mise à jour du statut)
            newConge.setIdConge(idCongeGenere); // Assigner l'ID généré
            newConge.setStatut("Approuvé");
            congeRequest.updateCongeRequest(newConge);
            System.out.println("✅ Congé mis à jour avec statut : " + newConge.getStatut());

            // 3️⃣ Supprimer le congé
            congeRequest.deleteCongeRequest(idCongeGenere);
            System.out.println("✅ Congé supprimé avec ID : " + idCongeGenere);

        } catch (ParseException e) {
            System.err.println("❌ Erreur de format de date !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors du traitement des congés !");
            e.printStackTrace();
        }
    }
}




