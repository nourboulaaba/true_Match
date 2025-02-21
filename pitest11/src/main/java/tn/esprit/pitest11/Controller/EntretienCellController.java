package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Entretien;
import tn.esprit.pitest11.Services.EntretienService;

import java.io.IOException;
import java.util.Optional;

public class EntretienCellController {

    private Entretien entretien;
    private DashAdminEntretienController parentController;
    private final EntretienService entretienService = new EntretienService();

    @FXML
    private Text Date;

    @FXML
    private Text Lieu;

    @FXML
    private Text approvement;

    @FXML
    private Button delete;

    @FXML
    private Text nomRecrutement;

    @FXML
    private Button update;

    @FXML
    private Text userName;

    public void setEntretien(Entretien entretien, DashAdminEntretienController parentController) {
        this.entretien = entretien;
        this.parentController = parentController;
        updateView();
    }

    private void updateView() {
        Date.setText("Date: " + entretien.getDate());
        Lieu.setText("Lieu: " + entretien.getLieu());
        approvement.setText("Approved: " + (entretien.isApproved() ? "Yes" : "No"));
        userName.setText("User: " + entretien.getUser().getName());
        nomRecrutement.setText("Recrutement: " + entretien.getRecrutement().getOffre().getTitre());
    }

    /**
     * Handles deleting an `Entretien` with confirmation.
     */
    @FXML
    void delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this entretien?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            entretienService.delete(entretien.getId());

            // Refresh the parent view
            if (parentController != null) {
                parentController.populateListView();
                parentController.populateCharts();
            }
        }
    }

    /**
     * Opens the `UpdateEntretien.fxml` pop-up and passes the selected `Entretien`.
     */
    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/UpdateEntretien.fxml"));
            AnchorPane pane = loader.load();

            UpdateEntretienController controller = loader.getController();
            controller.setEntretien(entretien, parentController); // Pass parentController for refresh

            Stage stage = new Stage();
            stage.setTitle("Update Entretien");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait(); // Wait for pop-up to close

            // Refresh parent after update
            parentController.populateListView();
            parentController.populateCharts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
