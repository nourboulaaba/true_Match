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
import org.example.pifinal.Services.DepartementService;

import java.io.IOException;
import java.util.List;

public class DashDepartementController {

    @FXML
    private Button addbtn;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private ListView<Departement> departementListView;

    @FXML
    private PieChart pieChart;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    private final DepartementService departementService = new DepartementService();
    private final ObservableList<Departement> departementList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        populateListView();
        populateCharts();

        departementListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                Departement selectedDepartement = departementListView.getSelectionModel().getSelectedItem();
                if (selectedDepartement != null) {
                    openOffreDashboard(selectedDepartement);
                }
            }
        });
    }

    private void openOffreDashboard(Departement departement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pifinal/DashOffre.fxml"));
            AnchorPane pane = loader.load();

            DashOffreController controller = loader.getController();
            controller.setDepartement(departement);
            controller.populateListView();

            Stage stage = new Stage();
            stage.setTitle("Offres du Département: " + departement.getNom());
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateListView() {
        List<Departement> departements = departementService.readAll();
        departementList.setAll(departements);
        departementListView.setItems(departementList);
        departementListView.setCellFactory(param -> new ListCell<>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(Departement departement, boolean empty) {
                super.updateItem(departement, empty);
                if (empty || departement == null) {
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/org/example/pifinal/DepartementCell.fxml"));
                        try {
                            setGraphic(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    DepartementCellController controller = loader.getController();
                    controller.setDepartement(departement, DashDepartementController.this);
                }
            }
        });
    }

    void populateCharts() {
        List<Departement> departements = departementService.readAll();
        pieChart.getData().clear();
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Departement dep : departements) {
            pieChart.getData().add(new PieChart.Data(dep.getNom(), dep.getNbEmployes()));
            series.getData().add(new XYChart.Data<>(dep.getNom(), dep.getNbEmployes()));
        }

        barChart.getData().add(series);
    }

    @FXML
    void add(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pifinal/AddDepartement.fxml"));
            AnchorPane pane = loader.load();

            AddDepartementController controller = loader.getController();
            controller.setDashDepartementController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Département");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait();

            populateListView();
            populateCharts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void search(ActionEvent event) {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            populateListView();
        } else {
            List<Departement> filteredList = departementService.readAll().stream()
                    .filter(dep -> dep.getNom().toLowerCase().contains(searchText))
                    .toList();
            departementList.setAll(filteredList);
        }
    }


}
