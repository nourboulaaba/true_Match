package Controller;

import Entities.utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.utilisateurService;

import java.io.IOException;
import java.util.List;

public class GestionRH {

    @FXML
    private ImageView currentUserImage;

    @FXML
    private Label currentUserName;

    @FXML
    private Label currentUserLastName;

    @FXML
    private Label currentUserEmail;

    @FXML
    private GridPane userGridPane;

    private utilisateur currentUser;
    private utilisateur selectedUser = null;
    private final utilisateurService userService = new utilisateurService();

    // Initialize the controller
    public void initialize() {
        afficherUsersDansGrid(); // Display users on startup
    }

    // Set the current logged-in user
    public void setCurrentUser(utilisateur user) {
        this.currentUser = user;

        if (user != null) {
            System.out.println("GestionRH - Current User Set: " + user.getId());

            // Display the profile photo if available
            if (currentUserImage != null && user.getProfilePhoto() != null && !user.getProfilePhoto().isEmpty()) {
                currentUserImage.setImage(new Image("file:" + user.getProfilePhoto()));
            }

            // Display user details
            if (currentUserName != null) {
                currentUserName.setText(user.getFirstName());
            }
            if (currentUserLastName != null) {
                currentUserLastName.setText(user.getLastName());
            }
            if (currentUserEmail != null) {
                currentUserEmail.setText(user.getEmail());
            }
        } else {
            System.out.println("GestionRH - currentUser is null!");
        }
    }

    // Display all users in the GridPane
    public void afficherUsersDansGrid() {
        userGridPane.getChildren().clear(); // Clear the GridPane before displaying

        List<utilisateur> utilisateurs = userService.getAll();

        int colonne = 0;
        int ligne = 0;

        for (utilisateur user : utilisateurs) {
            VBox userCard = createUserCard(user); // Create a card for each user
            userGridPane.add(userCard, colonne, ligne);

            colonne++;
            if (colonne == 3) { // Max 3 columns, then move to the next row
                colonne = 0;
                ligne++;
            }
        }
    }

    // Create a user card (VBox) for display
    private VBox createUserCard(utilisateur user) {
        VBox userCard = new VBox();
        userCard.setPadding(new Insets(10));
        userCard.setSpacing(5);
        userCard.setStyle("-fx-border-color: black; -fx-border-radius: 5px; -fx-padding: 10px;");

        // User image
        ImageView userImage = new ImageView();
        if (user.getProfilePhoto() != null && !user.getProfilePhoto().isEmpty()) {
            userImage.setImage(new Image("file:" + user.getProfilePhoto()));
        } else {
            userImage.setImage(new Image("file:default.png")); // Default image
        }
        userImage.setFitHeight(50);
        userImage.setFitWidth(50);

        // User details
        Label nameLabel = new Label(user.getFirstName() + " " + user.getLastName());
        Label emailLabel = new Label("Email: " + user.getEmail());

        // Options menu
        MenuButton menuButton = new MenuButton("Options");
        MenuItem modifyItem = new MenuItem("Modifier");
        modifyItem.setOnAction(e -> handleModifierUtilisateur(user));
        MenuItem deleteItem = new MenuItem("Supprimer");
        deleteItem.setOnAction(e -> {
            if (user != null) {
                handleSupprimerUtilisateur(user);  // Utiliser directement l'utilisateur sélectionné
            } else {
                showAlert("Aucun utilisateur sélectionné !");
            }
        });
        menuButton.getItems().addAll(modifyItem, deleteItem);


        // Add components to the card
        userCard.getChildren().addAll(userImage, nameLabel, emailLabel, menuButton);
        return userCard;
    }

    // Handle user deletion
    private void handleSupprimerUtilisateur(utilisateur user) {
        if (user == null) {
            showAlert("Veuillez sélectionner un utilisateur !");
            return;
        }

//        // Vérifie si l'ID de l'utilisateur est valide
//        if (user.getId() <= 0) {
//            showAlert("ID utilisateur invalide !");
//            return;
//        }

        System.out.println("Deleting user with ID: " + user.getId()); // Debug statement

        if (user.getId() == currentUser.getId()) { // Ne pas permettre la suppression de soi-même
            showAlert("Vous ne pouvez pas vous supprimer vous-même !");
            return;
        }

        if (confirmDelete(user)) {
            userService.deleteByID(user.getId());
            afficherUsersDansGrid(); // Refresh the user list
            showAlert("Utilisateur supprimé avec succès !");
        }
    }


    // Handle user modification
    public void handleModifierUtilisateur(utilisateur user) {
        if (user == null) {
            showAlert("Veuillez sélectionner un utilisateur !");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProfile.fxml"));
            AnchorPane newUserPane = loader.load();

            // Pass the selected user to the new controller
            // ModifierProfileController controller = loader.getController();
//            controller.setUser(user);

            Scene newUserScene = new Scene(newUserPane, 600, 400);
            Stage stage = (Stage) userGridPane.getScene().getWindow(); // Use the current stage
            stage.setScene(newUserScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur de chargement de la scène.");
        }
    }

    // Show a confirmation dialog for deletion
    private boolean confirmDelete(utilisateur user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer " + user.getLastName() + " ?");
        ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);
        return alert.showAndWait().orElse(noButton) == yesButton;
    }

    // Show an alert dialog
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    // Handle adding a new user
    @FXML
    public void handleAjouterUtilisateur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur de chargement de la scène.");
        }
    }
}