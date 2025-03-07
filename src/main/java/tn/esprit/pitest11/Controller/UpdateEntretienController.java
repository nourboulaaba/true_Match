package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Entretien;
import tn.esprit.pitest11.Services.EntretienService;

import java.time.LocalDate;

public class UpdateEntretienController {

    private Entretien entretien;
    private EntretienService entretienService = new EntretienService();
    private DashAdminEntretienController parentController;

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField latitudeField;

    @FXML
    private TextField lieuField;

    @FXML
    private TextField longitudeField;

    @FXML
    private Button updateButton;

    /**
     * Sets the `Entretien` to be updated and populates the fields.
     */
    public void setEntretien(Entretien entretien, DashAdminEntretienController parentController) {
        this.entretien = entretien;
        this.parentController = parentController;
        populateFields();
    }

    /**
     * Populates the input fields with the existing `Entretien` data.
     */
    private void populateFields() {
        datePicker.setValue(entretien.getDate());
        lieuField.setText(entretien.getLieu());
        latitudeField.setText(String.valueOf(entretien.getLatitude()));
        longitudeField.setText(String.valueOf(entretien.getLongitude()));
    }

    /**
     * Handles the update action, saves the updated data, and refreshes the parent view.
     */
    @FXML
    void handleUpdate(ActionEvent event) {
        try {
            // Validate input fields
            if (datePicker.getValue() == null || lieuField.getText().isEmpty() ||
                    latitudeField.getText().isEmpty() || longitudeField.getText().isEmpty()) {
                showAlert("Validation Error", "Please fill in all fields.");
                return;
            }

            // Update the Entretien object
            entretien.setDate(datePicker.getValue());
            entretien.setLieu(lieuField.getText());
            entretien.setLatitude(Double.parseDouble(latitudeField.getText()));
            entretien.setLongitude(Double.parseDouble(longitudeField.getText()));

            // Save the updated Entretien to the database
            entretienService.update(entretien, entretien.getId());

            // Refresh the parent view
            if (parentController != null) {
                parentController.populateListView();
                parentController.populateCharts();
            }

            // Close the pop-up
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Latitude and Longitude must be valid numbers.");
        }
    }

    /**
     * Handles the cancel action, closes the pop-up without saving changes.
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
