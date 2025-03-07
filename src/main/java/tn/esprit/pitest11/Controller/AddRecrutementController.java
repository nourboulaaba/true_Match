package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import tn.esprit.pitest11.Entities.Offre;
import tn.esprit.pitest11.Entities.Recrutement;
import tn.esprit.pitest11.service.OffreService;
import tn.esprit.pitest11.service.RecrutementService;
import tn.esprit.pitest11.utils.TwilioAPI;

import java.util.List;

public class AddRecrutementController {

    private DashAdminRecrutementController parentController;
    private final OffreService offreService = new OffreService();
    private final RecrutementService recrutementService = new RecrutementService();

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField nbEntretienField;

    @FXML
    private ComboBox<Offre> offreComboBox;

    @FXML
    private Button saveButton;

    /**
     * Sets the parent controller to allow refreshing the ListView.
     */
    public void setDashAdminRecrutementController(DashAdminRecrutementController controller) {
        this.parentController = controller;
    }

    /**
     * Populates the `offreComboBox` with available offers.
     */
    public void populateOffreComboBox() {
        List<Offre> offres = offreService.readAll();
        offreComboBox.getItems().addAll(offres);
    }

    /**
     * Handles canceling the form and closing the pop-up.
     */
    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles saving a new `Recrutement` to the database.
     */
    @FXML
    void handleSave(ActionEvent event) {
        if (offreComboBox.getValue() == null || dateDebutPicker.getValue() == null || nbEntretienField.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        try {
            Recrutement newRecrutement = new Recrutement();
            newRecrutement.setOffre(offreComboBox.getValue());
            newRecrutement.setDateDebut(dateDebutPicker.getValue());
            newRecrutement.setDateFin(dateFinPicker.getValue()); // Can be null
            newRecrutement.setNbEntretien(Integer.parseInt(nbEntretienField.getText()));

            // Save to database
            recrutementService.add(newRecrutement);
            TwilioAPI twilioAPI = new TwilioAPI();
            twilioAPI.sendSMS("+21650316723","L'offre du recrutement  : "+newRecrutement.getOffre().getTitre());

            // Refresh parent view
            if (parentController != null) {
                parentController.populateListView();
                parentController.populateCharts();
            }

            // Close the pop-up
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Number of interviews must be a valid integer.");
        }
    }

    /**
     * Displays an alert message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
