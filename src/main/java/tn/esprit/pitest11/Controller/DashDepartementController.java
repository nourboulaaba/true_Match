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
import tn.esprit.pitest11.Model.Departement;
import tn.esprit.pitest11.Services.DepartementService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DashDepartementController {

    public ComboBox filter;
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
        setupFilterOptions();

        departementListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Departement selectedDepartement = departementListView.getSelectionModel().getSelectedItem();
                if (selectedDepartement != null) {
                    openOffreDashboard(selectedDepartement);
                }
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterDepartements(newValue);
        });
    }
    private void setupFilterOptions() {
        filter.getItems().addAll("Aucun Filtre", "Budget Ascendant", "Budget Descendant");
        filter.getSelectionModel().select("Aucun Filtre");
        filter.setOnAction(event -> applySorting());
    }
    void filterDepartements(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            populateListView(); // Reload all departments if search is cleared
            populateCharts();
        } else {
            List<Departement> filteredList = departementService.readAll().stream()
                    .filter(dep -> dep.getNom().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
            departementList.setAll(filteredList);
            departementListView.setItems(departementList);
            applySorting();
        }
    }

    private void applySorting() {
        Object selected = filter.getValue();
        if (selected == null) {
            return; // No selection, do nothing
        }

        String selectedOption = selected.toString();

        if (selectedOption.equals("Aucun Filtre")) {
            return;
        }

        if (selectedOption.equals("Budget Ascendant")) {
            departementList.sort(Comparator.comparingInt(Departement::getBudget));
        } else if (selectedOption.equals("Budget Descendant")) {
            departementList.sort(Comparator.comparingInt(Departement::getBudget).reversed());
        }
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
        applySorting();

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
        filterDepartements(searchText);
    }
}
