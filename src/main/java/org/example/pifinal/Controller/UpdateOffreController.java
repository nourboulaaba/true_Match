package org.example.pifinal.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.pifinal.Model.Offre;
import org.example.pifinal.Services.OffreService;

public class UpdateOffreController {
    private Offre offre;
    private DashOffreController dashOffreController; // Reference to refresh after update
    private final OffreService offreService = new OffreService();

    public void setOffre(Offre offre, DashOffreController controller) { // Accept DashOffreController
        this.offre = offre;
        this.dashOffreController = controller;
        populateFields();
    }

    @FXML
    private Button cancelButton;

    @FXML
    private TextField departementField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField salaireMaxField;

    @FXML
    private TextField salaireMinField;

    @FXML
    private TextField titreField;

    @FXML
    private Button updateButton;

    private void populateFields() {
        if (offre != null) {
            titreField.setText(offre.getTitre());
            descriptionField.setText(offre.getDescription());
            salaireMinField.setText(String.valueOf(offre.getSalaireMin()));
            salaireMaxField.setText(String.valueOf(offre.getSalaireMax()));
            departementField.setText(offre.getDepartement().getNom());
            departementField.setDisable(true); // Departement cannot be changed
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        cancelButton.getScene().getWindow().hide(); // Close the pop-up
    }

    @FXML
    void update(ActionEvent event) {
        if (validateInputs()) {
            offre.setTitre(titreField.getText());
            offre.setDescription(descriptionField.getText());
            offre.setSalaireMin(Integer.parseInt(salaireMinField.getText()));
            offre.setSalaireMax(Integer.parseInt(salaireMaxField.getText()));

            offreService.update(offre, offre.getId());
            showAlert("Succès", "Offre mise à jour avec succès !");
            if (dashOffreController != null) {
                dashOffreController.populateListView(); // Refresh the main list
                dashOffreController.populateCharts();
            }
            cancel(event); // Close pop-up
        }
    }

    private boolean validateInputs() {
        if (titreField.getText().trim().isEmpty() ||
                descriptionField.getText().trim().isEmpty() ||
                salaireMinField.getText().trim().isEmpty() ||
                salaireMaxField.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return false;
        }

        try {
            int salaireMin = Integer.parseInt(salaireMinField.getText());
            int salaireMax = Integer.parseInt(salaireMaxField.getText());
            if (salaireMin > salaireMax) {
                showAlert("Erreur", "Le salaire minimum ne peut pas être supérieur au maximum.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides pour les salaires.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
