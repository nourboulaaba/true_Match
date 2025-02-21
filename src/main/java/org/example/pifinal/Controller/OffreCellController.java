package org.example.pifinal.Controller;

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
import org.example.pifinal.Model.Offre;
import org.example.pifinal.Services.OffreService;

import java.io.IOException;
import java.util.Optional;

public class OffreCellController {
    private Offre offre;
    private OffreService offreService = new OffreService();
    private DashOffreController parentController;

    @FXML
    private Button delete;

    @FXML
    private Text descOffre;

    @FXML
    private Text salaireMax;

    @FXML
    private Text salaireMin;

    @FXML
    private Text titreOffre;

    @FXML
    private Button update;

    public void setOffre(Offre offre, DashOffreController parentController) {
        this.offre = offre;
        this.parentController = parentController;
        updateView();
    }

    private void updateView() {
        titreOffre.setText(offre.getTitre());
        descOffre.setText(offre.getDescription());
        salaireMin.setText(String.valueOf(offre.getSalaireMin()));
        salaireMax.setText(String.valueOf(offre.getSalaireMax()));
    }

    @FXML
    void delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression");
        alert.setHeaderText("Voulez-vous supprimer cette offre ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            offreService.delete(offre.getId());
            parentController.populateListView();
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pifinal/UpdateOffre.fxml"));
            AnchorPane pane = loader.load();

            UpdateOffreController controller = loader.getController();
            controller.setOffre(offre, parentController);

            Stage stage = new Stage();
            stage.setTitle("Modifier Offre");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait();

            parentController.populateListView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
