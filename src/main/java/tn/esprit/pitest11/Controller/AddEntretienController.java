package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.pitest11.Entities.Entretien;
import tn.esprit.pitest11.Entities.Recrutement;
import tn.esprit.pitest11.Entities.User;
import tn.esprit.pitest11.service.EntretienService;
import tn.esprit.pitest11.service.UserService;

public class AddEntretienController {

    public TextField LieuField;
    private EntretienService entretienService = new EntretienService();
    private UserService userService = new UserService();
    private DashAdminEntretienController parentController;
    private Recrutement recrutement; // Recrutement passed from DashAdminEntretienController

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField latitudeField;

    @FXML
    private TextField OffreField;

    @FXML
    private TextField longitudeField;

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private Button saveButton;

    public void setRecrutement(Recrutement recrutement) {
        this.recrutement = recrutement;
        if (OffreField != null && recrutement != null) {
            OffreField.setText(recrutement.getOffre().getTitre());
            OffreField.setDisable(true);
        }
    }

    public void setDashAdminEntretienController(DashAdminEntretienController controller) {
        this.parentController = controller;
    }

    @FXML
    public void initialize() {
        // Populate user ComboBox
        userComboBox.getItems().addAll(userService.readAll());

        // Ensure that recrutement is set before UI loads
        if (recrutement != null) {
            OffreField.setText(recrutement.getOffre().getTitre());
            OffreField.setDisable(true);
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (userComboBox.getValue() == null || datePicker.getValue() == null || OffreField.getText().isEmpty() ||
                longitudeField.getText().isEmpty() || latitudeField.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields.");
            return;
        }

        try {
            Entretien newEntretien = new Entretien();
            newEntretien.setUser(userComboBox.getValue());
            newEntretien.setDate(datePicker.getValue());
            newEntretien.setLieu(LieuField.getText());
            newEntretien.setLongitude(Double.parseDouble(longitudeField.getText()));
            newEntretien.setLatitude(Double.parseDouble(latitudeField.getText()));
            newEntretien.setRecrutement(recrutement);
            newEntretien.setApproved(false); // Default approved state is null (handled in DB)

            entretienService.add(newEntretien);

            // Refresh the parent view
            if (parentController != null) {
                parentController.populateListView();
                parentController.populateCharts();
            }

            // Close the pop-up
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Longitude and Latitude must be valid numbers.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
