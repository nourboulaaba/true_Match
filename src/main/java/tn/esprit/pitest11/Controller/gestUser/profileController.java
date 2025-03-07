package tn.esprit.pitest11.Controller.gestUser;

import tn.esprit.pitest11.Entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import tn.esprit.pitest11.service.UserSession;
import javafx.stage.Modality;

import java.io.IOException;

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

    @FXML
    private Button ModifierUtilisateurButton;

    private User currentUser;

    @FXML
    private ImageView currentUserImage;

    @FXML
    private Label currentUserName;

    @FXML
    private Label currentUserLastName;

    @FXML
    private Label currentUserEmail;

    @FXML
    private Label currentUserPhoneNumber;

    @FXML
    private Label currentUserPoste;

    @FXML
    private GridPane userGridPane;

    @FXML
    private Label lblReference;

    @FXML
    private Label currentSalary;

    public void initialize() {
        // Récupère l'utilisateur connecté via le tn.esprit.pitest11.service
        User user = UserSession.getConnectedUser();



        setCurrentUser( user);
        // Si un utilisateur est connecté, on affiche ses informations
        if (user != null) {
            currentUser = UserSession.getConnectedUser();
            System.out.println(user);
        } else {
            System.out.println("❌ Aucun utilisateur connecté !");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Aucun utilisateur connecté");
            alert.setContentText("Aucun utilisateur connecté. Veuillez vous connecter.");
            alert.showAndWait();
        }
    }

    public void setCurrentUser(User user) {
        if (user.getId() == UserSession.getConnectedUser().getId()) user = UserSession.getConnectedUser();
        System.out.println("🔹 Chargement du profil utilisateur : " + (user != null ? user.getEmail() : "null"));

        if (user != null) {
            this.currentUser = user;

            // Mettre à jour les labels avec les informations de l'utilisateur connecté
            if (currentUserName != null) {
                currentUserName.setText(user.getFirstName());
            }
            if (currentUserLastName != null) {
                currentUserLastName.setText(user.getLastName());
            }
            if (currentUserEmail != null) {
                currentUserEmail.setText(user.getEmail());
            }
            if (currentUserPhoneNumber != null) {
                currentUserPhoneNumber.setText(user.getPhoneNumber());
            }

            // Si un poste et département sont associés à l'utilisateur, on les affiche également
            if (posteText != null && departementText != null) {
                posteText.setText(user.getRole() != null ? user.getRole().name() : "Non défini");
                departementText.setText("Département inconnu"); // Ajoute un département si nécessaire
            }

            // Afficher l'image de profil ou une image par défaut
            if (currentUserImage != null) {
                if (user.getProfilePhoto() != null && !user.getProfilePhoto().isEmpty()) {
                    try {
                        currentUserImage.setImage(new Image(user.getProfilePhoto()));
                    }catch (Exception e){
                        currentUserImage.setImage(new Image("/images/user.png")); // Image par défaut

                    }
                } else {
                    currentUserImage.setImage(new Image("/images/user.png")); // Image par défaut
                }
            }
        } else {
            System.out.println("❌ Erreur : utilisateur non trouvé !");
        }
    }

    public void handleModifier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProfile.fxml"));
            AnchorPane newUserPane = loader.load();

            // Pass the selected user to the new controller
            ModifierProfile controller = loader.getController();
            controller.setUser(UserSession.getConnectedUser());

            Scene newUserScene = new Scene(newUserPane);
            Stage stage = (Stage) currentUserName.getScene().getWindow(); // Use the current stage
            stage.setScene(newUserScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void navigateToChangePassword(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the change password popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auth/ChangePassword.fxml"));
            Parent root = loader.load();

            // Create a new stage (window) for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Change Password");

            // Set modality so that the popup blocks interaction with other windows
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Set the scene with the loaded root node and display the popup
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();  // Use showAndWait() if you need to wait until the popup is closed

        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, you can display an error dialog here
        }
    }

}
