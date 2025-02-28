package Controller;

import Entities.User;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.UserSession;
import utils.PreferenceManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashEmp {

    @FXML
    private JFXButton profil;
    @FXML
    private Label label;
    @FXML
    private Pane Emp;
    @FXML
    private VBox pnl_scroll;

    @FXML
    private Pane pnlCustomer, pnlOrders, pnlOverview, pnlMenus;

    @FXML
    private void handleButtonAction(MouseEvent event) {
        refreshNodes();
    }

    // @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        refreshNodes();
    }

    private void refreshNodes() {
        pnl_scroll.getChildren().clear();

        Node[] nodes = new Node[15];

        for (int i = 0; i < 10; i++) {
            try {
                nodes[i] = (Node) FXMLLoader.load(getClass().getResource("Item.fxml"));
                pnl_scroll.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    public void handleClicks(ActionEvent event) {
        if (event.getSource() == profil) {

            openAjouterAbonnement();

            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
            pnlCustomer.setStyle("-fx-background-color : #1620A1");
            pnlCustomer.toFront();
        }

    }

    private void openAjouterAbonnement() {
        try {
            // Charger le fichier FXML de AjouterAbonnement dans un Pane existant
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
            Pane ajoutAbonnementPane = loader.load(); // Charger le FXML

            // Ajouter le contenu dans le Pane principal de DashNUTRITIONNISTE
            pnlOrders.getChildren().setAll(ajoutAbonnementPane); // ou pnlOverview, pnlMenus, selon où tu veux l'afficher

            // Mettre à jour le style si nécessaire
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
        } catch (IOException e) {
            // showAlert("Error", "Failed to open the AjouterAbonnement page: " + e.getMessage());
        }
    }
    @FXML
    private void handleButtonProfile() {
        try {
            System.out.println(UserSession.getConnectedUser());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Profile.fxml"));
            Pane ajoutAbonnementPane = loader.load(); // Charger le FXML

            // Ajouter le contenu dans le Pane principal de DashNUTRITIONNISTE
            Emp.getChildren().setAll(ajoutAbonnementPane); // ou pnlOverview, pnlMenus, selon où tu veux l'afficher

            // Mettre à jour le style si nécessaire
            Emp.setStyle("-fx-background-color : #464F67");
            Emp.toFront();
            //  handleAjouterUtilisateur() ;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void logout(MouseEvent event) {
        PreferenceManager.clearAll();
        UserSession.setCurrentUser(null);
        try {
            Stage stage = (Stage) Emp.getScene().getWindow(); // Get reference to the login window's stage
            stage.setTitle("Welcome");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent p = loader.load();

            Scene scene = new Scene(p);

            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {

            e.printStackTrace();
            // Handle navigation failure
        }

    }
}

