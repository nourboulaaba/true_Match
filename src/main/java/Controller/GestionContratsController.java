package Controller;

import Entities.Contrat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

import service.contratService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GestionContratsController implements Initializable {

    @FXML
    private ComboBox<String> typeField;
    @FXML
    private DatePicker dateDebutField;
    @FXML
    private DatePicker dateFinField;
    @FXML
    private TextField salaireField;
    @FXML
    private TextField idEmployeField;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortFieldComboBox;
    @FXML
    private ToggleButton sortDirectionToggle;
    @FXML
    private ListView<Contrat> contratsListView;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button nextPageButton;
    @FXML
    private Label pageInfoLabel;
    @FXML
    private ComboBox<Integer> pageSizeComboBox;

    private final contratService service = new contratService();
    private Contrat selectedContrat;
    
    // Variables pour la pagination
    private int currentPage = 1;
    private int pageSize = 5;
    private int totalPages = 1;
    
    // Variables pour le tri
    private String currentSortField = "type";
    private boolean sortAscending = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des types de contrat
        typeField.setItems(FXCollections.observableArrayList(
            "CDI", "CDD", "Stage", "Intérim"
        ));
        
        // Initialisation des options de tri
        sortFieldComboBox.setItems(FXCollections.observableArrayList(
            "idContrat", "type", "salaire"
        ));
        sortFieldComboBox.setValue("idContrat");
        
        // Initialisation des options de pagination
        pageSizeComboBox.setItems(FXCollections.observableArrayList(5, 10, 20, 50));
        pageSizeComboBox.setValue(5);
        
        // Configuration de l'affichage des contrats
        setupContratListView();
        
        // Chargement initial des données
        refreshList();
        
        // Ajout des listeners pour les contrôles de recherche, tri et pagination
        setupEventListeners();
    }
    
    private void setupContratListView() {
        // Définir un affichage personnalisé pour chaque élément de la liste
        contratsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Contrat contrat, boolean empty) {
                super.updateItem(contrat, empty);
                if (empty || contrat == null) {
                    setText(null);
                } else {
                    setText("ID: " + contrat.getIdContrat() + 
                           " | Type: " + contrat.getType() +
                           " | Salaire: " + contrat.getSalaire() + " DT" +
                           " | Employé: " + contrat.getIdEmploye());
                }
            }
        });

        // Ajouter un listener pour détecter la sélection d'un élément
        contratsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedContrat = newSelection;
                populateFields(newSelection);
            }
        });
    }
    
    private void setupEventListeners() {
        // Listener pour la recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                refreshList();
            } else {
                searchContrats(newValue);
            }
        });
        
        // Listener pour le tri
        sortFieldComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentSortField = newValue;
                applySorting();
            }
        });
        
        // Listener pour la direction du tri
        sortDirectionToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            sortAscending = !newValue;
            sortDirectionToggle.setText(sortAscending ? "↑" : "↓");
            applySorting();
        });
        
        // Listener pour la taille de page
        pageSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                pageSize = newValue;
                currentPage = 1; // Retour à la première page
                refreshList();
            }
        });
    }

    private void refreshList() {
        // Récupérer le nombre total de contrats pour calculer le nombre de pages
        int totalContrats = service.getTotalCount();
        totalPages = (int) Math.ceil((double) totalContrats / pageSize);
        
        // Mettre à jour les contrôles de pagination
        updatePaginationControls();
        
        // Charger les contrats pour la page courante
        List<Contrat> contratsList = service.getAllPaginated(currentPage, pageSize);
        ObservableList<Contrat> contrats = FXCollections.observableArrayList(contratsList);
        contratsListView.setItems(contrats);
    }
    
    private void updatePaginationControls() {
        pageInfoLabel.setText("Page " + currentPage + " sur " + totalPages);
        prevPageButton.setDisable(currentPage <= 1);
        nextPageButton.setDisable(currentPage >= totalPages);
    }
    
    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            refreshList();
        }
    }
    
    @FXML
    private void handleNextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            refreshList();
        }
    }
    
    private void searchContrats(String keyword) {
        List<Contrat> searchResults = service.searchContrats(keyword);
        contratsListView.setItems(FXCollections.observableArrayList(searchResults));
        
        // Désactiver la pagination pendant la recherche
        prevPageButton.setDisable(true);
        nextPageButton.setDisable(true);
        pageInfoLabel.setText("Résultats de recherche");
    }
    
    private void applySorting() {
        List<Contrat> sortedList = service.getAllSorted(currentSortField, sortAscending);
        contratsListView.setItems(FXCollections.observableArrayList(sortedList));
        
        // Désactiver la pagination pendant le tri
        prevPageButton.setDisable(true);
        nextPageButton.setDisable(true);
        pageInfoLabel.setText("Résultats triés");
    }


    private void populateFields(Contrat contrat) {
        typeField.setValue(contrat.getType());
        dateDebutField.setValue(((java.sql.Date) contrat.getDateDebut()).toLocalDate());
        dateFinField.setValue(((java.sql.Date) contrat.getDateFin()).toLocalDate());
        salaireField.setText(String.valueOf(contrat.getSalaire()));
        idEmployeField.setText(String.valueOf(contrat.getIdEmploye()));
    }
//Quand tu ajoutes un contrat via le formulaire, GestionContratsController crée un objet Contrat et l'envoie à contratService pour l'insérer dans la base de données.
    @FXML
    private void handleAjouter() {
        if (!validateFields()) {
            return;
        }

        try {
            Contrat contrat = new Contrat(
                Integer.parseInt(idEmployeField.getText()),
                typeField.getValue(),
                java.sql.Date.valueOf(dateDebutField.getValue()),
                java.sql.Date.valueOf(dateFinField.getValue()),
                Double.parseDouble(salaireField.getText())
            );

            service.insert(contrat);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Contrat ajouté avec succès");
            refreshList();
            handleEffacer();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        if (selectedContrat == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un contrat à modifier");
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            Contrat contrat = new Contrat(
                selectedContrat.getIdContrat(),
                Integer.parseInt(idEmployeField.getText()),
                typeField.getValue(),
                java.sql.Date.valueOf(dateDebutField.getValue()),
                java.sql.Date.valueOf(dateFinField.getValue()),
                Double.parseDouble(salaireField.getText())
            );

            service.update(contrat);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Contrat modifié avec succès");
            refreshList();
            handleEffacer();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        if (selectedContrat == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un contrat à supprimer");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer ce contrat ?");
        confirmation.setContentText("Cette action est irréversible.");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                Contrat contratToDelete = service.getById(selectedContrat.getIdContrat());
                if (contratToDelete != null) {
                    service.delete(contratToDelete);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Contrat supprimé avec succès");
                    refreshList();
                    selectedContrat = null;
                    contratsListView.getSelectionModel().clearSelection();

                    handleEffacer();
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEffacer() {
        typeField.setValue(null);
        dateDebutField.setValue(null);
        dateFinField.setValue(null);
        salaireField.clear();
        idEmployeField.clear();
        selectedContrat = null;
        contratsListView.getSelectionModel().clearSelection();
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (typeField.getValue() == null || typeField.getValue().isEmpty()) {
            errors.append("- Le type de contrat est requis\n");
        }
        if (dateDebutField.getValue() == null) {
            errors.append("- La date de début est requise\n");
        }
        if (dateFinField.getValue() == null) {
            errors.append("- La date de fin est requise\n");
        }
        if (salaireField.getText().isEmpty()) {
            errors.append("- Le salaire est requis\n");
        } else {
            try {
                Double.parseDouble(salaireField.getText());
            } catch (NumberFormatException e) {
                errors.append("- Le salaire doit être un nombre valide\n");
            }
        }
        if (idEmployeField.getText().isEmpty()) {
            errors.append("- L'ID de l'employé est requis\n");
        } else {
            try {
                Integer.parseInt(idEmployeField.getText());
            } catch (NumberFormatException e) {
                errors.append("- L'ID de l'employé doit être un nombre entier\n");
            }
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Veuillez corriger les erreurs suivantes:\n" + errors.toString());
            return false;
        }

        return true;
    }

    @FXML
    private void handleRetourButton(javafx.event.ActionEvent event) {
        try {
            javafx.scene.Parent dashboardView = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboardView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
