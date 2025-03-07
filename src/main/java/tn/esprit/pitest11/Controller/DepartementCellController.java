package tn.esprit.pitest11.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Departement;
import tn.esprit.pitest11.Services.DepartementService;

import java.io.IOException;
import java.util.Optional;

public class DepartementCellController {
    private Departement departement;
    private DepartementService departementService = new DepartementService();
    private DashDepartementController parentController;

    @FXML
    private Text budgetDep;

    @FXML
    private Button delete;

    @FXML
    private Text nbEmployeDep;

    @FXML
    private Text nomDep;

    @FXML
    private Text respDep;

    @FXML
    private Button update;

    public void setDepartement(Departement departement, DashDepartementController parentController) {
        this.departement = departement;
        this.parentController = parentController;
        updateView();
    }

    private void updateView() {
        if (departement != null) {
            nomDep.setText(departement.getNom());
            budgetDep.setText(String.valueOf(departement.getBudget()));
            nbEmployeDep.setText(String.valueOf(departement.getNbEmployes()));

            // Prevent NullPointerException
            if (departement.getResponsable() != null) {
                respDep.setText(departement.getResponsable().getNom());
            } else {
                respDep.setText("Responsable: Aucun");
            }
        }
    }


    @FXML
    void delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression");
        alert.setHeaderText("Voulez-vous supprimer ce département ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            departementService.delete(departement.getId());
            parentController.populateListView();
            parentController.populateCharts();
        }
    }

    @FXML
    void update(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pifinal/UpdateDepartement.fxml"));
            AnchorPane pane = loader.load();

            UpdateDepartementController controller = loader.getController();
            controller.setDepartement(departement);

            Stage stage = new Stage();
            stage.setTitle("Modifier Département");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait();

            parentController.populateListView();
            parentController.populateCharts();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
