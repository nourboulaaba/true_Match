package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Departement;
import tn.esprit.pitest11.Model.Offre;
import tn.esprit.pitest11.Services.DepartementService;
import tn.esprit.pitest11.Services.OffreService;

public class AddOffreController {
    private Departement departement;
    private DashOffreController parentController; // To refresh after adding

    public void setDepartement(Departement departement) {
        this.departement = departement;
        depComboBox.setValue(departement);
        depComboBox.setDisable(true); // Prevent user from changing department
    }

    public void setDashOffreController(DashOffreController controller) {
        this.parentController = controller;
    }

    private final OffreService offreService = new OffreService();
    private final DepartementService departementService = new DepartementService();

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField salaireMinField;

    @FXML
    private TextField salaireMaxField;

    @FXML
    private ComboBox<Departement> depComboBox;

    @FXML
    private Button saveButton, cancelButton;

    @FXML
    public void initialize() {
        depComboBox.getItems().addAll(departementService.readAll());
    }

    @FXML
    void save(ActionEvent event) {
        if (validateInputs()) {
            Offre newOffre = new Offre();
            newOffre.setTitre(titreField.getText());
            newOffre.setDescription(descriptionField.getText());
            newOffre.setSalaireMin(Integer.parseInt(salaireMinField.getText()));
            newOffre.setSalaireMax(Integer.parseInt(salaireMaxField.getText()));
            newOffre.setDepartement(depComboBox.getValue());

            offreService.add(newOffre);

            if (parentController != null) {
                parentController.populateListView();
                parentController.populateCharts();
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre ajoutée avec succès !");
            closeWindow();
        }
    }

    private boolean validateInputs() {
        if (titreField.getText().trim().isEmpty() || descriptionField.getText().trim().isEmpty()
                || salaireMinField.getText().trim().isEmpty() || salaireMaxField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return false;
        }

        try {
            int salaireMin = Integer.parseInt(salaireMinField.getText());
            int salaireMax = Integer.parseInt(salaireMaxField.getText());

            if (salaireMin < 0 || salaireMax < 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les salaires doivent être des valeurs positives.");
                return false;
            }

            if (salaireMax < salaireMin) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le salaire maximum doit être supérieur au salaire minimum.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer des valeurs numériques valides pour les salaires.");
            return false;
        }

        if (depComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un département.");
            return false;
        }

        return true;
    }

    @FXML
    void cancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
