package tn.esprit.pitest11.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Entretien;
import tn.esprit.pitest11.Model.Recrutement;
import tn.esprit.pitest11.Services.EntretienService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

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

    // New search controls:
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> dateFilterComboBox;

    private final EntretienService entretienService = new EntretienService();
    private final ObservableList<Entretien> entretienList = FXCollections.observableArrayList();

    public void populateListView() {
        if (recrutement == null || entretienListView == null) {
            return; // Prevent NullPointerException
        }

        List<Entretien> entretiens = entretienService.readAllByRecrutementId(recrutement.getId());
        entretienList.setAll(entretiens);

        // Wrap the list in a FilteredList for dynamic filtering
        FilteredList<Entretien> filteredList = new FilteredList<>(entretienList, p -> true);
        entretienListView.setItems(filteredList);
        setupSearchFilter(filteredList);

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
                        controller.setEntretien(entretien, DashAdminEntretienController.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        populateCharts();
    }

    /**
     * Sets up dynamic filtering using the searchField and dateFilterComboBox.
     *
     * @param filteredList The FilteredList wrapping the original entretien list.
     */
    private void setupSearchFilter(FilteredList<Entretien> filteredList) {
        // Populate the date filter ComboBox
        dateFilterComboBox.setItems(FXCollections.observableArrayList("All", "Today", "This Week", "This Month"));
        dateFilterComboBox.getSelectionModel().select("All");

        // Listener for search text changes
        searchField.textProperty().addListener((observable, oldValue, newValue) -> updateFilter(filteredList));
        // Listener for date filter changes
        dateFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateFilter(filteredList));
    }

    /**
     * Updates the FilteredList predicate based on the search text and date filter.
     *
     * @param filteredList The FilteredList to update.
     */
    private void updateFilter(FilteredList<Entretien> filteredList) {
        String searchText = searchField.getText();
        String selectedDateFilter = dateFilterComboBox.getSelectionModel().getSelectedItem();

        filteredList.setPredicate(entretien -> {
            // Search by user name and lieu (case-insensitive)
            boolean matchesSearch = (searchText == null || searchText.isEmpty()) ||
                    (entretien.getUser().getName().toLowerCase().contains(searchText.toLowerCase()) ||
                            entretien.getLieu().toLowerCase().contains(searchText.toLowerCase()));

            // Date filtering (assumes Entretien has a getDate() method returning LocalDate)
            boolean matchesDate = true;
            if (selectedDateFilter != null && !selectedDateFilter.equals("All")) {
                LocalDate entretienDate = entretien.getDate(); // Adjust method name as needed
                if (entretienDate != null) {
                    LocalDate today = LocalDate.now();
                    switch (selectedDateFilter) {
                        case "Today":
                            matchesDate = entretienDate.equals(today);
                            break;
                        case "This Week":
                            TemporalField weekField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                            matchesDate = (entretienDate.get(weekField) == today.get(weekField)) &&
                                    (entretienDate.getYear() == today.getYear());
                            break;
                        case "This Month":
                            matchesDate = (entretienDate.getMonth() == today.getMonth()) &&
                                    (entretienDate.getYear() == today.getYear());
                            break;
                        default:
                            matchesDate = true;
                    }
                } else {
                    // If no date is set for the entretien, exclude it from date-filtered results.
                    matchesDate = false;
                }
            }
            return matchesSearch && matchesDate;
        });
    }

    /**
     * Populates PieChart and BarChart with data from the Entretien table.
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
            controller.setDashAdminEntretienController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Entretien");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.showAndWait();

            // Refresh list after adding a new entretien
            populateListView();
            populateCharts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Optional: Trigger search manually if needed.
     */
    public void search(ActionEvent actionEvent) {
        String text = searchField.getText();
        FilteredList<Entretien> filteredList = (FilteredList<Entretien>) entretienListView.getItems();
        if (text == null || text.trim().isEmpty()) {
            // If the search field is empty, show all entretiens.
            filteredList.setPredicate(ent -> true);
        } else {
            // Otherwise, filter by user name or lieu.
            filteredList.setPredicate(ent ->
                    ent.getUser().getName().toLowerCase().contains(text.toLowerCase()) ||
                            ent.getLieu().toLowerCase().contains(text.toLowerCase())
            );
        }
    }


}
