package Controller;

import Entities.Mission;
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
import service.missionService;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class GestionMissionsController implements Initializable {

    @FXML
    private TextField titreField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField destinationField;
    @FXML
    private TextField idEmployeField;
    
    @FXML
    private TableView<Mission> missionsTable;
    @FXML
    private TableColumn<Mission, Integer> idColumn;
    @FXML
    private TableColumn<Mission, String> titreColumn;
    @FXML
    private TableColumn<Mission, Date> dateColumn;
    @FXML
    private TableColumn<Mission, String> destinationColumn;
    @FXML
    private TableColumn<Mission, Integer> employeColumn;

    private final missionService service = new missionService();
    private Mission selectedMission;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idMission"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        employeColumn.setCellValueFactory(new PropertyValueFactory<>("idEmploye"));

        // Chargement des données
        refreshTable();

        // Listener pour la sélection dans la table
        missionsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedMission = newSelection;
                populateFields(newSelection);
            }
        });
    }

    private void refreshTable() {
        ObservableList<Mission> missions = FXCollections.observableArrayList(service.getAll());
        missionsTable.setItems(missions);
    }

    private void populateFields(Mission mission) {
        titreField.setText(mission.getTitre());
        dateField.setValue(((java.sql.Date) mission.getDate()).toLocalDate());
        destinationField.setText(mission.getDestination());
        idEmployeField.setText(String.valueOf(mission.getIdEmploye()));
    }

    @FXML
    private void handleAjouter() {
        try {
            Mission mission = new Mission(
                titreField.getText(),
                java.sql.Date.valueOf(dateField.getValue()),
                destinationField.getText(),
                Integer.parseInt(idEmployeField.getText())
            );
            
            service.insert(mission);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mission ajoutée avec succès");
            refreshTable();
            handleEffacer();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        if (selectedMission == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une mission à modifier");
            return;
        }

        try {
            Mission mission = new Mission(
                selectedMission.getIdMission(),
                titreField.getText(),
                java.sql.Date.valueOf(dateField.getValue()),
                destinationField.getText(),
                Integer.parseInt(idEmployeField.getText())
            );
            
            service.update(mission);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mission modifiée avec succès");
            refreshTable();
            handleEffacer();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        if (selectedMission == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une mission à supprimer");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer cette mission ?");
        confirmation.setContentText("Cette action est irréversible.");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                Mission missionToDelete = service.getById(selectedMission.getIdMission());
                if (missionToDelete != null) {
                    service.delete(missionToDelete);
                }
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Mission supprimée avec succès");
                refreshTable();
                handleEffacer();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEffacer() {
        titreField.clear();
        dateField.setValue(null);
        destinationField.clear();
        idEmployeField.clear();
        selectedMission = null;
        missionsTable.getSelectionModel().clearSelection();
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
