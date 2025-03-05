package controllerConge;

import entite.Conges;
import javafx.event.ActionEvent;
import service.MailService;
import utils.CongeRequest;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.mail.MessagingException;
import java.sql.SQLException;
import java.time.LocalDate;

public class Formulaire {
    @FXML private TextField idEmployeField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private ComboBox<String> typeCongeBox;
    @FXML private Button validerButton;

    @FXML
    public void initialize() {
        System.out.println("Initialisation du formulaire...");

        if (validerButton == null) {
            System.out.println("⚠ validerButton est NULL !");
        } else {
            System.out.println("✅ validerButton chargé avec succès.");
        }

        typeCongeBox.getItems().addAll("Annuel", "Maladie", "Sans solde", "Maternité");
    }

    @FXML
    private void handleSave() {
        try {
            System.out.println("handleSave() called"); // Debug

            if (idEmployeField.getText().isEmpty()) {
                showAlert("Erreur", "L'ID Employé est requis !");
                return;
            }

            int idEmploye;
            try {
                idEmploye = Integer.parseInt(idEmployeField.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID Employé doit être un nombre valide !");
                return;
            }

            // Vérifier si l'employé existe
            CongeRequest congeRequest = new CongeRequest();
            if (!congeRequest.doesEmployeExist(idEmploye)) {
                showAlert("Erreur", "L'ID Employé n'existe pas dans la base de données !");
                return;
            }

            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();
            String typeConge = typeCongeBox.getValue();

            if (dateDebut == null || dateFin == null || typeConge == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs !");
                return;
            }
            if (dateFin.isBefore(dateDebut)) {
                showAlert("Erreur", "La date de fin doit être après la date de début !");
                return;
            }

            // Sauvegarde dans la base de données
            congeRequest.addCongeRequest(new Conges(0, idEmploye, java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin), typeConge, "En attente"));

            // Envoi de l'email
            MailService.sendLeaveRequestNotification("Employé " + idEmploye, typeConge, dateDebut.toString(), dateFin.toString());

            showAlert("Succès", "Congé enregistré et email envoyé au RH !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue est survenue : " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleValider(ActionEvent actionEvent) {
        handleSave();
    }
}
