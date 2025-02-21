package tn.esprit.pitest11.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Recrutement;
import tn.esprit.pitest11.Services.RecrutementService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DashAdminRecrutementController {

    @FXML
    private Button addbtn;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private ListView<Recrutement> recrutementListView;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchbtn;

    private final RecrutementService recrutementService = new RecrutementService();
    private final ObservableList<Recrutement> recrutementList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        populateListView();
        populateCharts();

        // Navigate to entretien when a recrutement is clicked
        recrutementListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                Recrutement selectedRecrutement = recrutementListView.getSelectionModel().getSelectedItem();
                if (selectedRecrutement != null) {
                    openEntretienDashboard(selectedRecrutement);
                }
            }
        });
    }

    /**
     * Opens the Entretien Dashboard when a recrutement is clicked.
     */
    private void openEntretienDashboard(Recrutement recrutement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/DashAdminEntretien.fxml"));
            AnchorPane pane = loader.load();

            DashAdminEntretienController controller = loader.getController();
            controller.setRecrutement(recrutement);
            controller.populateListView();

            Stage stage = new Stage();
            stage.setTitle("Manage Entretiens");
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the ListView with Recrutement data.
     */
    void populateListView() {
        List<Recrutement> recrutements = recrutementService.readAll();
        recrutementList.setAll(recrutements);
        recrutementListView.setItems(recrutementList);
        recrutementListView.setCellFactory(param -> new ListCell<>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(Recrutement recrutement, boolean empty) {
                super.updateItem(recrutement, empty);
                if (empty || recrutement == null) {
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/RecrutementCell.fxml"));
                        try {
                            setGraphic(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    RecrutementCellController controller = loader.getController();
                    controller.setRecrutement(recrutement, DashAdminRecrutementController.this);
                }
            }
        });
    }

    /**
     * Populates PieChart and BarChart with data from Recrutement table.
     */
    void populateCharts() {
        List<Recrutement> recrutements = recrutementService.readAll();
        pieChart.getData().clear();
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Recrutement rec : recrutements) {
            pieChart.getData().add(new PieChart.Data("Recrutement " + rec.getId(), rec.getNbEntretien()));
            series.getData().add(new XYChart.Data<>("Recrutement " + rec.getId(), rec.getNbEntretien()));
        }

        barChart.getData().add(series);
    }

    /**
     * Deletes a Recrutement from the database and refreshes the UI.
     */
    public void deleteRecrutement(int id) {
        // Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this recrutement?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete from database
            recrutementService.delete(id);

            // Refresh UI
            populateListView();
            populateCharts();
        }
    }

    public void search(ActionEvent actionEvent) {
    }

    @FXML
    public void add(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/AddRecrutement.fxml"));
            AnchorPane pane = loader.load();

            AddRecrutementController controller = loader.getController();
            controller.setDashAdminRecrutementController(this); // Allow refresh after adding
            controller.populateOffreComboBox(); // Populate Offre ComboBox

            Stage stage = new Stage();
            stage.setTitle("Add Recrutement");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait(); // Wait for pop-up to close

            // Refresh list after adding a new recrutement
            populateListView();
            populateCharts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
