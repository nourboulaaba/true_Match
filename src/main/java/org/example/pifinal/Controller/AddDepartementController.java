package org.example.pifinal.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
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
        nomField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        budgetField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        nbEmployesField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        responsableComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateInputs());
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

        // Validate name field (nomField)
        String name = nomField.getText().trim();
        if (name.isEmpty()) {
            nomErrorLabel.setText("Le nom est requis.");
            nomErrorLabel.setTextFill(Color.RED);
            isValid = false;
        } else if (name.matches(".*\\d.*")) { // Check if name contains any digits
            nomErrorLabel.setText("Le nom ne doit pas contenir de chiffres.");
            nomErrorLabel.setTextFill(Color.RED);
            isValid = false;
        } else {
            nomErrorLabel.setText("");  // Clear the error if input is valid
        }

        // Validate budget field (budgetField)
        try {
            int budget = Integer.parseInt(budgetField.getText());
            if (budget < 0) {
                budgetErrorLabel.setText("Le budget doit être positif.");
                budgetErrorLabel.setTextFill(Color.RED);
                isValid = false;
            } else {
                budgetErrorLabel.setText("");  // Clear the error if input is valid
            }
        } catch (NumberFormatException e) {
            budgetErrorLabel.setText("Veuillez entrer un budget valide.");
            budgetErrorLabel.setTextFill(Color.RED);
            isValid = false;
        }

        // Validate number of employees field (nbEmployesField)
        try {
            int nbEmployes = Integer.parseInt(nbEmployesField.getText());
            if (nbEmployes < 0) {
                nbEmployesErrorLabel.setText("Le nombre d'employés doit être positif.");
                nbEmployesErrorLabel.setTextFill(Color.RED);
                isValid = false;
            } else {
                nbEmployesErrorLabel.setText("");  // Clear the error if input is valid
            }
        } catch (NumberFormatException e) {
            nbEmployesErrorLabel.setText("Veuillez entrer un nombre valide.");
            nbEmployesErrorLabel.setTextFill(Color.RED);
            isValid = false;
        }

        // Validate responsible selection (responsableComboBox)
        if (responsableComboBox.getValue() == null) {
            responsableErrorLabel.setText("Veuillez sélectionner un responsable.");
            responsableErrorLabel.setTextFill(Color.RED);
            isValid = false;
        } else {
            responsableErrorLabel.setText("");  // Clear the error if input is valid
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
