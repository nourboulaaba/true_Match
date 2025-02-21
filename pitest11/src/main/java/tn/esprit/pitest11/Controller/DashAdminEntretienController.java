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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Entretien;
import tn.esprit.pitest11.Model.Recrutement;
import tn.esprit.pitest11.Services.EntretienService;

import java.io.IOException;
import java.util.List;

public class DashAdminEntretienController {
    private Recrutement recrutement;

    public void setRecrutement(Recrutement recrutement) {
        this.recrutement = recrutement;
    }

    @FXML
    private ListView<Entretien> entretienListView;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;

    private final EntretienService entretienService = new EntretienService();
    private final ObservableList<Entretien> entretienList = FXCollections.observableArrayList();

    public void populateListView() {
        if (recrutement == null || entretienListView == null) {
            return; // Prevent NullPointerException
        }

        List<Entretien> entretiens = entretienService.readAllByRecrutementId(recrutement.getId());
        entretienList.setAll(entretiens);
        entretienListView.setItems(entretienList);
        entretienListView.setCellFactory(param -> new ListCell<>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(Entretien entretien, boolean empty) {
                super.updateItem(entretien, empty);
                if (empty || entretien == null) {
                    setGraphic(null);
                } else {
                    try {
                        if (loader == null) {
                            loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/EntretienCell.fxml"));
                            setGraphic(loader.load());
                        }
                        EntretienCellController controller = loader.getController();
                        controller.setEntretien(entretien, DashAdminEntretienController.this); // Pass parent controller
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        populateCharts();
    }

    /**
     * Populates PieChart and BarChart with data from Entretien table.
     */
    public void populateCharts() {
        List<Entretien> entretiens = entretienService.readAllByRecrutementId(recrutement.getId());
        pieChart.getData().clear();
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Entretien ent : entretiens) {
            pieChart.getData().add(new PieChart.Data(ent.getLieu(), 1));
            series.getData().add(new XYChart.Data<>(ent.getLieu(), 1));
        }

        barChart.getData().add(series);
    }

    @FXML
    void add(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/AddEntretien.fxml"));
            AnchorPane pane = loader.load();

            // Pass the recrutement to the AddEntretienController
            AddEntretienController controller = loader.getController();
            controller.setRecrutement(recrutement);
            controller.setDashAdminEntretienController(this); // Allow refresh after adding

            Stage stage = new Stage();
            stage.setTitle("Add Entretien");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait(); // Wait for pop-up to close

            // Refresh list after adding a new entretien
            populateListView();
            populateCharts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void search(ActionEvent actionEvent) {
    }
}
