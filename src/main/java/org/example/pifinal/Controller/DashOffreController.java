package org.example.pifinal.Controller;

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
import org.example.pifinal.Model.Departement;
import org.example.pifinal.Model.Offre;
import org.example.pifinal.Services.OffreService;

import java.io.IOException;
import java.util.List;

public class DashOffreController {

    @FXML
    private Button addbtn;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private ListView<Offre> offreListView;

    @FXML
    private PieChart pieChart;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    private Departement departement;
    private final OffreService offreService = new OffreService();
    private final ObservableList<Offre> offreList = FXCollections.observableArrayList();

    /**
     * Sets the department for which the offers should be displayed.
     */
    public void setDepartement(Departement departement) {
        this.departement = departement;
        populateListView();
        populateCharts();
    }

    @FXML
    public void initialize() {
        // Populate List and Charts if department is already set
        if (departement != null) {
            populateListView();
            populateCharts();
        }
    }

    /**
     * Populates the ListView with offers of the selected department.
     */
    public void populateListView() {
        if (departement != null) {
            List<Offre> offres = offreService.readAll().stream()
                    .filter(offre -> offre.getDepartement().getId() == departement.getId())
                    .toList();
            offreList.setAll(offres);
            offreListView.setItems(offreList);
            offreListView.setCellFactory(param -> new ListCell<>() {
                private FXMLLoader loader;

                @Override
                protected void updateItem(Offre offre, boolean empty) {
                    super.updateItem(offre, empty);
                    if (empty || offre == null) {
                        setGraphic(null);
                    } else {
                        if (loader == null) {
                            loader = new FXMLLoader(getClass().getResource("/org/example/pifinal/OffreCell.fxml"));
                            try {
                                setGraphic(loader.load());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        OffreCellController controller = loader.getController();
                        controller.setOffre(offre, DashOffreController.this);
                    }
                }
            });
        }
    }

    /**
     * Populates PieChart and BarChart with offer statistics.
     */
    public void populateCharts() {
        if (departement != null) {
            List<Offre> offres = offreService.readAll().stream()
                    .filter(offre -> offre.getDepartement().getId() == departement.getId())
                    .toList();

            pieChart.getData().clear();
            barChart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            for (Offre offre : offres) {
                pieChart.getData().add(new PieChart.Data(offre.getTitre(), offre.getSalaireMax()));
                series.getData().add(new XYChart.Data<>(offre.getTitre(), offre.getSalaireMax()));
            }

            barChart.getData().add(series);
        }
    }

    /**
     * Opens the AddOffre.fxml pop-up for adding a new offer.
     */
    @FXML
    void add(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pifinal/AddOffre.fxml"));
            AnchorPane pane = loader.load();

            AddOffreController controller = loader.getController();
            controller.setDepartement(departement); // Pass the selected department
            controller.setDashOffreController(this); // Allow refresh after adding

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Offre");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait();

            populateListView();
            populateCharts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles searching for offers by title.
     */
    @FXML
    void search(ActionEvent event) {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            populateListView();
        } else {
            List<Offre> filteredList = offreService.readAll().stream()
                    .filter(offre -> offre.getDepartement().getId() == departement.getId() &&
                            offre.getTitre().toLowerCase().contains(searchText))
                    .toList();
            offreList.setAll(filteredList);
        }
    }
}
