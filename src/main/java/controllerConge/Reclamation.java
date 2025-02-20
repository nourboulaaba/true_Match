package controllerConge;
import entite.Reclamations;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import utils.ReclamationRequest;
import java.io.File;
import java.time.LocalDate;
public class Reclamation {
    @FXML private TextField identifierField;
    @FXML private TextField sujetField;
    @FXML private TextArea descriptionArea;
    @FXML private Label imageLabel;
    @FXML private Button envoyerButton;

    private String imagePath = null;
    private final ReclamationRequest reclamationRequest = new ReclamationRequest();

    @FXML
    public void initialize() {
        envoyerButton.setDisable(true);
        identifierField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        sujetField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        descriptionArea.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
    }

    private void checkFields() {
        boolean validIdentifier = !identifierField.getText().trim().isEmpty();
        boolean validSujet = isValidText(sujetField.getText());
        boolean validDescription = isValidText(descriptionArea.getText());

        envoyerButton.setDisable(!(validIdentifier && validSujet && validDescription));
    }

    private boolean isValidText(String text) {
        return text != null && !text.trim().isEmpty() && text.matches("[a-zA-ZÀ-ÿ0-9\s.,!?()-]+$");
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

        if (identifier.isEmpty() || !isValidText(sujet) || !isValidText(description)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir correctement tous les champs.");
            alert.show();
            return;
        }

        Reclamations reclamation = new Reclamations(identifier, sujet, description, imagePath, LocalDate.now());
        if (reclamationRequest.ajouterReclamation(reclamation)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Réclamation envoyée avec succès !");
            alert.show();
            clearFields();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'envoi de la réclamation.");
            alert.show();
        }
    }

    private void clearFields() {
        identifierField.clear();
        sujetField.clear();
        descriptionArea.clear();
        imageLabel.setText("Aucune image sélectionnée");
        imagePath = null;
        envoyerButton.setDisable(true);
    }
}
