package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Recrutement;
import tn.esprit.pitest11.Services.RecrutementService;

public class UpdateRecrutementController {

    private Recrutement recrutement;
    private RecrutementService recrutementService = new RecrutementService();
    private DashAdminRecrutementController parentController;

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField nbEntretienField;

    @FXML
    private Button updateButton;

    /**
     * Sets the `Recrutement` to be updated and populates the fields.
     */
    public void setRecrutement(Recrutement recrutement, DashAdminRecrutementController parentController) {
        this.recrutement = recrutement;
        this.parentController = parentController;
        populateFields();
    }

    /**
     * Populates the input fields with the existing `Recrutement` data.
     */
    private void populateFields() {
        if (recrutement != null) {
            dateDebutPicker.setValue(recrutement.getDateDebut());
            dateFinPicker.setValue(recrutement.getDateFin());
            nbEntretienField.setText(String.valueOf(recrutement.getNbEntretien()));
        }
    }

    /**
     * Handles updating a `Recrutement`, saving the changes, and refreshing the parent view.
     */
    @FXML
    void handleUpdate(ActionEvent event) {
        try {
            // Validate input fields
            if (dateDebutPicker.getValue() == null || nbEntretienField.getText().isEmpty()) {
                showAlert("Validation Error", "Please fill in all required fields.");
                return;
            }

            // Update the Recrutement object
            recrutement.setDateDebut(dateDebutPicker.getValue());
            recrutement.setDateFin(dateFinPicker.getValue()); // Can be null
            recrutement.setNbEntretien(Integer.parseInt(nbEntretienField.getText()));

            // Save the updated Recrutement to the database
            recrutementService.update(recrutement, recrutement.getId());

            // Refresh the parent view
            if (parentController != null) {
                parentController.populateListView();
                parentController.populateCharts();
            }

            // Close the pop-up
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Number of interviews must be a valid integer.");
        }
    }

    /**
     * Handles canceling the update, closing the pop-up without saving.
     */
    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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
