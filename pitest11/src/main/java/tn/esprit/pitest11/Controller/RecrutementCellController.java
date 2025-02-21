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
import tn.esprit.pitest11.Model.Recrutement;

import java.io.IOException;
import java.util.Optional;

public class RecrutementCellController {

    private Recrutement recrutement;
    private DashAdminRecrutementController parentController;

    public void setRecrutement(Recrutement recrutement, DashAdminRecrutementController parentController) {
        this.recrutement = recrutement;
        this.parentController = parentController;
        updateView();
    }

    @FXML
    private Text DateDebut;

    @FXML
    private Text dateFin;

    @FXML
    private Button delete;

    @FXML
    private Text nbEntretien;

    @FXML
    private Text offreName;

    @FXML
    private Button update;

    private void updateView() {
        DateDebut.setText("Start: " + (recrutement.getDateDebut() != null ? recrutement.getDateDebut() : "N/A"));
        dateFin.setText("End: " + (recrutement.getDateFin() != null ? recrutement.getDateFin() : "N/A"));
        nbEntretien.setText("Interviews: " + recrutement.getNbEntretien());

        if (recrutement.getOffre() != null) {
            offreName.setText("Offre: " + recrutement.getOffre().getTitre());
            System.out.println("recr "+recrutement);
        } else {
            offreName.setText("Offre: N/A"); // Handle null case properly
        }
    }


    @FXML
    void Delete(ActionEvent event) {
        // Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this recrutement?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            parentController.deleteRecrutement(recrutement.getId());
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/UpdateRecrutement.fxml"));
            AnchorPane pane = loader.load();

            UpdateRecrutementController controller = loader.getController();

            controller.setRecrutement(recrutement, parentController); // Pass parentController for refresh

            Stage stage = new Stage();
            stage.setTitle("Update Recrutement");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait();  // Wait for pop-up to close

            parentController.populateListView();
            parentController.populateCharts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
