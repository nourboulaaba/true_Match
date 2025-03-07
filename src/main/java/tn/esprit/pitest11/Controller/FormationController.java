package tn.esprit.pitest11.Controller;

import tn.esprit.pitest11.Entities.formations;
import tn.esprit.pitest11.Entities.Review;
import tn.esprit.pitest11.service.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import tn.esprit.pitest11.service.ReviewService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class FormationController {

    @FXML
    private ListView<formations> listFormations;  // ListView pour afficher les formations

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtPrix;

    private FormationService formationService = new FormationService();
    @FXML
    private ListView<String> reviewsListView;  // ListView pour afficher les avis

    private ReviewService reviewService = new ReviewService();

    // Initialisation du contrôleur
    @FXML
    private void initialize() {
        // Chargement des formations
        loadFormations();
        listFormations.sceneProperty().addListener((obs, oldScene, newScene) -> {
           /* if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("/stylef.css").toExternalForm());
            }*/
        });

        // Personnalisation de l'affichage dans la ListView pour n'afficher que le nom, description et prix
        listFormations.setCellFactory(param -> new javafx.scene.control.ListCell<formations>() {
            @Override
            protected void updateItem(formations item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Afficher uniquement le nom, description et prix
                    setText(item.getName() + " - " + item.getDescription() + " - " + item.getPrix());
                }
            }
        });
        // Contrôle de saisie pour le champ Nom (lettres et espaces uniquement)
        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) {
                txtName.setText(oldValue); // Revenir à l'ancienne valeur si la saisie est incorrecte
                showAlert("Erreur de saisie", "Le nom ne doit contenir que des lettres et des espaces.");
            }
        });

        // Contrôle de saisie pour le champ Description (lettres et espaces uniquement)
        txtDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s]*")) {
                txtDescription.setText(oldValue);
                showAlert("Erreur de saisie", "La description ne doit contenir que des lettres et des espaces.");
            }
        });

        // Contrôle de saisie pour le champ Prix (chiffres uniquement avec un point pour les décimales)
        txtPrix.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtPrix.setText(oldValue);
                showAlert("Erreur de saisie", "Le prix doit être un nombre valide.");
            }
        });
    }

    // Charger toutes les formations et les afficher dans la ListView
    private void loadFormations() {
        List<formations> formationsList = formationService.getAll();
        ObservableList<formations> formationsObservableList = FXCollections.observableList(formationsList);
        listFormations.setItems(formationsObservableList);
    }

    // Méthode pour ajouter une nouvelle formation
    @FXML
    private void ajouterFormation() {
        String name = txtName.getText();
        String description = txtDescription.getText();
        String prix = txtPrix.getText();

        if (name.isEmpty() || description.isEmpty() || prix.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        formations newFormation = new formations(name, description, prix);
        formationService.insert(newFormation);
        loadFormations();  // Recharge la liste après ajout
        clearFields();     // Vide les champs
    }

    // Méthode pour modifier une formation
    @FXML
    private void modifierFormation() {
        String name = txtName.getText();
        String description = txtDescription.getText();
        String prix = txtPrix.getText();

        if (name.isEmpty() || description.isEmpty() || prix.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Récupération de la formation sélectionnée dans la ListView
        formations selectedFormation = listFormations.getSelectionModel().getSelectedItem();
        if (selectedFormation == null) {
            showAlert("Erreur", "Veuillez sélectionner une formation à modifier.");
            return;
        }

        // Modification de la formation sélectionnée
        selectedFormation.setName(name);
        selectedFormation.setDescription(description);
        selectedFormation.setPrix(prix);
        formationService.update(selectedFormation);  // Appel au tn.esprit.pitest11.service pour mettre à jour

        loadFormations(); // Recharge la liste après modification
        clearFields();    // Vide les champs
    }

    // Méthode pour supprimer une formation
    @FXML
    private void supprimerFormation() {
        // Récupération de la formation sélectionnée dans la ListView
        formations selectedFormation = listFormations.getSelectionModel().getSelectedItem();
        if (selectedFormation == null) {
            showAlert("Erreur", "Veuillez sélectionner une formation à supprimer.");
            return;
        }

        // Suppression de la formation sélectionnée
        formationService.delete(selectedFormation);
        loadFormations(); // Recharge la liste après suppression
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour vider les champs
    private void clearFields() {
        txtName.clear();
        txtDescription.clear();
        txtPrix.clear();
    }
    public void displayReviews(int formationId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            // Appeler le tn.esprit.pitest11.service pour obtenir les avis de la formation
            List<Review> reviews = reviewService.getReviewsByFormation(formationId);

            // Ajouter les avis à la ListView
            reviews.forEach(review -> {
                String reviewText = "Note: " + review.getRating() + " - " + review.getComment();
                reviewsListView.getItems().add(reviewText);
            });
        });
    }
}
