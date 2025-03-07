package tn.esprit.pitest11.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import tn.esprit.pitest11.Model.Entretien;
import tn.esprit.pitest11.Services.EntretienService;

import java.io.IOException;
import java.util.List;

public class DashUserController {

    private final EntretienService entretienService = new EntretienService();
    private final int userId = 1; // Hardcoded for testing, replace with dynamic user id

    private final ObservableList<Entretien> approvedEntretienList = FXCollections.observableArrayList();
    private final ObservableList<Entretien> notApprovedEntretienList = FXCollections.observableArrayList();

    @FXML
    private ListView<Entretien> approvedEntretien;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private ListView<Entretien> notApprovedEntretien;

    @FXML
    private PieChart pieChart;

    @FXML
    public void initialize() {
        populateListViews();
        populateCharts();
    }

    private void populateListViews() {
        List<Entretien> entretiens = entretienService.entretienByUserId(userId);

        approvedEntretienList.clear();
        notApprovedEntretienList.clear();

        for (Entretien ent : entretiens) {
            if (ent.isApproved()) {
                approvedEntretienList.add(ent);
            } else {
                notApprovedEntretienList.add(ent);
            }
        }

        notApprovedEntretien.setItems(notApprovedEntretienList);
        approvedEntretien.setItems(approvedEntretienList);

        // Load cells for not approved entretiens
        notApprovedEntretien.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Entretien entretien, boolean empty) {
                super.updateItem(entretien, empty);
                if (empty || entretien == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/EntretienCellUser.fxml"));
                        setGraphic(loader.load());

                        EntretienCellUserController controller = loader.getController();
                        controller.setEntretien(entretien);
                        controller.setDashUserController(DashUserController.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Load cells for approved entretiens (buttons hidden)
        approvedEntretien.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Entretien entretien, boolean empty) {
                super.updateItem(entretien, empty);
                if (empty || entretien == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/EntretienCellUser.fxml"));
                        setGraphic(loader.load());

                        EntretienCellUserController controller = loader.getController();
                        controller.setEntretien(entretien);
                        controller.hideButtons(); // Hide approve/decline buttons
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        populateCharts();
    }


    private void populateCharts() {
        pieChart.getData().clear();
        barChart.getData().clear();

        int approvedCount = approvedEntretienList.size();
        int notApprovedCount = notApprovedEntretienList.size();

        pieChart.getData().add(new PieChart.Data("Approved", approvedCount));
        pieChart.getData().add(new PieChart.Data("Not Approved", notApprovedCount));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Approved", approvedCount));
        series.getData().add(new XYChart.Data<>("Not Approved", notApprovedCount));

        barChart.getData().add(series);
    }

    public void refresh() {
        populateListViews();
        populateCharts();
    }
}
