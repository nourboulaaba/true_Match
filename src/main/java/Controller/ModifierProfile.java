package Controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import org.example.dao.DBConnection;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifierProfile implements Initializable {

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
    private JFXTextField nomField, prenomField, identifierField, emailField, telField, posteField, faceIDField, salaireField; // Champs texte
    @FXML
    private JFXPasswordField mdpField; // Champ mot de passe
    @FXML
    private ComboBox<String> roleBox; // Sélecteur de rôle
    @FXML
    private Button EnregistrerButton; // Bouton Enregistrer
    @FXML
    private Button AnnullerButton; // Bouton Annuler

    private Connection connection;  // Connexion à la base de données
    private String photoPath = ""; // Chemin de la photo de profil
    private String cvPath = ""; // Chemin du fichier CV

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.connection = DBConnection.getInstance().getConnection();

        // Associer les actions aux boutons
        EnregistrerButton.setOnAction(event -> handleModifier());
        AnnullerButton.setOnAction(event -> handleAnnuller());
        //uploadCVButton.setOnAction(event -> handleUploadCV());
    }

    /**
     * Méthode pour changer la photo de profil
     */
    @FXML
    private void handleChangePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            photoImageView.setImage(image);
            photoPath = selectedFile.getAbsolutePath(); // Stocker le chemin de l'image
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
     * Méthode pour enregistrer les modifications du profil
     */
    @FXML
    private void handleModifier() {
        // Récupérer les valeurs des champs
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String password = mdpField.getText();
        String tel = telField.getText();



        // Valider les champs obligatoires
//        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
        //return;


        // Hacher le mot de passe seulement s'il est fourni
        String hashedPassword = password.isEmpty() ? null : BCrypt.hashpw(password, BCrypt.gensalt());

        // Requête SQL pour mettre à jour le profil
        String updateQuery = "UPDATE utilisateur SET lastName = ?, firstName = ?, password = ?, phoneNumber = ?, profilePhoto = ?,  WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, tel);
            stmt.setString(5, photoPath);

            stmt.setString(6, cvPath);
            stmt.setString(7, email);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune modification effectuée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la mise à jour du profil.");
        }
    }

    /**
     * Méthode pour annuler et fermer la fenêtre
     */
    @FXML
    private void handleAnnuller() {
        Stage stage = (Stage) AnnullerButton.getScene().getWindow();
        stage.close();
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
}