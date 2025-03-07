package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Departement;
import tn.esprit.pitest11.Model.User;
import tn.esprit.pitest11.Services.DepartementService;
import tn.esprit.pitest11.Services.UserService;

import java.util.List;

public class UpdateDepartementController {
    private Departement departement;
    private final DepartementService departementService = new DepartementService();
    private final UserService userService = new UserService();

    @FXML
    private TextField nomField;

    @FXML
    private TextField budgetField;

    @FXML
    private TextField nbEmployesField;

    @FXML
    private ComboBox<User> responsableComboBox;

    @FXML
    private Label nomErrorLabel;

    @FXML
    private Label budgetErrorLabel;

    @FXML
    private Label nbEmployesErrorLabel;

    @FXML
    private Label responsableErrorLabel;

    @FXML
    private Button updateButton;

    @FXML
    private Button cancelButton;

    public void setDepartement(Departement departement) {
        this.departement = departement;
        populateFields();
    }

    @FXML
    public void initialize() {
        populateResponsableComboBox();
    }

    private void populateFields() {
        if (departement != null) {
            nomField.setText(departement.getNom());
            budgetField.setText(String.valueOf(departement.getBudget()));
            nbEmployesField.setText(String.valueOf(departement.getNbEmployes()));

            // Select current responsable in the ComboBox
            responsableComboBox.getSelectionModel().select(departement.getResponsable());
        }
    }

    private void populateResponsableComboBox() {
        List<User> users = userService.readAll();
        responsableComboBox.getItems().addAll(users);
    }

    @FXML
    void update(ActionEvent event) {
        if (validateInputs()) {
            departement.setNom(nomField.getText());
            departement.setBudget(Integer.parseInt(budgetField.getText()));
            departement.setNbEmployes(Integer.parseInt(nbEmployesField.getText()));
            departement.setResponsable(responsableComboBox.getValue());

            departementService.update(departement, departement.getId());

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Département mis à jour avec succès !");
            closeWindow();
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Validate Nom
        if (nomField.getText().trim().isEmpty()) {
            nomErrorLabel.setText("Le nom est requis.");
            isValid = false;
        } else {
            nomErrorLabel.setText("");
        }

        // Validate Budget
        try {
            int budget = Integer.parseInt(budgetField.getText());
            if (budget < 0) {
                budgetErrorLabel.setText("Le budget doit être positif.");
                isValid = false;
            } else {
                budgetErrorLabel.setText("");
            }
        } catch (NumberFormatException e) {
            budgetErrorLabel.setText("Veuillez entrer un budget valide.");
            isValid = false;
        }

        // Validate Nombre d'Employés
        try {
            int nbEmployes = Integer.parseInt(nbEmployesField.getText());
            if (nbEmployes < 0) {
                nbEmployesErrorLabel.setText("Le nombre d'employés doit être positif.");
                isValid = false;
            } else {
                nbEmployesErrorLabel.setText("");
            }
        } catch (NumberFormatException e) {
            nbEmployesErrorLabel.setText("Veuillez entrer un nombre valide.");
            isValid = false;
        }

        // Validate Responsable
        if (responsableComboBox.getValue() == null) {
            responsableErrorLabel.setText("Veuillez sélectionner un responsable.");
            isValid = false;
        } else {
            responsableErrorLabel.setText("");
        }

        return isValid;
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
