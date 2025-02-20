package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class profileController {

    @FXML
    private Text nomText;

    @FXML
    private Text prenomText;

    @FXML
    private Text emailText;

    @FXML
    private Text telephoneText;

    @FXML
    private Text posteText;

    @FXML
    private Text departementText;

    @FXML
    private ImageView photoImageView;

    private Connection connection;
    @FXML
    private Button ModifierUtilisateurButton;
    // Méthode pour initialiser la connexion à la base de données
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void initialize() {
        ModifierUtilisateurButton.setOnAction(event -> handleModifier());
    }

    // Méthode pour charger les données de l'utilisateur
    public void loadUser(String email) {
        String query = "SELECT lastName, firstName, email, phoneNumber, poste, departement, photo FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Récupérer les données de l'utilisateur
                String lastName = rs.getString("lastName");
                String firstName = rs.getString("firstName");
                String userEmail = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                String poste = rs.getString("poste");
                String departement = rs.getString("departement");
                String photoPath = rs.getString("photo");

                // Afficher les données dans l'interface
                nomText.setText("Nom: " + lastName);
                prenomText.setText("Prénom: " + firstName);
                emailText.setText("Email: " + userEmail);
                telephoneText.setText("Téléphone: " + phoneNumber);
                posteText.setText("Poste: " + poste);
                departementText.setText("Département: " + departement);

                // Charger la photo de profil
                if (photoPath != null && !photoPath.isEmpty()) {
                    Image image = new Image("file:" + photoPath);
                    photoImageView.setImage(image);
                }
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet email.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des données de l'utilisateur.");
        }
    }
    public void handleModifier() {
        try {
            // Charger la nouvelle scène pour ajouter un utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProfile.fxml"));
            AnchorPane newUserPane = loader.load();

            // Créer une nouvelle scène
            Scene newUserScene = new Scene(newUserPane, 600, 400);

            // Obtenir la scène actuelle (stage)
            Stage stage = (Stage) ModifierUtilisateurButton.getScene().getWindow();
            stage.setScene(newUserScene);  // Changer la scène actuelle avec la nouvelle scène

            // Afficher la nouvelle scène
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de chargement de la scène");
            alert.setContentText("Une erreur s'est produite lors du chargement de la scène pour ajouter un utilisateur.");
            alert.showAndWait();
        }
    }
}