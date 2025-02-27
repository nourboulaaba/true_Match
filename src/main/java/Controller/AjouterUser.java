package Controller;

import Entities.Role;
import Entities.User;
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
import service.UserService;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

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
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField identifierField;
    @FXML private TextField emailField;
    @FXML private PasswordField mdpField;
    @FXML private TextField telField;
    @FXML private TextField posteField;
    @FXML private TextField faceIDField;
    @FXML private TextField salaireField;
    @FXML private ComboBox<String> roleBox;

    private String cvPath;
    private String imagePath;
    private final UserService utilisateurService = new UserService();

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
        System.out.println("_______________________________>handleAjouterUtilisateur1 ");

        try {
            // Créez un utilisateur en utilisant le constructeur existant
            User utilisateur = new User(
                    nomField.getText(), // nom
                    prenomField.getText(), // prénom
                    emailField.getText(), // email
                    telField.getText(), // téléphone
                    BCrypt.hashpw(mdpField.getText(), BCrypt.gensalt()) // mot de passe haché
            );

            // Maintenant, vous devez définir les autres attributs après la création de l'objet
            utilisateur.setcin(identifierField.getText()); // Identifiant CIN
            utilisateur.setFaceId(faceIDField.getText()); // Face ID
            utilisateur.setSalary(Double.parseDouble(salaireField.getText())); // salaire
            utilisateur.setRole(Role.valueOf(roleBox.getValue().toUpperCase())); // rôle
            utilisateur.setHireDate(dateEmbauchePicker.getValue() != null ? dateEmbauchePicker.getValue().toString() : null); // date d'embauche
            utilisateur.setCv(cvPath); // CV
            utilisateur.setProfilePhoto(imagePath); // Photo de profil
            System.out.println("_______________________________>handleAjouterUtilisateur2 ");

            // Appeler la méthode insert() de utilisateurService
            boolean result = utilisateurService.insert(utilisateur);
            System.out.println("_______________________________>handleAjouterUtilisateur3 ");

            // Afficher une alerte de succès
            if (result) showAlert("Succès", "Utilisateur ajouté avec succès.");

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
