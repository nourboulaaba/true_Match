package Controller;

import Entities.Contrat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;
import service.contratService;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class GestionContratsController implements Initializable {

    @FXML
    private ComboBox<String> typeField;
    @FXML
    private DatePicker dateDebutField;
    @FXML
    private DatePicker dateFinField;
    @FXML
    private TextField salaireField;
    @FXML
    private TextField idEmployeField;
    
    @FXML
    private TableView<Contrat> contratsTable;
    @FXML
    private TableColumn<Contrat, Integer> idColumn;
    @FXML
    private TableColumn<Contrat, String> typeColumn;
    @FXML
    private TableColumn<Contrat, Date> dateDebutColumn;
    @FXML
    private TableColumn<Contrat, Date> dateFinColumn;
    @FXML
    private TableColumn<Contrat, Double> salaireColumn;
    @FXML
    private TableColumn<Contrat, Integer> employeColumn;

    private final contratService service = new contratService();
    private Contrat selectedContrat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idContrat"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        salaireColumn.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        employeColumn.setCellValueFactory(new PropertyValueFactory<>("idEmploye"));

        // Initialisation des types de contrat
        typeField.setItems(FXCollections.observableArrayList(
            "CDI", "CDD", "Stage", "Intérim"
        ));

        // Chargement des données
        refreshTable();

        // Listener pour la sélection dans la table
        contratsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedContrat = newSelection;
                populateFields(newSelection);
            }
        });
    }

    private void refreshTable() {
        ObservableList<Contrat> contrats = FXCollections.observableArrayList(service.getAll());
        contratsTable.setItems(contrats);
    }

    private void populateFields(Contrat contrat) {
        typeField.setValue(contrat.getType());
        dateDebutField.setValue(((java.sql.Date) contrat.getDateDebut()).toLocalDate());
        dateFinField.setValue(((java.sql.Date) contrat.getDateFin()).toLocalDate());
        salaireField.setText(String.valueOf(contrat.getSalaire()));
        idEmployeField.setText(String.valueOf(contrat.getIdEmploye()));
    }

    @FXML
    private void handleAjouter() {
        if (!validateFields()) {
            return;
        }

        try {
            Contrat contrat = new Contrat(
                Integer.parseInt(idEmployeField.getText()),
                typeField.getValue(),
                java.sql.Date.valueOf(dateDebutField.getValue()),
                java.sql.Date.valueOf(dateFinField.getValue()),
                Double.parseDouble(salaireField.getText())
            );
            
            service.insert(contrat);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Contrat ajouté avec succès");
            refreshTable();
            handleEffacer();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        if (selectedContrat == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un contrat à modifier");
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            Contrat contrat = new Contrat(
                selectedContrat.getIdContrat(),
                Integer.parseInt(idEmployeField.getText()),
                typeField.getValue(),
                java.sql.Date.valueOf(dateDebutField.getValue()),
                java.sql.Date.valueOf(dateFinField.getValue()),
                Double.parseDouble(salaireField.getText())
            );
            
            service.update(contrat);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Contrat modifié avec succès");
            refreshTable();
            handleEffacer();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        if (selectedContrat == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un contrat à supprimer");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer ce contrat ?");
        confirmation.setContentText("Cette action est irréversible.");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                Contrat contratToDelete = service.getById(selectedContrat.getIdContrat());
                if (contratToDelete != null) {
                    service.delete(contratToDelete);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Contrat supprimé avec succès");
                    refreshTable();
                    handleEffacer();
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEffacer() {
        typeField.setValue(null);
        dateDebutField.setValue(null);
        dateFinField.setValue(null);
        salaireField.clear();
        idEmployeField.clear();
        selectedContrat = null;
        contratsTable.getSelectionModel().clearSelection();
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (typeField.getValue() == null || typeField.getValue().isEmpty()) {
            errors.append("- Le type de contrat est requis\n");
        }
        if (dateDebutField.getValue() == null) {
            errors.append("- La date de début est requise\n");
        }
        if (dateFinField.getValue() == null) {
            errors.append("- La date de fin est requise\n");
        }
        if (salaireField.getText().isEmpty()) {
            errors.append("- Le salaire est requis\n");
        } else {
            try {
                Double.parseDouble(salaireField.getText());
            } catch (NumberFormatException e) {
                errors.append("- Le salaire doit être un nombre valide\n");
            }
        }
        if (idEmployeField.getText().isEmpty()) {
            errors.append("- L'ID de l'employé est requis\n");
        } else {
            try {
                Integer.parseInt(idEmployeField.getText());
            } catch (NumberFormatException e) {
                errors.append("- L'ID de l'employé doit être un nombre entier\n");
            }
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez corriger les erreurs suivantes:\n" + errors.toString());
            return false;
        }

        return true;
    }

    @FXML
    private void handleRetourButton(javafx.event.ActionEvent event) {
        try {
            javafx.scene.Parent dashboardView = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboardView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
