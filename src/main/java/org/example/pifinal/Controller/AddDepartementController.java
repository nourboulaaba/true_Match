package org.example.pifinal.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.pifinal.Model.Departement;
import org.example.pifinal.Model.User;
import org.example.pifinal.Services.DepartementService;
import org.example.pifinal.Services.UserService;

import java.util.List;

public class AddDepartementController {
    private final DepartementService departementService = new DepartementService();
    private final UserService userService = new UserService();
    private DashDepartementController parentController; // Link to DashDepartementController

    @FXML
    private TextField nomField;

    @FXML
    private TextField budgetField;

    @FXML
    private TextField nbEmployesField;

    @FXML
    private ComboBox<User> responsableComboBox;

    @FXML
    private Label nomErrorLabel, budgetErrorLabel, nbEmployesErrorLabel, responsableErrorLabel;

    @FXML
    private Button saveButton, cancelButton;

    public void setDashDepartementController(DashDepartementController controller) {
        this.parentController = controller;
    }

    @FXML
    public void initialize() {
        List<User> users = userService.readAll();
        responsableComboBox.getItems().addAll(users);
    }

    @FXML
    void save(ActionEvent event) {
        if (validateInputs()) {
            Departement newDepartement = new Departement();
            newDepartement.setNom(nomField.getText());
            newDepartement.setBudget(Integer.parseInt(budgetField.getText()));
            newDepartement.setNbEmployes(Integer.parseInt(nbEmployesField.getText()));
            newDepartement.setResponsable(responsableComboBox.getValue());

            departementService.add(newDepartement);

            if (parentController != null) {
                parentController.populateListView();
                parentController.populateCharts();
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Département ajouté avec succès !");
            closeWindow();
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (nomField.getText().trim().isEmpty()) {
            nomErrorLabel.setText("Le nom est requis.");
            isValid = false;
        } else {
            nomErrorLabel.setText("");
        }

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
