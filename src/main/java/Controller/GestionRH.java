package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class GestionRH {

    @FXML
    private Button ajouterUtilisateurButton;

    @FXML
    public void initialize() {
        ajouterUtilisateurButton.setOnAction(event -> handleAjouterUtilisateur());
    }

    @FXML
    public void handleAjouterUtilisateur() {
        try {
            // Charger la nouvelle scène pour ajouter un utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            AnchorPane newUserPane = loader.load();

            // Créer une nouvelle scène
            Scene newUserScene = new Scene(newUserPane, 600, 400);

            // Obtenir la scène actuelle (stage)
            Stage stage = (Stage) ajouterUtilisateurButton.getScene().getWindow();
            stage.setScene(newUserScene);  // Changer la scène actuelle avec la nouvelle scène

            // Afficher la nouvelle scène
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de chargement de la scène");
            alert.setContentText("Une erreur s'est produite lors du chargement de la scène pour ajouter un utilisateur.");
            alert.showAndWait();
        }
    }
}
