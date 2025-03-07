package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import tn.esprit.pitest11.Model.Entretien;
import tn.esprit.pitest11.Services.EntretienService;

public class EntretienCellUserController {

    private Entretien entretien;
    private final EntretienService entretienService = new EntretienService();
    private DashUserController parentController;

    @FXML
    private Text Date;

    @FXML
    private Text Lieu;

    @FXML
    private Button approve;

    @FXML
    private Button decline;

    @FXML
    private Text nomRecrutement;

    @FXML
    private Text offreName;

    public void setEntretien(Entretien entretien) {
        this.entretien = entretien;
        updateView();
    }

    public void setDashUserController(DashUserController controller) {
        this.parentController = controller;
    }

    private void updateView() {
        Date.setText("Date: " + entretien.getDate());
        Lieu.setText("Lieu: " + entretien.getLieu());
        nomRecrutement.setText("Recrutement: " + entretien.getRecrutement().getId());
        offreName.setText("Offre: " + (entretien.getRecrutement().getOffre() != null ? entretien.getRecrutement().getOffre().getTitre() : "N/A"));

        if (entretien.isApproved()) {
            approve.setVisible(false);
            decline.setVisible(false);
        }
    }

    @FXML
    void approve(ActionEvent event) {
        entretien.setApproved(true);
        entretienService.update(entretien, entretien.getId());
        showAlert("Success", "Entretien approved successfully.");
        parentController.refresh();
    }

    @FXML
    void decline(ActionEvent event) {
        entretienService.delete(entretien.getId());
        showAlert("Deleted", "Entretien declined and removed.");
        parentController.refresh();
    }

    public void hideButtons() {
        approve.setVisible(false);
        decline.setVisible(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
