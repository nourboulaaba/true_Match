package org.example;

import Entities.Contrat;
import Entities.Mission;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane; // Use AnchorPane if that's the type of root node in your FXML
import javafx.stage.Stage;
import service.contratService;
import service.missionService;
import java.util.Date;

public class Main extends Application {

    public static void main(String[] args) {
        // Launch the JavaFX Application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load your FXML file here
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
        // If the root element in FXML is AnchorPane, cast it to AnchorPane
        AnchorPane root = loader.load();  // Load the FXML and cast to AnchorPane
        Scene scene = new Scene(root);

        // Set the scene to the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("True Match");
        primaryStage.show();

        // Call your existing business logic if needed
        contratService contratService = new contratService();

        // Example business logic:
      //  Contrat contrat1 = new Contrat(1, 200, "CDI", new Date(), new Date(), 6000.0);
       // contratService.insert(contrat1);

        // Test CRUD for Mission
        missionService missionService = new missionService();
        // Example mission addition:
      //  Mission mission1 = new Mission("Mission Yasmine", new Date(), "Moknine", 2);
      //  missionService.insert(mission1);
    }
}
