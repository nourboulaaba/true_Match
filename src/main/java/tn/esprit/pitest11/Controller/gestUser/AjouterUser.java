package tn.esprit.pitest11.Controller.gestUser;

import tn.esprit.pitest11.Entities.Role;
import tn.esprit.pitest11.Entities.User;
import javafx.event.ActionEvent;
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
import tn.esprit.pitest11.service.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
    private File file;
    private String photoPath ;
    private Path destinationFile;
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

    /*@FXML
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
    */

    @FXML
    void uploadPhoto(ActionEvent event) {
        // Create a FileChooser instance
        FileChooser fileChooser = new FileChooser();

        // Set the extension filters to allow only image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show the file chooser dialog and get the selected file
        Stage stage = (Stage) photoImageView.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            // Get the destination folder (src/upload in this case)
            Path destinationFolder = Path.of("src", "upload");

            try {
                if (!Files.exists(destinationFolder)) {
                    Files.createDirectories(destinationFolder);
                }

                // Define the destination file path
                destinationFile = destinationFolder.resolve(file.getName());

                // Copy the selected image file to the destination folder
                Files.copy(file.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                imagePath = destinationFile.toUri().toString();
                // Load the image from the saved location
                Image image = new Image(imagePath);
                photoImageView.setImage(image);

                System.out.println("Image saved to: " + destinationFile.toString());

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error saving the image: " + e.getMessage());
            }
        }

    }


    @FXML
    public void handleAjouterUtilisateur() {
        // Vérification des champs obligatoires
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || emailField.getText().isEmpty() ||
                mdpField.getText().isEmpty() || telField.getText().isEmpty() || identifierField.getText().isEmpty() ||
                faceIDField.getText().isEmpty() || salaireField.getText().isEmpty() || roleBox.getValue() == null ||
                dateEmbauchePicker.getValue() == null) {
            showAlert("Erreur", "Tous les champs obligatoires doivent être remplis.");
            return;
        }

        // Validation de l'email
        if (!isValidEmail(emailField.getText())) {
            showAlert("Erreur", "Veuillez entrer une adresse email valide.");
            return;
        }

        // Validation du téléphone
        if (!isValidPhoneNumber(telField.getText())) {
            showAlert("Erreur", "Veuillez entrer un numéro de téléphone valide (8 chiffres).");
            return;
        }

        // Validation du salaire
        try {
            Double.parseDouble(salaireField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le salaire doit être un nombre valide.");
            return;
        }

        // Validation du rôle
        try {
            Role.valueOf(roleBox.getValue().toUpperCase());
        } catch (IllegalArgumentException e) {
            showAlert("Erreur", "Le rôle sélectionné n'est pas valide.");
            return;
        }

        // Si toutes les validations sont passées, créer l'utilisateur
        try {
            User utilisateur = new User(
                    nomField.getText(), // nom
                    prenomField.getText(), // prénom
                    emailField.getText(), // email
                    telField.getText(), // téléphone
                    BCrypt.hashpw(mdpField.getText(), BCrypt.gensalt()) // mot de passe haché
            );

            // Définir les autres attributs
            utilisateur.setcin(identifierField.getText()); // Identifiant CIN
            utilisateur.setFaceId(faceIDField.getText()); // Face ID
            utilisateur.setSalary(Double.parseDouble(salaireField.getText())); // salaire
            utilisateur.setRole(Role.valueOf(roleBox.getValue().toUpperCase())); // rôle
            utilisateur.setHireDate(dateEmbauchePicker.getValue().toString()); // date d'embauche
            utilisateur.setCv(cvPath); // CV
            utilisateur.setProfilePhoto(imagePath); // Photo de profil

            // Ajouter l'utilisateur
            boolean result = utilisateurService.insert(utilisateur);

            // Afficher une alerte de succès
            if (result) {
                showAlert("Succès", "Utilisateur ajouté avec succès.");
                clearFields(); // Réinitialiser les champs après l'ajout
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    // Méthode pour valider l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    // Méthode pour valider le numéro de téléphone
    private boolean isValidPhoneNumber(String phone) {
        String phoneRegex = "\\d{8}"; // 8 chiffres
        return phone.matches(phoneRegex);
    }

    // Méthode pour réinitialiser les champs
    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        identifierField.clear();
        emailField.clear();
        mdpField.clear();
        telField.clear();
        posteField.clear();
        faceIDField.clear();
        salaireField.clear();
        roleBox.getSelectionModel().clearSelection();
        dateEmbauchePicker.setValue(null);
        cvLabel.setText("Aucun fichier sélectionné");
        photoImageView.setImage(null);
        cvPath = null;
        imagePath = null;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auth/Login.fxml"));
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