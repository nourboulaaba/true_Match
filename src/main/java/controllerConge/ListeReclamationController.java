package controllerConge;

import entite.Reclamations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import utils.ReclamationRequest;

public class ListeReclamationController {

    @FXML
    private ListView<Reclamations> listViewReclamations;
    @FXML
    private ComboBox<String> classificationFilter;

    @FXML
    public void initialize() {
        // Ajouter des options au filtre
        classificationFilter.getItems().addAll("Toutes", "Urgente", "Moyenne", "Simple");
        classificationFilter.setValue("Toutes");

        // Ajouter un écouteur pour le filtrage
        classificationFilter.setOnAction(event -> filterReclamations());

        // Personnaliser l'affichage des éléments de la liste
        listViewReclamations.setCellFactory(new Callback<ListView<Reclamations>, ListCell<Reclamations>>() {
            @Override
            public ListCell<Reclamations> call(ListView<Reclamations> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Reclamations reclamation, boolean empty) {
                        super.updateItem(reclamation, empty);
                        if (empty || reclamation == null) {
                            setText(null);
                        } else {
                            setText("🔹 ID : " + reclamation.getId() + "\n"
                                    + "📝 Sujet : " + reclamation.getSujet() + "\n"
                                    + "📌 Description : " + reclamation.getDescription() + "\n"
                                    + "📅 Date : " + reclamation.getDateReclamation() + "\n"
                                    + "⚠ Classification : " + reclamation.getClassification());
                        }
                    }
                };
            }
        });

        // Charger les réclamations
        loadReclamations();
    }

    @FXML
    private void loadReclamations() {
        listViewReclamations.setItems(ReclamationRequest.getAllReclamations());
    }

    private void filterReclamations() {
        String selectedClassification = classificationFilter.getValue();
        if ("Toutes".equals(selectedClassification)) {
            listViewReclamations.setItems(ReclamationRequest.getAllReclamations());
        } else {
            listViewReclamations.setItems(ReclamationRequest.getReclamationsByClassification(selectedClassification));
        }
    }
}
