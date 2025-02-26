package Controller;

import Entities.Role;
import Entities.utilisateur;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import service.utilisateurService;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AjouterUser implements Initializable {
    @FXML
    private Button backToLoginButton;

    @FXML private Button uploadCVButton;
    @FXML private Button ajouterButton;
    @FXML private ImageView photoImageView;
    @FXML private Label cvLabel;
    @FXML private DatePicker dateEmbauchePicker;
    @FXML private JFXTextField nomField;
    @FXML private JFXTextField prenomField;
    @FXML private JFXTextField identifierField;
    @FXML private JFXTextField emailField;
    @FXML private JFXPasswordField mdpField;
    @FXML private JFXTextField telField;
    @FXML private JFXTextField posteField;
    @FXML private JFXTextField faceIDField;
    @FXML private JFXTextField salaireField;
    @FXML private ComboBox<String> roleBox;

    private String cvPath;
    private String imagePath;
    private final utilisateurService utilisateurService = new utilisateurService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uploadCVButton.setOnAction(event -> uploadCV());
        ajouterButton.setOnAction(event -> handleAjouterUtilisateur());
    }

    private void uploadCV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier CV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers PDF, DOCX", "*.pdf", "*.docx")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            cvPath = selectedFile.getAbsolutePath();
            cvLabel.setText(selectedFile.getName());
        } else {
            cvLabel.setText("Aucun fichier sélectionné");
        }
    }

    @FXML
    public void uploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une photo de profil");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            photoImageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    public void handleAjouterUtilisateur() {
        try {
            utilisateur utilisateur = new utilisateur(
                    nomField.getText(),
                    prenomField.getText(),
                    identifierField.getText(),
                    emailField.getText(),
                    BCrypt.hashpw(mdpField.getText(), BCrypt.gensalt()), // Hachage du mot de passe
                    telField.getText(),
                    posteField.getText(),
                    faceIDField.getText(),
                    Double.parseDouble(salaireField.getText()), // Conversion en double
                    Role.valueOf(roleBox.getValue().toUpperCase()), // 🔹 Convertir String en Role
                    dateEmbauchePicker.getValue() != null ? dateEmbauchePicker.getValue().toString() : null, // Gestion de la date
                    cvPath,
                    imagePath
            );

            // Call insert() and then show alert based on outcome
            utilisateurService.insert(utilisateur);

            showAlert("Succès", "Utilisateur ajouté avec succès.");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le salaire doit être un nombre valide.");
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "Le rôle sélectionné n'est pas valide.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }





    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void backToLogin() {
        try {
            // Load the Login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) backToLoginButton.getScene().getWindow();

            // Set the scene to login
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
