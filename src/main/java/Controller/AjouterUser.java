package Controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.mindrot.jbcrypt.BCrypt;
import org.example.dao.DBConnection;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjouterUser implements Initializable {

    @FXML
    private Button uploadCVButton; // Bouton pour télécharger le CV
    @FXML
    private Button ajouterButton; // Bouton pour ajouter un utilisateur
    @FXML
    private ImageView photoImageView; // Affichage de la photo de profil
    @FXML
    private Label cvLabel; // Label pour afficher le nom du fichier CV téléchargé
    @FXML
    private DatePicker dateEmbauchePicker; // DatePicker pour la date d'embauche
    @FXML
    private JFXTextField nomField; // Champ pour le nom
    @FXML
    private JFXTextField prenomField; // Champ pour le prénom
    @FXML
    private JFXTextField identifierField; // Champ pour l'identifiant
    @FXML
    private JFXTextField emailField; // Champ pour l'email
    @FXML
    private JFXPasswordField mdpField; // Champ pour le mot de passe
    @FXML
    private JFXTextField telField; // Champ pour le numéro de téléphone
    @FXML
    private JFXTextField posteField; // Champ pour le poste
    @FXML
    private JFXTextField faceIDField; // Champ pour le Face ID
    @FXML
    private JFXTextField salaireField; // Champ pour le salaire
    @FXML
    private ComboBox<String> roleBox; // ComboBox pour le rôle
    @FXML
    private Connection connection;

    private String imagePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Gérer l'événement de clic sur le bouton "Télécharger CV"
        uploadCVButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner un fichier CV");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichiers PDF, DOCX", "*.pdf", "*.docx")
            );

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                cvLabel.setText(selectedFile.getName()); // Affiche le nom du fichier
            } else {
                cvLabel.setText("Aucun fichier sélectionné");
            }
        });

        // Gérer l'événement de clic sur le bouton "Ajouter"
        ajouterButton.setOnAction(event -> handleAjouterUtilisateur());
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
            Image image = new Image(selectedFile.toURI().toString());
            photoImageView.setImage(image); // Affiche la nouvelle photo
        }
    }

    @FXML
    public void handleAjouterUtilisateur()
    {this.connection = DBConnection.getInstance().getConnection();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String identifier = identifierField.getText();
        String email = emailField.getText();
        String mdp = mdpField.getText();
        String tel = telField.getText();
        String poste = posteField.getText();
        String faceID = faceIDField.getText();
        String salaire = salaireField.getText();
        String role = roleBox.getValue();
        String dateEmbauche = dateEmbauchePicker.getValue() != null ? dateEmbauchePicker.getValue().toString() : null;
        String cv = cvLabel.getText();
//        // Vérification des champs obligatoires
//        if (nom.isEmpty() || prenom.isEmpty() || identifier.isEmpty() || email.isEmpty() || mdp.isEmpty() || tel.isEmpty() || poste.isEmpty() || role == null) {
//            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
//        return;
      //  }

        // Hachage du mot de passe
        String hashedPassword = BCrypt.hashpw(mdp, BCrypt.gensalt());

        // Insertion dans la base de données
        String insertQuery = "INSERT INTO utilisateur (lastName, firstName, identifier,  email,password, jobPosition, role, faceId, salary, hireDate, phoneNumber,cv ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)){
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, identifier);
            stmt.setString(4, email);
            stmt.setString(5, hashedPassword); // ✅ Correct pour password
            stmt.setString(6, poste); // ✅ Correct pour jobPosition
            stmt.setString(7, role); // ✅ Correct pour role
             // ✅ Correct pour email
            stmt.setString(8, faceID); // ✅ Correct pour faceId
            stmt.setString(9, salaire); // ✅ Correct pour salary
            stmt.setString(10, dateEmbauche); // ✅ Correct pour hireDate
            stmt.setString(11, tel); // ✅ Correct pour phoneNumber// ✅ Ajout de cv
            stmt.setString(12, cv);
           // stmt.setString(13, );

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Succès", "Utilisateur ajouté avec succès.");
            } else {
                showAlert("Erreur", "L'ajout de l'utilisateur a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'utilisateur.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}