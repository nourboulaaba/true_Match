package tn.esprit.pitest11.Controller;

import tn.esprit.pitest11.Entities.Mission;

import tn.esprit.pitest11.mail.EmailSender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.pitest11.service.UserService;
import tn.esprit.pitest11.service.missionService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class GestionMissionsController implements Initializable {

    @FXML
    private TextField titreField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField destinationField;
    @FXML
    private TextField idEmployeField;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> sortFieldComboBox;
    @FXML
    private ToggleButton sortDirectionToggle;
    @FXML
    private ListView<Mission> missionsList;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button nextPageButton;
    @FXML
    private Label pageInfoLabel;
    @FXML
    private ComboBox<Integer> pageSizeComboBox;

    private final missionService service = new missionService();
    private final UserService userService = new UserService();
    private Mission selectedMission;
    
    // Variables pour la pagination
    private int currentPage = 1;
    private int pageSize = 5;
    private int totalPages = 1;
    
    // Variables pour le tri
    private String currentSortField = "idMission";
    private boolean sortAscending = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des options de tri
        sortFieldComboBox.setItems(FXCollections.observableArrayList(
            "idMission", "titre", "date", "destination"
        ));
        sortFieldComboBox.setValue("idMission");
        
        // Initialisation des options de pagination
        pageSizeComboBox.setItems(FXCollections.observableArrayList(5, 10, 20, 50));
        pageSizeComboBox.setValue(5);
        
        // Configuration de l'affichage des missions
        setupMissionListView();
        
        // Chargement initial des données
        refreshList();
        
        // Ajout des listeners pour les contrôles de recherche, tri et pagination
        setupEventListeners();
    }
    
    private void setupMissionListView() {
        // Personnalisation des cellules de la liste
        missionsList.setCellFactory(param -> new ListCell<Mission>() {
            @Override
            protected void updateItem(Mission mission, boolean empty) {
                super.updateItem(mission, empty);
                if (empty || mission == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox();
                    Label id = new Label("ID : " + mission.getIdMission());
                    Label title = new Label("Titre : " + mission.getTitre());
                    Label date = new Label("Date : " + mission.getDate().toString());
                    Label destination = new Label("Destination : " + mission.getDestination());
                    Label employe = new Label("ID Employé : " + mission.getIdEmploye());
                    container.getChildren().addAll(id, title, date, destination, employe);
                    setGraphic(container);
                }
            }
        });

        // Listener pour la sélection dans la liste
        missionsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedMission = newSelection;
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
                searchMissions(newValue);
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
        // Récupérer le nombre total de missions pour calculer le nombre de pages
        int totalMissions = service.getTotalCount();
        totalPages = (int) Math.ceil((double) totalMissions / pageSize);
        
        // Mettre à jour les contrôles de pagination
        updatePaginationControls();
        
        // Charger les missions pour la page courante
        List<Mission> missionList = service.getAllPaginated(currentPage, pageSize);
        ObservableList<Mission> missions = FXCollections.observableArrayList(missionList);
        missionsList.setItems(missions);
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
    
    private void searchMissions(String keyword) {
        List<Mission> searchResults = service.searchMissions(keyword);
        missionsList.setItems(FXCollections.observableArrayList(searchResults));
        
        // Désactiver la pagination pendant la recherche
        prevPageButton.setDisable(true);
        nextPageButton.setDisable(true);
        pageInfoLabel.setText("Résultats de recherche");
    }
    
    private void applySorting() {
        List<Mission> sortedList = service.getAllSorted(currentSortField, sortAscending);
        missionsList.setItems(FXCollections.observableArrayList(sortedList));
        
        // Désactiver la pagination pendant le tri
        prevPageButton.setDisable(true);
        nextPageButton.setDisable(true);
        pageInfoLabel.setText("Résultats triés");
    }

    private void populateFields(Mission mission) {
        titreField.setText(mission.getTitre());
        dateField.setValue(((java.sql.Date) mission.getDate()).toLocalDate());
        destinationField.setText(mission.getDestination());
        idEmployeField.setText(String.valueOf(mission.getIdEmploye()));
    }

    @FXML
    private void handleAjouter() {
        try {
            int idEmploye = Integer.parseInt(idEmployeField.getText());
            System.out.println("🧐 ID Employé récupéré : " + idEmploye); // Vérifier la valeur récupérée

            Mission mission = new Mission(
                    titreField.getText(),
                    Date.valueOf(dateField.getValue()),
                    destinationField.getText(),
                    idEmploye
            );

            service.insert(mission);
            // Envoyer un email de notification à l'employé
            sendMissionNotificationEmail(mission, true);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mission ajoutée avec succès");
            refreshList();
            handleEffacer();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        if (selectedMission == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une mission à modifier");
            return;
        }

        try {
            Mission mission = new Mission(
                    selectedMission.getIdMission(),
                    titreField.getText(),
                    Date.valueOf(dateField.getValue()),
                    destinationField.getText(),
                    Integer.parseInt(idEmployeField.getText())
            );

            service.update(mission);
            // Envoyer un email de notification à l'employé
            sendMissionNotificationEmail(mission, false);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mission modifiée avec succès");
            refreshList();
            handleEffacer();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        if (selectedMission == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une mission à supprimer");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette mission ?", ButtonType.YES, ButtonType.NO);
        if (confirmation.showAndWait().get() == ButtonType.YES) {
            service.delete(selectedMission);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mission supprimée avec succès");
            refreshList();
            handleEffacer();
        }
    }

    @FXML
    private void handleEffacer() {
        titreField.clear();
        dateField.setValue(null);
        destinationField.clear();
        idEmployeField.clear();
        selectedMission = null;
        missionsList.getSelectionModel().clearSelection();
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
    
    /**
     * Envoie un email de notification à l'user assigné à une mission
     * @param mission La mission concernée
     * @param isNew Indique s'il s'agit d'une nouvelle mission ou d'une mise à jour
     */
    private void sendMissionNotificationEmail(Mission mission, boolean isNew) {
        try {
            // Récupérer l'email de l'user directement depuis la base de données
            String email = getUserEmailById(mission.getIdEmploye());
            
            if (email != null && !email.isEmpty()) {
                // Préparer le sujet et le contenu de l'email
                String subject = isNew ? 
                        "Nouvelle mission assignée : " + mission.getTitre() :
                        "Mise à jour de votre mission : " + mission.getTitre();
                
                StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append("Bonjour,\n\n");
                
                if (isNew) {
                    messageBuilder.append("Une nouvelle mission vous a été assignée :\n\n");
                } else {
                    messageBuilder.append("Votre mission a été mise à jour :\n\n");
                }
                
                messageBuilder.append("Titre : ").append(mission.getTitre()).append("\n");
                messageBuilder.append("Date : ").append(mission.getDate()).append("\n");
                messageBuilder.append("Destination : ").append(mission.getDestination()).append("\n\n");
                
                messageBuilder.append("Veuillez consulter votre espace personnel pour plus de détails.\n\n");
                messageBuilder.append("Cordialement,\nL'équipe True Match");
                
                // Envoyer l'email
                EmailSender.sendEmail(email, subject, messageBuilder.toString());
                System.out.println("✅ Email de notification envoyé à " + email);
            } else {
                System.out.println("⚠️ Impossible d'envoyer l'email : adresse email de l'user non disponible");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Récupère l'email d'un user à partir de son ID
     * @param userId L'ID de l'user
     * @return L'adresse email de l'user ou null si non trouvée
     */
    private String getUserEmailById(int userId) {
        try {
            java.sql.Connection conn = tn.esprit.pitest11.example.dao.DBConnection.getInstance().getConnection();
            String query = "SELECT email FROM user WHERE id = ?";
            
            try (java.sql.PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, userId);
                java.sql.ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (java.sql.SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de l'email : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
