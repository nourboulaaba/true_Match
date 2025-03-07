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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.pitest11.Model.Recrutement;
import tn.esprit.pitest11.Services.RecrutementService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class DashAdminRecrutementController {


    @FXML
    private Button stats;

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

    // New ComboBox for filtering by date
    @FXML
    private ComboBox<String> dateFilterComboBox;

    private final RecrutementService recrutementService = new RecrutementService();
    private final ObservableList<Recrutement> recrutementList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        populateListView();
        populateCharts();
        setupSearchFilter();

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
     * Sets up the dynamic search filter by wrapping the recruitmentList in a FilteredList.
     */
    private void setupSearchFilter() {
        // Wrap the original list in a FilteredList
        FilteredList<Recrutement> filteredList = new FilteredList<>(recrutementList, p -> true);
        recrutementListView.setItems(filteredList);

        // Listener for changes in the search field text
        searchField.textProperty().addListener((observable, oldValue, newValue) -> updateFilter(filteredList));

        // Populate the date filter combo box with options
        dateFilterComboBox.setItems(FXCollections.observableArrayList("All", "Today", "This Week", "This Month"));
        dateFilterComboBox.getSelectionModel().select("All");

        // Listener for changes in the ComboBox selection
        dateFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateFilter(filteredList));
    }

    /**
     * Updates the filter predicate of the FilteredList based on the search text and date filter.
     *
     * @param filteredList the FilteredList wrapping the recrutementList.
     */
    private void updateFilter(FilteredList<Recrutement> filteredList) {
        String searchText = searchField.getText();
        String selectedDateFilter = dateFilterComboBox.getSelectionModel().getSelectedItem();

        filteredList.setPredicate(recrutement -> {
            // Check if the offre titre matches the search text (case-insensitive)
            boolean matchesSearch = (searchText == null || searchText.trim().isEmpty()) ||
                    recrutement.getOffre().getTitre().toLowerCase().contains(searchText.toLowerCase());

            // If "All" is selected, show all recrutements regardless of date
            boolean matchesDate = true;
            if (selectedDateFilter != null && !selectedDateFilter.equalsIgnoreCase("All")) {
                LocalDate dateDebut = recrutement.getDateDebut();
                if (dateDebut != null) {
                    LocalDate today = LocalDate.now();
                    switch (selectedDateFilter) {
                        case "Today":
                            matchesDate = dateDebut.equals(today);
                            break;
                        case "This Week":
                            TemporalField weekField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                            matchesDate = (dateDebut.get(weekField) == today.get(weekField)) &&
                                    (dateDebut.getYear() == today.getYear());
                            break;
                        case "This Month":
                            matchesDate = (dateDebut.getMonth() == today.getMonth()) &&
                                    (dateDebut.getYear() == today.getYear());
                            break;
                        default:
                            matchesDate = true;
                            break;
                    }
                } else {
                    matchesDate = false;
                }
            }
            return matchesSearch && matchesDate;
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
        // If dynamic filtering is active, the ListView's items are already set to the FilteredList.
        // If not, uncomment the line below:
        // recrutementListView.setItems(recrutementList);
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
     * Populates PieChart and BarChart with data from the Recrutement table.
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this recrutement?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            recrutementService.delete(id);
            populateListView();
            populateCharts();
        }
    }

    /**
     * Optional: Trigger search manually if needed.
     */
    @FXML
    public void search(ActionEvent actionEvent) {
        String text = searchField.getText();
        FilteredList<Recrutement> filteredList = (FilteredList<Recrutement>) recrutementListView.getItems();
        if (text == null || text.trim().isEmpty()) {
            // If the search field is empty, show all recrutements
            filteredList.setPredicate(r -> true);
        } else {
            // Otherwise, filter by matching the offre titre (case-insensitive)
            filteredList.setPredicate(r ->
                    r.getOffre().getTitre().toLowerCase().contains(text.toLowerCase())
            );
        }
    }



    @FXML
    public void add(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/AddRecrutement.fxml"));
            AnchorPane pane = loader.load();

            AddRecrutementController controller = loader.getController();
            controller.setDashAdminRecrutementController(this);
            controller.populateOffreComboBox();

            Stage stage = new Stage();
            stage.setTitle("Add Recrutement");
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
    void stats(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/pitest11/Stats.fxml"));
            AnchorPane pane = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Statistics");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
