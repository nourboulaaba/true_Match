package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Charger le fichier FXML pour Formations
            FXMLLoader loaderFormations = new FXMLLoader(getClass().getResource("/Formations.fxml"));
            Parent rootFormations = loaderFormations.load();

            // Charger le fichier FXML pour Certificat
            FXMLLoader loaderCertificat = new FXMLLoader(getClass().getResource("/Certificat.fxml"));
            Parent rootCertificat = loaderCertificat.load();

            // Créer le TabPane
            TabPane tabPane = new TabPane();

            // Créer les onglets pour Formations et Certificats
            Tab tabFormations = new Tab("Formations", rootFormations);
            Tab tabCertificat = new Tab("Certificats", rootCertificat);

            // Ajouter les onglets au TabPane
            tabPane.getTabs().add(tabFormations);
            tabPane.getTabs().add(tabCertificat);

            // Configurer le stage
            Scene scene = new Scene(tabPane);
            scene.getStylesheets().add(getClass().getResource("/stylef.css").toExternalForm());
            stage.setTitle("Gestion des Formations et Certificats");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement de l'application : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
