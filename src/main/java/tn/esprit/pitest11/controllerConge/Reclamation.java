package tn.esprit.pitest11.controllerConge;
import tn.esprit.pitest11.Entities.Reclamations;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import tn.esprit.pitest11.service.GeminiService;
import tn.esprit.pitest11.utils.ReclamationRequest;
import java.io.File;
import java.time.LocalDate;
public class Reclamation {
    @FXML private TextField identifierField;
    @FXML private TextField sujetField;
    @FXML private TextArea descriptionArea;
    @FXML private Label imageLabel;
    @FXML private Label classificationLabel;
    @FXML private Button envoyerButton;

    private String imagePath = null;
    private final ReclamationRequest reclamationRequest = new ReclamationRequest();

    @FXML
    public void initialize() {
        envoyerButton.setDisable(true);
        identifierField.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        sujetField.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
        descriptionArea.textProperty().addListener((obs, oldVal, newVal) -> checkFields());
    }

    private void checkFields() {
        boolean valid = !identifierField.getText().trim().isEmpty() &&
                !sujetField.getText().trim().isEmpty() &&
                !descriptionArea.getText().trim().isEmpty();
        envoyerButton.setDisable(!valid);
    }

    @FXML
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.getAbsolutePath();
            imageLabel.setText(file.getName());
        }
    }

    @FXML
    private void envoyerReclamation() {
        String identifier = identifierField.getText().trim();
        String sujet = sujetField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (identifier.isEmpty() || sujet.isEmpty() || description.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Veuillez remplir correctement tous les champs.").show();
            return;
        }

        // Vérification si l'identifier existe dans la table user
        if (!reclamationRequest.identifierExiste(identifier)) {
            new Alert(Alert.AlertType.ERROR, "L'identifiant saisi n'existe pas dans la base de données.").show();
            return;
        }

        // Utilisation de l'API Google Gemini pour la classification
        String classification = GeminiService.analyserReclamation(sujet, description);
        classificationLabel.setText(classification);

        Reclamations reclamation = new Reclamations(identifier, sujet, description, imagePath, LocalDate.now(), classification);
        if (reclamationRequest.ajouterReclamation(reclamation)) {
            new Alert(Alert.AlertType.INFORMATION, "Réclamation envoyée avec succès !").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Erreur lors de l'envoi.").show();
        }
    }
}


