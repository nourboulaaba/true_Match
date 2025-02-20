package Controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard {

    @FXML
    private Label label;

    @FXML
    private VBox pnl_scroll;

    /*@FXML
    private JFXButton Emp;*/

    @FXML
    private Pane Emp;
    @FXML
    private Pane Profile;

    @FXML
    private void handleButtonAction(MouseEvent event) {

        refreshNodes();
    }


   /* @FXML
    private void handleButtonActionx() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionRH.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dashboard");

            stage.show();
            Pane ajoutAbonnementPane = loader.load(); // Charger le FXML

            // Ajouter le contenu dans le Pane principal de DashNUTRITIONNISTE
            Emp.getChildren().setAll(ajoutAbonnementPane); // ou pnlOverview, pnlMenus, selon où tu veux l'afficher

            // Mettre à jour le style si nécessaire
            Emp.setStyle("-fx-background-color : #464F67");
            Emp.toFront();
        } catch (Exception e) {
           // showAlert("Error", "Failed to open the dashboard: " + e.getMessage());
        }
    }*/
   @FXML
   private void handleButtonActionx() {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionRH.fxml"));
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
    private void handleButtonProfile() {
        try {
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
    public void handleAjouterUtilisateur() {
        try {
            // Charger le contenu de AjouterUser.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            AnchorPane newUserPane = loader.load();

            // Ajouter le contenu dans le Pane principal (Emp)
            Emp.getChildren().setAll(newUserPane);

            // Mettre à jour le style si nécessaire
            Emp.setStyle("-fx-background-color : #464F67");
            Emp.toFront();
            handleAjouterUtilisateur();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de chargement de la scène");
            alert.setContentText("Une erreur s'est produite lors du chargement de la scène pour ajouter un utilisateur.");
            alert.showAndWait();
        }
    }


   // @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        refreshNodes();
    }

    private void refreshNodes()
    {
        pnl_scroll.getChildren().clear();

        Node [] nodes = new  Node[15];

        for(int i = 0; i<10; i++)
        {
            try {
                nodes[i] = (Node)FXMLLoader.load(getClass().getResource("Item.fxml"));
                pnl_scroll.getChildren().add(nodes[i]);

            } catch (IOException ex) {
                Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
