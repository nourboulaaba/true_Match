package Controller;

import Entities.utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.utilisateurService;
import javafx.geometry.Insets;  // Assurez-vous que c'est la bonne classe


import java.awt.*;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    private utilisateur currentUser;

    @FXML
    private ImageView currentUserImage;
    @FXML
    private Label currentUserName;
    @FXML
    private Label currentUserLastName;
    @FXML
    private Label currentUserEmail;
    @FXML
    private Label currentUserPassword;
    @FXML
    private GridPane userGridPane;

    private utilisateurService userService = new utilisateurService();

    public void initialize() {
        utilisateur user = userService.getConnectedUser(); // Remplace par ta méthode de récupération de l'utilisateur connecté
        if (user != null) {
            setCurrentUser(user);
        } else {
            System.out.println(" Aucun utilisateur connecté !");
        }
    }

    public void setCurrentUser(utilisateur user) {
        System.out.println("🔹 Chargement du profil utilisateur : " + (user != null ? user.getEmail() : "null"));

        if (user != null) {
            this.currentUser = user;

            // Mettre à jour les labels
            if (currentUserName != null) {
                currentUserName.setText(user.getFirstName());
            }
            if (currentUserLastName != null) {
                currentUserLastName.setText(user.getLastName());
            }
            if (currentUserEmail != null) {
                currentUserEmail.setText(user.getEmail());
            }

            // Afficher l'image ou une image par défaut
            if (currentUserImage != null) {
                if (user.getProfilePhoto() != null && !user.getProfilePhoto().isEmpty()) {
                    currentUserImage.setImage(new Image("file:" + user.getProfilePhoto()));
                } else {
                    currentUserImage.setImage(new Image("/images/default_profile.png")); // Image par défaut
                }
            }
        } else {
            System.out.println("❌ Erreur : utilisateur non trouvé !");
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
