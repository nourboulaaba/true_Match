package controllerConge;

import entite.Conges;
import utils.CongeRequest;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class Formulaire {
    @FXML private TextField idEmployeField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private ComboBox<String> typeCongeBox;
    @FXML private Button validerButton; // Correction du nom du bouton

    @FXML
    public void initialize() {
        // Ajout des options pour le type de congé
        typeCongeBox.getItems().addAll("Annuel", "Maladie", "Sans solde", "Maternité");
    }

    @FXML
    private void handleSave() {
        try {
            int idEmploye = Integer.parseInt(idEmployeField.getText());
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();
            String typeConge = typeCongeBox.getValue();
            String statut = "En attente"; // Statut fixe

            if (dateDebut == null || dateFin == null || typeConge == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs !");
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                showAlert("Erreur", "La date de fin doit être après la date de début !");
                return;
            }

            // Création et enregistrement du congé
            CongeRequest congesRequest = new CongeRequest();
            Conges conge = new Conges(0, idEmploye, java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin), typeConge, statut);
            congesRequest.addCongeRequest(conge);

            showAlert("Succès", "Congé enregistré avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID Employé doit être un nombre !");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'enregistrement !");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
