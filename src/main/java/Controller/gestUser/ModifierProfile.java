package Controller.gestUser;

import Entities.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import service.UserService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.dao.DBConnection;
import utils.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModifierProfile implements Initializable {
    private User user;
    @FXML
    private JFXButton AnnullerButton;

    @FXML
    private JFXButton ProfileButton;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Text emailError;

    @FXML
    private TextField emailField;


    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private VBox modifierProfilePane;

    @FXML
    private Text nomError;

    @FXML
    private TextField nomField;

    @FXML
    private Text prenomError;

    @FXML
    private TextField prenomField;

    @FXML
    private Text telError;

    @FXML
    private TextField telephoneField;
    ///////////////
    @FXML
    private ImageView photoImageView;  // ImageView pour la photo de profil

    @FXML
    private Button uploadCVButton; // Bouton pour télécharger le CV
    @FXML
    private Button ajouterButton; // Bouton pour enregistrer les modifications
    @FXML
    private Label cvLabel; // Label pour afficher le nom du fichier CV
    @FXML
    private DatePicker dateEmbauchePicker; // Sélecteur de date d'embauche
    @FXML
    private JFXTextField identifierField, telField, posteField, faceIDField, salaireField; // Champs texte
    @FXML
    private JFXPasswordField mdpField; // Champ mot de passe
    @FXML
    private ComboBox<String> roleBox; // Sélecteur de rôle

    private UserService userService = new UserService();
    private Path destinationFile;
    private File file;

    @FXML
    private ImageView imageView;  // ImageView to display the image

    private String fileImage;
    private Connection connection;  // Connexion à la base de données
    private String photoPath = ""; // Chemin de la photo de profil
    private String cvPath = ""; // Chemin du fichier CV

    @FXML
    void handleButtonProfile(ActionEvent event) {
    }

    void clearError() {
        emailError.setText("");


        nomError.setText("");
        prenomError.setText("");
        telError.setText("");

    }

    @FXML
    void saveProfile(ActionEvent event) {
        clearError();
        boolean isValid = true;
        if (nomField.getText().isEmpty()) {
            nomError.setText("Veuillez entrer le nom");
            isValid = false;
        }
        if (prenomField.getText().isEmpty()) {
            prenomError.setText("Veuillez entrer le prénom");
            isValid = false;
        }
        if (emailField.getText().isEmpty()) {
            emailError.setText("Veuillez entrer votre email");
            isValid = false;
        }
        if (telephoneField.getText().isEmpty()) {
            telError.setText("Veuillez entrer le numéro de telephone");
            isValid = false;
        }

        // Valider les champs obligatoires
//        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
        //return;


        // Hacher le mot de passe seulement s'il est fourni
        // String hashedPassword = password.isEmpty() ? null : BCrypt.hashpw(password, BCrypt.gensalt());

        // Requête SQL pour mettre à jour le profil
        //String updateQuery = "UPDATE utilisateur SET lastName = ?, firstName = ?, phoneNumber = ?, profilePhoto = ?  WHERE email = ?";
        if (isValid) {
            try {
                user.setEmail(emailField.getText());
                user.setPhoneNumber(telephoneField.getText());
                user.setFirstName(prenomField.getText());
                user.setLastName(nomField.getText());
                user.setProfilePhoto(photoPath);
                userService.updateOne(user);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès !");
                handleAnnuller();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la mise à jour du profil.");
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.connection = DBConnection.getInstance().getConnection();

        // Associer les actions aux boutons
        AnnullerButton.setOnAction(event -> handleAnnuller());
        //uploadCVButton.setOnAction(event -> handleUploadCV());
    }

    @FXML
    void handleChangePhoto(ActionEvent event) {
        // Create a FileChooser instance
        FileChooser fileChooser = new FileChooser();

        // Set the extension filters to allow only image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show the file chooser dialog and get the selected file
        Stage stage = (Stage) imageView.getScene().getWindow();
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
                photoPath = destinationFile.toUri().toString();
                // Load the image from the saved location
                Image image = new Image(photoPath);
                imageView.setImage(image);

                System.out.println("Image saved to: " + destinationFile.toString());

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error saving the image: " + e.getMessage());
            }
        }

    }

    /**
     * Méthode pour télécharger un CV
     */
    @FXML
    private void handleUploadCV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            cvLabel.setText(selectedFile.getName()); // Afficher le nom du fichier
            cvPath = selectedFile.getAbsolutePath(); // Stocker le chemin du fichier
        }
    }


    /**
     * Méthode pour annuler et fermer la fenêtre
     */
    @FXML
    private void handleAnnuller() {
        Stage stage = (Stage) emailField.getScene().getWindow(); // Get reference to the login window's stage
        try {
            if (PreferenceManager.getString("role", "RH").equals("RH")) {
                stage.setTitle("Dashboard");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
                Parent p = loader.load();
                Scene scene = new Scene(p);
                stage.setScene(scene);
            } else {
                stage.setTitle("Dashboard");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashEmployee.fxml"));
                Parent p = loader.load();
                Scene scene = new Scene(p);
                stage.setScene(scene);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Méthode pour afficher une alerte
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUser(User user) {

        this.user = user;
        prenomField.setText(user.getFirstName());
        nomField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        telephoneField.setText(user.getPhoneNumber());
        Image image;
        try {
            if (user.getProfilePhoto() != null && !user.getProfilePhoto().equals("")) {
                photoPath = user.getProfilePhoto();

                image = new Image(user.getProfilePhoto());
            }else {
                image = new Image("/images/user.png");//get from resource

            }
        } catch (Exception e) {
            image = new Image("/images/user.png");//get from resource
        }
        imageView.setImage(image);

    }
}